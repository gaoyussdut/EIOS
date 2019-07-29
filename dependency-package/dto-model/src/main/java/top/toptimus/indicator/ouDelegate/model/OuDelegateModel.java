package top.toptimus.indicator.ouDelegate.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ouDelegate.dao.OuDelegateDao;
import top.toptimus.indicator.ouDelegate.dto.OuDelegateDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 业务委托关系定义 TODO，一个业务委托关系会对应多个下推逻辑定义
 *
 * @author gaoyu
 * @since 2019-07-19
 */
public class OuDelegateModel {
    /*
        K:业务组织id，V：（K：业务组织类型，V:多个委托业务组织id和业务委托关系主键）
     */
    private Map<String, Map<IndicatorType, List<OuDelegateDto>>> ouIdIndicatorDelegateMap = new HashMap<>();

    /**
     * 构造函数
     *
     * @param ouDelegateDaos 业务组织委托dao列表
     */
    public OuDelegateModel(List<OuDelegateDao> ouDelegateDaos) {
        ouDelegateDaos.forEach(ouDelegateDao -> {
            if (this.ouIdIndicatorDelegateMap.containsKey(ouDelegateDao.getOuId())) {
                //  ou id补充逻辑
                if (this.ouIdIndicatorDelegateMap.get(ouDelegateDao.getOuId()).containsKey(ouDelegateDao.getDelegateIndicatorType())) {
                    //  业务组织类型补充逻辑
                    this.ouIdIndicatorDelegateMap
                            .get(ouDelegateDao.getOuId())
                            .get(ouDelegateDao.getDelegateIndicatorType())
                            .add(
                                    new OuDelegateDto(
                                            ouDelegateDao.getDelegeateId()
                                            , ouDelegateDao.getDelegateOuId()
                                    )
                            );
                } else {
                    //  业务组织类型新增逻辑
                    this.ouIdIndicatorDelegateMap
                            .get(ouDelegateDao.getOuId())
                            .put(
                                    ouDelegateDao.getDelegateIndicatorType()
                                    , Lists.newArrayList(
                                            new OuDelegateDto(
                                                    ouDelegateDao.getDelegeateId()
                                                    , ouDelegateDao.getDelegateOuId()
                                            )
                                    )
                            );
                }
            } else {
                //  ou id新增逻辑
                this.ouIdIndicatorDelegateMap.put(
                        ouDelegateDao.getOuId()
                        , new HashMap<IndicatorType, List<OuDelegateDto>>() {{
                            put(
                                    ouDelegateDao.getDelegateIndicatorType()
                                    , Lists.newArrayList(
                                            new OuDelegateDto(
                                                    ouDelegateDao.getDelegeateId()
                                                    , ouDelegateDao.getDelegateOuId()
                                            )
                                    )
                            );
                        }}
                );
            }
        });
    }


    /**
     * 取得某业务类型下的业务委托组织一览
     *
     * @param ouId          ou id
     * @param indicatorType 业务组织类型
     * @return 业务委托组织一览
     */
    public List<OuDelegateDto> getDelegateOuList(String ouId, IndicatorType indicatorType) {
        return this.ouIdIndicatorDelegateMap.get(ouId).get(indicatorType);
    }

    /**
     * 新增业务委托关系
     *
     * @param ouId          ou id
     * @param indicatorType 业务组织类型
     * @param delegateOuId  委托业务组织
     * @return 业务委托关系，用来持久化到数据库
     */
    public OuDelegateDao generateOuDelegate(String ouId, IndicatorType indicatorType, String delegateOuId) {
        String delegeateId = UUID.randomUUID().toString();  //  业务委托关系主键
        if (this.ouIdIndicatorDelegateMap.containsKey(ouId)) {
            if (this.ouIdIndicatorDelegateMap.get(ouId).containsKey(indicatorType)) {
                //  已经有旧的委托业务组织，要判断重复
                for (OuDelegateDto ouDelegateDto : this.ouIdIndicatorDelegateMap.get(ouId).get(indicatorType)) {
                    if (ouDelegateDto.getDelegateOuId().equals(delegateOuId)) {
                        //  重复
                        throw new RuntimeException("业务组织委托关系定义重复");
                    }
                }
                //  未重复
                this.ouIdIndicatorDelegateMap.get(ouId).get(indicatorType).add(
                        new OuDelegateDto(
                                delegeateId, delegateOuId   //  委托业务组织id和业务委托关系主键
                        )
                );
            } else {
                //  纯新增
                this.ouIdIndicatorDelegateMap.get(ouId).put(
                        indicatorType
                        , Lists.newArrayList(
                                new OuDelegateDto(
                                        delegeateId, delegateOuId   //  委托业务组织id和业务委托关系主键
                                )
                        )
                );
            }
        } else {
            throw new RuntimeException("业务组织不存在");
        }

        //  返回值，新增入数据库
        return new OuDelegateDao(
                delegeateId    //  业务委托关系主键
                , ouId
                , delegateOuId
                , indicatorType
        );
    }

    /**
     * 接触业务委托关系
     *
     * @param ouId          ou id
     * @param indicatorType 业务组织类型
     * @param delegateOuId  委托业务组织id
     * @return 业务委托关系主键，用来删除
     */
    public String releaseOuDelegate(String ouId, IndicatorType indicatorType, String delegateOuId) {
        String delegateId = null;
        for (OuDelegateDto ouDelegateDto : this.ouIdIndicatorDelegateMap.get(ouId).get(indicatorType)) {
            if (ouDelegateDto.getDelegateId().equals(delegateOuId)) {
                delegateId = ouDelegateDto.getDelegateId(); //  业务委托关系主键返回值赋值
                this.ouIdIndicatorDelegateMap.get(ouId).get(indicatorType).remove(ouDelegateDto);
            }
        }

        if (StringUtils.isEmpty(delegateId)) {
            throw new RuntimeException("系统异常，业务委托关系已经被解除");
        } else {
            //  返回业务委托关系主键，用来删除
            return delegateId;
        }
    }
}
