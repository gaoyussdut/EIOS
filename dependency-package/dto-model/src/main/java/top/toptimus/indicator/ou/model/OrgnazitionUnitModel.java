package top.toptimus.indicator.ou.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OU充血模型
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@NoArgsConstructor
public class OrgnazitionUnitModel {
    @Getter
    private Map<String, OrgnazitionUnitDao> orgnazitionUnitMap = new HashMap<>();   //  OU列表
    /*
        各类型业务组织的dto
     */
    private Map<IndicatorType, Map<String, OrgnazitionUnitDto>> orgnazitionAttributeMap = new HashMap<>();  //  不同属性下的组织列表

    /**
     * 构造函数，初始化OU列表
     *
     * @param orgnazitionUnitDaos 数据库中的ou列表
     */
    public OrgnazitionUnitModel(List<OrgnazitionUnitDao> orgnazitionUnitDaos) {
        orgnazitionUnitDaos.forEach(orgnazitionUnitDao -> {
            this.orgnazitionUnitMap.put(orgnazitionUnitDao.getOuID(), orgnazitionUnitDao);   //  填充OU列表

            //  遍历组织下的多属性，生成不同属性下的组织列表
            orgnazitionUnitDao.getOrgnazitionUnitAttributes().keySet().forEach(indicatorType -> this.generateOrgnazitionAttributeMap(indicatorType, orgnazitionUnitDao));
        });
    }

    /**
     * 不同属性下的组织列表
     *
     * @param indicatorType      业务指标类型
     * @param orgnazitionUnitDao OU定义
     */
    private void generateOrgnazitionAttributeMap(IndicatorType indicatorType, OrgnazitionUnitDao orgnazitionUnitDao) {
        if (this.orgnazitionAttributeMap.containsKey(indicatorType)) {
            this.orgnazitionAttributeMap.get(indicatorType).put(orgnazitionUnitDao.getOuID(), orgnazitionUnitDao.buildOrgnazitionUnitDto(indicatorType));
        } else {
            this.orgnazitionAttributeMap.put(
                    indicatorType
                    , new HashMap<String, OrgnazitionUnitDto>() {{
                        put(orgnazitionUnitDao.getOuID(), orgnazitionUnitDao.buildOrgnazitionUnitDto(indicatorType));
                    }}
            );
        }
    }

    /**
     * 不同属性下的组织列表
     *
     * @param indicatorType      业务指标类型
     * @param orgnazitionUnitDto OU定义,包含单一业务组织属性
     */
    private void generateOrgnazitionAttributeMap(IndicatorType indicatorType, OrgnazitionUnitDto orgnazitionUnitDto) {
        if (this.orgnazitionAttributeMap.containsKey(indicatorType)) {
            this.orgnazitionAttributeMap.get(indicatorType).put(orgnazitionUnitDto.getOuID(), orgnazitionUnitDto);
        } else {
            this.orgnazitionAttributeMap.put(
                    indicatorType
                    , new HashMap<String, OrgnazitionUnitDto>() {{
                        put(orgnazitionUnitDto.getOuID(), orgnazitionUnitDto);
                    }}
            );
        }
    }

    /**
     * 更新业务组织
     *
     * @param orgnazitionUnitDto 业务组织dto
     */
    public OrgnazitionUnitModel updateOrgnazitionUnit(OrgnazitionUnitDto orgnazitionUnitDto) {
        //  OU列表更新
        if (this.orgnazitionUnitMap.containsKey(orgnazitionUnitDto.getOuID())) {
            //  更新逻辑
            this.orgnazitionUnitMap.get(orgnazitionUnitDto.getOuID())
                    .getOrgnazitionUnitAttributes()
                    .put(
                            orgnazitionUnitDto.getOrgnazitionUnitAttribute().getIndicatorType()
                            , orgnazitionUnitDto.getOrgnazitionUnitAttribute()
                    );
        } else {
            //  新增逻辑
            this.orgnazitionUnitMap.put(orgnazitionUnitDto.getOuID(), new OrgnazitionUnitDao(orgnazitionUnitDto));
        }

        //  遍历组织下的多属性，生成不同属性下的组织列表
        this.generateOrgnazitionAttributeMap(orgnazitionUnitDto.getOrgnazitionUnitAttribute().getIndicatorType(), orgnazitionUnitDto);

        return this;
    }

    /**
     * 取得上级业务组织
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 上级业务组织dto
     */
    public OrgnazitionUnitDto getParentOrgnazitionUnit(String ouId, IndicatorType indicatorType) {
        if (this.orgnazitionAttributeMap.containsKey(indicatorType)) {
            return this.orgnazitionAttributeMap.get(indicatorType).get(
                    this.orgnazitionAttributeMap.get(indicatorType).get(ouId).getOrgnazitionUnitAttribute().getParentId()
            );
        } else {
            throw new RuntimeException(ouId + "没有上级业务组织");
        }
    }

    /**
     * 取得下级业务组织列表
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 下级业务组织dto列表
     */
    public List<OrgnazitionUnitDto> getChildOrgnazitionUnits(String ouId, IndicatorType indicatorType) {
        if (this.orgnazitionAttributeMap.containsKey(indicatorType)) {
            return this.getChildOrgnazitionUnits(ouId, this.orgnazitionAttributeMap.get(indicatorType));
        } else {
            throw new RuntimeException(ouId + "没有上级业务组织");
        }
    }

    /**
     * 取得下级业务组织列表
     *
     * @param parentOuID 上级ou id
     * @param unitMap    业务组织列表
     * @return 下级业务组织dto列表
     */
    private List<OrgnazitionUnitDto> getChildOrgnazitionUnits(String parentOuID, Map<String, OrgnazitionUnitDto> unitMap) {
        return new ArrayList<OrgnazitionUnitDto>() {
            {
                unitMap.keySet().forEach(ouId -> {
                    if (
                            StringUtils.isNotEmpty(unitMap.get(ouId).getOrgnazitionUnitAttribute().getParentId())   //  判空
                                    && unitMap.get(ouId).getOrgnazitionUnitAttribute().getParentId().equals(parentOuID)
                            ) {
                        add(unitMap.get(ouId));
                    }
                });
            }
        };
    }

    /**
     * 取得当前业务组织属性
     *
     * @param ouId 业务组织id
     * @return 业务组织属性
     */
    public OrgnazitionUnitDao getOrgnazitionUnitDao(String ouId) {
        return this.getOrgnazitionUnitMap().get(ouId);
    }

    /**
     * 获取某一属性下的业务组织树
     *
     * @param indicatorType 业务组织类型
     * @return 业务组织树
     */
    public Map<String, OrgnazitionUnitDto> getOrgnazitionTreeViewByAttribute(IndicatorType indicatorType) {
        return this.orgnazitionAttributeMap.get(indicatorType);
    }
}
