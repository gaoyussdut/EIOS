package top.toptimus.indicator.indicatorBill.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.indicatorBill.base.BaseIndicatorOuRel;

/**
 * 指标单据转换配置关系dto
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@Getter
@NoArgsConstructor
public class IndicatorOuRelDto extends BaseIndicatorOuRel {
    private String id;  //  业务主键

    public IndicatorOuRelDto(String id, String sourceOuId, String targetOuId, String sourceMetaId, String targetMetaId, boolean isPushDown, String procedureName, IndicatorType indicatorType) {
        super(sourceOuId, targetOuId, sourceMetaId, targetMetaId, isPushDown, procedureName, indicatorType);
        this.id = id;
    }

}
