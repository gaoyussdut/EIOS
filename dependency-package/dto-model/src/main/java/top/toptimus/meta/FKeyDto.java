package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.meta.metaFkey.MetaFKeyDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.io.Serializable;

/**
 * meta中key的信息
 * Created by jianghao on 2018/5/10.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FKeyDto implements Serializable {

    private static final long serialVersionUID = 3825804003848787201L;

    private Integer id;
    private String metaId; //外键与tokenMetaId关联
    private String key;
    private String caption;
    private String fkeyType; // 是否是select和selectIntern

    public MetaFKeyDTO buildMetaFKeyDTO() {
        return new MetaFKeyDTO(this.key, this.caption, this.fkeyType);
    }

    /**
     * 构建方法
     *
     * @param metaInfoDTO 一条meta信息
     * @return fkey
     */
    public FKeyDto build(MetaInfoDTO metaInfoDTO) {
        this.metaId = metaInfoDTO.getMetaId();
        this.key = metaInfoDTO.getKey();
        this.caption = metaInfoDTO.getCaption();
        this.fkeyType = metaInfoDTO.getFkeytype();
        return this;
    }

}
