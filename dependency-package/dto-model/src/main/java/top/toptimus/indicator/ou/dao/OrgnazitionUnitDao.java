package top.toptimus.indicator.ou.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.formula.util.DateUtil;
import top.toptimus.indicator.ou.base.BaseOrgnazitionUnit;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * OU定义,包含所有业务组织属性，本质上是个数据库DAO
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@Getter
@NoArgsConstructor
public class OrgnazitionUnitDao extends BaseOrgnazitionUnit {
    protected String ouID;    //  ou的id

    /*
        K:业务指标类型，此处为了定义业务组织类型,V:业务组织属性
     */
    private Map<IndicatorType, OrgnazitionUnitAttribute> orgnazitionUnitAttributes = new HashMap<>();

    /**
     * 更新业务组织，不带业务组织属性
     *
     * @param orgnazitionUnitBaseInfoDto 业务组织dto
     */
    public OrgnazitionUnitDao(OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto) {
        this.ouID = orgnazitionUnitBaseInfoDto.getOuID();
        this.ouCode = orgnazitionUnitBaseInfoDto.getOuCode();
        this.ouName = orgnazitionUnitBaseInfoDto.getOuName();
        this.createDate = null == orgnazitionUnitBaseInfoDto.getCreateDate() ? new Date() : orgnazitionUnitBaseInfoDto.getCreateDate();
        this.createUser = orgnazitionUnitBaseInfoDto.getCreateUser();
        this.enableDate = null == orgnazitionUnitBaseInfoDto.getEnableDate() ? DateUtil.parseDate("1900-01-01", "yyyy-MM-DD") : orgnazitionUnitBaseInfoDto.getEnableDate(); //  TODO    bug
        this.pOuID = orgnazitionUnitBaseInfoDto.getPOuID();
        this.level = orgnazitionUnitBaseInfoDto.getLevel();
        this.description = orgnazitionUnitBaseInfoDto.getDescription();

        this.disableDate = null == orgnazitionUnitBaseInfoDto.getDisableDate() ? DateUtil.parseDate("1900-01-01", "yyyy-MM-DD") : orgnazitionUnitBaseInfoDto.getDisableDate();  //  TODO bug
        this.disableUser = orgnazitionUnitBaseInfoDto.getDisableUser();
        this.isEntity = orgnazitionUnitBaseInfoDto.isEntity();
    }

    public OrgnazitionUnitDao(OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto, OrgnazitionUnitAttribute orgnazitionUnitAttribute) {
        this.ouID = orgnazitionUnitBaseInfoDto.getOuID();
        this.ouCode = orgnazitionUnitBaseInfoDto.getOuCode();
        this.ouName = orgnazitionUnitBaseInfoDto.getOuName();
        this.createDate = orgnazitionUnitBaseInfoDto.getCreateDate();
        this.createUser = orgnazitionUnitBaseInfoDto.getCreateUser();
        this.enableDate = orgnazitionUnitBaseInfoDto.getEnableDate();
        this.pOuID = orgnazitionUnitBaseInfoDto.getPOuID();
        this.level = orgnazitionUnitBaseInfoDto.getLevel();
        this.description = orgnazitionUnitBaseInfoDto.getDescription();

        this.disableDate = orgnazitionUnitBaseInfoDto.getDisableDate();
        this.disableUser = orgnazitionUnitBaseInfoDto.getDisableUser();
        this.isEntity = orgnazitionUnitBaseInfoDto.isEntity();

        if (null != orgnazitionUnitAttribute)
            this.orgnazitionUnitAttributes.put(
                    orgnazitionUnitAttribute.getIndicatorType()
                    , orgnazitionUnitAttribute
            );
    }

    /**
     * 根据业务指标类型创建ou dto
     *
     * @param indicatorType 业务指标类型
     * @return ou dto
     */
    public OrgnazitionUnitDto buildOrgnazitionUnitDto(IndicatorType indicatorType) {
        if (this.orgnazitionUnitAttributes.containsKey(indicatorType)) {
            return new OrgnazitionUnitDto(
                    this.ouID
                    , this.ouCode
                    , this.ouName
                    , this.createDate
                    , this.createUser
                    , this.enableDate
                    , this.pOuID
                    , this.level
                    , this.isDisabled
                    , this.disableDate
                    , this.disableUser
                    , this.description
                    , this.isEntity
            ).buildOrgnazitionUnitAttribute(this.orgnazitionUnitAttributes.get(indicatorType));
        } else {
            throw new RuntimeException("业务组织类型" + indicatorType.name() + "不存在");
        }
    }

    /**
     * 返回ou基础信息
     *
     * @return ou基础信息
     */
    public OrgnazitionUnitBaseInfoDto buildOrgnazitionUnitBaseInfoDto() {
        return new OrgnazitionUnitBaseInfoDto(
                this.ouID
                , this.ouCode
                , this.ouName
                , this.createDate
                , this.createUser
                , this.enableDate
                , this.pOuID
                , this.level
                , this.isDisabled
                , this.disableDate
                , this.disableUser
                , this.description
                , this.isEntity
        );
    }
}
