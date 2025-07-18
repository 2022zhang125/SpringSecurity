package cn.believesun.controller;

import cn.believesun.captcha.MyCodeGenerator;
import cn.believesun.filter.CaptchaFilter;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class CaptchaController {

    /*@Autowired
    private MyCodeGenerator generator;*/
    @GetMapping("/common/captcha")
    public void generateController(HttpServletResponse response, HttpServletRequest request) throws IOException {
         ICaptcha captcha = CaptchaUtil.createCircleCaptcha(120, 20, 4, 10, 1);
//        ICaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 20, generator, 10);
        response.setContentType("image/jpeg");
        System.out.println("CaptchaCode = " + captcha.getCode());
        request.getSession().setAttribute("captcha", captcha.getCode());
        captcha.write(response.getOutputStream());
    }
}
