package cn.believesun.service.impl;

import cn.believesun.mapper.TUserMapper;
import cn.believesun.pojo.TUser;
import cn.believesun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        TUser tUser = tUserMapper.selectUserByActNo(username);
        if(tUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        UserDetails userDetails = User.builder()
                .username(tUser.getLoginAct())
                .password(tUser.getLoginPwd())
                .authorities(AuthorityUtils.NO_AUTHORITIES) // 空权限，EmptyList
                // .accountExpired(true) 账号已过期
                .build();
        return userDetails;
    }
}
