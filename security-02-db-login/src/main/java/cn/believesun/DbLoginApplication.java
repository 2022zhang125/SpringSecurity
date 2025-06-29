package cn.believesun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.believesun.mapper")
public class DbLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbLoginApplication.class, args);
    }

}
