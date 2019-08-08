package top.toptimus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.businessUnit.*;
import top.toptimus.businessunit.BusinessUnitModel;
import top.toptimus.common.enums.BusinessUnitEdgeTypeEnum;
import top.toptimus.common.enums.RuleTypeEnum;
import top.toptimus.common.enums.TaskStatusEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.event.BusinessUnitEventEntity;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.query.BusinessUnitFacadeQueryEntity;
import top.toptimus.entity.query.SchemaFacadeQueryEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.meta.relation.MetaRelDTO;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.schema.BillSchemaDTO;
import top.toptimus.schema.SchemaDTO;
import top.toptimus.token.relation.TokenRelDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.transformationService.TransformationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lzs
 */
@Service
public class BusinessUnitService {

    @Autowired
    private BusinessUnitEventEntity businessUnitEventEntity;
    @Autowired
    private BusinessUnitFacadeQueryEntity businessUnitFacadeQueryEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;
    @Autowired
    private TransformationService transformationService;
    @Autowired
    private SchemaFacadeQueryEntity schemaFacadeQueryEntity;


    /**
     * 根据组织架构Id取得其下的业务单元一览
     *
     * @param orgId 组织架构Id
     * @return Result
     */
    public Result findBusinessUnitByOrgId(String orgId) {
        try {
            return Result.success(businessUnitFacadeQueryEntity.findBusinessUnitByOrgId(orgId));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 单据保存
     *
     * @return Result
     */
    public Result saveBill() {
        try {


            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }


    /**
     * 凭证接收,调用存储过程转换单据,返回单据的metaid和tokenId
     *
     * @param certificateMetaId  凭证metaId
     * @param certificateTokenId 业务单元id
     * @param businessUnitId     凭证tokenId
     * @return Result
     */
    public Result receiveCertificate(
            String certificateMetaId
            , String certificateTokenId
            , String businessUnitId
    ) {
        return Result.success(
                businessUnitEventEntity
                        .receiveCertificate(
                                certificateMetaId
                                , certificateTokenId
                                , businessUnitId));
    }

    /**
     * 根据凭证查看原单
     *
     * @param certificateMetaId  凭证metaId
     * @param businessUnitId     业务单元id
     * @param certificateTokenId 凭证tokenId
     */
    public Result getSourceBill(
            String certificateMetaId
            , String certificateTokenId
            , String businessUnitId
    ) {
        return Result.success(
                businessUnitEventEntity
                        .getSourceBill(
                                certificateMetaId
                                , certificateTokenId
                                , businessUnitId));
    }

    /**
     * 判断节点是否可新增
     */
    public boolean isCreatable(String businessUnitCode, String metaId) {
        BusinessUnitEdgeDTO businessUnitEdgeDTO = businessUnitFacadeQueryEntity.findEdgeByToMetaId(businessUnitCode, metaId);

        switch (businessUnitEdgeDTO.getEdgeType()) {
            case NEW: {
                return true;
            }
            case NEW_ONLY: {
                //唯一新建判断是否已存在
                if (businessUnitFacadeQueryEntity.getTaskInsByTaskMetaId(businessUnitCode, metaId).size() > 0) {
                    return false;
                } else {
                    return true;
                }
            }
            default: {
                return false;
            }

        }

    }

    /**
     * 获取业务单元下级meta
     *
     * @param businessUnitCode 业务单元Id
     * @param metaId
     * @return Result
     */
    public List<TokenMetaInformationDto> getPushDownMeta(String businessUnitCode, String metaId, String tokenId) {

        BusinessUnitModel businessUnitModel = new BusinessUnitModel()
                .build(businessUnitFacadeQueryEntity
                        .findNextTaskMetaByBusinessUnitCodeAndFromMetaId(businessUnitCode, metaId));

        List<String> nextMetaIds = new ArrayList<>();


        TaskInsDTO taskInsDTO = businessUnitFacadeQueryEntity.getTaskInsByTaskMetaIdAndTaskTokenId(businessUnitCode, metaId, tokenId);
        if (taskInsDTO.getStatus().equals(TaskStatusEnum.CLOSE)) {
            nextMetaIds.addAll(businessUnitModel.getNextPushDownFSMetaIds());
        } else if (taskInsDTO.getStatus().equals(TaskStatusEnum.OPEN)) {
            nextMetaIds.addAll(businessUnitModel.getNextPushDownSSMetaIds());
        }

        return metaQueryFacadeEntity.findMetaFormationsByMetaIds(nextMetaIds);

    }

    /**
     * 获取业务单元关联meta
     *
     * @param businessUnitCode 业务单元Id
     * @param metaId
     * @return Result
     */
    public List<TokenMetaInformationDto> getRelMeta(String businessUnitCode, String metaId) {

        List<String> nextMetaIds = new ArrayList<String>() {{
            businessUnitFacadeQueryEntity
                    .findNextTaskMetaByBusinessUnitCodeAndFromMetaId(businessUnitCode, metaId)
                    .forEach(businessUnitEdgeDTO -> {
                        add(businessUnitEdgeDTO.getToMetaId());
                    });
        }};

        return metaQueryFacadeEntity.findMetaFormationsByMetaIds(nextMetaIds);
    }

    /**
     * 获取业务节点的关联meta下的数据
     *
     * @param businessUnitCode 业务单元Id
     * @param preMetaId        前置metaId
     * @param preTokenId       前置tokenId
     * @param metaId           当前metaId
     * @return Result
     */
    public List<TokenDataDto> getRelData(String businessUnitCode, String preMetaId, String preTokenId, String metaId) {
        List<String> tokenIds = new ArrayList<>();
        if (StringUtils.isEmpty(preMetaId) && StringUtils.isEmpty(preTokenId)) {
            businessUnitFacadeQueryEntity
                    .getTaskInsByTaskMetaId(
                            businessUnitCode
                            , metaId
                    )
                    .forEach(taskInsDTO -> {
                        tokenIds
                                .add(taskInsDTO.getTaskTokenId());
                    });
        } else {
            businessUnitFacadeQueryEntity
                    .getTaskInsByTaskMetaIdAndProcessInsId(
                            businessUnitCode
                            , businessUnitFacadeQueryEntity.getTaskInsByTaskMetaIdAndTaskTokenId(businessUnitCode, preMetaId, preTokenId).getProcessInsId()
                            , metaId
                    )
                    .forEach(taskInsDTO -> {
                        tokenIds.add(taskInsDTO.getTaskTokenId());
                    });
        }
        return tokenQueryFacadeEntity.getMetaTokenData(metaId, tokenIds);
    }

    /**
     * 手动下推
     *
     * @param businessUnitCode 业务单元Id
     * @param preMetaId        前置metaId
     * @param preTokenId       前置tokenId
     * @param metaId           下推到的metaId
     * @return Result
     */
    public TokenDataDto pushDown(String businessUnitCode, String preMetaId, String preTokenId, String metaId) {

        TaskInsDTO taskInsDTO = businessUnitFacadeQueryEntity.getTaskInsByTaskMetaIdAndTaskTokenId(businessUnitCode, preMetaId, preTokenId);
        String edgeType = null;
        if (taskInsDTO.getStatus().equals(TaskStatusEnum.CLOSE)) {
            edgeType = BusinessUnitEdgeTypeEnum.PUSHDOWN_FS.name();
        } else if (taskInsDTO.getStatus().equals(TaskStatusEnum.OPEN)) {
            edgeType = BusinessUnitEdgeTypeEnum.PUSHDOWN_SS.name();
        }

        String targetTokenId = UUID.randomUUID().toString();

        //保存任务实例
        businessUnitEventEntity.saveTaskIns(new TaskInsDTO(businessUnitCode, taskInsDTO.getProcessInsId(), metaId, targetTokenId, TaskStatusEnum.OPEN));

        return transformationService.transformation(
                preMetaId
                , preTokenId
                , metaId
                , targetTokenId
                , businessUnitFacadeQueryEntity.findEdgeByFromMetaIdAndToMetaId(businessUnitCode, preMetaId, metaId, edgeType).getRuleId()
                , RuleTypeEnum.PUSHDOWN
        );
    }

    /**
     * 单据提交
     *
     * @param tokenDataDto
     * @param metaId
     */
    public void submit(String businessUnitCode, TokenDataDto tokenDataDto, String metaId) {
        BusinessUnitTaskDefinitionDTO businessUnitTaskDefinitionDTO = businessUnitFacadeQueryEntity.findBusinessUnitTaskByMetaId(metaId);

        //状态规则
        if (!StringUtils.isEmpty(businessUnitTaskDefinitionDTO.getStatusRuleId())) {
            StatusRuleDTO statusRuleDTO = businessUnitFacadeQueryEntity.findStatusRuleById(businessUnitTaskDefinitionDTO.getStatusRuleId());
            businessUnitEventEntity.excuteStoredProcedure(statusRuleDTO.getStoredProcedure(), tokenDataDto.getTokenId());
        }

        TaskInsDTO taskInsDTO = businessUnitFacadeQueryEntity.getTaskByTokenId(businessUnitCode, tokenDataDto.getTokenId());

        List<BusinessUnitEdgeDTO> businessUnitEdgeDTOList = businessUnitFacadeQueryEntity.findNextTaskMetaByBusinessUnitCodeAndFromMetaId(businessUnitCode, metaId);
        businessUnitEdgeDTOList.forEach(businessUnitEdgeDTO -> {
            //自动下推和反写
            switch (businessUnitEdgeDTO.getEdgeType()) {
                case PUSHDOWN_AUTO:
                    if (taskInsDTO.getStatus().equals(TaskStatusEnum.CLOSE)) {
                        String targetTokenId = UUID.randomUUID().toString();
                        transformationService.transformation(
                                metaId
                                , tokenDataDto.getTokenId()
                                , businessUnitEdgeDTO.getToMetaId()
                                , targetTokenId
                                , businessUnitEdgeDTO.getRuleId()
                                , RuleTypeEnum.PUSHDOWN
                        );
                        //保存任务实例
                        businessUnitEventEntity.saveTaskIns(new TaskInsDTO(
                                businessUnitCode
                                , taskInsDTO.getProcessInsId()
                                , businessUnitEdgeDTO.getToMetaId()
                                , targetTokenId
                                , TaskStatusEnum.OPEN));
                    }
                    break;
                case REVERSE:
                    transformationService.transformation(
                            metaId
                            , tokenDataDto.getTokenId()
                            , businessUnitEdgeDTO.getToMetaId()
                            , businessUnitFacadeQueryEntity.getTaskInsByTaskMetaIdAndProcessInsId(
                                    businessUnitCode
                                    , taskInsDTO.getProcessInsId()
                                    , businessUnitEdgeDTO.getToMetaId()).get(0).getTaskTokenId()
                            , businessUnitEdgeDTO.getRuleId()
                            , RuleTypeEnum.REVERSE
                    );

            }

        });
        // 如果反写凭证，则执行存储过程
        List<CertificateDefinitionDTO> certificateDefinitionDTOList = businessUnitFacadeQueryEntity.findCertificateByMetaId(metaId);
        if (certificateDefinitionDTOList.size() > 0) {
            certificateDefinitionDTOList.forEach(certificateDefinitionDTO -> {
                String certificateTokenId = businessUnitEventEntity.excuteStoredProcedure(certificateDefinitionDTO.getStoredProcedure(), tokenDataDto.getTokenId());
                List<HandoverDefinitionDTO> handoverDefinitionDTOList = businessUnitFacadeQueryEntity.findHandoverByFromBUAndCertificateMetaId(
                        businessUnitCode
                        , certificateDefinitionDTO.getCertificateMetaId()
                        , metaId);
                // 查找交接业务单元，并保存交接实例
                List<HandoverInsDTO> handoverInsDTOList = new ArrayList<>();
                handoverDefinitionDTOList.forEach(handoverDefinitionDTO -> handoverInsDTOList.add(new HandoverInsDTO().build(handoverDefinitionDTO, certificateTokenId, tokenDataDto.getTokenId())));
                businessUnitEventEntity.saveHandoverIns(handoverInsDTOList);
            });
        }
    }

    /**
     * 获取schemaDTO
     *
     * @param id
     * @return
     */
    public SchemaDTO findSchemaById(String id) {
        // 首先找出schemaId
        SchemaDTO schemaDTO = schemaFacadeQueryEntity.findSchemaById(id);

        if(!StringUtils.isEmpty(schemaDTO.getBillHeader().getMetaId())){
            List<MetaRelDTO> metaRelDTOS = metaQueryFacadeEntity.getRelMetasByBillMeta(schemaDTO.getBillHeader().getMetaId());
            if(metaRelDTOS!=null&&metaRelDTOS.size()>0){
                metaRelDTOS.forEach(metaRelDTO -> {
                    schemaDTO.getRelBillDTOList().add(new BillSchemaDTO(metaRelDTO.getEntryMetaId(),metaRelDTO.getMetaType(),metaRelDTO.getOrder_()));
                });
            }
            List<TokenRelDTO> tokenRelDTOs = tokenQueryFacadeEntity.getRelTokenByBillMetaIdAndBillTokenId(schemaDTO.getBillHeader().getMetaId(),schemaDTO.getBillHeader().getTokenId());
            schemaDTO.getRelBillDTOList().forEach(relBillDTO -> {
                tokenRelDTOs.forEach(tokenRelDTO -> {
                    if(relBillDTO.getMetaId().equals(tokenRelDTO.getEntryMetaId())){
                        relBillDTO.build(tokenRelDTO.getEntryTokenId());
                    }
                });
            });
        }
        return schemaDTO;
    }

    /**
     * 删除schema
     * @param id
     */
    public void deleteSchema(String id) {
        businessUnitEventEntity.deleteSchema(id);
    }
}
