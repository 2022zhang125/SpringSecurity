package cn.believesun.handler;

import cn.believesun.constant.Constant;
import cn.believesun.pojo.TUser;
import cn.believesun.utils.R;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        TUser tUser = (TUser) authentication.getPrincipal();
        String json = JSONUtil.toJsonStr(R.SUCCESS(tUser.getUsername() + "登出成功！已删除Token", null));
        // 为了保证我的authentication能给获取得到值，所以这里我们应该将我们的Filter放在他前面
        // 消除Redis中的数据，让JWT失效
        redisTemplate.opsForHash().delete(Constant.REDIS_TOKEN_KEY,tUser.getUsername());
        response.getWriter().write(json);
    }
}
