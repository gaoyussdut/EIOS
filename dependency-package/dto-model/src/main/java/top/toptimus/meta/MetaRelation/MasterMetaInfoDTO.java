package top.toptimus.meta.MetaRelation;

import lombok.*;
import top.toptimus.common.enums.process.MetaRelEnum;

import java.io.Serializable;

/**
 * 单据关联表单的信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MasterMetaInfoDTO implements Serializable {
    private static final long serialVersionUID = 7874063356171171873L;
    private String entryMasterMetaId;//（分录、关联单据、引用单据）的主数据meta
    private String metaName; // meta名称
    private MetaRelEnum metaRelEnum;     //关联类型
    private String businessCode;  //业务码（单据转换用）

    public MasterMetaInfoDTO build(String metaName) {
        this.metaName = metaName;
        return this;
    }
}
