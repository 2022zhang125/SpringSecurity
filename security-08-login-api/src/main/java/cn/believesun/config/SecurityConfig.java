package cn.believesun.config;

import cn.believesun.handler.MyAuthenticationSuccessHandler;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.lang.reflect.Array;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource corsConfigurationSource, MyAuthenticationSuccessHandler handler) throws Exception {
        return httpSecurity
                // 关闭CSRF验证（CSRF:跨站请求伪造）就是那个表单的特殊token，后期用jwt即可
                .csrf(AbstractHttpConfigurer::disable)
                // 配置跨域问题
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource);
                })
                .formLogin(formLogin -> {
                    formLogin.loginPage("http://localhost:8081");
                    // action 请求发向 /user/login
                    formLogin.loginProcessingUrl("/user/login");
                    formLogin.successHandler(handler);
                })
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers(HttpMethod.OPTIONS).permitAll();
                    // 对所有请求进行拦截操作
                    authorizeRequests.anyRequest().authenticated();
                })
                .build();
    }
}
