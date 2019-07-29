package top.toptimus.indicator.ouDelegate.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ouDelegate.base.BaseOuDelegate;

/**
 * 业务组织委托dao
 *
 * @author gaoyu
 * @since 2019-07-19
 */
@NoArgsConstructor
public class OuDelegateDao extends BaseOuDelegate {
    /*
    业务委托关系主键
    下推逻辑定义@link top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao
    挂在业务委托关系主键下面，一个业务委托关系会对应多个下推逻辑定义
     */
    @Getter
    private String delegeateId;

    /**
     * @param delegeateId           业务委托关系主键
     * @param ouId                  业务组织id
     * @param delegateOuId          委托业务组织id
     * @param delegateIndicatorType 委托业务类型
     */
    public OuDelegateDao(String delegeateId, String ouId, String delegateOuId, IndicatorType delegateIndicatorType) {
        super(ouId, delegateOuId, delegateIndicatorType);
        this.delegeateId = delegeateId;
    }

}
