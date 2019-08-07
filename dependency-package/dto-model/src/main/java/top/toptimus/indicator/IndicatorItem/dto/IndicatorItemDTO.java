package top.toptimus.indicator.IndicatorItem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.IndicatorItem.base.BaseIndicatorItem;
import top.toptimus.indicator.ou.base.IndicatorType;

/**
 * 业务指标项定义dto
 *
 * @author gaoyu
 * @since 2019-08-06
 */
@Getter
@NoArgsConstructor
public class IndicatorItemDTO extends BaseIndicatorItem {
    private String indicatorItemId; //  指标项id

    public IndicatorItemDTO(String indicatorItemId, String indicatorItemName, IndicatorType indicatorType) {
        super(indicatorItemName, indicatorType);
        this.indicatorItemId = indicatorItemId;
    }
}
