package top.toptimus.entity.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.process.MetaRelEnum;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.merkle.GraphQLModel;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.repository.BusinessCodeRepository;
import top.toptimus.repository.FkeyRuleRepository;
import top.toptimus.repository.RuleDefinitionRepository;
import top.toptimus.rule.*;
import top.toptimus.transformation.TransformationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * rule query facade
 *
 * @author lzs
 * @since 2019-2-19
 */
@Component
public class RuleQueryFacadeEntity {

    @Autowired
    private RuleDefinitionRepository ruleDefinitionRepository;
    @Autowired
    private FkeyRuleRepository fkeyRuleRepository;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private BusinessCodeRepository businessCodeRepository;

    /**
     * 获取全部规则定义
     *
     * @return 规则定义list
     */
    public List<BusinessCodeDTO> getBusinessCodeDefinitionDTOs() {
        return businessCodeRepository.findAll();
    }

    /**
     * 获取规则属性信息
     *
     * @return 规则定义list
     */
    public BusinessCodeDTO getBusinessCodeDefinitionDTO(String businessCode) {
        return businessCodeRepository.findById(businessCode);
    }

    /**
     * 获取fkey规则
     *
     * @param businessCode 业务吗
     * @param ruleType     规则类型
     * @return 规则字段规则DTO列表
     */
    public List<FkeyRuleDTO> getFkeyRuleByBusinessCode(String businessCode, String ruleType) {
        return fkeyRuleRepository.findByBusinessCodeAndruleType(businessCode, ruleType);
    }

    /**
     * 获取fkey规则
     *
     * @param businessCode 事务码
     * @param metaId       meta id
     * @return 规则字段规则DTO
     */
    private List<FkeyRuleDTO> getFkeyRuleByBusinessCodeAndMetaId(String businessCode, String metaId) {
        return fkeyRuleRepository.findByBusinessCodeAndMetaId(businessCode, metaId);
    }

    /**
     * 获取来源字段信息(SELECT平铺)
     *
     * @return Result
     */
    public List<MetaRuleDTO> getCalculateFkeyByBusinessCode(String billMetaId) {
        // 分录的metaId
        List<String> metaIds = metaQueryFacadeEntity.findEntryMetaByBillMeta(billMetaId, MetaRelEnum.ENTRY);

        return new ArrayList<MetaRuleDTO>() {{
            //fkey初始化
            metaIds.forEach(metaId -> {
                List<MetaInfoDTO> metaInfoDTOList = metaQueryFacadeEntity.findByMetaId(metaId);
                metaInfoDTOList.addAll(
                        new ArrayList<MetaInfoDTO>() {{
                            metaInfoDTOList.forEach(metaInfoDTO -> {
                                //SELECT类型处理
                                if (metaInfoDTO.getFkeytype().equals("SELECT")) {
                                    List<MetaInfoDTO> selectMetaInfoDTOList = metaQueryFacadeEntity.findByMetaId(metaInfoDTO.getMetaKey());
                                    addAll(selectMetaInfoDTOList);
                                }
                            });
                        }}
                );
                if (metaId.equals(billMetaId)) {
                    add(new MetaRuleDTO(metaId, metaQueryFacadeEntity.getMetaInfo(metaId).getTokenMetaName(), MetaRelEnum.BILL_HEADER.name()).build(metaInfoDTOList));
                } else {
                    add(new MetaRuleDTO(metaId, metaQueryFacadeEntity.getMetaInfo(metaId).getTokenMetaName(), MetaRelEnum.ENTRY.name()).build(metaInfoDTOList));
                }

            });
        }};
    }

