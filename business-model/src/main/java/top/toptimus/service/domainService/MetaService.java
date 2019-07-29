package top.toptimus.service.domainService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.result.Result;
import top.toptimus.entity.meta.event.MetaEventEntity;
import top.toptimus.entity.meta.event.TalendModelMetaEventEntity;
import top.toptimus.entity.meta.event.TokenMetaEventEntity;
import top.toptimus.entity.meta.query.ConfigQueryEntity;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.meta.query.TalendModelMetaQueryFacadeEntity;
import top.toptimus.entity.tokendata.query.TokenDataSqlRetrieveEntity;
import top.toptimus.entity.tokendata.query.TokenMetaQueryFacadeEntity;
import top.toptimus.meta.*;
import top.toptimus.meta.MetaRelation.MasterBillMetaRelationDTO;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.service.secutiry.UserService;
import top.toptimus.user.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * 元数据服务
 * Created by JiangHao on 2018/12/12.
 */
@Service
public class MetaService {

    @Autowired
    private TokenMetaQueryFacadeEntity tokenMetaQueryFacadeEntity;
    @Autowired
    private TokenDataSqlRetrieveEntity tokenDataSqlRetrieveEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private TalendModelMetaEventEntity talendModelMetaEventEntity;
    @Autowired
    private TalendModelMetaQueryFacadeEntity talendModelMetaQueryFacadeEntity;
    @Autowired
    private MetaEventEntity metaEventEntity;
    @Autowired
    private TokenMetaEventEntity tokenMetaEventEntity;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigQueryEntity configQueryEntity;

