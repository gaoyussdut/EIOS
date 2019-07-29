package top.toptimus.indicator.indicatorBill.model;

import com.google.common.collect.Lists;
import top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao;
import top.toptimus.indicator.indicatorBill.dao.IndicatorTokenDefineDao;
import top.toptimus.indicator.indicatorBill.dto.IndicatorOuRelDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录指标关系的model,entity层封装
 *
 * @author gaoyu
 * @since 2019-7-16
 */
public class IndicatorBillRelModel {
    /*
        K：源ou id，V：指标单据meta和ou的关系配置
     */
    private Map<String, List<IndicatorOuRelDto>> indicatorOURelDaoMap = new HashMap<>();
    private List<IndicatorOuRelDto> indicatorOuRelDtos; //  非KV记录

    /**
     * 构造函数，记录指标单据meta和ou的关系配置
     *
     * @param indicatorOURelDaos 指标单据meta和ou的关系配置
     */
    public IndicatorBillRelModel(List<IndicatorOuRelDao> indicatorOURelDaos) {
        indicatorOURelDaos.forEach(indicatorOURelDao -> {
            if (this.indicatorOURelDaoMap.containsKey(indicatorOURelDao.getSourceOuId())) {
                this.indicatorOURelDaoMap.get(indicatorOURelDao.getSourceOuId()).add(indicatorOURelDao.buildIndicatorOuRelDto());
            } else {
                this.indicatorOURelDaoMap.put(indicatorOURelDao.getSourceOuId(), Lists.newArrayList(indicatorOURelDao.buildIndicatorOuRelDto()));
            }
        });
        this.indicatorOuRelDtos = new ArrayList<IndicatorOuRelDto>() {{
            indicatorOURelDaos.forEach(indicatorOuRelDao -> add(indicatorOuRelDao.buildIndicatorOuRelDto()));
        }};
    }

    /**
     * 根据model缓存中的指标单据meta和ou的关系配置KV生成指标单据转换日志，用来存储
     *
     * @param relId         转换的配置项id
     * @param sourceTokenId 源单token id
     * @param targetTokenId 目标单token id
     * @return 指标单据转换日志
     */
    public IndicatorTokenDefineDao buildIndicatorTokenDefineDao(
            String relId
            , String sourceTokenId
            , String targetTokenId
    ) {
        return new IndicatorTokenDefineDao(
                this.getIndicatorOuRelDao(relId)
                , sourceTokenId
                , targetTokenId
        );
    }

    /**
     * 根据业务组织单元id取得指标单据转换配置关系列表
     *
     * @param ouId ou id
     * @return 指标单据转换配置关系列表
     */
    public List<IndicatorOuRelDto> getIndicatorOuRelDaosByOuId(String ouId) {
        return this.indicatorOURelDaoMap.get(ouId);
    }

    /**
     * 根据id查询转换配置关系
     *
     * @param relId 业务主键
     * @return 指标单据转换配置关系
     */
    private IndicatorOuRelDto getIndicatorOuRelDao(String relId) {
        for (IndicatorOuRelDto indicatorOuRelDto : this.indicatorOuRelDtos) {
            if (relId.equals(indicatorOuRelDto.getId())) {
                return indicatorOuRelDto;
            }
        }
        throw new RuntimeException("配置错误");
    }

}
