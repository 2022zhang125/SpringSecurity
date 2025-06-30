package cn.believesun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {

    @GetMapping("/")
    // 被@ResponseBody注解标注的方法，其允许返回值为JSON、字符串，如果没有标注，则返回 逻辑视图地址
    public @ResponseBody String getUser(){
        return "Hello Spring Security";
    }

    @GetMapping("/toLogin")
    public String goLogin(){
        // 跳转页面
        return "login";
    }
}
