# SpringSecurity

## 前期提要
    username默认为 user
    password会在项目创建时创建一个UUID
    登录成功后，前端产生的JSESSIONID会与后端的Session.getId()进行匹配，然后执行链继续执行。
> 使用了SpringSecurity后，所有的Controller接口都需要登录后才能访问
> 如果没有登录，他会重定向到登录页！

## 问题解决
    问题： java.lang.IllegalArgumentException: Given that there is no default password encoder configured, each password must have a password encoding prefix. Please either prefix this password with '{noop}' or set a default password encoder in `DelegatingPasswordEncoder`.
       没有配置密码加密器
    解决方法：
        在配置类中用@Bean注解标注一个返回值为PasswordEncoder的方法，其会new一个BCryptPasswordEncoder的实现类。

    问题：在配置了自定义登录界面后，SpringSecurity的功能失效了！！！
    解决方法：在配置类中手动将功能加回来，同时**屏蔽SpringSecurity对登录页请求的拦截**
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .formLogin(formLogin -> {
                // 将用户登录拦截的登录页面重定向到我们自定义Controller
                formLogin.loginPage("/toLogin");
            })

            // 手动加回来对所有请求的认证
            .authorizeHttpRequests(authorizeHttpRequests -> {
                // 添加请求认证
                authorizeHttpRequests
                        .requestMatchers("/toLogin").permitAll() // 允许toLogin绕过Filter，直接进行访问，permit（允许）deny（拒绝）
                        .anyRequest().authenticated(); // 除了上述除外的请求以外，对所有的请求都进行认证
            })

            .build();
}
```
    
## 框架分析
    UsernamePasswordAuthenticationFilter            (接收密码)
    调用 loadUserByUsername(String username)         (查询数据库根据用户名，并将其封装为UserDetails对象返回给SpringSecurity框架)
    框架检查3个状态值（账号是否启用，账号是否被锁定，账号是否过期）
    
    返回Filter中检查Password，调用this.passwordEncoder.match()去匹配 明文和密文。
    this.addititonalAuthentication(user,(UsernamePasswordAuthentication) authentication)
    
    @JsonIgnore 忽略jackson的返回字段

## 注意事项
     - 总而言之，言而总之，角色权限控制时，在add集合时添加ROLE_ 前缀并且PreAuthorize中使用hasRole;
        - 资源权限控制时，不添加ROLE_前缀，使用PreAuthorize的hasAuthority即可。
     - SpringBoot脚手架自动生成的Lombok依赖有问题，记得换成以下格式，不然用不了
     ```xml
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.32</version>
            <scope>provided</scope>
        </dependency>
     ```