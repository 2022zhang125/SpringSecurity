package cn.believesun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@MapperScan("cn.believesun.mapper")
@SpringBootApplication
public class LoginApiJwtApplication implements CommandLineRunner {

    @Autowired
    private RedisTemplate redisTemplate;
    public static void main(String[] args) {
        SpringApplication.run(LoginApiJwtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 解决Redis乱码问题
        // 设置大Key
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 设置小Key
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 设置小Key的Value
        redisTemplate.setHashValueSerializer(RedisSerializer.string());
    }
}
