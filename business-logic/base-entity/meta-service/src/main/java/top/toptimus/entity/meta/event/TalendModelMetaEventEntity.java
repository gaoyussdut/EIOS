package top.toptimus.entity.meta.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.meta.TalendModelMetaDto;
import top.toptimus.repository.meta.TalendModelMetaRepository;

import java.util.List;
import java.util.Map;

/**
 * 主数据实体类
 *
 * @author gaoyu
 */
@Component
public class TalendModelMetaEventEntity {
    @Autowired
    private TalendModelMetaRepository talendModelMetaRepository;

    /**
     * 保存全部TalendModelMeta
     * TODO 事务控制    for循环
     *
     * @param talendModelMetas talendModelMetas
     */
    public void saveAllTalendModelMeta(Map<String, List<TalendModelMetaDto>> talendModelMetas) {
        for (String key : talendModelMetas.keySet()) {
            talendModelMetaRepository.delectAllByTalendId(key);
            talendModelMetaRepository.saveAll(talendModelMetas.get(key));
        }
    }
}
