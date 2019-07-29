package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.io.Serializable;

/**
 * meta中key的顺序信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FKeyOrderDto implements Serializable {

    private static final long serialVersionUID = 5383309971814868328L;
    private Integer id;
    private String metaId; //外键与tokenMetaId关联
    private String key;
    private String order_; // 顺序

    public FKeyOrderDto build(MetaInfoDTO metaInfoDTO) {
        this.metaId = metaInfoDTO.getMetaId();
        this.key = metaInfoDTO.getKey();
        this.order_ = metaInfoDTO.getOrder_();
        return this;
    }
}
