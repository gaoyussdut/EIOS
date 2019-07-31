package top.toptimus.entity.ou;

import org.springframework.stereotype.Component;
import top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao;
import top.toptimus.indicator.indicatorBill.dto.IndicatorOuRelDto;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;
import top.toptimus.indicator.ou.model.OrgnazitionUnitModel;

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
    private final ThreadLocal<OrgnazitionUnitModel> orgnazitionUnitModelThreadLocal = ThreadLocal.withInitial(OrgnazitionUnitModel::new);   //  单据线程缓存


    /**
     * 从数据库中加载OU充血模型，在retrieve方法中调用
     */
    private void initOuData() {
        List<OrgnazitionUnitDao> orgnazitionUnitDaos = new ArrayList<>();   //  TODO    从数据库中取数据

        this.orgnazitionUnitModelThreadLocal.set(new OrgnazitionUnitModel(orgnazitionUnitDaos));
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
    ) {
        OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto = this.orgnazitionUnitModelThreadLocal.get().createOrgnazitionUnit(pOuId, ouCode, ouName, createDate, createUser);
        //  TODO    持久化数据库

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
    public OrgnazitionUnitBaseInfoDto getOrgnazitionUnitDao(String ouId) {
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
     * 选择上级业务组
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 下级业务组织dto列表
     */
    public List<OrgnazitionUnitDto> getOrgnazitionUnitsByIndicatorType(String ouId, IndicatorType indicatorType) {
        try {
            return this.orgnazitionUnitModelThreadLocal.get().getOrgnazitionUnitsByIndicatorType(ouId, indicatorType);
        } catch (Exception e) {
            this.initOuData();
            return this.orgnazitionUnitModelThreadLocal.get().getOrgnazitionUnitsByIndicatorType(ouId, indicatorType);
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
        //  TODO    持久化数据库
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
    public void generateOrgnazitionUnitAttribute(String ouId, OrgnazitionUnitAttribute orgnazitionUnitAttribute) {
        OrgnazitionUnitDto orgnazitionUnitDto = this.orgnazitionUnitModelThreadLocal.get().generateOrgnazitionUnitAttribute(ouId, orgnazitionUnitAttribute);
        //  TODO    持久化数据库
    }


    /**
     * 下推关系
     * 记录指标单据meta和ou的关系配置,从数据库中初始化调用
     * TODO 异步，一次性加载可能有性能问题
     */
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
