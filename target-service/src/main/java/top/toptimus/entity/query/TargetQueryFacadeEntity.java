package top.toptimus.entity.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.filter.FilterDTO;
import top.toptimus.repository.TargetRepository;
import top.toptimus.targetdto.TargetDataDTO;
import top.toptimus.targetdto.TargetPageableDTO;

import java.util.List;

/**
 * target query facade
 *
 * @author lzs
 * @since 2019-2-19
 */
@Component
public class TargetQueryFacadeEntity {

    @Autowired
    private TargetRepository targetRepository;

    public TargetPageableDTO getTargetGeneralView(Integer pageSize, Integer pageNo, List<FilterDTO> filterCondition) {
        return targetRepository.getTargetGeneralView(pageSize,pageNo,filterCondition);
    }

    public TargetDataDTO getTargetDetail(String targetDataId) {
        return targetRepository.getTargetDetail(targetDataId);
    }

    public List<TargetDataDTO> getTargetByTargetTypeId(String targetTypeId) {
        return targetRepository.getTargetByTargetTypeId(targetTypeId);
    }
}
