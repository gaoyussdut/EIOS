package top.toptimus.indicator.ou.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.BaseOrgnazitionUnit;

import java.util.Date;

/**
 * OU定义,包含单一业务组织属性，dto
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@Getter
@NoArgsConstructor
public class OrgnazitionUnitDto extends BaseOrgnazitionUnit {
    protected String ouID;    //  ou的id
    private OrgnazitionUnitAttribute orgnazitionUnitAttribute;  //  业务组织属性

    /**
     * dao转dto用
     *
     * @param ouID        ou id
     * @param ouCode      ou编码
     * @param ouName      ou名称
     * @param createDate  创建时间
     * @param createUser  创建人
     * @param enableDate  启用时间
     * @param level       级别
     * @param isDisabled  是否禁用
     * @param disableDate 禁用时间
     * @param disableUser 禁用人
     * @param description 描述
     */
    public OrgnazitionUnitDto(String ouID, String ouCode, String ouName, Date createDate, String createUser, Date enableDate, String pOuID, int level, boolean isDisabled, Date disableDate, String disableUser, String description) {
        super(ouCode, ouName, createDate, createUser, enableDate, pOuID, level, isDisabled, disableDate, disableUser, description);
        this.ouID = ouID;
    }

    /**
     * 业务组织基础信息
     *
     * @param ouID       ou id
     * @param ouCode     ou编码
     * @param ouName     ou名称
     * @param createDate 创建时间
     * @param createUser 创建人
     */
    public OrgnazitionUnitDto(String ouID, String ouCode, String ouName, Date createDate, String createUser) {
        super(ouCode, ouName, createDate, createUser);
        this.ouID = ouID;
    }

    /**
     * 更新业务组织级别为顶层
     *
     * @return this
     */
    public OrgnazitionUnitDto buildTopLevel() {
        this.level = 0;
        this.pOuID = null;
        return this;
    }

    /**
     * 更新业务组织级别
     *
     * @param level 级别
     * @return this
     */
    public OrgnazitionUnitDto buildOrgTree(int level, String pOuId) {
        this.level = level;
        this.pOuID = pOuId;
        return this;
    }

    /**
     * 初始化构造函数
     *
     * @param ouCode      ou的编码
     * @param ouName      ou的名称
     * @param createUser  创建人
     * @param description 描述
     */
    public OrgnazitionUnitDto(String ouID, String ouCode, String ouName, String createUser, String description) {
        this.ouID = ouID;
        this.ouCode = ouCode;
        this.ouName = ouName;
        this.createDate = new Date();
        this.createUser = createUser;
        this.enableDate = new Date();
        this.description = description;
    }

    /**
     * 补充业务组织属性
     *
     * @param orgnazitionUnitAttribute 业务组织属性
     * @return this
     */
    public OrgnazitionUnitDto buildOrgnazitionUnitAttribute(OrgnazitionUnitAttribute orgnazitionUnitAttribute) {
        this.orgnazitionUnitAttribute = orgnazitionUnitAttribute;
        return this;
    }
}
