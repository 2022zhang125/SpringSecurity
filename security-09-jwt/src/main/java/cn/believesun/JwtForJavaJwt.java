package cn.believesun;

import cn.hutool.jwt.signers.JWTSignerUtil;
import cn.utils.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

public class JwtForJavaJwt {
    public static void main(String[] args) {
        // 第一种：使用Java-jwt的方式
        String token = JWTUtil.createToken("123123123");
        System.out.println(token);
        System.out.println(JWTUtil.verifyToken(token));
        System.out.println(JWTUtil.parseToken(token));
        // 第二种：使用HuTool包的方式
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("username","张三");
        payload.put("password","123456");
        payload.put("birthDay",new Date());
        // 默认使用HS256算法
        String hutoolJWT = cn.hutool.jwt.JWTUtil.createToken(payload, JWTUtil.secret.getBytes());
        System.out.println(hutoolJWT);
        // 自定义使用的
        System.out.println(cn.hutool.jwt.JWTUtil.createToken(payload, JWTSignerUtil.hs512(JWTUtil.secret.getBytes(StandardCharsets.UTF_8))));
    }
}