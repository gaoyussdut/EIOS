package top.toptimus.indicator.ou.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;

/**
 * 业务组织属性
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@Getter
@NoArgsConstructor
public class OrgnazitionUnitAttribute {
    private String parentId;    //  上级业务组织ID
    private IndicatorType indicatorType;    //  业务指标类型，此处为了定义业务组织类型
    private boolean isCU;   //  是否CU
    //  TODO    业务组织详细描述

    /**
     * 存在上级id
     *
     * @param parentId      上级id
     * @param indicatorType 业务组织类型
     * @param isCU          是否cu
     */
    public OrgnazitionUnitAttribute(String parentId, IndicatorType indicatorType, boolean isCU) {
        this.parentId = parentId;
        this.indicatorType = indicatorType;
        this.isCU = isCU;
    }

    /**
     * 不存在上级id
     *
     * @param indicatorType 业务组织类型
     * @param isCU          是否cu
     */
    public OrgnazitionUnitAttribute(IndicatorType indicatorType, boolean isCU) {
        this.indicatorType = indicatorType;
        this.isCU = isCU;
    }
}
