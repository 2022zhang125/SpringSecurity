package cn.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    public static final String secret = "sdfhgaewui2yif803{:>}L}";
    /**
     * 生成JWT
     */
    public static String createToken(String userJson) {
        //组装头数据
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return JWT.create()
                //头
                .withHeader(header)
                //自定义数据
                .withClaim("user", userJson)
                //签名算法
                .sign(Algorithm.HMAC256(secret));
    }
    /**
     * 验证JWT
     * @param token 要验证的jwt的字符串
     */
    public static Boolean verifyToken(String token) {
        try {
            // 使用秘钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            //验证JWT
            jwtVerifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析JWT的数据
     */
    public static String parseToken(String token) {
        try {
            // 使用秘钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            //验证JWT
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Claim user = decodedJWT.getClaim("user");
            return user.asString();
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
