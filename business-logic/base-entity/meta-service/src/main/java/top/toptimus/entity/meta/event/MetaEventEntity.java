package top.toptimus.entity.meta.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.meta.FKeyCaptionDto;
import top.toptimus.meta.FKeyTypeDto;
import top.toptimus.meta.SelfDefiningMetaDTO;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.model.meta.event.SaveMetaInfoModel;
import top.toptimus.model.meta.event.SaveMetaModel;
import top.toptimus.model.meta.event.SaveMetaSelectInfoModel;
import top.toptimus.repository.meta.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * meta事务处理
 *
 * @author gaoyu
 */
@Component
public class MetaEventEntity {
    /**
     * repository
     */
    @Autowired
    private FKeyTypeRepository fKeyTypeRepository;
    @Autowired
    private MetaTableDDLRepository metaTableDDLRepository;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private SelfDefiningMetaRepository selfDefiningMetaRepository;
    @Autowired
    private ProcessTableRepository processTableRepository;
    @Autowired
    private FKeyRepository fKeyRepository;

    /**
     * 保存meta 分成4个结构
     *
     * @param tokenMetaInfoDTO tokenMetaInfoDTO
     * @apiNote top.toptimus.entity.meta.aop.MetaAop#saveBusinessAspectAfterReturning(java.lang.Object)
     */
    @SuppressWarnings("UnusedReturnValue")
    public SaveMetaModel saveMeta(TokenMetaInfoDTO tokenMetaInfoDTO) {
        SaveMetaModel saveMetaModel = new SaveMetaModel(tokenMetaInfoDTO);
        saveMetaModel.build(fKeyTypeRepository.findAllByFkeys(saveMetaModel.getFkeys()));
        return saveMetaModel;
    }

