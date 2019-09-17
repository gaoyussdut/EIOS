package top.toptimus.entity.processIns.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.toptimus.businessmodel.process.dto.ProcessTaskDTO;
import top.toptimus.common.enums.process.UserTaskEdgeEnum;
import top.toptimus.common.enums.process.UserTaskStatusEnum;
import top.toptimus.common.enums.process.VertexTypeEnum;
import top.toptimus.constantConfig.Constants;
import top.toptimus.entity.processDefinition.query.ProcessQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.processDefinition.UserTaskDto;
import top.toptimus.processDefinition.UserTaskEdgeDto;
import top.toptimus.processDefinition.UserTaskInsDTO;
import top.toptimus.processIns.ProcessInsDto;
import top.toptimus.processModel.UserTaskPoInfoModel;
import top.toptimus.repository.processIns.ProcessInsRepository;
import top.toptimus.repository.processIns.ProcessTaskRepository;
import top.toptimus.repository.processIns.UserTaskInsRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 流程实例实体
 */
@Component
public class ProcessInsEventEntity {
    @Autowired
    private ProcessInsRepository processInsRepository;
    @Autowired
    private UserTaskInsRepository userTaskInsRepository;
    @Autowired
    private ProcessTaskRepository processTaskRepository;
    @Autowired
    private ProcessQueryFacadeEntity processQueryFacadeEntity;


    // --------------------------------------------修改库所状态-------------------------------------------

    /**
     * UserTask 状态变更
     *
     * @param placeDTO 库所  userTaskId tokenId
     */
    public void transictionUserTaskInProcess(PlaceDTO placeDTO) {
        // 1.通过节点Id取得节点信息
        UserTaskDto userTaskDto = processQueryFacadeEntity.findByUserTaskId(placeDTO.getUserTaskId());
        if ( null == userTaskDto) {
            throw new TopException(TopErrorCode.PROC_NODE_NOT_FOUND);
        }

        // 2.存储关系 t_user_task_ins 修改当前关系 存储下个关系
        // 2.1更新原有的状态 由于之前实例化此实例化节点一定存在
        userTaskInsRepository.updateStatus(new UserTaskInsDTO().build(placeDTO.getUserTaskId(),
                placeDTO.getBillTokenId(), UserTaskStatusEnum.CLOSED.name()));

        // 查找下一节点
        List<UserTaskEdgeDto> userTaskEdgeDtos = processQueryFacadeEntity.findNextUserTask(placeDTO.getUserTaskId());

        // 待启动节点预留列表
        List<UserTaskEdgeDto> toBeStartUserTaskEdgeDto = new ArrayList<>();
        if (userTaskEdgeDtos != null) {
            for (UserTaskEdgeDto userTaskEdgeDto : userTaskEdgeDtos) {
                // 1.正常UserTask
                if (userTaskEdgeDto.getType().equals(UserTaskEdgeEnum.USERTASK.name())
                        || userTaskEdgeDto.getType().equals(UserTaskEdgeEnum.ENDEVENT.name())) {
                    toBeStartUserTaskEdgeDto.add(userTaskEdgeDto);
                }
            }
        }

        // 2.2追加下个节点,将状态变成Running -> userTaskEdgeDtos
        // 2.3持久化实例节点包含状态
        userTaskInsRepository.saveAll(new ArrayList<UserTaskInsDTO>() {
            private static final long serialVersionUID = 1235899983501268054L;
            {
                if (toBeStartUserTaskEdgeDto.size() > 0) {
                    for (UserTaskEdgeDto dao : toBeStartUserTaskEdgeDto) {
                        if (!StringUtils.isEmpty(dao.getUserTaskId())) {
                            add(new UserTaskInsDTO().build(userTaskDto.getProcessId(), dao.getUserTaskId(),
                                    dao.getMetaId(), placeDTO.getBillTokenId(),
                                    UserTaskStatusEnum.RUNNING.name()));
                        }
                    }
                }
            }
        });
    }

    /**
     * 实例化流程及节点任务(根据processId，tokenId)
     *
     * @param processId      流程id
     * @return tokenId
     * @apiNote top.toptimus.entity.processIns.aop.UserTaskPoInfoAop#generateUserTaskPoInfo(java.lang.Object) aop
     */
    public UserTaskPoInfoModel insertProcessIns(String processId, String tokenId, PlaceDTO placeDTO) {
        if (StringUtils.isEmpty(tokenId)) {
            // 如果为空的场合,赋值
            tokenId = UUID.randomUUID().toString();
        } else {
            // 判定此processId和tokenId是否被实例化
            if (processInsRepository.findProcessInsByProcessIdAndTokenId(processId, tokenId) != null) {
                throw new TopException(TopErrorCode.PROC_REPECT_INSTANCE_PROCESS);
            }
        }
        /* 1实例流程 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 1.1.实例流程
        processInsRepository.save(new ProcessInsDto(UUID.randomUUID().toString(), processId, processId+'_'+sdf.format(new Date()), tokenId));
        // 存疑，应该可以统一封装在下文的事务处理中
        // 1.2.查找头节点
        List<UserTaskDto> userTaskDtos = processQueryFacadeEntity.findByProcessIdAndType(processId,
                VertexTypeEnum.STARTEVENT);
        // 1.3.实例化头节点
        List<UserTaskInsDTO> userTaskInsDTOS = new ArrayList<>();
        String userTaskId = Constants.jsonData_EmptyString;
        for (UserTaskDto userTaskDto : userTaskDtos) {
            userTaskId = userTaskDto.getUserTaskId();// 找到头节点
            userTaskInsDTOS.add(
                    new UserTaskInsDTO(processId, userTaskDto, tokenId)
            );
        }

        /* 实例化流程节点的任务 */
        return new UserTaskPoInfoModel(userTaskInsDTOS, userTaskId, tokenId, placeDTO);
    }

    /**
     * 业务所对应的流程提交
     *
     * @param processTaskDTO 业务审批实例
     */
    public void saveProcessTask(ProcessTaskDTO processTaskDTO) {
        processTaskRepository.save(processTaskDTO);
    }


}
