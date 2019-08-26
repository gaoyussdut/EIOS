package top.toptimus.indicator.role.base;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 角色基类
 *
 * @author gaoyu
 * @since 2019-08-21
 */
@Getter
@NoArgsConstructor
public abstract class BaseRole {
    protected String roleName;    //  角色名称
    protected String createUser;  //  创建人
    protected Date createDate;    //  创建时间
    protected boolean isEnable = true;   //  是否启用

    /**
     * 构造函数
     *
     * @param roleName   角色名称
     * @param createUser 创建人
     * @param createDate 创建时间
     */
    public BaseRole(String roleName, String createUser, Date createDate) {
        this.roleName = roleName;
        this.createUser = createUser;
        this.createDate = createDate;
    }
}
