package top.toptimus.model.meta.event;

import lombok.Getter;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.meta.*;
import top.toptimus.meta.property.MetaFieldDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * top.toptimus.entity.meta.event.MetaEventEntity#saveMeta(top.toptimus.meta.TokenMetaInfoDTO)
 * 保存meta的充血model类
 *
 * @author gaoyu
 */
@Getter
public class SaveMetaModel extends BaseModel {

    private TokenMetaInfoDTO tokenMetaInfoDTO;
    // meta下所有的fkey
    private List<FKeyDto> fKeyDaos = new ArrayList<>();
    // meta下所有fkey的可见和读写属性
    private List<RWpermissionDto> rWpermissionDaos = new ArrayList<>();
    // meta下fkey的ralValue 部分有
    private List<RalValueDto> ralValueDaos = new ArrayList<>();
    // 效验fkey是否存在于基础fkey类型的表中 如不存在说明是错误的meta 抛出异常中断保存
    private List<String> fkeys = new ArrayList<>();
    private List<String> fkeyList = new ArrayList<>();

    public SaveMetaModel(TokenMetaInfoDTO tokenMetaInfoDTO) {
        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
        // 遍历meta下所有fkey
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            // fkey和fkey的名称
            this.fKeyDaos.add(new FKeyDto(null, tokenMetaInfoDTO.getTokenMetaId(), metaField.getKey(),
                    metaField.getCaption(), metaField.getType().getType().name()));
            // fkey的可见和读写属性
            this.rWpermissionDaos.add(new RWpermissionDto(null, tokenMetaInfoDTO.getTokenMetaId(), metaField.getKey(),
                    metaField.getVisible(), metaField.getReadonly(), metaField.getRequired(),""));
            // 如果RalValue不为空 将其保存
            if (null != metaField.getType().getRalValue()) {
                this.ralValueDaos.add(new RalValueDto(null, tokenMetaInfoDTO.getTokenMetaId(), metaField.getKey(),
                        metaField.getType().getRalValue().getMetaName(), metaField.getType().getRalValue().getFkey(),
                        metaField.getType().getRalValue().getMetaKey()));
            }
        }

        for (FKeyDto fKeyDao : this.fKeyDaos) {
            this.fkeys.add(fKeyDao.getKey());
        }
    }

    public void build(List<FKeyTypeDto> fKeyTypeDaoList) {
        List<String> fkeyList = new ArrayList<>();
        for (FKeyTypeDto fKeyTypeDao : fKeyTypeDaoList) {
            fkeyList.add(fKeyTypeDao.getKey());
        }
        for (String fkey : this.getFkeys()) {
            if (!fkeyList.contains(fkey)) {
                this.buildErrorMessage(false, "未知的fkey:" + fkey);
                throw new RuntimeException("未知的fkey:" + fkey);
            }
        }
    }
}
