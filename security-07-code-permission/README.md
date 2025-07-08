#  Security-07-code-permission（基于资源代码的权限管理）

> 与Security-06-role-permission（RBAC）差不多，就一些小的地方进行修改就好

## TUser类新增属性

修改了`getAuthorieies()`方法

```java
private List<TPermission> tPermissionList;
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    // 配置RoleList
    if (this.tPermissionList.isEmpty()) {
        return List.of();
    }
    // 封装role对象
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    for(TPermission tPermission : tPermissionList){
        authorities.add(new SimpleGrantedAuthority(tPermission.getCode()));
    }
    return authorities;
}
```



---



## UserServiceImpl的修改

```java
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
```

其他的没啦！