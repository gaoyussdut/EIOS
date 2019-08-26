package top.toptimus.service.ou;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.entity.ou.OuEntity;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ou服务
 *
 * @author gaoyu
 * @since 2019-08-20
 */
@Service
public class OuService {
    @Autowired
    private OuEntity ouEntity;

    /**
     * 取得所有业务组织
     *
     * @param pageNo   页号
     * @param pageSize 页宽
     * @return 业务组织列表
     */
    public List<OrgnazitionUnitBaseInfoDto> getAllOrgnazition(int pageNo, int pageSize) {
        this.ouEntity.initOuData();
        return this.ouEntity.getAllOrgnazition(pageNo, pageSize);
    }

    /**
     * 取得组织机构一览kv
     *
     * @return 组织机构一览kv
     */
    public Map<String, String> getOrgIdAndName() {
        return this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getOrgIdAndName();
    }

    /**
     * 取得业务组织基础信息
     *
     * @param ouId 业务组织id
     */
    public OrgnazitionUnitBaseInfoDto getOrgnazitionUnitBaseInfo(String ouId) {
        return this.ouEntity.getOrgnazitionUnitBaseInfo(ouId);
    }

    /**
     * 更新业务组织基础信息
     *
     * @param orgnazitionUnitBaseInfoDto 业务组织基础属性
     */
    public void updateOrgnazitionUnitBaseInfo(OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto) {
        this.ouEntity.updateOrgnazitionUnitBaseInfo(orgnazitionUnitBaseInfoDto);
    }
}
