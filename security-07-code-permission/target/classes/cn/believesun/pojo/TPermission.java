package cn.believesun.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * 权限表
 * t_permission
 */
@Data
public class TPermission implements Serializable {
    /**
     * 主键ID自增
     */
    private Integer id;

    /**
     * 资源名
     */
    private String name;

    /**
     * 资源代码(权限标识符)
     */
    private String code;

    /**
     * 统一资源标识符
     */
    private String url;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 父资源ID
     */
    private Integer parentId;

    /**
     * 资源排序
     */
    private Integer orderNo;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单对应要渲染的Vue组件名称
     */
    private String component;

    private static final long serialVersionUID = 1L;
}