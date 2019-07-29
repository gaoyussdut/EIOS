package top.toptimus.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.util.List;

/**
 * 来源字段DTO
 * Created by lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CalculateFkeyDTO {
    private String metaId;
    private String metaName;
    private String metaType;
    private List<MetaInfoDTO> metaInfoDTOList;
}
