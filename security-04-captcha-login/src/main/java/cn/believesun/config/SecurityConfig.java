package cn.believesun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // 配置密码加密器，用于 SpringSecurity 去比较 UserDetails的实现类User的密码 与 用户输入的是否一致。
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 自定义登录页，通过参数注入的方式进行操作
    // 如果不采用前后端分离，则使用Thymeleaf模版引擎
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(formLogin -> {
                    // 将用户登录拦截的登录页面重定向到我们自定义Controller
                    formLogin
                            .loginProcessingUrl("/login") // 告诉SpringSecurity用户表单数据提交的位置，等同于我们表单的action。
                            .loginPage("/toLogin");
                })

                // 手动加回来对所有请求的认证
                .authorizeHttpRequests(authorizeHttpRequests -> {
                    // 添加请求认证
                    authorizeHttpRequests
                            .requestMatchers("/toLogin","/common/captcha").permitAll() // 允许toLogin绕过Filter，直接进行访问
                            .anyRequest().authenticated(); // 对所有的请求都进行认证
                })

                .build();
    }
}
