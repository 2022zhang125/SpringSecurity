# SpringSecurity01

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
        在配置类中用@Bean注解标注一个返回值为PasswordEncoder的方法，其会new一个BCryptPasswordEncoder的实现类.