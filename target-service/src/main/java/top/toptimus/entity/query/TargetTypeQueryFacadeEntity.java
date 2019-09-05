package top.toptimus.entity.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.TargetTypeRepository;
import top.toptimus.targetdto.TargetTypeDTO;
import top.toptimus.targetdto.TargetTypePageableDTO;

/**
 * target query facade
 *
 * @author lzs
 * @since 2019-2-19
 */
@Component
public class TargetTypeQueryFacadeEntity {

    @Autowired
    private TargetTypeRepository targetTypeRepository;

    public TargetTypePageableDTO getTargetTypeGeneralView(Integer pageSize, Integer pageNo) {
        return targetTypeRepository.getTargetTypeGeneralView(pageSize,pageNo);
    }

    /**
     * 目标类型预览
     *
     * @param targetTypeId
     * @return result
     */
    public TargetTypeDTO getTargetTypeDetail(String targetTypeId) {
        return targetTypeRepository.getTargetTypeDetail(targetTypeId);
    }
}
