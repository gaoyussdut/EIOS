package top.toptimus.meta.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;

import java.io.Serializable;
import java.util.UUID;

/**
 * meta字段基础信息，包括各种读写的信息
 *
 * @author liushikuan
 * @since 2017年10月23日09:59:01
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetaFieldDTO implements Serializable {

    private static final long serialVersionUID = 780741466542567104L;
    /**
     * 对应
     */
    private String fieldId;
    /**
     * 业务类型<br>
     * tracker类型如：辅助资料，科目表，结算方式，币别<br>
     * bo类型如:计量单位组，基础资料（员工、部门等） 对应key
     */
    private String key;
    /**
     * 预定义的Key名
     */
    private String fieldName;
    /**
     * alias
     */
    private String caption;

    private FieldValueType type;

    /* 读写属性 */
    private Boolean visible; // 该字段是否可见
    private Boolean required; // 该字段是否必填
    private Boolean readonly; // 该字段是否只读

    public MetaFieldDTO createPlainMetaFIeld(String key, String fieldName, String caption, FkeyTypeEnum type, Boolean visible, Boolean required, Boolean readonly) {
        this.fieldId = UUID.randomUUID().toString();
        this.key = key;
        this.fieldName = fieldName;
        this.caption = caption;
        this.type = new FieldValueType(type, null);
        this.visible = visible;
        this.required = required;
        this.readonly = readonly;
        return this;
    }

    public MetaFieldDTO createPlainMetaFIeldString(String key, String fieldName, String caption
            , String ralfieldName, String metaKey, String metaName, String fkey, String templateTokenId
            , String type, String visible, String required, String readonly) {
        this.fieldId = UUID.randomUUID().toString();
        this.key = key;
        this.fieldName = fieldName;
        this.caption = caption;
        FkeyTypeEnum fkeyTypeEnum = FkeyTypeEnum.valueOf(type.toUpperCase());
        this.type = new FieldValueType(fkeyTypeEnum, new RalValue(metaKey, metaName, fkey));
        this.visible = Boolean.valueOf(visible);
        this.required = Boolean.valueOf(required);
        this.readonly = Boolean.valueOf(readonly);
        return this;
    }

    public MetaFieldDTO createSelectMetaFIeld(String key, String fieldName, String caption, FkeyTypeEnum type, RalValue ralValue, Boolean visible, Boolean required, Boolean readonly) {
        this.fieldId = UUID.randomUUID().toString();
        this.key = key;
        this.fieldName = fieldName;
        this.caption = caption;
        this.type = new FieldValueType(type, ralValue);
        this.visible = visible;
        this.required = required;
        this.readonly = readonly;
        return this;
    }

    public MetaFieldDTO createBaseMetaField(String key, FieldValueType type) {
        this.key = key;
        this.type = type;
        return this;
    }

    public MetaFieldDTO createBaseMetaField(String key, FieldValueType type, String caption) {
        this.key = key;
        this.type = type;
        this.caption = caption;
        return this;
    }

    /**
     * @param key     key
     * @param type    类型
     * @param caption 别名
     * @return 字段
     */
    public MetaFieldDTO createBaseMetaField(String key, FieldValueType type, String caption, Boolean visible, Boolean required, Boolean readonly) {
        this.key = key;
        this.type = type;
        this.caption = caption;
        this.visible = visible;
        this.required = required;
        this.readonly = readonly;
        return this;
    }

}
