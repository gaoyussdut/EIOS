package top.toptimus.indicator.IndicatorTarget.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.IndicatorTarget.base.BaseIndicatorTarget;
import top.toptimus.indicator.IndicatorTarget.dto.IndicatorTargetDTO;

/**
 * 业务指标dao
 *
 * @author gaoyu
 * @since 2019-08-06
 */
@Getter
@NoArgsConstructor
public class IndicatorTargetDao extends BaseIndicatorTarget {
    private String id;  //  数据库主键

    public IndicatorTargetDao(IndicatorTargetDTO indicatorTargetDTO) {
        super(
                indicatorTargetDTO.getTargetCode()
                , indicatorTargetDTO.getIndicatorItemId()
                , indicatorTargetDTO.getYear()
                , indicatorTargetDTO.getTargetCurrency()
                , indicatorTargetDTO.getCreateUser()
                , indicatorTargetDTO.getCreateDate()
                , indicatorTargetDTO.getUpdateDate()
                , indicatorTargetDTO.getIndicatorTargetStatusEnum()
                , indicatorTargetDTO.getIndicatorType()
        );
        this.id = indicatorTargetDTO.getId();
    }

    /*
        dao中不提供buildIndicatorTargetDTO方法，indicatorItemName是通过left join带出来的
        本dao只提供持久化功能
     */
}
