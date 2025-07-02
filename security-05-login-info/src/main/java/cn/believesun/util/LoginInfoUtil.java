package cn.believesun.util;

import cn.believesun.pojo.TUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginInfoUtil {
    /**
     * 通过Security上下文信息获取到Authentication对象中的Principal个人信息
     * @return TUser对象
     */
    public static TUser getCurrentLoginUser(){
        return (TUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
