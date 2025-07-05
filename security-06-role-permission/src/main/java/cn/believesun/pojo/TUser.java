package cn.believesun.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户表
 * t_user
 */
@Data
public class TUser implements Serializable, UserDetails {
    private List<TRole> tRoleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 配置RoleList
        if (this.tRoleList.isEmpty()) {
            return List.of();
        }
        // 封装role对象
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(TRole tRole : tRoleList){
            authorities.add(new SimpleGrantedAuthority(tRole.getRole()));
        }
        return authorities;
    }

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
    @JsonIgnore
    public String getPassword() {
        return this.loginPwd;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    /**
     * 主键，自动增长，用户ID
     */
    private Integer id;

    /**
     * 登录账号
     */
    private String loginAct;

    /**
     * 登录密码
     */
    @JsonIgnore
    private String loginPwd;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户手机
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 账户是否没有过期，0已过期 1正常
     */
    private Integer accountNoExpired;

    /**
     * 密码是否没有过期，0已过期 1正常
     */
    private Integer credentialsNoExpired;

    /**
     * 账号是否没有锁定，0已锁定 1正常
     */
    private Integer accountNoLocked;

    /**
     * 账号是否启用，0禁用 1启用
     */
    private Integer accountEnabled;

    /**
     * 创建时间
     */
    // 注意：新版本的Jackson好像默认就是 东八区，所以这里加不加都行。
    @JsonFormat
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 编辑时间
     */
    @JsonFormat
    private LocalDateTime editTime;

    /**
     * 编辑人
     */
    private Integer editBy;

    /**
     * 最近登录时间
     */
    @JsonFormat
    private LocalDateTime lastLoginTime;

    private static final long serialVersionUID = 1L;
}