# Security-03-login-page

> 自定义登录页的具体流程

## 准备页面

> 这里使用Thymeleaf模版，因为SpringBoot官网中说，如果不采用前后端分离的开发方式，则使用Thymeleaf模版来进行一体式开发

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
</head>
<body>

  <form action="/login" method="post">
    账号：<input type="text" name="username"> <br/>
    密码：<input type="password" name="password"> <br/>
    <input name="_csrf" type="hidden" th:value="${_csrf.token}">
    <input type="submit" value="登 录">
  </form>

</body>
</html>
```

### 注意事项

> form表单两点：
>
> - 第一点：action的路径需要再Config中指定；
> - 第二点：需要有一个隐藏域格式必须为 <input name="_csrf" type="hidden" th:value="${_csrf.token}">

作用：第一点的作用在于，告诉SpringSecurity我们的数据发送到那个地方。第二点用于区别其他请求，只有携带了 `_csrf.token` 的请求才被接收。



---

## 控制类添加

> 添加一个Controller，用于跳转到我们的Login.html页面。后将其配置到Config配置类中，让其生效

```java
@GetMapping("/toLogin")
public String goLogin(){
    // 跳转页面
    return "login";
}
```



---



## 配置类修改

>  新建方法 + securityFilterChain(HttpSecurity httpSecurity) ：SecurityFilterChain，用于在执行链中修改一些配置，使用@Bean注解注入即可。HttpSecurity使用参数注入的形式进行注入操作。

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        // 对表单进行修改
        .formLogin(formLogin -> {
            // 将用户登录拦截的登录页面重定向到我们自定义Controller
            formLogin
                .loginPage("/toLogin"); // 告诉框架，登录时执行的Controller
                .loginProcessingUrl("/login") // 告诉SpringSecurity用户表单数据提交的位置，等同于我们表单的action。
        })

        // 手动加回来对所有请求的认证
        .authorizeHttpRequests(authorizeHttpRequests -> {
            // 添加请求认证
            authorizeHttpRequests
                .requestMatchers("/toLogin").permitAll() // 允许toLogin绕过Filter，直接进行访问
                .anyRequest().authenticated(); // 对所有的请求都进行认证
        })

        .build();
}
```

因为，在手动配置了自定义登录页时，会导致SpringSecurity会失去一些默认的功能，比如说请求的拦截，没错！拦截都能丢。因此，这里我们需要手动将请求加回来。

```java
.authorizeHttpRequests(authorizeHttpRequests -> {
    // 添加请求认证
    authorizeHttpRequests
        .requestMatchers("/toLogin").permitAll() // 允许toLogin绕过Filter，直接进行访问
        .anyRequest().authenticated(); // 对所有的请求都进行认证
})
```

> authorizeHttpRequests.requestMatchers("/toLogin")  // 匹配 “/toLogin” 路径，让其 permitAll() 放行，也就是不进行登录拦截
>
> 使用 .anyRequest().authenticated(); 对剩余的其他请求进行拦截操作。