    /**
     * 获取字段规则信息
     *
     * @return Result
     */
    public List<MetaRuleDTO> getMetaRuleDTOByBusinessCode(String businessCode, String billMetaId) {
        // 分录的metaId
        List<String> metaIds = metaQueryFacadeEntity.findEntryMetaByBillMeta(billMetaId, MetaRelEnum.ENTRY);

        return
                new ArrayList<MetaRuleDTO>() {{
                    //新增
                    if (StringUtils.isEmpty(businessCode)) {
                        //fkey初始化
                        metaIds.forEach(metaId -> {
                            List<MetaInfoDTO> metaInfoDTOList = metaQueryFacadeEntity.findByMetaId(metaId);
                            List<FkeyRuleDTO> fkeyRuleDTOList = new ArrayList<>();
                            metaInfoDTOList.forEach(metaInfoDTO -> {
//                                if (metaInfoDTO.getFkeytype().equals("SELECT")) {
//                                    List<MetaInfoDTO> selectMetaInfoDTOList = metaQueryFacadeEntity.findByMetaId(metaInfoDTO.getMetaKey());
//                                    selectMetaInfoDTOList.forEach(selectMetaInfoDTO -> {
//                                        FkeyRuleDTO fkeyRuleDTO = new FkeyRuleDTO(selectMetaInfoDTO.getMetaId(), selectMetaInfoDTO.getKey(), selectMetaInfoDTO.getCaption(), selectMetaInfoDTO.getFkeytype());
//                                        fkeyRuleDTOList.add(fkeyRuleDTO);
//                                    });
//                                } else {
                                FkeyRuleDTO fkeyRuleDTO = new FkeyRuleDTO(metaId, metaInfoDTO.getKey(), metaInfoDTO.getCaption(), metaInfoDTO.getFkeytype());
                                fkeyRuleDTOList.add(fkeyRuleDTO);
//                                }
                            });
                            if (metaId.equals(billMetaId)) {
                                add(new MetaRuleDTO(metaId, metaQueryFacadeEntity.getMetaInfo(metaId).getTokenMetaName(), MetaRelEnum.BILL_HEADER.name(), fkeyRuleDTOList));
                            } else {
                                add(new MetaRuleDTO(metaId, metaQueryFacadeEntity.getMetaInfo(metaId).getTokenMetaName(), MetaRelEnum.ENTRY.name(), fkeyRuleDTOList));
                            }

                        });

                    }
                    //编辑
                    else {
                        metaIds.forEach(metaId -> {
                            if (metaId.equals(billMetaId)) {
                                add(new MetaRuleDTO(metaId, metaQueryFacadeEntity.getMetaInfo(metaId).getTokenMetaName(), MetaRelEnum.BILL_HEADER.name(), getFkeyRuleByBusinessCodeAndMetaId(businessCode, metaId)));
                            } else {
                                add(new MetaRuleDTO(metaId, metaQueryFacadeEntity.getMetaInfo(metaId).getTokenMetaName(), MetaRelEnum.ENTRY.name(), getFkeyRuleByBusinessCodeAndMetaId(businessCode, metaId)));
                            }
                        });
                    }
                }};
    }

//    /**
//     * 生成据转换DTO
//     *
//     * @param cacheId              缓存id
//     * @param authId               auth id
//     * @param currentCachePlaceDTO 缓存中的当前单据DTO
//     * @param preCachePlaceDTO     缓存中的源单
//     * @return 单据转换DTO
//     */
//    public TransformationDTO generateTransformation(
//            String cacheId
//            , String authId
//            , PlaceDTO currentCachePlaceDTO
//            , PlaceDTO preCachePlaceDTO
//    ) {
//        return new TransformationDTO(preCachePlaceDTO, currentCachePlaceDTO)    // 构造单据转换DTO
//                .build(
//                        this.getBusinessCode(currentCachePlaceDTO.getPreMetaTokenRelationDTO().getSourceBillMetaId(), currentCachePlaceDTO.getBillMetaid())
//                        , currentCachePlaceDTO
//                        , preCachePlaceDTO
//                );  //  根据主数据清洗单据转换DTO
//    }

    /**
     * 根据源单据meta和目标单据meta获取事务码
     *
     * @param sourceMetaId 源单据meta
     * @param targetMetaId 目标单据meta
     * @return 事务码
     */
    public String getBusinessCode(String sourceMetaId, String targetMetaId) {
        RuleDefinitionDTO ruleDefinitionDTO = ruleDefinitionRepository.findBySourceMetaAndTargetMeta(sourceMetaId, targetMetaId);
        return ruleDefinitionDTO.getBusinessCode();
    }

    /**
     * 获取GraphQlModel
     *
     * @param transformationDTO 单据转换上下级单据索引信息
     */
    public GraphQLModel getGraphQLModel(TransformationDTO transformationDTO) {

//        RuleModel ruleModel = new RuleModel(businessCodeRepository.findByOriginMetaAndTargetMeta(transformationDTO.getPrePlaceDTO().getBillMetaId(), transformationDTO.getCurrentPlaceDTO().getBillMetaId()));
//        List<RuleDefinitionDTO> ruleDefinitionDTOList = ruleDefinitionRepository.findByBusinessCode(ruleModel.getBusinessCodeDTO().getBusinessCode());
//        ruleDefinitionDTOList.forEach(ruleDefinitionDTO -> {
//            ruleDefinitionDTO.build(fkeyRuleRepository.findByRuleId(ruleDefinitionDTO.getRuleId()));
//        });
//        ruleModel.build(ruleDefinitionDTOList);
        return null;

    }

    /**
     * 根据事务码获取ruleDTO
     *
     * @param businessCode 事务码
     * @return 规则配置页面DTO
     */
    public RuleDTO getBusinessCodeDetail(String businessCode) {

        BusinessCodeDTO businessCodeDTO = businessCodeRepository.findById(businessCode);
        List<RuleDefinitionDTO> ruleDefinitionDTOList = ruleDefinitionRepository.findByBusinessCode(businessCode);
        ruleDefinitionDTOList.forEach(ruleDefinitionDTO -> {
            ruleDefinitionDTO.build(fkeyRuleRepository.findByRuleId(ruleDefinitionDTO.getRuleId()));
        });
        return new RuleDTO(businessCodeDTO, ruleDefinitionDTOList);
    }

    /**
     * 根据事务码获取规则
     *
     * @param businessCode 事务码
     * @return 规则属性DTO列表
     */
    public List<RuleDefinitionDTO> getRuleDefinitionByBusinessCode(String businessCode) {
        return ruleDefinitionRepository.findByBusinessCode(businessCode);
    }

    /**
     * 获取全部规则定义
     *
     * @return 规则定义list
     */
    public List<BusinessCodeDTO> findByOriginMetaAndTargetMetaAndType(String originMetaId, String type) {
        return businessCodeRepository.findByOriginMetaAndTargetMetaAndType(originMetaId, type);
    }
}
