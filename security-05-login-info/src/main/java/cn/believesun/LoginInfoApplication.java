package cn.believesun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.believesun.mapper")
public class LoginInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginInfoApplication.class, args);
    }

}
