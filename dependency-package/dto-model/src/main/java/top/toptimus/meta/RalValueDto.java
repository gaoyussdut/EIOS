package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.meta.metaview.MetaInfoDTO;

/**
 * key字段的关联信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RalValueDto {

    private Integer id;
    private String tokenMetaId;
    private String key;
    private String metaName;
    private String fkey;
    private String metaKey;


    public RalValueDto build(MetaInfoDTO metaInfoDTO) {
        this.tokenMetaId = metaInfoDTO.getMetaId();
        this.key = metaInfoDTO.getKey();
        this.metaName = metaInfoDTO.getMetaName();
        this.fkey = metaInfoDTO.getFKey();
        this.metaKey = metaInfoDTO.getMetaKey();
        return this;
    }
}
