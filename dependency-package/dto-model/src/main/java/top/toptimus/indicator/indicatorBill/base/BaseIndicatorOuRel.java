package top.toptimus.indicator.indicatorBill.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;

/**
 * 指标单据meta和ou的关系配置，即指标单据转换配置关系
 * 分为记录ou和指标meta的关系
 * 指标meta和子meta的关系，包括手动新建的meta和下推的meta
 * <p>
 * <p>
 * 指标单据的meta定义
 * <p>
 * 包括单据meta定义
 * 资源定义
 * 指标库定义
 * 去他娘的，这些的定义都他妈在前台组件化封装好，去他大爷的
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseIndicatorOuRel {
    private String sourceOuId;    //  源ou的id
    private String targetOuId;  //  目标ou的id
    private String sourceMetaId;  //  源单指标meta定义
    private String targetMetaId;    //  目标指标meta定义
    private boolean isPushDown; //  是否下推，如果是下推就不是新建分解   TODO，枚举
    private String procedureName;  //  如果是下推，要执行的存储过程名（或者流计算或者restful api）
    private IndicatorType indicatorType;    //  业务类型
}
