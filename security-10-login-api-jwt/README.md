# Security-10-login-api-jwt

> 主要解决了上一个模块残留的问题——前后端分离后登录成功也不能访问其他Controller。
>
> 解决方法：
>
> - 后端：生成JWT并存入Redis中并在第一次登陆成功后发送给前端，在日后的请求时进行Filter验证（需在LogoutFilter之前），验证成功后将用户数据存入Authentication中。
> - 前端：将登陆成功的Jwt存入SessionStorage中，每次请求携带jwt，后端进行验证
>
> 新增功能：前后端权限控制（RBAC），MyAccessDeniedHandler类处理没权限问题

---

详细信息如下：

## 后端生成JWT存入Redis并发送给前端

```java
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        TUser tUser = (TUser) authentication.getPrincipal();
        String user = JSONUtil.toJsonStr(tUser);

        // 生成JWT（token）保存用户信息
        String token = JWTUtil.createToken(Map.of("user", user), Constant.SECRET.getBytes());

        redisTemplate.opsForHash().put(Constant.REDIS_TOKEN_KEY, tUser.getUsername(), token);

        // 发送response
        String json = JSONUtil.toJsonStr(R.SUCCESS("登陆成功！", token));
        response.getWriter().write(json);

    }
}
```



---



## 前端接收并存入SessionStroage

```js
const response = await axios.post('http://localhost:8080/user/login', userInfo);
    if (response.data.code === 200) {
      this.$message.success(response.data.msg)
      sessionStorage.setItem('loginToken', response.data.data);
      this.$router.push({
        path: '/welcome',
      })
    } else {
      this.$message.error(response.data.msg);
      this.username = '';
      this.password = '';
    }
```



---



## 后端拦截请求验证JWT

> 注意：这里最好使用<font color=red>**统一化处理response**</font>的writer，不然可能会出现和OutputStream异常的问题！！！
>
> 将首页和登录请求进行pass处理

```java
@Component
public class JwtVerifyFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String[] passList = {"/user/login", "/"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 放行不需要过滤的请求
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authorization = request.getHeader("Authorization");

            // 验证token是否存在且有效
            if (!StringUtils.hasText(authorization) || !JWTUtil.verify(authorization, Constant.SECRET.getBytes())) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "验证不通过，请登录后再试");
                return;
            }

            // 验证token是否与redis中一致
            TUser user = JSONUtil.toBean(JWTUtil.parseToken(authorization).getPayload("user").toString(), TUser.class);
            String redis_token = (String) redisTemplate.opsForHash().get(Constant.REDIS_TOKEN_KEY, user.getUsername());

            if (redis_token == null || !redis_token.equals(authorization)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token已失效，请重新登录");
                return;
            }

            // 验证通过，设置SecurityContext
            // 这一步很重要，不然后面的Filter不知道你是否通过了！
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            user, user.getPassword(), user.getAuthorities()));

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(JSONUtil.toJsonStr(R.FAIL(message)));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.asList(passList).contains(request.getRequestURI());
    }
}

```



> SecurityConfig配置Filter

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception {
        return httpSecurity
                ...
                // 确保在登出之前就获取到这个用户数据
                .addFilterBefore(jwtVerifyFilter, LogoutFilter.class)
                .build();
    }
```



---



## 处理无权限问题

> 当在配置类中配置了`exceptionHandling`时，我们触发无权限时会自动调用其`accessDeniedHandler`方法指定的Handler

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception {
        return httpSecurity
                ...
                // 当用户没有权限的时候，执行这个handler
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(myAccessDeniedHandler);
                })
                .build();
    }
```

这个Handler类似于上述的successHandler......

```java
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JSONUtil.toJsonStr(R.FAIL("不是哥们！没权限呐~")));
    }
}

```



**至此！SpringSecurity结束！！！！完结撒花~~~**