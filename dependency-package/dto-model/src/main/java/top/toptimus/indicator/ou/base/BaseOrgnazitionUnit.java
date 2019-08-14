package top.toptimus.indicator.ou.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * OU基类
 *
 * @author gaoyu
 * @since 2019-07-19
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseOrgnazitionUnit {
    protected String ouCode;  //  ou的编码
    protected String ouName;  //  ou的名称
    protected Date createDate = null;    //  创建时间
    protected String createUser;  //  创建人
    protected Date enableDate = null;  //  启用时间

    /*
        传统组织架构信息
     */
    protected String pOuID; //  上级ou id
    protected int level;    //  级别

    /*
        禁用信息
     */
    protected boolean isDisabled = false; //  是否禁用
    protected Date disableDate = null;   //  禁用时间
    protected String disableUser;  //  禁用人

    protected String description; //  描述

    protected boolean isEntity; //  是否业务组织实体
}
