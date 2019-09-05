package top.toptimus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.result.Result;
import top.toptimus.entity.event.TargetEventEntity;
import top.toptimus.entity.query.TargetQueryFacadeEntity;
import top.toptimus.entity.query.TargetTypeQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.filter.FilterDTO;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.targetdto.TargetDataDTO;
import top.toptimus.targetdto.TargetTypeDTO;

import java.util.List;

@Service
public class TargetService {

    @Autowired
    private TargetTypeQueryFacadeEntity targetTypeQueryFacadeEntity;
    @Autowired
    private TargetEventEntity targetEventEntity;
    @Autowired
    private TargetQueryFacadeEntity targetQueryFacadeEntity;


    public Result getTargetGeneralView(Integer pageSize, Integer pageNo, List<FilterDTO> filterCondition) {
        try {
            return Result.success(
                    targetQueryFacadeEntity.getTargetGeneralView(pageSize, pageNo, filterCondition)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    public Result getTargetTypeGeneralView(Integer pageSize, Integer pageNo) {
        try {
            return Result.success(
                    targetTypeQueryFacadeEntity.getTargetTypeGeneralView(pageSize, pageNo)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 目标类型预览
     *
     * @param targetTypeId
     * @return result
     */
    public Result getTargetTypeDetail(String targetTypeId) {
        try {
            return Result.success(
                    targetTypeQueryFacadeEntity.getTargetTypeDetail(targetTypeId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 目标类型保存
     *
     * @param targetTypeDTO
     * @return result
     */
    public Result saveTargetType(TargetTypeDTO targetTypeDTO) {
        try {
            if (StringUtils.isEmpty(targetTypeQueryFacadeEntity.getTargetTypeDetail(targetTypeDTO.getTargetTypeId()).getTargetTypeId())) {
                targetEventEntity.saveTargetType(targetTypeDTO);
            } else {
                targetEventEntity.updateTargetType(targetTypeDTO);
            }
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 目标预览
     *
     * @param targetDataId
     * @return result
     */
    public Result getTargetDetail(String targetDataId) {
        try {
            return Result.success(
                    targetQueryFacadeEntity.getTargetDetail(targetDataId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 目标保存
     *
     * @param targetDataDTO
     * @return result
     */
    public Result saveTarget(TargetDataDTO targetDataDTO) {
        try {
            if (StringUtils.isEmpty(targetQueryFacadeEntity.getTargetDetail(targetDataDTO.getTargetDataId()).getTargetDataId())) {
                targetEventEntity.saveTarget(targetDataDTO);
            } else {
                targetEventEntity.updateTarget(targetDataDTO);
            }
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 删除目标类型
     *
     * @param targetTypeId
     * @return result
     */
    public Result deleteTargetType(String targetTypeId) {
        try {
            if (targetQueryFacadeEntity.getTargetByTargetTypeId(targetTypeId).size() > 0) {
                return new ResultErrorModel(TopErrorCode.TARGET_TYPE_HAS_BEEN_ASSOCIATED).getResult();
            } else {
                targetEventEntity.deleteTargetType(targetTypeId);
                return Result.success();
            }
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 删除目标
     *
     * @param targetDataId
     * @return result
     */
    public Result deleteTarget(String targetDataId) {
        try {
            targetEventEntity.deleteTarget(targetDataId);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }
}
