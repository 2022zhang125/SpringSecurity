# Security-05-login-info

> 主要学习了怎么获取登录用户信息

获取登录用户的主要思路是通过 `UsernamePasswordAuthenticationToken` 类来实现的。主要的调用关系如下

`Principal` 主接口 下的 `Authentication `子接口 下的实现类 `UsernamePasswordAuthenticationToken` 返回来实现的。

## 优化返回对象

> 由于返回对象的有用属性很少，因此我们需要重新复写User对象，不能用SpringSecurity提供的User类来进行 build() 。我们可以返回自己的TUser对象，只需要让其实现 UserDetails 接口即可。并复写方法即可

```java
package cn.believesun.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 用户表
 * t_user
 */
@Data
public class TUser implements Serializable, UserDetails {

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNoExpired == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNoLocked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNoExpired == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.accountEnabled == 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.loginPwd;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

```



然后复写其 `loadUserByUsername` 方法即可。

```java
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
        return tUser;
    }
}
```



---



##  对于自定义返回对象的jackson格式问题

> 由于返回值在默认情况下时不支持 东八区 以及隐藏一些隐私信息，所以我们使用JackSon对象的注解来实现。

- **@JsonIgnore**
  - 被该注解标注的属性/方法将不会被序列化。
- **@JsonFormat**
  - 该注解可以自定义时间的显示格式。
  - 属性 pattern = "yyyy-MM-dd HH:mm:ss SSS" 修改格式
  - 属性 timezone = "GTM+8" 修改为东八区显示（现在好像默认是东八区）
  - 或者在配置文件中直接指定，这样就不用一直写了。


---



## 获取用户对象的几种方式

> 先说结论，我们以后使用通常是将其封装为`LoginInfoUtil` 类进行调用的，使用SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 来返回对象

```java
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
```



下面展示其他获取对象的方法（不常用）

```java
package cn.believesun.controller;

import cn.believesun.pojo.TUser;
import cn.believesun.util.LoginInfoUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.security.Security;

@Controller
public class UserController {

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

```

