package cn.believesun.service.impl;

import cn.believesun.mapper.TUserMapper;
import cn.believesun.pojo.TUser;
import cn.believesun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser tUser = userMapper.selectByUsername(username);
        if(tUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 自定义TUser对象,作为UserDetail的返回类型
        return tUser;
    }
}
