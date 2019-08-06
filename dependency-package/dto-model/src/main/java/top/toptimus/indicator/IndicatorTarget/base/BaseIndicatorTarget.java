package top.toptimus.indicator.IndicatorTarget.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.IndicatorTarget.statusEnum.IndicatorTargetStatusEnum;

import java.util.Date;

/**
 * 销售指标dao
 *
 * @author gaoyu
 * @since 2019-08-06
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseIndicatorTarget {
    private String targetCode;  //  目标编号
    private String indicatorItemId; //  指标项id
    private String year;    //  指标年度
    private double targetCurrency;  //  目标总额
    private String createUser;  //  发布人
    private Date createDate;    //  创建日期
    private Date updateDate;    //  最近修改日期
    private IndicatorTargetStatusEnum indicatorTargetStatusEnum;    //  销售指标状态枚举

}
