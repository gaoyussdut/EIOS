package top.toptimus.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则属性DTO
 * Created by lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetaRuleDTO {
    private String metaId;
    private String metaName;
    private String metaType;
    private List<FkeyRuleDTO> fkeyRuleDTOList = new ArrayList<>();

    public MetaRuleDTO (String metaId, String metaName, String metaType){
        this.metaId = metaId;
        this.metaName = metaName;
        this.metaType = metaType;
    }

    public MetaRuleDTO build(List<MetaInfoDTO> metaInfoDTOList){
        metaInfoDTOList.forEach(metaInfoDTO -> {
            this.fkeyRuleDTOList.add(new FkeyRuleDTO(metaInfoDTO.getMetaId(),metaInfoDTO.getKey(),metaInfoDTO.getCaption(),metaInfoDTO.getFkeytype()));
        });
        return this;
    }
}
