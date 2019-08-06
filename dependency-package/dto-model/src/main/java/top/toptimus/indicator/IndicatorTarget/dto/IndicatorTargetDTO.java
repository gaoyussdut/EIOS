package top.toptimus.indicator.IndicatorTarget.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.IndicatorTarget.base.BaseIndicatorTarget;
import top.toptimus.indicator.IndicatorTarget.statusEnum.IndicatorTargetStatusEnum;

import java.util.Date;

/**
 * 销售指标dto
 *
 * @author gaoyu
 * @since 2019-08-06
 */
@Getter
@NoArgsConstructor
public class IndicatorTargetDTO extends BaseIndicatorTarget {
    private String id;  //  主键
    private String indicatorItemName; //  指标项名称，数据库join查出来，保存时候不存

    /**
     * 构造函数
     *
     * @param id                        主键
     * @param targetCode                目标编号
     * @param indicatorItemId           指标项id
     * @param indicatorItemName         指标项名称，数据库join查出来，保存时候不存
     * @param year                      指标年度
     * @param targetCurrency            目标总额
     * @param createUser                发布人
     * @param createDate                创建日期
     * @param updateDate                最近修改日期
     * @param indicatorTargetStatusEnum 销售指标状态枚举
     */
    public IndicatorTargetDTO(
            String id
            , String targetCode
            , String indicatorItemId
            , String indicatorItemName
            , String year
            , double targetCurrency
            , String createUser
            , Date createDate
            , Date updateDate
            , IndicatorTargetStatusEnum indicatorTargetStatusEnum
    ) {
        super(targetCode, indicatorItemId, year, targetCurrency, createUser, createDate, updateDate, indicatorTargetStatusEnum);
        this.id = id;
        this.indicatorItemName = indicatorItemName;
    }
}
