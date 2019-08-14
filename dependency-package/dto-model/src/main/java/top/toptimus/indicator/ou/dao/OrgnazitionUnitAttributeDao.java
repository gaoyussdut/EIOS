package top.toptimus.indicator.ou.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;

@Getter
@NoArgsConstructor
public class OrgnazitionUnitAttributeDao extends OrgnazitionUnitAttribute {
    private String ouId;

    /**
     * 构造函数
     */
    public OrgnazitionUnitAttributeDao(String ouId, String parentId, IndicatorType indicatorType, boolean isCU) {
        super(parentId, indicatorType, isCU);
        this.ouId = ouId;
    }

    public OrgnazitionUnitAttribute buildOrgnazitionUnitAttribute() {
        return new OrgnazitionUnitAttribute(
                this.getParentId()
                , this.getIndicatorType()
                , this.isCU()
        );
    }
}
