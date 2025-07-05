package cn.believesun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("cn.believesun.mapper")
@SpringBootApplication
public class RolePermissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RolePermissionApplication.class, args);
	}
}
