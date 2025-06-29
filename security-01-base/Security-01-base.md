# Security-01-base

## Spring Security的基本使用

- 使用方法

  - 导入Maven

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ```

  

- 现象

当导入该依赖后，如果没有自己设定账号和密码，则会自动生成一个 <user,UUID.randomUUID()>的默认账号密码

## 在yml中自定义用户名和密码

```yaml
spring:
  application:
    name: security-01-base
  security:
    # 如果没有自己设定账号和密码，则会自动生成一个 <user,UUID.randomUUID()>的默认账号密码
    user:
      name: user
      password: 111111

```

但，这样不符合我们的意愿，本意是将`数据库`中的注册过的用户名和密码作为登录账户，而不是自定义的！因此，我们在db-login中详细的解决了这一问题。