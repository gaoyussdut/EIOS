package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.talend.mdm.commmon.metadata.ComplexTypeMetadata;
import org.talend.mdm.commmon.metadata.FieldMetadata;
import org.talend.mdm.commmon.metadata.ReferenceFieldMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * talend字段元数据清洗结构，包含关联信息、字段信息
 *
 * @author gaoyu
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TalendMetaInfo {
    private String referencePath;   //  关联键
    private String referenceEntityTypeName; //  关联表
    private String fieldEntityTypeName; //  表名
    private String fieldPath;   //  字段名
    private String fieldType; //  字段类型
    private boolean hasReference = false;   //  是否有关联表

    /**
     * talend元数据抽出关系
     *
     * @param fieldMetadata talend meta
     * @return talend字段元数据清洗结构，包含关联信息、字段信息
     */
    public TalendMetaInfo build(FieldMetadata fieldMetadata) {
        return TalendMetaInfo.getEntityTypeName(
                new TalendMetaInfo().build(
                        fieldMetadata.getEntityTypeName()
                        , fieldMetadata.getPath()
                        , fieldMetadata.getType().getName()
                )
                , fieldMetadata
        );
    }

    /**
     * 构建通用字段
     *
     * @param fieldEntityTypeName 表名
     * @param fieldPath           字段名
     * @param fieldType           字段类型
     * @return this
     */
    private TalendMetaInfo build(
            String fieldEntityTypeName
            , String fieldPath
            , String fieldType
    ) {
        this.fieldEntityTypeName = fieldEntityTypeName;
        this.fieldPath = fieldPath;
        this.fieldType = fieldType;
        return this;
    }

    /**
     * 构建关联字段
     *
     * @param referencePath           关联键
     * @param referenceEntityTypeName 关联表
     * @return this
     */
    private TalendMetaInfo build(
            String referencePath
            , String referenceEntityTypeName
    ) {
        this.hasReference = true;
        this.referencePath = referencePath;
        this.referenceEntityTypeName = referenceEntityTypeName;
        return this;
    }

    /**
     * 取外键逻辑
     *
     * @param fieldMetadata talend meta数据
     * @return 外键
     */
    private static TalendMetaInfo getEntityTypeName(
            TalendMetaInfo talendMetaInfo
            , FieldMetadata fieldMetadata
    ) {
        try {
            //  外键判断
            FieldMetadata reference = ((ReferenceFieldMetadata) fieldMetadata).getReferencedField();
            if (null != reference) {
//                logger.info(
//                        "   ,关联键:" + reference.getPath()
//                                + ",关联表:" + reference.getEntityTypeName()
//                );
                return talendMetaInfo.build(
                        reference.getPath()
                        , reference.getEntityTypeName()
                );
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return talendMetaInfo;
    }

    /**
     * 抽取外键关系，将所有的有外键关系的表都取出来
     *
     * @param complexTypes            schema中所有meta的定义
     * @param referenceEntityTypeName 关联表名
     * @return talend字段元数据清洗结构，包含关联信息、字段信息 list
     */
    public static List<TalendMetaInfo> build(
            Collection<ComplexTypeMetadata> complexTypes
            , String referenceEntityTypeName
    ) {
        return new ArrayList<TalendMetaInfo>() {{
            complexTypes.forEach(complexTypeMetadata -> complexTypeMetadata.getFields().forEach(fieldMetadata -> {
                TalendMetaInfo talendMetaInfo = TalendMetaInfo.getTalendMetaInfo(fieldMetadata, referenceEntityTypeName);
                if (null != talendMetaInfo) {
                    add(talendMetaInfo);
                }
            }));

        }};
    }

    /**
     * 取外键逻辑
     *
     * @param fieldMetadata talend meta数据
     * @return 外键
     */
    private static TalendMetaInfo getTalendMetaInfo(
            FieldMetadata fieldMetadata
            , String referenceEntityTypeName
    ) {
        try {
            //  外键判断
            FieldMetadata reference = ((ReferenceFieldMetadata) fieldMetadata).getReferencedField();
            if (null != reference) {
                if (referenceEntityTypeName.equals(reference.getEntityTypeName())) {
                    //  关联表相同
                    return new TalendMetaInfo(
                            reference.getPath()   //  关联键
                            , reference.getEntityTypeName() //  关联表
                            , fieldMetadata.getEntityTypeName() //  表名
                            , fieldMetadata.getPath()   //  字段名
                            , fieldMetadata.getType().getName() //  字段类型
                            , true   //  是否有关联表
                    );
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

}
