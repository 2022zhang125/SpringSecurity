package cn.believesun.handler;

import cn.believesun.constant.Constant;
import cn.believesun.pojo.TUser;
import cn.believesun.utils.R;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // tmd是人啊！nm用@Autowird就报错是吧，tmdcao。
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        TUser tUser = (TUser) authentication.getPrincipal();
        String user = JSONUtil.toJsonStr(tUser);

        // 生成JWT（token）保存用户信息
        String token = JWTUtil.createToken(Map.of("user", user), Constant.SECRET.getBytes());

        // 存入Redis 30s 过期
        // redisTemplate.opsForValue().set("token_" + ((TUser) authentication.getPrincipal()).getUsername(), token, 30, TimeUnit.SECONDS);

        redisTemplate.opsForHash().put(Constant.REDIS_TOKEN_KEY, tUser.getUsername(), token);

        // 发送response
        String json = JSONUtil.toJsonStr(R.SUCCESS("登陆成功！", token));
        response.getWriter().write(json);

    }
}
