# Security-08-login-api（前后端分离，但是没有解决SESSION问题）

> 对于前后端分离问题，主要修改Config配置文件的问题。比如：CORS跨域问题，CSRF跨站请求伪造问题，Response返回值封装问题，successHandler登陆成功后的转发问题同样还有登录失败......，前后端分离导致的SESSION不可用问题啊~~~我们一一解决一下

## CORS跨域问题

> 调用`cors`的`configurationSource`将配置类进行加载就好，很简单吧

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource corsConfigurationSource) throws Exception {
    return httpSecurity
            ....
            // 配置跨域问题
            .cors(cors -> {
                cors.configurationSource(corsConfigurationSource);
            })
            ...
            .build();
}
}
```

> 配置config类

```java
@Bean // 跨域访问配置
public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("*"));
    config.setAllowedHeaders(Arrays.asList("*"));

    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", config);
    return urlBasedCorsConfigurationSource;
}
```



---



## CSRF跨站请求伪造问题

> 后期使用JWT去解决

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return httpSecurity
                ....
                .csrf(AbstractHttpConfigurer::disable)
            	...
                .build();
    }
}
```



## 登陆/登出成功/失败时的Handler问题

> 其实也很简单，就自己写个类实现接口，然后Autowired进来就好。

```java
@Autowired
private MyAuthenticationSuccessHandler successHandler;

@Autowired
private MyAuthenticationFailureHandler failureHandler;

@Autowired
private MyLogoutSuccessHandler logoutSuccessHandler;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return httpSecurity
                ...
                .formLogin(formLogin -> {
                    formLogin
                            .loginPage("http://localhost:8081")
                            // action 请求发向 /user/login
                            .loginProcessingUrl("/user/login")
                            .successHandler(successHandler)
                            .failureHandler(failureHandler);
                })
                .logout(logout -> {
                    logout
                            .logoutUrl("/user/logout")
                            // 登出成功,SpringSecurity没有登出失败
                            .logoutSuccessHandler(logoutSuccessHandler);
                })
                ...
                .build();
    }
```



---



## Response返回值封装问题

> 一个标准的Response类，在其他类（Handler）进行返回时调用的，然后通过`JSONUtil.toJsonStr()`方法即可转为JSON，通过`response.getWriter().write(json);`进行写入

这里使用的时@Builder进行创建的，别问，问就是装b。

```java
package cn.believesun.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class R {
    private String msg;
    private Integer code;
    private Object data;

    public static R SUCCESS(String msg,Integer code,Object data) {
        return R.builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
    }
    public static R FAIL(String msg,Integer code) {
        return R.builder()
                .msg(msg)
                .code(code)
                .build();
    }
}
```



---



## 前后端分离导致的SESSION不可用问题

> 这个问题，解决方法是使用JWT去解决，这个模块就没解决了，只是吧SESSIONID给关掉，省点资源

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource  corsConfigurationSource) throws Exception {
    return httpSecurity
            ...
            // 由于前后端分离时的SESSION无法使用，所以我们这里直接禁用即可。还能节省内存
            .sessionManagement(sessionManagement -> {
                // Session创建策略为stateless（无状态），表示不记录SESSION
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .build();
}
```

