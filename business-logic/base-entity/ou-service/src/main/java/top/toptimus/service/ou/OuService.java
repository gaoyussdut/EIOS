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
     * 新增业务组织
     *
     * @param pOuId       上级业务组织id
     * @param ouCode      业务组织编码
     * @param ouName      业务组织名称
     * @param createDate  创建时间
     * @param createUser  创建人
     * @param description 描述
     * @return 业务组织DTO
     */
    public OrgnazitionUnitBaseInfoDto createOrgnazitionUnit(
            String pOuId
            , String ouCode
            , String ouName
            , Date createDate
            , String createUser
            , String description
    ) {
        this.ouEntity.initOuData();
        if (this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getOrgnazitionUnitMap().isEmpty()) {
            pOuId = UUID.randomUUID().toString();   //  给要创建的组织赋初值
            //  业务组织未初始化
            this.ouEntity.createTopOrgnazitionUnit( //  仅针对正泰,创建头部组织
                    null
                    , pOuId
                    , "正泰"
                    , createDate
                    , createUser
                    , ""
            );
        }
        return this.ouEntity.createOrgnazitionUnit(
                pOuId
                , ouCode
                , ouName
                , createDate
                , createUser
                , true
                , description
        );
    }

    /**
     * 取得组织机构一览kv
     *
     * @return 组织机构一览kv
     */
    public Map<String, String> getOrgIdAndName() {
        return this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getOrgIdAndName();
    }
}
