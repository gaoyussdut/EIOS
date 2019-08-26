package top.toptimus.service.ou;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.entity.ou.OuEntity;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 为了正泰定制的ou服务
 *
 * @author gaoyu
 * @since 2019-08-20
 */
@Service
public class Ou_zhengtai_Service {
    @Autowired
    private OuEntity ouEntity;

    /**
     * 取得所有业务组织 TODO，正泰定制
     *
     * @param ouName   组织名称
     * @param pOuName  上级组织名称
     * @param pageNo   页号
     * @param pageSize 页宽
     * @return 业务组织列表
     */
    public List<OrgnazitionUnitBaseInfoDto> getAllOrgnazitionByParameter(String ouName, String pOuName, int pageNo, int pageSize) {
        this.ouEntity.initOuData();

        if (Strings.isNullOrEmpty(pOuName) && Strings.isNullOrEmpty(ouName)) {
            //  非模糊查询
            return this.ouEntity.getAllOrgnazition(pageNo, pageSize);
        } else {
            //  模糊查询
            return this.fuzzyLookup(ouName, pOuName, pageNo, pageSize);
        }
    }

    /**
     * 模糊查询
     *
     * @param ouName   组织名称
     * @param pOuName  上级组织名称
     * @param pageNo   页号
     * @param pageSize 页宽
     * @return 业务组织列表
     */
    private List<OrgnazitionUnitBaseInfoDto> fuzzyLookup(String ouName, String pOuName, int pageNo, int pageSize) {
        List<OrgnazitionUnitBaseInfoDto> rtn = new ArrayList<>();
        for (OrgnazitionUnitBaseInfoDto orgnazitionUnitBaseInfoDto : this.ouEntity.getAllOrgnazition()) {
            boolean nameMatch = false;
            boolean pNameMatch = false;
            if (!Strings.isNullOrEmpty(ouName)) {    //  模糊查询机构名称
                if (null != orgnazitionUnitBaseInfoDto.getOuName()
                        && orgnazitionUnitBaseInfoDto.getOuName().contains(ouName)) {
                    nameMatch = true;
                }
            } else {
                // 不匹配默认匹配
                nameMatch = true;
            }
            if (!Strings.isNullOrEmpty(pOuName)) {    //  模糊查询机构名称
                if (null != orgnazitionUnitBaseInfoDto.getPOuName()
                        && orgnazitionUnitBaseInfoDto.getPOuName().contains(pOuName)) {
                    pNameMatch = true;
                }
            } else {
                // 不匹配默认匹配
                pNameMatch = true;
            }
            if (pNameMatch && nameMatch) {
                rtn.add(orgnazitionUnitBaseInfoDto);
            }
        }
        System.out.println(JSON.toJSONString(rtn));
        return rtn.size() >= pageNo * pageSize ?
                rtn.subList((pageNo - 1) * pageSize, pageSize)
                : rtn.subList((pageNo - 1) * pageSize, rtn.size());
    }

    /**
     * 新增业务组织   TODO    正泰定制
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

}
