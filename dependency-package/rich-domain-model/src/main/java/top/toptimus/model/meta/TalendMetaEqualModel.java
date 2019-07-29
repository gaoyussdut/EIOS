package top.toptimus.model.meta;

import lombok.NoArgsConstructor;
import top.toptimus.meta.TalendModelMetaDto;

import java.util.List;

/**
 * @author gaoyu
 * @since 2018-12-25
 */
@NoArgsConstructor
public class TalendMetaEqualModel {
    /**
     * 比较meta是否一致
     *
     * @param dbModelMetas      根据x_pk_x_talend_id 查询出的所有的meta
     * @param currentModelMetas TalendModelMeta List
     * @return TalendModelMeta List
     */
    public List<TalendModelMetaDto> equalsMeta(List<TalendModelMetaDto> dbModelMetas, List<TalendModelMetaDto> currentModelMetas) {
        for (TalendModelMetaDto dbModelMeta : dbModelMetas) {
            for (TalendModelMetaDto currentModelMeta : currentModelMetas) {
                if (dbModelMeta.getMetaId().equals(currentModelMeta.getMetaId())) {
                    if (dbModelMeta.getMetaJson().equals(currentModelMeta.getMetaJson())) {
                        currentModelMeta.buildStatus(0);
                        break;
                    } else {
                        currentModelMeta.buildStatus(1);
                        break;
                    }
                }
            }
        }
        return currentModelMetas;
    }
}
