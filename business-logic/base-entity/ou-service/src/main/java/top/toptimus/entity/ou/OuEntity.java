package top.toptimus.entity.ou;

import org.springframework.stereotype.Component;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;
import top.toptimus.indicator.ou.model.OrgnazitionUnitModel;

import java.util.Date;
import java.util.List;


@Component
public class OuEntity {
    private final ThreadLocal<OrgnazitionUnitModel> orgnazitionUnitModelThreadLocal = ThreadLocal.withInitial(OrgnazitionUnitModel::new);   //  单据线程缓存

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
    public OrgnazitionUnitDto createOrgnazitionUnit(
            String pOuId
            , String ouCode
            , String ouName
            , Date createDate
            , String createUser
    ) {
        OrgnazitionUnitDto orgnazitionUnitDto = this.orgnazitionUnitModelThreadLocal.get().createOrgnazitionUnit(pOuId, ouCode, ouName, createDate, createUser);
        //  TODO    持久化数据库

        return orgnazitionUnitDto;
    }


    /**
     * 更新业务组织
     *
     * @param orgnazitionUnitDto 业务组织dto
     */
    public void updateOrgnazitionUnit(OrgnazitionUnitDto orgnazitionUnitDto) {
        this.orgnazitionUnitModelThreadLocal.get().updateOrgnazitionUnit(orgnazitionUnitDto);
        //  TODO    持久化数据库
    }

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
     * 取得业务组织顶层节点
     *
     * @return 顶层ou基础信息
     */
    public OrgnazitionUnitBaseInfoDto getTopLevelOrgnazitionUnitDao() {
        return this.orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().buildOrgnazitionUnitBaseInfoDto();
    }

    /**
     * 取得当前业务组织属性
     *
     * @param ouId 业务组织id
     * @return 业务组织属性
     */
    public OrgnazitionUnitDao getOrgnazitionUnitDao(String ouId) {
        return this.orgnazitionUnitModelThreadLocal.get().getOrgnazitionUnitDao(ouId);
    }

    /**
     * 取得下级业务组织列表
     *
     * @param ouId          业务组织dto id
     * @param indicatorType 业务组织类型
     * @return 下级业务组织dto列表
     */
    public List<OrgnazitionUnitDto> getChildOrgnazitionUnits(String ouId, IndicatorType indicatorType) {
        return this.orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(ouId, indicatorType);
    }
}
