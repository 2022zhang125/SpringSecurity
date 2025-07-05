package cn.believesun.controller;

import cn.believesun.pojo.TUser;
import cn.believesun.util.LoginInfoUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

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

    // 总接口
    @GetMapping("/info")
    public @ResponseBody Object getUserInfo(Principal principal){
        // 这里我们获得的信息很少，因此我们需要修改Login方法，将UserDetails的实现从SpringSecurity的User变为自定义的TUser对象
        return principal;
    }

    // 子接口
    @GetMapping("/info2")
    public @ResponseBody Object getUserInfo2(Authentication authentication)  {
        return authentication;
    }

    // 子接口的实现类
    @GetMapping("/info3")
    public @ResponseBody Object getUserInfo3(UsernamePasswordAuthenticationToken authentication){
        return authentication;
    }

    // 通过Security上下文拿
    @GetMapping("/info4")
    public @ResponseBody Object getUserInfo4(){
        // 也可以拿到用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    // 编写工具类获取当前登录者的信息
    @GetMapping("/info5")
    public @ResponseBody TUser getUserInfo5(){
        TUser userInfo = LoginInfoUtil.getCurrentLoginUser();
        return userInfo;
    }
}
