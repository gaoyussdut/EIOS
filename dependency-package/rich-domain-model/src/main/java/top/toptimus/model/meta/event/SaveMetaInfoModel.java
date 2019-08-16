package top.toptimus.model.meta.event;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.meta.*;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * top.toptimus.entity.meta.event.MetaEventEntity#saveMetaInfoDTO(java.util.List)
 * 保存meta信息的充血model
 *
 * @author gaoyu
 */
@Getter
public class SaveMetaInfoModel extends BaseModel {
    private String metaId;
    private List<FKeyDto> fKeyDaos = new ArrayList<>();
    private List<RWpermissionDto> rWpermissionDaos = new ArrayList<>();
    private List<RalValueDto> ralValueDaos = new ArrayList<>();
    private List<FKeyOrderDto> fKeyOrderDaos = new ArrayList<>();
    private TokenMetaInformationDto tokenMetaInformationDto = new TokenMetaInformationDto();
    private String tableName;
    private TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO = new TokenTemplateDefinitionDTO();
    private List<MetaInfoDTO> metaInfoDaos = new ArrayList<>();

    public SaveMetaInfoModel(List<MetaInfoDTO> metaInfoDTOs) {
        this.metaInfoDaos = metaInfoDTOs;
        try {
            if (metaInfoDTOs == null || metaInfoDTOs.size() == 0) {
                this.metaId = null;
            } else {
                this.metaId = metaInfoDTOs.get(0).getMetaId();
                this.tableName = metaId;
                for (MetaInfoDTO metaInfoDTO : metaInfoDTOs) {
                    // meta中key的信息保存 t_token_meta_fkey
                    this.fKeyDaos.add(new FKeyDto().build(metaInfoDTO));
                    // key字段中的读写属性保存 t_token_meta_permissions
                    this.rWpermissionDaos.add(new RWpermissionDto().build(metaInfoDTO));
                    if (StringUtils.isNotEmpty(metaInfoDTO.getFKey())
                            && StringUtils.isNotEmpty(metaInfoDTO.getMetaKey())
                            && StringUtils.isNotEmpty(metaInfoDTO.getMetaName())
                            ) {
                        // key字段的关联信息保存 t_token_meta_ralvalue
                        this.ralValueDaos.add(new RalValueDto().build(metaInfoDTO));
                    }
                    // meta中key的顺序信息保存 t_token_meta_fkey_order
                    this.fKeyOrderDaos.add(new FKeyOrderDto().build(metaInfoDTO));
                }
            }
        } catch (Exception e) {
            this.buildErrorMessage(false, e.getMessage());
            e.printStackTrace();
        }
    }

    public SaveMetaInfoModel build(TokenMetaInformationDto tokenMetaInformationDto){
        this.tokenMetaInformationDto = tokenMetaInformationDto;
        return this;
    }

    public SaveMetaInfoModel build(TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO){
        this.tokenTemplateDefinitionDTO = tokenTemplateDefinitionDTO;
        return this;
    }

}
