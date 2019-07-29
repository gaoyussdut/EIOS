package top.toptimus.model.meta.event;

import lombok.Getter;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.common.enums.DomainTypeEnum;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * top.toptimus.entity.meta.event.MetaEventEntity#saveMetaInfoDTOS(java.util.List)
 * 追加多条MetaInfos的充血模型
 *
 * @author gaoyu
 */
@Getter
public class SaveMetaInfosModel extends BaseModel {
    private List<MetaInfoDTO> metaInfoDTOS;
    private TokenMetaInfoDTO tokenMetaInfoDTO;
    private Map<String, MetaFieldDTO> metaFieldMap = new HashMap<>();

    public SaveMetaInfosModel(List<MetaInfoDTO> metaInfoDTOS) {
        this.metaInfoDTOS = metaInfoDTOS;
    }

    public void build(TokenMetaInfoDTO tokenMetaInfoDTO) {
        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
        if (tokenMetaInfoDTO.getMetaType().equals(DomainTypeEnum.BO.name())) { // BO的场合
            Map<String, MetaFieldDTO> metaFieldMap = new HashMap<>();
            for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
                if (!metaFieldMap.containsKey(metaField.getKey())) {
                    metaFieldMap.put(metaField.getKey(), metaField);
                }
            }
        }
    }
}
