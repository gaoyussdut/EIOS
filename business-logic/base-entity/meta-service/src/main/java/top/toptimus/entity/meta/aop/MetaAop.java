package top.toptimus.entity.meta.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.DomainTypeEnum;
import top.toptimus.entity.meta.aop.eventHandler.MetaSaveHandler;
import top.toptimus.entity.meta.event.MetaEventEntity;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.model.meta.event.SaveMetaInfoModel;
import top.toptimus.model.meta.event.SaveMetaInfosModel;
import top.toptimus.model.meta.event.SaveMetaModel;
import top.toptimus.repository.meta.*;
import top.toptimus.repository.meta.tokentemplate.TokenTemplateRepository;

@Aspect
@Component
public class MetaAop {
    @Autowired
    private MetaSaveHandler metaSaveHandler;
    @Autowired
    private FKeyRepository fKeyRepository;
    @Autowired
    private RWpermissionRepositroy rwPermissionRepository;
    @Autowired
    private RalvalueRepository ralValueRepository;
    @Autowired
    private FKeyOrderRepository fKeyOrderRepository;
    @Autowired
    private MetaEventEntity metaEventEntity;
    @Autowired
    private MetaTableDDLRepository metaTableDDLRepository;
    @Autowired
    private ProcessTableRepository processTableRepository;
    @Autowired
    private TokenMetaInformationRepository tokenMetaInformationRepository;
    @Autowired
    private TokenTemplateRepository tokenTemplateRepository;

    /**
     * 保存meta 分成4个结构
     *
     * @param retValue SaveMetaModel
     */
    @AfterReturning(returning = "retValue"
            , pointcut = "execution(" +
            "public * top.toptimus.entity.meta.event.MetaEventEntity.saveMeta(..))"
    )
    public void saveBusinessAspectAfterReturning(Object retValue) {
        SaveMetaModel saveMetaModel = (SaveMetaModel) retValue;
        // 保存meta信息
        metaSaveHandler.saveMeta(saveMetaModel.getTokenMetaInfoDTO(), saveMetaModel.getFKeyDaos(), saveMetaModel.getRWpermissionDaos(), saveMetaModel.getRalValueDaos());
    }

    /**
     * 保存meta信息
     *
     * @param retValue SaveMetaInfoModel
     */
    @AfterReturning(returning = "retValue"
            , pointcut = "execution(" +
            "public * top.toptimus.entity.meta.event.MetaEventEntity.saveMetaInfo(..))"
    )
    public void saveMetaInfoDTOAfterReturning(Object retValue) {
        SaveMetaInfoModel saveMetaInfoModel = (SaveMetaInfoModel) retValue;
        if (saveMetaInfoModel.isSuccess()) {
            if (!StringUtils.isEmpty(saveMetaInfoModel.getMetaId())) {
                // 全删除
                fKeyRepository.deleteByMetaId(saveMetaInfoModel.getMetaId());
                rwPermissionRepository.deleteByMetaId(saveMetaInfoModel.getMetaId());
                ralValueRepository.deleteByMetaId(saveMetaInfoModel.getMetaId());
                fKeyOrderRepository.deleteByMetaId(saveMetaInfoModel.getMetaId());
                processTableRepository.delete(saveMetaInfoModel.getMetaId());
                tokenMetaInformationRepository.delete(saveMetaInfoModel.getTokenMetaInformationDto().getTokenMetaId());
                tokenTemplateRepository.delete(saveMetaInfoModel.getTokenTemplateDefinitionDTO().getTokenTemplateId());

                // t_token_meta_fkey
                fKeyRepository.saveAll(saveMetaInfoModel.getFKeyDaos());
                // t_token_meta_permissions
                rwPermissionRepository.saveAll(saveMetaInfoModel.getRWpermissionDaos());
                if (saveMetaInfoModel.getFKeyDaos().size() > 0) {
                    // t_token_meta_ralvalue
                    ralValueRepository.saveAll(saveMetaInfoModel.getRalValueDaos());
                }
                // t_token_meta_fkey_order
                fKeyOrderRepository.saveAll(saveMetaInfoModel.getFKeyOrderDaos());
                // t_process_table
                processTableRepository.save(saveMetaInfoModel.getTableName(),saveMetaInfoModel.getMetaId());
                // t_token_meta_formation
                tokenMetaInformationRepository.save(saveMetaInfoModel.getTokenMetaInformationDto());
                // t_tokentemplate_definition
                tokenTemplateRepository.save(saveMetaInfoModel.getTokenTemplateDefinitionDTO());
            }
        }
        //  TODO    错误日志

    }

//    /**
//     * 保存meta和table关系
//     *
//     * @param retValue SaveMetaInfosModel
//     */
//    @AfterReturning(returning = "retValue"
//            , pointcut = "execution(" +
//            "public * top.toptimus.entity.meta.event.MetaEventEntity.saveMetaInfoDTO(..))"
//    )
//    public void saveMetaInfoDTOSAfterReturning(Object retValue) {
//        SaveMetaInfosModel saveMetaInfosModel = (SaveMetaInfosModel) retValue;
//        //  TODO    AOP 事务控制
//        // 追加meta
//        metaEventEntity.saveMetaInfoDTO(saveMetaInfosModel.getMetaInfoDTOS());
//        // 创建视图
//        if (saveMetaInfosModel.getMetaInfoDTOS() != null && saveMetaInfosModel.getMetaInfoDTOS().size() != 0) {
//
//            if (saveMetaInfosModel.getTokenMetaInfoDTO().getMetaType().equals(DomainTypeEnum.BO.name())) { // BO的场合
//                // 删除视图
//                metaTableDDLRepository.dropViewDDL(
//                        saveMetaInfosModel.getTokenMetaInfoDTO().getTokenMetaId()
//                        , saveMetaInfosModel.getTokenMetaInfoDTO().getTokenMetaName()
//                );
//                // 创建表
//                metaTableDDLRepository.createTable(saveMetaInfosModel.getTokenMetaInfoDTO().getTokenMetaId(), saveMetaInfosModel.getMetaFieldMap());
//                // 创建视图
//                metaTableDDLRepository.createViewDDL(saveMetaInfosModel.getTokenMetaInfoDTO());
//            }
//        }
//    }
}
