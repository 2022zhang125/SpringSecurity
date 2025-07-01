# Security-04-captcha-login

# **验证码登录，具体实现**

> 注册时，显示验证码登录。这里使用到 Hutool包 来生成验证码图片。

## 添加依赖

[糊涂工具官网]:https://hutool.cn

这里，我们采用局部导入，只导入captcha（验证码）模块，不导入其他模块。

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-captcha</artifactId>
    <version>5.8.38</version>
</dependency>
```



---



## 修改Thymeleaf模版页面

添加验证码标签，并设定src

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
</head>
<body>

  <form action="/user/login" method="post">
    账号：<input type="text" name="username"> <br/>
    密码：<input type="password" name="password"> <br/>
    <!--src去后端获取验证码图片-->
    验证码：<input type="text" name="captcha"> <img src="/common/captcha"> <br/>
    <input name="_csrf" type="hidden" th:value="${_csrf.token}">
    <input type="submit" value="登 录">
  </form>

</body>
</html>
```

主要代码：

```html
验证码：<input type="text" name="captcha"> <img src="/common/captcha"> <br/>
```

当页面显示出来的时候，该图片会请求 http://localhost:8080/common/captcha 去加载图片资源



---



## 创建CaptchaController控制层

在该层中，去**创建验证码**，**储存验证码**以及**将验证码发送给前端**

- 创建验证码

```java
// 参数说明：width，height，验证码个数，Circle圈数，字体大小为高度的多少倍（这里是1倍也就是20ps）
ICaptcha captcha = CaptchaUtil.createCircleCaptcha(120, 20, 4, 10, 1);
```

- 储存验证码

储存验证码一般将其保存在Session会话中。

```java
// 注入HttpServletRequest request
request.getSession().setAttribute("captcha", captcha.getCode());
```

- 将验证码发送给前端

注意！需要设置一下response的相应格式，不然虽然前端正常显示但不能通过接口去在网页查看。

```java
response.setResponseType("image/jpeg");
```

将其写入response中，然后随着响应发送给前端即可，通过参数注入HttpServletResponse response

```java
captcha.write(response.getOutputStream());
```

完整代码：

```java
@Controller
public class CaptchaController {
    @GetMapping("/common/captcha")
    public void generateController(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ICaptcha captcha = CaptchaUtil.createCircleCaptcha(120, 20, 4, 10, 1);
        response.setContentType("image/jpeg");
        System.out.println("CaptchaCode = " + captcha.getCode());
        request.getSession().setAttribute("captcha", captcha.getCode());
        captcha.write(response.getOutputStream());
    }
}
```



---



## 将路径进行配置

对表单action进行配置，向框架指定数据提交位置

```java
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
    return httpSecurity
        .formLogin(formLogin ->{
            formLogin
	            // 设置首页
                .loginPage("/toLogin")
                // 告诉框架提交位置
                .loginProcessingUrl("/user/login");
        })
        // 放行 /toLogin 以及 /common/catcha
        .authorizeHttpRequests(authorizeHttpRequests -> {
            .requestMatchers("/toLogin","/common/captcha").permitAll()
            .anyRequest().authenticated(); // 对所有的请求都进行认证
        }
        .build();
}
```



---



# 自定义验证码

实现一下接口即可

```java
public interface CodeGenerator extends Serializable {
    String generate();

    boolean verify(String var1, String var2);
}
```

具体实现如下

```java
package cn.believesun.captcha;

import cn.hutool.captcha.generator.CodeGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MyCodeGenerator implements CodeGenerator {
    @Override
    public String generate() {
        // 生成一个四位数的验证码
        String code = String.valueOf(1000 + new Random().nextInt(9000));
        return code;
    }

    @Override
    public boolean verify(String s, String s1) {
        return false;
    }
}
```

```java
package cn.believesun.controller;

import cn.believesun.captcha.MyCodeGenerator;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class CaptchaController {

    @Autowired
    private MyCodeGenerator generator;

    @GetMapping("/common/captcha")
    public void generateController(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ICaptcha captcha = CaptchaUtil.createCircleCaptcha(120, 20, generator, 10);
        response.setContentType("image/jpeg");
        System.out.println("CaptchaCode = " + captcha.getCode());
        request.getSession().setAttribute("captcha", captcha.getCode());
        captcha.write(response.getOutputStream());
    }
}
```

