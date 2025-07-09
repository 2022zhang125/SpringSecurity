package cn.believesun.config;

import cn.believesun.filter.JwtVerifyFilter;
import cn.believesun.handler.MyAccessDeniedHandler;
import cn.believesun.handler.MyAuthenticationFailureHandler;
import cn.believesun.handler.MyAuthenticationSuccessHandler;
import cn.believesun.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableMethodSecurity // 允许权限访问注解的使用
@Configuration
public class SecurityConfig {
    @Bean
    public CorsConfigurationSource configurationSource() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        configurationSource.registerCorsConfiguration("/**", config);
        return configurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private MyAuthenticationSuccessHandler successHandler;

    @Autowired
    private MyAuthenticationFailureHandler failureHandler;

    @Autowired
    private MyLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private JwtVerifyFilter jwtVerifyFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception {
        return httpSecurity
                .formLogin(formLogin -> {
                    formLogin
                            .loginPage("http://localhost:8081")
                            .loginProcessingUrl("/user/login")
                            .successHandler(successHandler)
                            .failureHandler(failureHandler);
                })
                .logout(logout -> {
                    logout.logoutUrl("/user/logout")
                            .logoutSuccessHandler(logoutSuccessHandler);
                })
                .authorizeHttpRequests(authorizeRequests -> {
                    // 对所有请求进行拦截
                    authorizeRequests
                            .requestMatchers("/test").permitAll()
                            .anyRequest().authenticated();
                })
                // 确保在登出之前就获取到这个用户数据
                .addFilterBefore(jwtVerifyFilter, LogoutFilter.class)

                .csrf(AbstractHttpConfigurer::disable)
                // 跨域请求问题
                .cors(cors -> {
                    cors.configurationSource(configurationSource);
                })
                // 关SESSION处理
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // 当用户没有权限的时候，执行这个handler
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(myAccessDeniedHandler);
                })
                .build();
    }
}
