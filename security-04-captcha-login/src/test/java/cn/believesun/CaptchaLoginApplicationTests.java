package cn.believesun;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class CaptchaLoginApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    // 测试加密方式
    @Test
    void contextLoads() {
        String password = "123";
        for (int i = 0; i < 5; i++) {
            System.out.println(password);
            String encode = passwordEncoder.encode(password);
            password = passwordEncoder.encode(encode);
        }
    }
}
