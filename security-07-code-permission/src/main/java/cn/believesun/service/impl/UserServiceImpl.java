package cn.believesun.service.impl;

import cn.believesun.mapper.TPermissionMapper;
import cn.believesun.mapper.TRoleMapper;
import cn.believesun.mapper.TUserMapper;
import cn.believesun.pojo.TPermission;
import cn.believesun.pojo.TRole;
import cn.believesun.pojo.TUser;
import cn.believesun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TPermissionMapper tPermissionMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        TUser tUser = tUserMapper.selectUserByActNo(username);
        if(tUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 基于资源的权限控制
         List<TPermission> tPermissionList = tPermissionMapper.selectCodesByUserId(tUser.getId());
         tUser.setTPermissionList(tPermissionList);
        return tUser;
    }
}