    /**
     * 保存meta下所有fkey为基础类型的
     *
     * @param tokenMetaInfoDTO tokenMetaInfoDTO
     */
    public void saveMetaInfoFkey(TokenMetaInfoDTO tokenMetaInfoDTO) {
        List<FKeyTypeDto> fKeyTypeDaos = new ArrayList<>();
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            if (!metaField.getType().getType().equals(FkeyTypeEnum.SELECT)
                    && !metaField.getType().getType().equals(FkeyTypeEnum.SELECT_INTERN)) {
                fKeyTypeDaos.add(new FKeyTypeDto(metaField.getKey(), metaField.getType().getType()));
            }
        }
        fKeyTypeRepository.saveAll(fKeyTypeDaos);
    }

    /**
     * 保存meta下所有fkey为SELECT和SELECT_INTERN 的基础类型
     *
     * @param tokenMetaInfoDTO tokenMetaInfoDTO
     */
    public void saveMetaInfoFKeySelectAndSelectIntern(TokenMetaInfoDTO tokenMetaInfoDTO) {
        SaveMetaSelectInfoModel saveMetaSelectInfoModel = new SaveMetaSelectInfoModel(tokenMetaInfoDTO);
        saveMetaSelectInfoModel.build(fKeyTypeRepository.findAllByFkeys(saveMetaSelectInfoModel.getFkeys()));
        fKeyTypeRepository.saveAll(saveMetaSelectInfoModel.getNewFKeyTypeDaos());
    }

    /**
     * 保存meta信息，
     *
     * @param metaInfoDTOs meta信息列表
     * @apiNote top.toptimus.entity.meta.aop.MetaAop#saveMetaInfoDTOAfterReturning(java.lang.Object)
     */
    @SuppressWarnings("UnusedReturnValue")
    public SaveMetaInfoModel saveMetaInfoDTO(List<MetaInfoDTO> metaInfoDTOs) {
        if (metaInfoDTOs.isEmpty()) {
            throw new RuntimeException("meta list为空");
        } else {
            this.createTableByMetaInfoDTOS(metaInfoDTOs.get(0).getMetaId(), metaInfoDTOs);  //  创建表
        }

        // 保存用的model
        return new SaveMetaInfoModel(metaInfoDTOs);
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     * @param metaIds   meta id列表
     */
    public void createTableByMetaInfo(String tableName, List<String> metaIds) {
        List<MetaInfoDTO> metaInfoDTOS = metaQueryFacadeEntity.findByMetaIds(metaIds);
        Map<String, MetaInfoDTO> metaInfoMap = new HashMap<String, MetaInfoDTO>() {{
            for (MetaInfoDTO metaInfoDTO : metaInfoDTOS) {
                put(metaInfoDTO.getKey(), metaInfoDTO);
            }
        }};

        metaTableDDLRepository.createTableByMetaInfo(tableName, metaInfoMap);
    }

    /**
     * 创建表
     *
     * @param tableName    表名
     * @param metaInfoDTOS meta info list
     */
    private void createTableByMetaInfoDTOS(String tableName, List<MetaInfoDTO> metaInfoDTOS) {
        Map<String, MetaInfoDTO> metaInfoMap = new HashMap<String, MetaInfoDTO>() {{
            for (MetaInfoDTO metaInfoDTO : metaInfoDTOS) {
                put(metaInfoDTO.getKey(), metaInfoDTO);
            }
        }};

        metaTableDDLRepository.createTableByMetaInfo(tableName, metaInfoMap);
    }

    /**
     * ALTER TABLE xxx add column
     *
     * @param metaId       meta id，也就是表名
     * @param metaInfoDTOS meta信息列表
     * @apiNote TODO
     */
    @SuppressWarnings("UnusedReturnValue")
    public SaveMetaInfoModel alterTableAddColumnsByMetaInfo(String metaId, List<MetaInfoDTO> metaInfoDTOS) {
        List<MetaInfoDTO> originalMetaInfoDTOs =
                metaQueryFacadeEntity.findByMetaId(metaId);   //  数据库当前的的meta info
        List<MetaInfoDTO> alterMetaInfoDTOS = new ArrayList<>();    //  变更后的meta

        if (metaInfoDTOS.isEmpty()) {
            throw new RuntimeException("meta list为空");
        } else {
            this.alterTableAddColumns(
                    metaId
                    , new ArrayList<MetaInfoDTO>() {
                        {
                            metaInfoDTOS.forEach(
                                    metaInfoDTO -> {
                                        boolean isAlterKey = true; //  要做add column的key
                                        for (MetaInfoDTO originalMetaInfoDTO : originalMetaInfoDTOs) {
                                            if (originalMetaInfoDTO.getFKey().equals(metaInfoDTO.getFKey())) {
                                                //  key相同时不变更
                                                isAlterKey = false;
                                                break;
                                            }
                                        }
                                        if (isAlterKey) {
                                            add(metaInfoDTO);   //  要做add column的key记录
                                            alterMetaInfoDTOS.add(metaInfoDTO);
                                        }
                                    }
                            );
                        }
                    }
            );   //  变更表
        }

        return new SaveMetaInfoModel(alterMetaInfoDTOS);
    }


    /**
     * ALTER TABLE xxx add column
     *
     * @param tableName    表名
     * @param metaInfoDTOS meta info list
     */
    private void alterTableAddColumns(String tableName, List<MetaInfoDTO> metaInfoDTOS) {
        Map<String, MetaInfoDTO> metaInfoMap = new HashMap<String, MetaInfoDTO>() {{
            for (MetaInfoDTO metaInfoDTO : metaInfoDTOS) {
                put(metaInfoDTO.getKey(), metaInfoDTO);
            }
        }};

        metaTableDDLRepository.createTableByMetaInfo(tableName, metaInfoMap);
    }

    /**
     * 保存自定义meta
     *
     * @param selfDefiningMetaDTO 用户自定义meta
     */
    public void saveSelfDefiningMeta(SelfDefiningMetaDTO selfDefiningMetaDTO) {
        selfDefiningMetaRepository.save(selfDefiningMetaDTO);
    }

    /**
     * 根据主数据metaID查找旗下的视图metaId并修改旗下的Caption
     *
     * @param masterMetaId    主数据metaID
     * @param fKeyCaptionDtos 字段Fkey和描述caption
     */
    public void updateMetaCaption(String masterMetaId, List<FKeyCaptionDto> fKeyCaptionDtos) {
        // 根据指定的主数据metaId Fkey Caption 修改数据
        for (FKeyCaptionDto fKeyCaptionDto : fKeyCaptionDtos) {
            fKeyTypeRepository.updateMetaFkeyCaption(masterMetaId, fKeyCaptionDto.getKey(), fKeyCaptionDto.getCaption());
        }
    }

    /**
     * 保存meta信息，
     *
     * @param saveMetaInfoModel meta信息列表
     * @apiNote top.toptimus.entity.meta.aop.MetaAop#saveMetaInfoDTOAfterReturning(java.lang.Object)
     */
    @SuppressWarnings("UnusedReturnValue")
    public SaveMetaInfoModel saveMetaInfo(SaveMetaInfoModel saveMetaInfoModel) {
        this.createTableByMetaInfoDTOS(saveMetaInfoModel.getMetaId(), saveMetaInfoModel.getMetaInfoDaos());  //  创建表
//        processTableRepository.save(saveMetaInfoModel.getTableName(),saveMetaInfoModel.getMetaId());//存table表
//        fKeyRepository.saveAll(saveMetaInfoModel.getFKeyDaos());

        // 保存用的model
        return saveMetaInfoModel;
    }


}