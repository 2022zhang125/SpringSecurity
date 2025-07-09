package cn.believesun.config;

import cn.believesun.handler.MyAuthenticationFailureHandler;
import cn.believesun.handler.MyAuthenticationSuccessHandler;
import cn.believesun.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    // 密码加码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

    @Autowired
    private MyAuthenticationSuccessHandler successHandler;

    @Autowired
    private MyAuthenticationFailureHandler failureHandler;

    @Autowired
    private MyLogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return httpSecurity
                // 关闭CSRF验证（CSRF:跨站请求伪造）就是那个表单的特殊token，后期用jwt即可
                .csrf(AbstractHttpConfigurer::disable)
                // 配置跨域问题
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource);
                })
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
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(HttpMethod.OPTIONS).permitAll()
                            // 对所有请求进行拦截操作
                            .anyRequest().authenticated();
                })
                // 由于前后端分离时的SESSION无法使用，所以我们这里直接禁用即可。还能节省内存
                .sessionManagement(sessionManagement -> {
                    // Session创建策略为stateless（无状态），表示不记录SESSION
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .build();
    }
}
