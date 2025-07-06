package cn.believesun.utils;

import cn.believesun.pojo.TUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserLoginInfoUtil {
    public static TUser getUserInfo(){
        return (TUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
