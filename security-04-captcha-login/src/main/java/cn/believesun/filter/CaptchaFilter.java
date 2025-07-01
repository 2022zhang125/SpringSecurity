package cn.believesun.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 判断验证码是否正确
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userCaptcha = request.getParameter("captcha");
        String uri = request.getRequestURI(); // /login
        if(uri.equals("/login")){
            userCaptcha = userCaptcha.isEmpty() ? "" : userCaptcha.trim();
            String realCaptcha = (String) request.getSession().getAttribute("captcha");
            // 使用equalsIgnoreCase 去忽略大小写
            if(!userCaptcha.equalsIgnoreCase(realCaptcha)){
                // 抛异常？这里重定向到首页
                response.sendRedirect("/");
            }else{
                // 验证通过
                filterChain.doFilter(request,response);
            }
        }else{
            // 不做验证，直接通过
            filterChain.doFilter(request,response);
        }
    }
}
