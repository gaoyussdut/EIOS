package top.toptimus.model.meta.query;

import lombok.Getter;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.meta.*;
import top.toptimus.meta.property.FieldValueType;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.meta.property.RalValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回TokenMetaInfoDTO的model
 *
 * @author gaoyu
 */
@Getter
public class TokenMetaInfoModel {
    private TokenMetaInfoDTO tokenMetaInfoDTO;

    /**
     * 根据 tokenMetaInformationDao
     * KeyTypeDaos
     * rWpermissionDaos
     * ralValueDaos
     * 构造 TokenMetaInfo
     *
     * @param tokenMetaInformationDto meta信息定义，token名和类型
     * @param fKeyDaoList             meta中key的信息
     * @param rWpermissionDaos        key字段中的读写属性
     * @param ralValueDaos            key字段的关联信息
     */
    public TokenMetaInfoModel(TokenMetaInformationDto tokenMetaInformationDto
            , List<FKeyDto> fKeyDaoList
            , List<RWpermissionDto> rWpermissionDaos
            , List<RalValueDto> ralValueDaos) {
        // MetaField 列表
        List<MetaFieldDTO> metaFields = new ArrayList<>();
        for (FKeyDto fKey : fKeyDaoList) {
//            String fieldId = "";
            /*
             * FKey
             */
            String key = fKey.getKey();
            String caption = fKey.getCaption();
            /*
             * FKeyTypeDao
             */
            FkeyTypeEnum type = FkeyTypeEnum.valueOf(fKey.getFkeyType());
            /*
             * RWpermissionDao
             */
            Boolean visible = null; // 该字段是否可见
            Boolean required = null; // 该字段是否必填
            Boolean readonly = null; // 该字段是否只读
            /*
             * RalValueDao
             */
            String metaKey = "";
            String metaName = "";
            String fkey = "";
            FieldValueType fType;


            //遍历 rWpermissionDaos
            for (RWpermissionDto rWpermissionDao : rWpermissionDaos) {
                //当key值相同时
                //对读写属性进行赋值
                if (rWpermissionDao.getKey().equals(fKey.getKey())) {
                    visible = rWpermissionDao.getVisible();
                    required = rWpermissionDao.getRequired();
                    readonly = rWpermissionDao.getReadonly();
                    break;
                }
            }
            //遍历 ralValueDaos
            //对 ralValue 中的项目进行赋值
            if (type == FkeyTypeEnum.SELECT_INTERN || type == FkeyTypeEnum.SELECT) {
                for (RalValueDto ralValueDao : ralValueDaos) {
                    if (ralValueDao.getKey().equals(fKey.getKey())) {
                        metaKey = ralValueDao.getMetaKey();
                        metaName = ralValueDao.getMetaName();
                        fkey = ralValueDao.getFkey();
                        break;
                    }
                }
            }
            //判断 ralValue 是否为孔
            if (metaKey.equals("") && metaName.equals("") && fkey.equals("")) {
                // 根据 type 和 ralValue 构造 fType 对象，ralValue 为空时
                fType = new FieldValueType(type, null);
            } else {
                // ralValue 不为空时
                // 对 ralValue 中的项目进行赋值
                RalValue ralValue = new RalValue(metaKey, metaName, fkey);
                // 根据 type 和 ralValue 构造 fType 对象
                fType = new FieldValueType(type, ralValue);
            }
            // 根据遍历得到的数据构造 MetaField 并将其添加到 metaFields 中
            metaFields.add(new MetaFieldDTO(null, key, key, caption, fType, visible, required, readonly));
        }

        //根据 TokenMetaId MetaType TokenMetaName metaFields 构造 tokenMetaInfo
        this.tokenMetaInfoDTO = new TokenMetaInfoDTO(tokenMetaInformationDto, metaFields);
    }
}
