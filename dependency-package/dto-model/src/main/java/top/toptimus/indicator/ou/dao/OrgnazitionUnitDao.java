package top.toptimus.indicator.ou.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.BaseOrgnazitionUnit;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;

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
     * 更新
     *
     * @param orgnazitionUnitDto 业务组织dto
     */
    public OrgnazitionUnitDao(OrgnazitionUnitDto orgnazitionUnitDto) {
        this.ouID = orgnazitionUnitDto.getOuID();
        this.ouCode = orgnazitionUnitDto.getOuCode();
        this.ouName = orgnazitionUnitDto.getOuName();
        this.createDate = orgnazitionUnitDto.getCreateDate();
        this.createUser = orgnazitionUnitDto.getCreateUser();
        this.enableDate = orgnazitionUnitDto.getEnableDate();
        this.level = orgnazitionUnitDto.getLevel();
        this.description = orgnazitionUnitDto.getDescription();

        if (null != orgnazitionUnitDto.getOrgnazitionUnitAttribute())
            this.orgnazitionUnitAttributes.put(
                    orgnazitionUnitDto.getOrgnazitionUnitAttribute().getIndicatorType()
                    , orgnazitionUnitDto.getOrgnazitionUnitAttribute()
            );
        this.disableDate = orgnazitionUnitDto.getDisableDate();
        this.disableUser = orgnazitionUnitDto.getDisableUser();

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
                    , this.level
                    , this.isDisabled
                    , this.disableDate
                    , this.disableUser
                    , this.description
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
                , this.level
                , this.isDisabled
                , this.disableDate
                , this.disableUser
                , this.description
        );
    }
}
