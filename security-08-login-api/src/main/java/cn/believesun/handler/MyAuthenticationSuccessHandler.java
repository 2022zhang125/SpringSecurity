package cn.believesun.handler;

import cn.believesun.utils.R;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        // 这里的R对象使用@Build建造者模式的链式编程
        String json = JSONUtil.toJsonStr(R.SUCCESS("登录成功",200,authentication.getPrincipal()));
        response.getWriter().write(json);
    }
}
