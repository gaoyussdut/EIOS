package top.toptimus.indicator.ouDelegate.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;

/**
 * ou业务委托
 *
 * @author gaoyu
 * @since 2019-07-19
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class BaseOuDelegate {
    private String ouId;    //  业务组织id
    private String delegateOuId;    //  委托业务组织id
    private IndicatorType delegateIndicatorType;    //  委托业务类型
}
