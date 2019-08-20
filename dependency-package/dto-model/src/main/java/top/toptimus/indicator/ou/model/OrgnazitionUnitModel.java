package top.toptimus.indicator.ou.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao;
import top.toptimus.indicator.indicatorBill.model.IndicatorBillRelModel;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitAttributeDao;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;

import java.util.*;

/**
 * OU充血模型
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@NoArgsConstructor
public class OrgnazitionUnitModel {
    @Getter
    private Map<String, OrgnazitionUnitDao> orgnazitionUnitMap = new HashMap<>();   //  OU列表 ,K:ou id
    /*
        各类型业务组织的dto
     */
    private Map<IndicatorType, Map<String, OrgnazitionUnitDto>> orgnazitionAttributeMap = new HashMap<>();  //  不同属性下的组织列表

    @Getter
    private IndicatorBillRelModel indicatorBillRelModel = new IndicatorBillRelModel();    //  记录指标关系的model

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
     * 维护业务组织属性
     *
     * @param orgnazitionUnitAttributeDaos 业务组织属性列表
     */
    public void buildorgnazitionUnitAttributes(List<OrgnazitionUnitAttributeDao> orgnazitionUnitAttributeDaos) {
        Map<String, List<OrgnazitionUnitAttribute>> orgnazitionUnitAttributeMap = new HashMap<>();

        for (OrgnazitionUnitAttributeDao orgnazitionUnitAttributeDao : orgnazitionUnitAttributeDaos) {
            if (orgnazitionUnitAttributeMap.containsKey(orgnazitionUnitAttributeDao.getOuId())) {
                //  存在ouid
                orgnazitionUnitAttributeMap.get(orgnazitionUnitAttributeDao.getOuId()).add(orgnazitionUnitAttributeDao.buildOrgnazitionUnitAttribute());
            } else {
                //  新增
                orgnazitionUnitAttributeMap.put(orgnazitionUnitAttributeDao.getOuId(), Lists.newArrayList(orgnazitionUnitAttributeDao.buildOrgnazitionUnitAttribute()));
            }
        }
        for (String ouId : orgnazitionUnitAttributeMap.keySet()) {
            this.updateOrgnazitionUnitAttributes(ouId, orgnazitionUnitAttributeMap.get(ouId));
        }
    }


    /**
     * 更新业务组织
     *
     * @param orgnazitionUnitBaseInfoDto 业务组织dto,不带业务组织属性
     */
    public OrgnazitionUnitModel updateOrgnazitionUnit(OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto) {
        this.orgnazitionUnitMap.put(orgnazitionUnitBaseInfoDto.getOuID(), new OrgnazitionUnitDao(orgnazitionUnitBaseInfoDto));
        return this;
    }

    /**
     * 更新业务组织
     *
     * @param ouId                      业务组织id
     * @param orgnazitionUnitAttributes 业务组织属性列表
     * @return this
     */
    public OrgnazitionUnitModel updateOrgnazitionUnitAttributes(String ouId, List<OrgnazitionUnitAttribute> orgnazitionUnitAttributes) {
        for (OrgnazitionUnitAttribute orgnazitionUnitAttribute : orgnazitionUnitAttributes) {
            this.updateOrgnazitionUnitAttribute(ouId, orgnazitionUnitAttribute);
        }
        return this;
    }

    /**
     * 更新业务组织
     *
     * @param ouId                     业务组织id
     * @param orgnazitionUnitAttribute 业务组织属性
     */
    private void updateOrgnazitionUnitAttribute(String ouId, OrgnazitionUnitAttribute orgnazitionUnitAttribute) {
        //  OU列表更新
        if (this.orgnazitionUnitMap.containsKey(ouId)) {
            //  更新逻辑
            this.orgnazitionUnitMap.get(ouId)
                    .getOrgnazitionUnitAttributes()
                    .put(
                            orgnazitionUnitAttribute.getIndicatorType()
                            , orgnazitionUnitAttribute
                    );
        } else {
            //  新增逻辑
            this.orgnazitionUnitMap.put(
                    ouId
                    , new OrgnazitionUnitDao(
                            orgnazitionAttributeMap.get(orgnazitionUnitAttribute.getIndicatorType()).get(ouId).buildOrgnazitionUnitBaseInfoDto()
                            , orgnazitionUnitAttribute
                    )
            );
        }
        this.generateOrgnazitionAttributeMap(
                orgnazitionUnitAttribute.getIndicatorType()
                , this.orgnazitionUnitMap.get(ouId)   //  变更业务组织属性
        );
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
     * 新增顶层业务组织，必然为虚体，只能负责填写指标，不能分配任务
     *
     * @param pOuId      上级业务组织id
     * @param ouCode     业务组织编码
     * @param ouName     业务组织名称
     * @param createDate 创建时间
     * @param createUser 创建人
     * @return 业务组织DTO
     */
    public OrgnazitionUnitBaseInfoDto createTopOrgnazitionUnit(
            String pOuId
            , String ouCode
            , String ouName
            , Date createDate
            , String createUser
    ) {
        return this.createOrgnazitionUnit(pOuId, ouCode, ouName, createDate, createUser, false);
    }

    /**
     * 新增业务组织
     *
     * @param pOuId      上级业务组织id
     * @param ouCode     业务组织编码
     * @param ouName     业务组织名称
     * @param createDate 创建时间
     * @param createUser 创建人
     * @param isEntity   是否实体
     * @return 业务组织DTO
     */
    public OrgnazitionUnitBaseInfoDto createOrgnazitionUnit(
            String pOuId
            , String ouCode
            , String ouName
            , Date createDate
            , String createUser
            , boolean isEntity
    ) {
        //  新建组织树
        OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto = new OrgnazitionUnitBaseInfoDto(
                UUID.randomUUID().toString()
                , ouCode
                , ouName
                , createDate
                , createUser
                , isEntity
        );
        if (StringUtils.isEmpty(pOuId)) {
            //  没有上级业务组织，就是完全新建逻辑
            if (this.orgnazitionUnitMap.isEmpty()) {
                //  新增逻辑
                this.orgnazitionUnitMap.put(
                        orgnazitionUnitBaseInfoDto.getOuID()
                        , new OrgnazitionUnitDao(
                                orgnazitionUnitBaseInfoDto.buildTopLevel()    //  业务组织级别为0
                        )
                );
                return orgnazitionUnitBaseInfoDto;
            } else {
                //  业务组织树不能重新建立
                throw new RuntimeException("请选择业务组织的上级节点");
            }
        } else {
            //  上级业务组织不为空
            if (this.getOrgnazitionUnitMap().containsKey(pOuId)) {
                //  当前map下能找到上级业务组织

                //  新增逻辑
                this.orgnazitionUnitMap.put(
                        orgnazitionUnitBaseInfoDto.getOuID()
                        , new OrgnazitionUnitDao(
                                orgnazitionUnitBaseInfoDto.buildOrgTree(
                                        this.getOrgnazitionUnitMap().get(pOuId).getLevel() + 1
                                        , pOuId
                                )    //  业务组织级别为上级业务组织level+1,上级业务组织为Pid
                        )
                );
                return orgnazitionUnitBaseInfoDto;
            } else {
                throw new RuntimeException("请业务组织的上级节点错误");
            }
        }
    }

    /**
     * 补充业务组织属性
     *
     * @param ouId                     业务组织id
     * @param orgnazitionUnitAttribute 业务组织属性
     */
    public OrgnazitionUnitDto generateOrgnazitionUnitAttribute(String ouId, OrgnazitionUnitAttribute orgnazitionUnitAttribute) {
        this.orgnazitionUnitMap.get(ouId).getOrgnazitionUnitAttributes().put(
                orgnazitionUnitAttribute.getIndicatorType()
                , orgnazitionUnitAttribute
        );
        return this.orgnazitionUnitMap.get(ouId).buildOrgnazitionUnitDto(orgnazitionUnitAttribute.getIndicatorType());
    }

    /**
     * 根据业务组织类型取得上级业务组织
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 上级业务组织dto
     */
    public OrgnazitionUnitDto getParentOrgnazitionUnit(String ouId, IndicatorType indicatorType) {
        try {
            return this.orgnazitionAttributeMap.get(indicatorType).get(
                    this.orgnazitionAttributeMap.get(indicatorType).get(ouId).getOrgnazitionUnitAttribute().getParentId()
            );
        } catch (Exception e) {
            throw new RuntimeException(ouId + "没有上级业务组织");
        }
    }

    /**
     * 取得上级业务组织
     *
     * @param ouId 业务组织dto id
     * @return 上级业务组织dto
     */
    public OrgnazitionUnitBaseInfoDto getParentOrgnazitionUnit(String ouId) {
        return this.orgnazitionUnitMap
                .get(this.orgnazitionUnitMap.get(ouId).getPOuID())
                .buildOrgnazitionUnitBaseInfoDto();
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
            throw new RuntimeException(indicatorType.name() + "," + ouId + "没有上级业务组织");
        }
    }

    /**
     * 取得下级业务组织列表
     *
     * @param ouId 业务组织dto id
     * @return 下级业务组织dto列表
     */
    public List<OrgnazitionUnitBaseInfoDto> getChildOrgnazitionUnits(String ouId) {
        return new ArrayList<OrgnazitionUnitBaseInfoDto>() {{
            orgnazitionUnitMap.keySet().forEach(id -> {
                if (ouId.equals(orgnazitionUnitMap.get(id).getPOuID())) {
                    add(orgnazitionUnitMap.get(id).buildOrgnazitionUnitBaseInfoDto());
                }
            });
        }};
    }

    /**
     * 选择上级业务组
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 上级业务组织dto列表
     */
    public List<OrgnazitionUnitDto> getParentOrgnazitionUnitsByIndicatorType(String ouId, IndicatorType indicatorType) {
        if (this.orgnazitionAttributeMap.containsKey(indicatorType)) {
            return new ArrayList<OrgnazitionUnitDto>() {{
                String pid = orgnazitionAttributeMap.get(indicatorType).get(ouId).getPOuID();   //  通过业务组织属性找上级组织id
                if (orgnazitionAttributeMap.get(indicatorType).containsKey(pid)) {    //  通过pid找上级组织
                    add(orgnazitionAttributeMap.get(indicatorType).get(pid));
                }
            }};
        } else {
            throw new RuntimeException(indicatorType.name() + "组织列表为空");
        }
    }

    /**
     * 取得上级业务组织列表
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
                                    &&
                                    unitMap.get(ouId).getOrgnazitionUnitAttribute().getParentId().equals(parentOuID)
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
    public OrgnazitionUnitBaseInfoDto getOrgnazitionUnitDao(String ouId) {
        return this.getOrgnazitionUnitMap().get(ouId).buildOrgnazitionUnitBaseInfoDto();
    }

    /**
     * 取得业务组织顶层节点
     *
     * @return 业务组织属性
     */
    public OrgnazitionUnitDao getTopLevelOrgnazitionUnitDao() {
        for (String ouId : this.getOrgnazitionUnitMap().keySet()) {
            if (0 == this.getOrgnazitionUnitMap().get(ouId).getLevel()) {
                return this.getOrgnazitionUnitMap().get(ouId);
            }
        }
        throw new RuntimeException("业务组织树没有初始化");
    }

    /**
     * 获取某一属性下的业务组织实体树
     *
     * @param indicatorType 业务组织类型
     * @return 业务组织树
     */
    public Map<String, OrgnazitionUnitDto> getOrgnazitionTreeViewByAttribute(IndicatorType indicatorType) {
        return new HashMap<String, OrgnazitionUnitDto>() {{
            orgnazitionAttributeMap.get(indicatorType).keySet().forEach(ouId -> {
                if (orgnazitionAttributeMap.get(indicatorType).get(ouId).isEntity()) {
                    put(ouId, orgnazitionAttributeMap.get(indicatorType).get(ouId));
                }
            });
        }};
    }

    /**
     * 记录指标单据meta和ou的关系配置,从数据库中初始化调用
     *
     * @param indicatorOURelDaos 指标单据meta和ou的关系配置
     */
    public void buildIndicatorBillRelModel(List<IndicatorOuRelDao> indicatorOURelDaos) {
        this.indicatorBillRelModel = new IndicatorBillRelModel(indicatorOURelDaos);
    }

    /**
     * 取得所有业务组织
     *
     * @param pageNo   页号
     * @param pageSize 页宽
     * @return 业务组织列表
     */
    public List<OrgnazitionUnitBaseInfoDto> getAllOrgnazition(int pageNo, int pageSize) {
        List<OrgnazitionUnitDao> orgnazitionUnitDaos = new ArrayList<OrgnazitionUnitDao>() {{
            getOrgnazitionUnitMap().keySet().forEach(ouId -> add(getOrgnazitionUnitMap().get(ouId)));
        }};

        if (orgnazitionUnitDaos.size() < (pageNo - 1) * pageSize) {
            throw new RuntimeException("分页下标越界");
        } else {
            //  排序
            orgnazitionUnitDaos.sort(
                    Comparator.comparingInt(OrgnazitionUnitDao::getLevel)   //  按照层级排序
                            .thenComparing(OrgnazitionUnitDao::getOuCode)   //  按照编码排序
            );
            return new ArrayList<OrgnazitionUnitBaseInfoDto>() {{
                if (orgnazitionUnitDaos.size() >= pageNo * pageSize)
                    orgnazitionUnitDaos.subList((pageNo - 1) * pageSize, pageNo * pageSize).forEach(orgnazitionUnitDao -> add(
                            buildOrgnazitionUnitBaseInfoDtoWithPOuName(orgnazitionUnitDao)
                    ));
                else
                    orgnazitionUnitDaos.subList((pageNo - 1) * pageSize, orgnazitionUnitDaos.size()).forEach(orgnazitionUnitDao -> add(
                            buildOrgnazitionUnitBaseInfoDtoWithPOuName(orgnazitionUnitDao)
                    ));
            }};
        }
    }

    /**
     * 带上级组织名称返回OU定义
     *
     * @param orgnazitionUnitDao OU定义dao
     * @return OU定义
     */
    private OrgnazitionUnitBaseInfoDto buildOrgnazitionUnitBaseInfoDtoWithPOuName(OrgnazitionUnitDao orgnazitionUnitDao) {
        if (this.getOrgnazitionUnitMap().containsKey(orgnazitionUnitDao.getPOuID()))
            return orgnazitionUnitDao.buildOrgnazitionUnitBaseInfoDto()
                    .buildPOuName(
                            this.getOrgnazitionUnitMap().get(orgnazitionUnitDao.getPOuID()).getOuName()
                    );
        else
            return orgnazitionUnitDao.buildOrgnazitionUnitBaseInfoDto();
    }
}
