package cn.believesun.filter;

import cn.believesun.constant.Constant;
import cn.believesun.pojo.TUser;
import cn.believesun.utils.R;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Component
public class JwtVerifyFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String[] passList = {"/user/login", "/"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 放行不需要过滤的请求
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authorization = request.getHeader("Authorization");

            // 验证token是否存在且有效
            if (!StringUtils.hasText(authorization) || !JWTUtil.verify(authorization, Constant.SECRET.getBytes())) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "验证不通过，请登录后再试");
                return;
            }

            // 验证token是否与redis中一致
            TUser user = JSONUtil.toBean(JWTUtil.parseToken(authorization).getPayload("user").toString(), TUser.class);
            String redis_token = (String) redisTemplate.opsForHash().get(Constant.REDIS_TOKEN_KEY, user.getUsername());

            if (redis_token == null || !redis_token.equals(authorization)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token已失效，请重新登录");
                return;
            }

            // 验证通过，设置SecurityContext
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            user, user.getPassword(), user.getAuthorities()));

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(JSONUtil.toJsonStr(R.FAIL(message)));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.asList(passList).contains(request.getRequestURI());
    }
}
