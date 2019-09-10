package top.toptimus.tokendata.field;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 字段定义
 *
 * @author gaoyu
 */
@NoArgsConstructor
@Data
public class FkeyField implements Serializable {
    private static final long serialVersionUID = -5883024816746049863L;
    /**
     * 1、日期、数量、金额、备注等用户手填/手选字段
     * 2、辅助资料(客户、供应商、业务类别、会计科目、业务组织)，用内码区分
     * 3、带序列号或者批号的BO，用序列号或者批号区分
     * 4、表单，用批号区分
     */
    protected String fieldDomainType;


    protected FkeyTypeEnum dataType;
    protected String key;  //  FKey->Key 20180528
    protected String metaFieldType; //  @link com.footprint.model.meta.property.FieldValueType.type

    /**
     * 每个字段的特定值。
     * 当MetaFieldType为com.footprint.common.enums.FkeyTypeEnum#SelectType时
     * save的时候使用businessId和jsonData
     * retrieve的时候把businessId和jsonData直接取出
     * 当MetaFieldType为com.footprint.common.enums.FkeyTypeEnum#SelectInternType时
     * save的时候不存
     * 取得时候根据com.footprint.model.meta.property.RalValue#fkey找到businessId，然后根据com.footprint.model.meta.property.RalValue#metaKey取得数据
     */
    protected String businessId;
    protected String jsonData;//JSON数据
    protected List<String> multiSelectedDataBusinessIds;

    public FkeyField(String fieldDomainType, FkeyTypeEnum dataType, String key, String metaFieldType, String businessId, String jsonData) {
        this.fieldDomainType = fieldDomainType;
        this.dataType = dataType;
        this.key = key;
        this.metaFieldType = metaFieldType;
        this.businessId = businessId;
        this.jsonData = jsonData;
    }

    public FkeyField createPlainFkeyField(FkeyTypeEnum type, String key, String jsonData) {
        this.dataType = type;
        this.key = key;
        this.jsonData = jsonData;
        return this;
    }

    /**
     * 多选SELECT
     */
    public FkeyField createMultiSelect(FkeyTypeEnum type, String key, String jsonData) {
        this.dataType = type;
        this.key = key;
        this.multiSelectedDataBusinessIds = JSON.parseArray(jsonData, String.class);
        this.jsonData = jsonData;
        return this;
    }

    /**
     * 多选SELECT默认值
     */
    public FkeyField createMultiSelectd(FkeyTypeEnum type, String key, String businessId, String jsonData) {
        this.dataType = type;
        this.key = key;
        this.businessId = businessId;
        this.multiSelectedDataBusinessIds = JSON.parseArray(jsonData, String.class);
        this.jsonData = jsonData;
        return this;
    }

    public FkeyField createSelectFkeyField(FkeyTypeEnum type, String key, String businessId, String jsonData) {
        this.dataType = type;
        this.key = key;
        this.businessId = businessId;
        this.jsonData = jsonData;
        return this;
    }

    public FkeyField createSelectFkeyField(MetaInfoDTO metaInfoDTO, String businessId, String jsonData) {
        this.dataType = FkeyTypeEnum.valueOf(metaInfoDTO.getFkeytype());
        this.key = metaInfoDTO.getKey();
        this.businessId = businessId;
        this.jsonData = jsonData;
        return this;
    }

    public FkeyField createSelectInternFkeyField(FkeyTypeEnum type, String key, String jsonData) {
        this.dataType = type;
        this.key = key;
        this.jsonData = jsonData;
        return this;
    }

    public FkeyField build(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }

    /**
     * 数据保存时使用
     * key没匹配上的时候，返回空
     * key是intern，返回空
     * key是SelectType，jsonData是id
     * 其他的keyjsonData是value
     *
     * @param fkeyField  字段数据
     * @param metaFields 字段列表
     * @return 字段数据
     */
    public FkeyField build(FkeyField fkeyField, List<MetaFieldDTO> metaFields) {
        // 根据key遍历fkeyField.getFkey()，找type
        String type = "";
        // 遍历metaField中的Key匹配fkeyField中的FKey 并将Fkey的type作为持久化条件
        for (MetaFieldDTO metaField : metaFields) {
            if (metaField.getKey().equals(fkeyField.getKey())) {
                type = fkeyField.getDataType().name();
                break;
            }
        }
        // 如果key是intern，返回空
        if (type.equals(FkeyTypeEnum.SELECT_INTERN.name())) {
            return null;
        }

        return new FkeyField(fkeyField.getFieldDomainType()
//                , fkeyField.getInternNo()
//                , fkeyField.getBatchNo()
//                , fkeyField.getSerialNo()
                , fkeyField.getDataType()
                , fkeyField.getKey()
                , fkeyField.getMetaFieldType()
                , fkeyField.getBusinessId()
                , fkeyField.getJsonData());
    }

    /**
     * key没匹配上的时候，抛出异常
     * key是intern，返回空
     *
     * @param metaFields 字段列表
     * @return 字段数据
     */
    public FkeyField build(List<MetaFieldDTO> metaFields) {
        // 根据key遍历fkeyField.getFkey()，找type
        String type = "";
        // 遍历metaField中的Key匹配fkeyField中的FKey 并将Fkey的type作为持久化条件
        for (MetaFieldDTO metaField : metaFields) {
            if (metaField.getKey().equals(this.getKey())) {
                type = this.getDataType().name();
                break;
            }
        }
        // 如果没有匹配上key 抛出异常
        if ("".equals(type)) {
            throw new IllegalArgumentException("无效的Key");
        }
        // 如果key是intern，返回空
        if (type.equals(FkeyTypeEnum.SELECT_INTERN.name()) || "".equals(type)) {
            return null;
        } else {
            // 否则持久化数据
            return this;
        }

    }

    public FkeyField build(FkeyTypeEnum dataType) {
        return new FkeyField(this.getFieldDomainType()
                , dataType
                , this.getKey()
                , this.getMetaFieldType()
                , this.getBusinessId()
                , this.jsonData);
    }


}
