package top.toptimus.indicator.ou.dto;

import lombok.Getter;
import top.toptimus.indicator.ou.base.BaseOrgnazitionUnit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * OU基础信息
 *
 * @author gaoyu
 * @since 2019-07-29
 */
public class OrgnazitionUnitBaseInfoDto extends BaseOrgnazitionUnit {
    @Getter
    private String ouID;    //  ou的id
    @Getter
    private String pOuName; //  上级ou名称

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
     * @param pOuName     上级业务组织名称
     */
    public OrgnazitionUnitBaseInfoDto(String ouID, String ouCode, String ouName, Date createDate, String createUser, Date enableDate, String pOuID, int level, boolean isDisabled, Date disableDate, String disableUser, String description, boolean isEntity, String pOuName) {
        super(ouCode, ouName, createDate, createUser, enableDate, pOuID, level, isDisabled, disableDate, disableUser, description, isEntity);
        this.ouID = ouID;
        this.pOuName = pOuName;
    }

    /**
     * dao转dto用
     *
     * @param rs 查询结果集
     */
    public OrgnazitionUnitBaseInfoDto(ResultSet rs) throws SQLException {
        super(rs.getString("ou_code")
                , rs.getString("ou_name")
                , rs.getDate("create_date")
                , rs.getString("create_user")
                , rs.getDate("enable_date")
                , rs.getString("p_ou_id")
                , rs.getInt("level")
                , rs.getBoolean("is_disabled")
                , rs.getDate("disable_date")
                , rs.getString("disable_user")
                , rs.getString("description")
                , rs.getBoolean("is_entity")
        );
        this.ouID = rs.getString("ou_id");
        this.pOuName = rs.getString("p_ou_name");
    }

    /**
     * 补充上级业务组织名称
     *
     * @param pOuName 上级业务组织名称
     * @return this
     */
    public OrgnazitionUnitBaseInfoDto buildPOuName(String pOuName) {
        this.pOuName = pOuName;
        return this;
    }

    /**
     * 构造函数，初始化基础属性，不带业务组织级别
     *
     * @param ouID        ou id
     * @param ouCode      ou编码
     * @param ouName      ou名称
     * @param createDate  创建时间
     * @param createUser  创建人
     * @param description 描述
     */
    public OrgnazitionUnitBaseInfoDto(String ouID, String ouCode, String ouName, Date createDate, String createUser, boolean isEntity, String description) {
        super();
        this.ouID = ouID;
        this.ouCode = ouCode;
        this.ouName = ouName;
        this.createDate = createDate;
        this.createUser = createUser;
        this.isEntity = isEntity;
        this.description = description;
    }

    /**
     * 更新业务组织级别为顶层
     *
     * @return this
     */
    public OrgnazitionUnitBaseInfoDto buildTopLevel() {
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
    public OrgnazitionUnitBaseInfoDto buildOrgTree(int level, String pOuId) {
        this.level = level;
        this.pOuID = pOuId;
        return this;
    }
}
