package top.toptimus.model.meta.event;

import lombok.Getter;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.meta.FKeyTypeDto;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * top.toptimus.entity.meta.event.MetaEventEntity#saveMetaInfoFKeySelectAndSelectIntern(top.toptimus.meta.TokenMetaInfoDTO)
 * 保存meta下所有fkey为SELECT和SELECT_INTERN 的基础类型的充血模型
 *
 * @author gaoyu
 */
@Getter
public class SaveMetaSelectInfoModel extends BaseModel {
    private List<FKeyTypeDto> newFKeyTypeDaos = new ArrayList<>();
    private List<String> fkeys = new ArrayList<>();
    private Map<String, String> keyAndMetaFkey = new HashMap<>();

    public SaveMetaSelectInfoModel(TokenMetaInfoDTO tokenMetaInfoDTO) {
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            if (metaField.getType().getType().equals(FkeyTypeEnum.SELECT)
                    || metaField.getType().getType().equals(FkeyTypeEnum.SELECT_INTERN)) {
                this.keyAndMetaFkey.put(metaField.getKey(), metaField.getType().getRalValue().getFkey());
                this.fkeys.add(metaField.getType().getRalValue().getFkey());
                // fKeyTypeDaos.add(
                // new FKeyTypeDao(metaField.getKey(), metaField.getType().getType())
                // );
            }
        }
    }

    public void build(List<FKeyTypeDto> fKeyTypeDaoList) {
        for (String key : this.keyAndMetaFkey.keySet()) {
            for (FKeyTypeDto fKeyTypeDao : fKeyTypeDaoList) {
                if (this.keyAndMetaFkey.get(key).equals(fKeyTypeDao.getKey())) {
                    this.newFKeyTypeDaos.add(new FKeyTypeDto(key, fKeyTypeDao.getType()));
                    break;
                }
            }
        }
    }
}
