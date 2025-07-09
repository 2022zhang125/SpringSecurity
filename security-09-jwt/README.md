# Security-09-jwt（对JWT生成的两种方式）

> 主要有两种方式：java-jwt和Hutool包的jwt，以后用就直接复制即可

JWT：`header `+ `payload `+ `sign ` 三部分组成。注意：JWT是`Base64`加密的，不是安全的，一般不用于存储password等一些私密数据，但是也不是不行，反正payload里面存储的密码是`BCryptPasswordEncoder`加密的。



## Java-jwt方式

> 这里是Util类

```java
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
```



---



## Hutool-jwt（常用）

> 这个比较简单，也比较好用

```java
package cn.believesun;

import cn.hutool.jwt.signers.JWTSignerUtil;
import cn.utils.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

public class JwtForJavaJwt {
    public static void main(String[] args) {
        // 第一种：使用Java-jwt的方式
        // 这里传入的 123123123 是payload数据，也就是我们想要携带的数据
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
```

