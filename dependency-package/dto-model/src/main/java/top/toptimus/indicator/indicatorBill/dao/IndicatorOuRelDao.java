package top.toptimus.indicator.indicatorBill.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.indicatorBill.base.BaseIndicatorOuRel;
import top.toptimus.indicator.indicatorBill.dto.IndicatorOuRelDto;

/**
 * 指标单据转换配置关系dao
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@Getter
@NoArgsConstructor
public class IndicatorOuRelDao extends BaseIndicatorOuRel {
    private String id;  //  业务主键

    public IndicatorOuRelDao(String id, String sourceOuId, String targetOuId, String sourceMetaId, String targetMetaId, boolean isPushDown, String procedureName, IndicatorType indicatorType) {
        super(sourceOuId, targetOuId, sourceMetaId, targetMetaId, isPushDown, procedureName, indicatorType);
        this.id = id;
    }

    public IndicatorOuRelDto buildIndicatorOuRelDto() {
        return new IndicatorOuRelDto(
                this.id
                , this.getSourceOuId()
                , this.getTargetOuId()
                , this.getSourceMetaId()
                , this.getTargetMetaId()
                , this.isPushDown()
                , this.getProcedureName()
                , this.getIndicatorType()
        );
    }
}
