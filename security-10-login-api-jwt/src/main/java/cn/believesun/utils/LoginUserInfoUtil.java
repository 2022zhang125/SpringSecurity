package cn.believesun.utils;

import cn.believesun.pojo.TUser;
import org.springframework.security.core.context.SecurityContextHolder;

// 获取登录用户信息
public class LoginUserInfoUtil {
    public static TUser getUserInfo() {
        return (TUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