    /**
     * 获取meta中key的列表
     *
     * @param metaId     metaId
     * @param isReadonly isReadonly
     * @return meta信息
     */
    public Result getCaptionAndKeyByMetaId(String metaId, boolean isReadonly) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findCaptionAndKeyByMetaId(metaId, isReadonly)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 取得对应meta的信息一览
     *
     * @param metaId meta id
     * @return meta info
     */
    public Result getMetaInfoByMetaId(String metaId) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findByMetaId(metaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 查询所有Fkeys
     *
     * @return meta字段
     */
    public Result findAllFkeys(String type, Integer pageNo, Integer pageSize) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findAllFkeys(type, pageNo, pageSize)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 追加Meta
     *
     * @param tokenMetaInforMationDtos meta信息定义，token名和类型
     */
    public Result saveAllTokenMetaInformationDao(List<TokenMetaInformationDto> tokenMetaInforMationDtos) {
        try {
            tokenMetaEventEntity.saveAll(tokenMetaInforMationDtos);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 删除Meta
     *
     * @param tokenMetaInforMationDtos meta信息定义，token名和类型
     */
    public Result deleteTokenMetaInformationDao(List<TokenMetaInformationDto> tokenMetaInforMationDtos) {
        try {
            tokenMetaEventEntity.deleteAll(tokenMetaInforMationDtos);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据meta取得Fkey
     *
     * @param metaId meta id
     * @return 字段
     */
    public Result findFkeyByMetaId(String metaId) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findFkeyByMetaId(metaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 追加多条MetaInfos
     *
     * @param metaInfoDTOS meta列表
     */
    public Result saveMetaInfoDTO(List<MetaInfoDTO> metaInfoDTOS) {
        try {
            return Result.success(
                    metaEventEntity.saveMetaInfoDTO(metaInfoDTOS)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     * @param metaIds   meta id列表
     */
    public Result createTable(String tableName, List<String> metaIds) {
        try {
            metaEventEntity.createTableByMetaInfo(tableName, metaIds);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 查找全部的key的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    public Result getAllKeyCaption() {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findAllKeyCaption()
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据key查找对应的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    public Result getAllKeyCaptionByKeys(List<String> keys) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findAllKeyCaptionByKeys(keys)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据x_pk_x_talend_id 查询所有的metaJson
     *
     * @param x_pk_x_talend_id x_pk_x_talend_id
     * @return List<TalendModelMeta>
     */
    public Result getAllByXPkXTalendId(String x_pk_x_talend_id) {
        try {
            return Result.success(
                    talendModelMetaQueryFacadeEntity.findAllByXPkXTalendId(x_pk_x_talend_id)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 保存全部TalendModelMeta
     *
     * @param talendModelMetas talendModelMetas
     */
    public Result saveAllTalendModelMeta(Map<String, List<TalendModelMetaDto>> talendModelMetas) {
        try {
            talendModelMetaEventEntity.saveAllTalendModelMeta(talendModelMetas);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 比较meta是否和数据库中的meta一致
     *
     * @param talendModelMetas talendModelMetas
     */
    public Result equalsTalendModelMeta(Map<String, List<TalendModelMetaDto>> talendModelMetas) {
        try {
            return Result.success(
                    talendModelMetaQueryFacadeEntity.equalsTalendModelMeta(talendModelMetas)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 查询所有meta信息
     *
     * @param pageNo   页号
     * @param pageSize 页宽
     * @return meta信息定义，token名和类型
     */
    public Result findAllTokenMetaInformation(Integer pageNo, Integer pageSize) {
        try {
            return Result.success(
                    tokenMetaQueryFacadeEntity.findAllTokenMetaInformation(pageNo, pageSize)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取用户自定义Meta
     *
     * @param userId           user
     * @param memorandvnMetaId 备查账meta id
     * @return Result
     */
    public Result getSelfDefiningMeta(String userId, String memorandvnMetaId) {
        try {
            SelfDefiningMetaDTO selfDefingMetaDTO = tokenMetaQueryFacadeEntity.getSelfDefiningMeta(userId, memorandvnMetaId);
            if (StringUtils.isEmpty(selfDefingMetaDTO.getSelfDefiningMeta())) {
                return Result.success(
                        selfDefingMetaDTO.getDefaultMetaId()
                );
            } else {
                return Result.success(
                        selfDefingMetaDTO.getSelfDefiningMeta()
                );
            }

        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取用户自定义Meta
     *
     * @param memorandvnMetaId 备查账meta id
     * @return Result
     */
    public Result getSelfDefiningMeta(String memorandvnMetaId) {
        return this.getSelfDefiningMeta(
                ((UserDTO) userService.getUserInfoByAccessToken().getData()).getId()
                , memorandvnMetaId
        );
    }

    /**
     * 保存用户自定义MetaId
     *
     * @param userId           用户Id
     * @param memorandvnMetaId 备查帐ID
     * @param tokenMetaInfoDTO meta字段信息
     */
    public Result saveSelfDefiningMeta(String userId, String memorandvnMetaId, TokenMetaInfoDTO tokenMetaInfoDTO) {
        try {
            SelfDefiningMetaDTO selfDefingMetaDTO = tokenMetaQueryFacadeEntity.getSelfDefiningMeta(userId, memorandvnMetaId);
            if (StringUtils.isEmpty(selfDefingMetaDTO.getSelfDefiningMeta())) {
                String metaId = "self_defining_meta_" + userId;
                String metaName = "用戶" + userId + "自定义Meta";
                tokenMetaInfoDTO.build(metaId, metaName);
                metaEventEntity.saveMeta(tokenMetaInfoDTO);
                metaEventEntity.saveSelfDefiningMeta(new SelfDefiningMetaDTO(memorandvnMetaId, userId, metaId));
            } else {
                metaEventEntity.saveMeta(tokenMetaInfoDTO);
            }
            return Result.success(
                    tokenMetaInfoDTO.getTokenMetaId()
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 主数据meta获取单据内meta关系
     *
     * @param metaId meta id
     */
    public Result getMasterDataMetaRelation(String metaId) {
        try {
            return Result.success(
                    tokenMetaQueryFacadeEntity.getMasterDataMetaRelation(metaId));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 保存主数据meta单据内meta关系(保存)
     *
     * @param metaRelationDTO meta关系
     */
    public Result saveMasterDataMetaRelation(MetaRelationDTO metaRelationDTO) {
        try {
            tokenMetaQueryFacadeEntity.saveMasterDataMetaRelation(metaRelationDTO);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据表头主数据metaid查该表单关联的主数据meta
     *
     * @param billMetaId 表头meta id
     * @return Result
     */
    public Result getMasterBillMetaRelation(String billMetaId) {
        try {
            return Result.success(tokenMetaQueryFacadeEntity.getMasterBillMetaRelation(billMetaId));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 保存主数据meta的关联关系
     *
     * @param masterBillMetaRelationDTO 主数据meta的关联关系
     */
    public Result saveMasterBillMetaRelation(MasterBillMetaRelationDTO masterBillMetaRelationDTO) {

        try {
            tokenMetaQueryFacadeEntity.saveMasterBillMetaRelation(masterBillMetaRelationDTO);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }

    }

    /**
     * 根据idlist获取全部meta关系
     *
     * @param billMetaIdList meta id列表
     * @return Result
     */
    public Result getAllMasterDataMetaRelation(List<String> billMetaIdList) {
        try {
            return Result.success(
                    tokenMetaQueryFacadeEntity.getAllMasterDataMetaRelation(billMetaIdList));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据数据类型获取meta
     *
     * @param metaDataType meta数据类型
     * @param pageNo       页码
     * @param pageSize     页宽
     * @return Result
     */
    public Result getAllMetaByMetaDataType(String metaDataType, Integer pageNo, Integer pageSize) {
        try {
            return Result.success(
                    tokenMetaQueryFacadeEntity.getAllMasterDataMeta(metaDataType, pageNo, pageSize)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据类型获取meta
     *
     * @param metaType     meta类型
     * @param metaDataType meta数据类型
     * @param pageNo       页码
     * @param pageSize     页宽
     * @return Result
     */
    public Result getAllMetaByMetaType(String metaType, String metaDataType, Integer pageNo, Integer pageSize) {
        try {
            return Result.success(
                    tokenMetaQueryFacadeEntity.getAllMetaByMetaType(metaType, metaDataType, pageNo, pageSize)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取主数据meta视图meta
     */
    public Result getViewMetaByMasterDataMeta(String metaId) {
        try {
            return Result.success(
                    tokenMetaQueryFacadeEntity.getViewMetaByMasterDataMeta(metaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据角色查询表头meta所能查看的分录、备查账meta
     *
     * @param billMetaId 表头metaId
     * @return MetaAuthRelationDTO
     */
    public MetaAuthRelationDTO getMetaAuthRelation(String billMetaId) {
        return configQueryEntity.findMetaAuthRelationByMetaIdAndRoleId(
                billMetaId
                , userService.getUserRolesInfoByAccessToken().get(0).getId()
        );
    }

    /**
     * 根据角色查询表头meta所能查看的分录、备查账meta
     *
     * @param billMetaId 表头metaId
     * @return MetaAuthRelationDTO
     */
    public Result getMetaAuthRelation(String billMetaId, String roleId) {
        return Result.success(configQueryEntity.findMetaAuthRelationByMetaIdAndRoleId(
                billMetaId
                , roleId
                )
        );
    }

    /**
     * 保存role和单据视图meta的关系
     *
     * @param metaAuthRelationDTO role和单据视图meta的关系
     */
    public Result saveMetaAuthRelation(MetaAuthRelationDTO metaAuthRelationDTO) {
        try {
            configQueryEntity.saveMetaAuthRelation(metaAuthRelationDTO);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据主数据meta查询已配置的单据的权限
     *
     * @param metaId 单据数据metaid
     * @return Result
     */
    public Result getMetaAuthRelationDaosByMetaId(String metaId) {
        try {
            return Result.success(configQueryEntity.findMetaAuthRelationDaosByMetaId(metaId));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据主数据metaID查找旗下的视图metaId并修改旗下的Caption
     *
     * @param fKeyCaptionDtos 字段Fkey和描述caption
     * @param masterMetaId    主数据metaID
     * @return Result
     */
    public Result updateMetaCaption(List<FKeyCaptionDto> fKeyCaptionDtos, String masterMetaId) {
        try {
            metaEventEntity.updateMetaCaption(masterMetaId, fKeyCaptionDtos);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据metaIds获取metaId的定义信息
     *
     * @param metaIds metaIds
     */
    public Result getMetaFormationsByMetaIds(List<String> metaIds) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findMetaFormationsByMetaIds(metaIds)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取所有表单的配置关联信息
     *
     * @return List<MasterBillMetaRelationDTO>
     */
    public Result getAllBillRelationInfo() {
        try {
            return Result.success(
                    configQueryEntity.findAllBillRelationInfo()
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据metaId查询SignGroupDTO信息
     *
     * @param metaId 单据metaId
     * @return 汇签组信息
     */
    public Result findBySignMetaId(String metaId) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.findBySignMetaId(metaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }


    /**
     * 根据主数据的表头metaId获取单据的关联信息
     *
     * @param billMasterMetaId 表头的主数据metaId
     * @return Result
     */
    public Result getBillRelationInfoByBillMasterMetaId(String billMasterMetaId) {
        try {
            return Result.success(
                    configQueryEntity.findBillRelationInfoById(billMasterMetaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据备查帐查询单据
     *
     * @param memorandvnMetaId 备查账metaId
     * @return Result(List<MetaRelationDTO>)
     */
    public Result findByMasterMemorandvnMetaId(String memorandvnMetaId){
        List<MetaRelationDTO> metaRelationDTOList = metaQueryFacadeEntity.findByMasterMemorandvnMetaId(
                tokenDataSqlRetrieveEntity.getTableName(memorandvnMetaId));
        return Result.success(metaRelationDTOList!=null && metaRelationDTOList.size()>0 ? metaRelationDTOList.get(0) : null);
    }

    /**
     * 根据业务单元id取凭证metaId
     * @param businessUnitId  业务单元id
     */
    public Result getCertificateMetaIdByBUID(String businessUnitId)
    {
        return Result.success(metaQueryFacadeEntity.getCertificateMetaIdByBUID(businessUnitId));
    }


    /**
     * 根据fkey查看Constant
     *
     * @param fkey fkey
     * @return Result
     */
    public Result getConstantByKey(String fkey) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.getConstantByKey(fkey)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

}
