package top.toptimus.entity.meta.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.meta.*;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.model.meta.event.SaveMetaInfoModel;
import top.toptimus.model.meta.event.SaveMetaModel;
import top.toptimus.model.meta.event.SaveMetaSelectInfoModel;
import top.toptimus.repository.meta.FKeyTypeRepository;
import top.toptimus.repository.meta.MetaTableDDLRepository;
import top.toptimus.repository.meta.SelfDefiningMetaRepository;

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
     * @param masterMetaId     主数据metaID
     * @param fKeyCaptionDtos  字段Fkey和描述caption
     */
    public void updateMetaCaption(String masterMetaId, List<FKeyCaptionDto> fKeyCaptionDtos) {
        // 根据指定的主数据metaId Fkey Caption 修改数据
        for (FKeyCaptionDto fKeyCaptionDto : fKeyCaptionDtos) {
            fKeyTypeRepository.updateMetaFkeyCaption(masterMetaId ,fKeyCaptionDto.getKey(),fKeyCaptionDto.getCaption());
        }
    }


}