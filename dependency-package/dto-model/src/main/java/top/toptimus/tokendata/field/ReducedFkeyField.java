package top.toptimus.tokendata.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReducedFkeyField implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8851991806896916585L;
    /**
     * 1、日期、数量、金额、备注等用户手填/手选字段
     * 2、辅助资料(客户、供应商、业务类别、会计科目、业务组织)，用内码区分
     * 3、带序列号或者批号的BO，用序列号或者批号区分
     * 4、表单，用批号区分
     */
    protected String key;  //  FKey
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

//    /**
//     * 将MetaField数据简化为只包涵key,businessId,jsonData的ReducedFkeyField
//     *
//     * @param cleanFkeyFieldList 字段
//     * @return reducedTokenDataDtoList    清洗后的字段
//     */
//    public List<ReducedFkeyField> cleanFkeyField(List<FkeyField> cleanFkeyFieldList) {
//        // 清洗后要返回的poTokenDataDtoList
//        List<ReducedFkeyField> reducedFkeyFieldList = new ArrayList<ReducedFkeyField>();
//        // 将TokenDataDto中的key,businessId,jsonData存入简化DTO中
//        for (FkeyField fkeyField : cleanFkeyFieldList) {
//            reducedFkeyFieldList.add(
//                    new ReducedFkeyField(fkeyField.getKey(), fkeyField.getBusinessId(), fkeyField.getJsonData()));
//        }
//        return reducedFkeyFieldList;
//    }

    /**
     * 构造方法
     *
     * @param key      key
     * @param jsonData 值
     * @return this
     */
    public ReducedFkeyField build(String key, String jsonData) {
        this.key = key;
        this.jsonData = jsonData;
        return this;
    }
}