package top.toptimus.meta.csv;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * meta索引文件
 *
 * @author gaoyu
 * @since 2019-08-16
 */
@NoArgsConstructor
@Getter
public class MetaIndexDTO {
    private String metaId;  //  meta id
    private String tokenMetaName;   //  文件名，也是token meta名称
    private boolean isModify = false;   //  是否修改

    /**
     * 构造函数
     *
     * @param metaId        meta id
     * @param tokenMetaName 文件名，也是token meta名称
     */
    public MetaIndexDTO(String metaId, String tokenMetaName) {
        this.metaId = metaId;
        this.tokenMetaName = tokenMetaName;
    }
}
