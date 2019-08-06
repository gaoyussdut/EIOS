package top.toptimus.indicator.IndicatorTarget.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.IndicatorTarget.statusEnum.IndicatorTargetStatusEnum;
import top.toptimus.indicator.ou.base.IndicatorType;

import java.util.Date;

/**
 * 业务指标dao
 *
 * @author gaoyu
 * @since 2019-08-06
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseIndicatorTarget {
    protected String targetCode;  //  目标编号
    protected String indicatorItemId; //  指标项id
    protected String year;    //  指标年度
    protected double targetCurrency;  //  目标总额
    protected String createUser;  //  发布人
    protected Date createDate;    //  创建日期
    protected Date updateDate;    //  最近修改日期
    protected IndicatorTargetStatusEnum indicatorTargetStatusEnum;    //  销售指标状态枚举
    protected IndicatorType indicatorType;    //  业务指标类型
}
