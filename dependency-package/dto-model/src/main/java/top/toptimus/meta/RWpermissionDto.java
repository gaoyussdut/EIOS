package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.meta.metaview.MetaInfoDTO;


/**
 * key字段中的读写属性
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RWpermissionDto {

    private Integer id;
    private String tokenMetaId;
    private String key;
    private Boolean visible; //是否可见
    private Boolean readonly; //只读
    private Boolean required;
    private String validation; // Json特殊校验

    /**
     * 构建方法
     *
     * @param metaInfoDTO meta info
     * @return this
     */
    public RWpermissionDto build(MetaInfoDTO metaInfoDTO) {
        this.tokenMetaId = metaInfoDTO.getMetaId();
        this.key = metaInfoDTO.getKey();
        this.visible = metaInfoDTO.isVisible();
        this.readonly = metaInfoDTO.isReadOnly();
        this.required = metaInfoDTO.isRequired();
        this.validation = metaInfoDTO.getValidation();
        return this;
    }
}
