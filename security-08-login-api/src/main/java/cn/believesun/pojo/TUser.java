package cn.believesun.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户表
 * t_user
 */
@Data
public class TUser implements Serializable , UserDetails {

    private static final long serialVersionUID = 1L;
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
    @JsonIgnore
    private Integer accountNoExpired;

    /**
     * 密码是否没有过期，0已过期 1正常
     */
    @JsonIgnore
    private Integer credentialsNoExpired;

    /**
     * 账号是否没有锁定，0已锁定 1正常
     */
    @JsonIgnore
    private Integer accountNoLocked;

    /**
     * 账号是否启用，0禁用 1启用
     */
    @JsonIgnore
    private Integer accountEnabled;

    /**
     * 创建时间
     */
    @JsonFormat
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 编辑时间F
     */
    @JsonFormat
    private Date editTime;

    /**
     * 编辑人
     */
    private Integer editBy;

    /**
     * 最近登录时间
     */
    @JsonFormat
    private Date lastLoginTime;

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNoExpired > 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNoLocked > 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNoExpired > 0;
    }

    @Override
    public boolean isEnabled() {
        return this.accountEnabled > 0;
    }

    @Override
    @JsonIgnore
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
        return this.loginAct;
    }
}