package top.toptimus.dynamicSQLModel.baseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;

import java.util.*;

/**
 * meta数据清洗
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ConvertStruct {
    private List<String> attributes = new ArrayList<>();    //  主表属性，普通类型字段
    private String selectFkey;

    /**
     * meta数据清洗
     *
     * @param tokenMetaInfoDTO meta信息
     * @return K:tableName V:中间结构
     */
    public static Map<String, Map<String, ConvertStruct>> generateConvertStructMap(TokenMetaInfoDTO tokenMetaInfoDTO, String tableName, List<ForeignAttributeSuite> foreignAttributeSuites) {
        Map<String, Map<String, ConvertStruct>> convertStrucDoubletMapp = new HashMap<>(); //  K:表中的FKey   K:metaId V:中间结构
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            if (metaField.getType().getType().name().equals(FkeyTypeEnum.SELECT.name())) {
                generateConvertStructMap(
                        convertStrucDoubletMapp
                        , metaField.getKey()    //  表中的FKey
                        , metaField.getType().getRalValue().getMetaKey()    //  表名
                        , metaField.getType().getRalValue().getFkey()   //  字段名
                        , metaField.getKey()   //  关联键名
                        , foreignAttributeSuites //  取关联表字段别名结构
                );
            } else if (metaField.getType().getType().name().equals(FkeyTypeEnum.SELECT_INTERN.name())) {
                String fieldFkey = getFkey(tokenMetaInfoDTO, metaField.getType().getRalValue().getMetaName());   //  表中的FKey
                if (null != fieldFkey)
                    generateConvertStructMap(
                            convertStrucDoubletMapp
                            , fieldFkey    //  表中的FKey
                            , metaField.getType().getRalValue().getMetaKey()    //  表名
                            , metaField.getType().getRalValue().getFkey()   //  字段名
                            , null   //  关联键名
                            , foreignAttributeSuites //  取关联表字段别名结构
                    );
            } else {
                // 没有RAL，原表,meta就是tablename
                generateConvertStructMap(
                        convertStrucDoubletMapp
                        , metaField.getKey()    //  表中的FKey
                        , tableName    //  表名
                        , metaField.getKey()    //  字段名
                        , null  //  关联键名
                        , foreignAttributeSuites //  取关联表字段别名结构
                );
            }
        }
        return convertStrucDoubletMapp;
    }

    /**
     * 根据meta名和meta info取select的key
     *
     * @param tokenMetaInfoDTO meta info
     * @param metaName         meta名
     * @return meta的key
     */
    private static String getFkey(TokenMetaInfoDTO tokenMetaInfoDTO, String metaName) {
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            if (metaField.getType().getType().name().equals(FkeyTypeEnum.SELECT.name())
                    && metaField.getType().getRalValue().getMetaName().equals(metaName)
                    ) {
                return metaField.getKey();
            }
        }
        return null;
    }

    /**
     * 生成中间数据
     *
     * @param convertStrucDoubletMap K:表中的FKey   K:metaId V:中间结构
     * @param fKey                   表中的fKey
     * @param metaId                 meta名
     * @param metaKey                字段名
     * @param selectFkey             关联键名
     */
    private static void generateConvertStructMap(
            Map<String, Map<String, ConvertStruct>> convertStrucDoubletMap
            , String fKey
            , String metaId
            , String metaKey
            , String selectFkey
            , List<ForeignAttributeSuite> foreignAttributeSuites
    ) {
        Map<String, ConvertStruct> convertStructMap = new HashMap<>();
        if (convertStrucDoubletMap.containsKey(fKey)) {
            convertStructMap = convertStrucDoubletMap.get(fKey);
        }

        if (convertStructMap.containsKey(metaId)) { //当表名存在，加入属性
            ConvertStruct convertStruct = convertStructMap.get(metaId);
            List<String> attributes = convertStruct.getAttributes();
            attributes.add(metaKey); //  加入属性
            buildForeignAttributeSuites(selectFkey, metaKey, fKey, metaId, foreignAttributeSuites);
            String orgSelectFkey = convertStruct.getSelectFkey();
            convertStructMap.put(metaId, new ConvertStruct().build(attributes, orgSelectFkey == null ? selectFkey : orgSelectFkey));
        } else {
            buildForeignAttributeSuites(selectFkey, metaKey, fKey, metaId, foreignAttributeSuites);
            convertStructMap.put(metaId, new ConvertStruct().build(
                    Collections.singletonList(metaKey)  //  加入属性
                    , selectFkey
            ));
        }
        convertStrucDoubletMap.put(fKey, convertStructMap);
    }

    private static void buildForeignAttributeSuites(String selectId, String attribute, String fKey, String metaId, List<ForeignAttributeSuite> foreignAttributeSuites) {
        foreignAttributeSuites.add(new ForeignAttributeSuite(selectId, attribute, fKey, metaId));
    }

    ConvertStruct build(List<String> attributes, String selectFkey) {
        this.attributes = attributes;
        this.selectFkey = selectFkey;
        return this;
    }

}