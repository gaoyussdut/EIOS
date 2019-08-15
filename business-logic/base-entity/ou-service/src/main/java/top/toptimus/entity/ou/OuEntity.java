package top.toptimus.entity.ou;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao;
import top.toptimus.indicator.indicatorBill.dto.IndicatorOuRelDto;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;
import top.toptimus.indicator.ou.model.OrgnazitionUnitModel;
import top.toptimus.repository.ou.OuRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * OU实体类
 *
 * @author gaoyu
 * @since 2019-07-29
 */
@Component
public class OuEntity {
    @Getter
    private final ThreadLocal<OrgnazitionUnitModel> orgnazitionUnitModelThreadLocal = ThreadLocal.withInitial(OrgnazitionUnitModel::new);   //  单据线程缓存
    @Autowired
    private OuRepository ouRepository;  //  ou repo

    /**
     * 从数据库中加载OU充血模型，在retrieve方法中调用
     */
    public void initOuData() {
        /*
            ou初始化
         */
        this.orgnazitionUnitModelThreadLocal.set(new OrgnazitionUnitModel(ouRepository.findAllOrgnazitionUnitDao()));
        /*
            ou属性初始化
         */
        this.orgnazitionUnitModelThreadLocal.get().buildorgnazitionUnitAttributes(ouRepository.findAllOrgnazitionUnitAttributeDao());
    }

    private void initIndicator() {
        List<IndicatorOuRelDao> indicatorOURelDaos = new ArrayList<>();   //  TODO    从数据库中取数据

        this.orgnazitionUnitModelThreadLocal.get().buildIndicatorBillRelModel(indicatorOURelDaos);
    }

    /*
        业务组织基本信息维护（不带业务组织类型
     */

    /**
     * 新增业务组织
     *
     * @param pOuId      上级业务组织id
     * @param ouCode     业务组织编码
     * @param ouName     业务组织名称
     * @param createDate 创建时间
     * @param createUser 创建人
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
        OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto = this.orgnazitionUnitModelThreadLocal.get().createOrgnazitionUnit(pOuId, ouCode, ouName, createDate, createUser, isEntity);
        this.ouRepository.createorgnazitionUnit(new OrgnazitionUnitDao(orgnazitionUnitBaseInfoDto)); //  持久化
        return orgnazitionUnitBaseInfoDto;
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
        OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto = this.orgnazitionUnitModelThreadLocal.get().createTopOrgnazitionUnit(pOuId, ouCode, ouName, createDate, createUser);
        this.ouRepository.createorgnazitionUnit(new OrgnazitionUnitDao(orgnazitionUnitBaseInfoDto)); //  持久化
        return orgnazitionUnitBaseInfoDto;
    }

    /**
     * 取得上级业务组织
     *
     * @param ouId 业务组织dto id
     * @return 上级业务组织dto
     */
    public OrgnazitionUnitBaseInfoDto getParentOrgnazitionUnit(String ouId) {
        try {
            return this.orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnit(ouId);
        } catch (Exception e) {
            this.initOuData();
            return this.orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnit(ouId);
        }
    }

    /**
     * 取得下级业务组织列表
     *
     * @param ouId 业务组织dto id
     * @return 下级业务组织dto列表
     */
    public List<OrgnazitionUnitBaseInfoDto> getChildOrgnazitionUnits(String ouId) {
        try {
            return this.orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(ouId);
        } catch (Exception e) {
            this.initOuData();
            return this.orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(ouId);
        }
    }

    /**
     * 取得当前业务组织属性
     *
     * @param ouId 业务组织id
     * @return 业务组织属性
     */
    public OrgnazitionUnitBaseInfoDto getOrgnazitionUnitBaseInfo(String ouId) {
        try {
            return this.orgnazitionUnitModelThreadLocal.get().getOrgnazitionUnitDao(ouId).buildOrgnazitionUnitBaseInfoDto();
        } catch (Exception e) {
            this.initOuData();
            return this.orgnazitionUnitModelThreadLocal.get().getOrgnazitionUnitDao(ouId).buildOrgnazitionUnitBaseInfoDto();
        }
    }

    /*
        组织类型属性维护
     */

    /**
     * 按照业务类别选择上级业务组
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 上级业务组织dto列表
     */
    public OrgnazitionUnitDto getParentOrgnazitionUnitByIndicatorType(String ouId, IndicatorType indicatorType) {
        try {
            return this.getParentOrgnazitionUnitsByIndicatorType(ouId, indicatorType);
        } catch (Exception e) {
            this.initOuData();
            return this.getParentOrgnazitionUnitsByIndicatorType(ouId, indicatorType);
        }
    }


    /**
     * 清洗列表，按照业务类别选择上级业务组
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 上级业务组织dto列表
     */
    private OrgnazitionUnitDto getParentOrgnazitionUnitsByIndicatorType(String ouId, IndicatorType indicatorType) {
        List<OrgnazitionUnitDto> orgnazitionUnitDtos = this.orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnitsByIndicatorType(ouId, indicatorType);
        if (orgnazitionUnitDtos.isEmpty()) {
            throw new RuntimeException("未找到上级组织");
        } else if (1 != orgnazitionUnitDtos.size()) {
            throw new RuntimeException("上级组织为多条");
        } else {
            return orgnazitionUnitDtos.get(0);
        }
    }

