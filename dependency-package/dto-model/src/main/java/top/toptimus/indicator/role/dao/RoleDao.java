package top.toptimus.indicator.role.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.role.base.BaseRole;

import java.util.Date;

/**
 * 角色dao
 *
 * @author gaoyu
 * @since 2019-08-21
 */
@Getter
@NoArgsConstructor
public class RoleDao extends BaseRole {
    private String id;  //  角色id

    /**
     * 构造函数
     *
     * @param id         角色id
     * @param roleName   角色名称
     * @param createUser 创建人
     * @param createDate 创建时间
     */
    public RoleDao(String id, String roleName, String createUser, Date createDate) {
        super(roleName, createUser, createDate);
        this.id = id;
    }
}
