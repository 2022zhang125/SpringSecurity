package cn.believesun;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class LoginApiJwtApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        String secret = "dasljhfwjh*&()*>;";
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("name", "test");
        payload.put("age", "18");
        String jwt = JWTUtil.createToken(payload,secret.getBytes(CharsetUtil.CHARSET_UTF_8));
        redisTemplate.opsForValue().set("jwt:" + "test", jwt, 30, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("jwt:" + "test"));
    }

}
