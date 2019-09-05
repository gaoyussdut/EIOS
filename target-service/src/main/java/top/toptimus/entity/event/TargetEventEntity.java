package top.toptimus.entity.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.TargetRepository;
import top.toptimus.repository.TargetTypeRepository;
import top.toptimus.targetdto.TargetDataDTO;
import top.toptimus.targetdto.TargetTypeDTO;

/**
 * target event
 *
 * @author lzs
 * @since 2019-2-19
 */
@Component
public class TargetEventEntity {

    @Autowired
    private TargetTypeRepository targetTypeRepository;
    @Autowired
    private TargetRepository targetRepository;

    /**
     * 目标类型保存
     *
     * @param targetTypeDTO
     * @return result
     */
    public void saveTargetType(TargetTypeDTO targetTypeDTO) {
        targetTypeRepository.saveTargetType(targetTypeDTO);
    }

    /**
     * 目标类型更新
     *
     * @param targetTypeDTO
     * @return result
     */
    public void updateTargetType(TargetTypeDTO targetTypeDTO) {
        targetTypeRepository.updateTargetType(targetTypeDTO);
    }

    /**
     * 目标保存
     *
     * @param targetDataDTO
     * @return result
     */
    public void saveTarget(TargetDataDTO targetDataDTO) {
        targetRepository.saveTarget(targetDataDTO);
    }

    /**
     * 目标更新
     *
     * @param targetDataDTO
     * @return result
     */
    public void updateTarget(TargetDataDTO targetDataDTO) {
        targetRepository.updateTarget(targetDataDTO);
    }

    /**
     * 删除目标类型
     *
     * @param targetTypeId
     * @return result
     */
    public void deleteTargetType(String targetTypeId) {
        targetTypeRepository.deleteTargetType(targetTypeId);
    }

    /**
     * 删除目标
     *
     * @param targetDataId
     * @return result
     */
    public void deleteTarget(String targetDataId) {
        targetRepository.deleteTarget(targetDataId);
    }
}
