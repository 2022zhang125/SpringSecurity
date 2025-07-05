package cn.believesun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("cn.believesun.mapper")
@SpringBootApplication
public class CodePermissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodePermissionApplication.class, args);
    }

}
