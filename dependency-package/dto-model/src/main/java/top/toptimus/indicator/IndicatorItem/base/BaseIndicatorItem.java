package top.toptimus.indicator.IndicatorItem.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;

/**
 * 业务指标项定义基类
 *
 * @author gaoyu
 * @since 2019-08-06
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseIndicatorItem {
    private String indicatorItemName; //  指标项名称
    private IndicatorType indicatorType;    //  业务指标类型
}
