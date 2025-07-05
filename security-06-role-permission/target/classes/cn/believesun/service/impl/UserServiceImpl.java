package cn.believesun.service.impl;

import cn.believesun.mapper.TRoleMapper;
import cn.believesun.mapper.TUserMapper;
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
    private TRoleMapper tRoleMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        TUser tUser = tUserMapper.selectUserByActNo(username);
        if(tUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 从数据库中查询，给角色权限
        List<TRole> tRoleList = tRoleMapper.selectRolesByUserId(tUser.getId());
        tUser.setTRoleList(tRoleList);
        return tUser;
    }
}
