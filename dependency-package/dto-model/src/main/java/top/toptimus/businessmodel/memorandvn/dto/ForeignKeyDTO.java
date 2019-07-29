package top.toptimus.businessmodel.memorandvn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 外键DTO
 *
 * @author lizongsheng
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForeignKeyDTO implements Serializable {

    private static final long serialVersionUID = -4145017339384927567L;

    private String fieldEntityTypeName; // 关联的metaID
    private String fieldPath;  // 关联的metaID的外键
    private String fieldType;  // 外键类型
    private boolean hasReference;
    private String referenceEntityTypeName; // 本身metaId
    private String referencePath; // 主键
    private String relationTTID; // 关联meta所在ttID
    private String relationTTName; // 关联meta所在ttName

    public void build(String relationTTID, String relationTTName) {
        this.relationTTID = relationTTID;
        this.relationTTName = relationTTName;
    }
}
