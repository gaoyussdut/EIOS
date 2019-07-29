package top.toptimus.meta.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;

import java.io.Serializable;

/**
 * 字段值的类型
 *
 * @author jp
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FieldValueType implements Serializable {

    /**
     * 数据类型
     */
    private FkeyTypeEnum type;
    /**
     * 关联副表属性：SrcTableName + SrcFieldName + ExpFieldName
     */
    private RalValue ralValue;

}
