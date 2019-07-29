package top.toptimus.entity.meta.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.meta.TalendModelMetaDto;
import top.toptimus.model.meta.TalendMetaEqualModel;
import top.toptimus.repository.meta.TalendModelMetaRepository;

import java.util.List;
import java.util.Map;

/**
 * talend查询接口
 *
 * @author gaoyu
 */
@Component
public class TalendModelMetaQueryFacadeEntity {

    @Autowired
    private TalendModelMetaRepository talendModelMetaRepository;

    /**
     * 根据x_pk_x_talend_id 查询所有的meta
     *
     * @param x_pk_x_talend_id x_pk_x_talend_id
     * @return List<TalendModelMeta>
     */
    public List<TalendModelMetaDto> findAllByXPkXTalendId(String x_pk_x_talend_id) {
        return talendModelMetaRepository.findAllByXPkXTalendId(x_pk_x_talend_id);
    }

    /**
     * 比较meta是否和数据库中的meta一致
     *
     * @param talendModelMetas talendModelMetas
     */
    public Map<String, List<TalendModelMetaDto>> equalsTalendModelMeta(Map<String, List<TalendModelMetaDto>> talendModelMetas) {
        talendModelMetas.keySet().forEach(key -> {
            List<TalendModelMetaDto> modelMetaStatus = new TalendMetaEqualModel().equalsMeta(
                    talendModelMetaRepository.findAllByXPkXTalendId(key)    //  根据x_pk_x_talend_id 查询所有的meta
                    , talendModelMetas.get(key) //  TalendModelMeta List
            );
            talendModelMetas.put(key, modelMetaStatus);
        });
        return talendModelMetas;
    }
}
