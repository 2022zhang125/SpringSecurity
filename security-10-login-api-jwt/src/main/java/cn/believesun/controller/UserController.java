package cn.believesun.controller;

import cn.believesun.utils.LoginUserInfoUtil;
import cn.believesun.utils.R;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @GetMapping("/test")
    public @ResponseBody String test() {
        return JSONUtil.toJsonStr(R.SUCCESS("测试成功！", LoginUserInfoUtil.getUserInfo()));
    }
}
