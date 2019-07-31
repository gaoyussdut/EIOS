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
    protected Date createDate;    //  创建时间
    protected String createUser;  //  创建人
    protected Date enableDate;  //  启用时间

    /*
        传统组织架构信息
     */
    protected String pOuID; //  上级ou id
    protected int level;    //  级别

    /*
        禁用信息
     */
    protected boolean isDisabled = false; //  是否禁用
    protected Date disableDate;   //  禁用时间
    protected String disableUser;  //  禁用人

    protected String description; //  描述

    /**
     * 构造函数
     *
     * @param ouCode     ou的编码
     * @param ouName     ou的名称
     * @param createDate 创建时间
     * @param createUser 创建人
     */
    public BaseOrgnazitionUnit(String ouCode, String ouName, Date createDate, String createUser) {
        this.ouCode = ouCode;
        this.ouName = ouName;
        this.createDate = createDate;
        this.createUser = createUser;
    }
}