    /**
     * 更新业务组织
     *
     * @param ouId                      业务组织id
     * @param orgnazitionUnitAttributes 业务组织属性列表
     */
    public void updateOrgnazitionUnitAttributes(String ouId, List<OrgnazitionUnitAttribute> orgnazitionUnitAttributes) {
        this.orgnazitionUnitModelThreadLocal.get().updateOrgnazitionUnitAttributes(ouId, orgnazitionUnitAttributes);
        for (OrgnazitionUnitAttribute orgnazitionUnitAttribute : orgnazitionUnitAttributes) {
            ouRepository.saveOrgnazitionUnitAttribute(ouId, orgnazitionUnitAttribute);
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
        try {
            return this.orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(ouId, indicatorType);
        } catch (Exception e) {
            this.initOuData();
            return this.orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(ouId, indicatorType);
        }
    }

    /**
     * 根据业务组织类型取得上级业务组织
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 上级业务组织dto
     */
    @Deprecated
    public OrgnazitionUnitDto getParentOrgnazitionUnit(String ouId, IndicatorType indicatorType) {
        try {
            return this.orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnit(ouId, indicatorType);
        } catch (Exception e) {
            this.initOuData();
            return this.orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnit(ouId, indicatorType);
        }
    }

    /**
     * 取得业务组织顶层节点
     *
     * @return 顶层ou基础信息
     */
    public OrgnazitionUnitBaseInfoDto getTopLevelOrgnazitionUnitDao() {
        try {
            return this.orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().buildOrgnazitionUnitBaseInfoDto();
        } catch (Exception e) {
            this.initOuData();
            return this.orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().buildOrgnazitionUnitBaseInfoDto();
        }
    }

    /*
        指标单据转换配置
     */

    /**
     * 根据业务组织单元id取得指标单据转换配置关系列表
     *
     * @param ouId ou id
     * @return 指标单据转换配置关系列表
     */
    @Deprecated
    public List<IndicatorOuRelDto> getIndicatorOuRelDaosByOuId(String ouId, IndicatorType indicatorType) {
        try {
            return this.orgnazitionUnitModelThreadLocal.get()
                    .getIndicatorBillRelModel()
                    .getIndicatorOuRelDaosByOuId(
                            ouId
                            , indicatorType
                    );
        } catch (Exception e) {
            this.initIndicator();
            return this.orgnazitionUnitModelThreadLocal.get()
                    .getIndicatorBillRelModel()
                    .getIndicatorOuRelDaosByOuId(
                            ouId
                            , indicatorType
                    );
        }
    }

    /**
     * 新增指标下推关系
     *
     * @param indicatorOuRelDto 指标单据meta和ou的关系配置
     */
    @Deprecated
    public void buildIndicatorOURel(IndicatorOuRelDto indicatorOuRelDto) {
        this.orgnazitionUnitModelThreadLocal.get().getIndicatorBillRelModel().buildIndicatorOURel(indicatorOuRelDto);
    }

    /*
        其他乱七八糟接口
     */

//
//
//    /**
//     * 更新业务组织
//     *
//     * @param orgnazitionUnitDtos 业务组织dto列表
//     */
//    public void updateOrgnazitionUnit(List<OrgnazitionUnitDto> orgnazitionUnitDtos) {
//        this.orgnazitionUnitModelThreadLocal.get().updateOrgnazitionUnits(orgnazitionUnitDtos);
//        //  TODO    持久化数据库
//    }

    /**
     * 补充业务组织属性
     *
     * @param ouId                     业务组织id
     * @param orgnazitionUnitAttribute 业务组织属性
     */
    @Deprecated
    public void generateOrgnazitionUnitAttribute(String ouId, OrgnazitionUnitAttribute orgnazitionUnitAttribute) {
        OrgnazitionUnitDto orgnazitionUnitDto = this.orgnazitionUnitModelThreadLocal.get().generateOrgnazitionUnitAttribute(ouId, orgnazitionUnitAttribute);
        //  TODO    持久化数据库
    }


    /**
     * 下推关系
     * 记录指标单据meta和ou的关系配置,从数据库中初始化调用
     * TODO 异步，一次性加载可能有性能问题
     */
    @Deprecated
    public void initIndicatorBillRel() {
        //  指标单据meta和ou的关系配置    TODO    从数据库中取
        List<IndicatorOuRelDao> indicatorOURelDaos = new ArrayList<>();

        this.orgnazitionUnitModelThreadLocal.get().buildIndicatorBillRelModel(indicatorOURelDaos);
    }

    /**
     * 新增下推关系
     *
     * @param sourceOuId    源ou的id
     * @param targetOuId    目标ou的id
     * @param sourceMetaId  源单指标meta定义
     * @param targetMetaId  目标指标meta定义
     * @param isPushDown    是否下推，如果是下推就不是新建分解
     * @param procedureName 如果是下推，要执行的存储过程名（或者流计算或者restful api）
     * @param indicatorType 业务类型
     */
    @Deprecated
    public void addIndicatorBillRel(
            String sourceOuId
            , String targetOuId
            , String sourceMetaId
            , String targetMetaId
            , boolean isPushDown
            , String procedureName
            , IndicatorType indicatorType
    ) {
        this.orgnazitionUnitModelThreadLocal.get().getIndicatorBillRelModel().buildIndicatorOURel(
                new IndicatorOuRelDto(
                        UUID.randomUUID().toString()
                        , sourceOuId
                        , targetOuId
                        , sourceMetaId
                        , targetMetaId
                        , isPushDown
                        , procedureName
                        , indicatorType
                )
        );
    }
}
