package cn.believesun.controller;

import cn.believesun.utils.UserLoginInfoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @GetMapping("/test")
    public @ResponseBody Object test() {
        return UserLoginInfoUtil.getUserInfo();
    }
}
