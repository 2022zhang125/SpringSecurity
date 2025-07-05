package cn.believesun.captcha;

import cn.hutool.captcha.generator.CodeGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MyCodeGenerator implements CodeGenerator {
    @Override
    public String generate() {
        // 生成一个四位数的验证码
        String code = String.valueOf(1000 + new Random().nextInt(9000));
        return code;
    }

    @Override
    public boolean verify(String s, String s1) {
        return false;
    }
}
