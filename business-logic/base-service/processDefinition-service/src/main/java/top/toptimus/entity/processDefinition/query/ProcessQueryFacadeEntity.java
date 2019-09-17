package top.toptimus.entity.processDefinition.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.process.VertexTypeEnum;
import top.toptimus.entity.tokendata.query.TokenMetaQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.processDefinition.ProcessFileDto;
import top.toptimus.processDefinition.UserTaskDto;
import top.toptimus.processDefinition.UserTaskEdgeDto;
import top.toptimus.repository.processDefinition.ProcessFileRepository;
import top.toptimus.repository.processDefinition.UserTaskEdgeRepository;
import top.toptimus.repository.processDefinition.UserTaskRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * process query facade
 *
 * @author lzs
 * @since 2018-6-21
 */
@Component
public class ProcessQueryFacadeEntity {

    /**
     * repository
     */
    @Autowired
    private UserTaskRepository userTaskRepository;
    @Autowired
    private ProcessFileRepository processFileRepository;
    @Autowired
    private UserTaskEdgeRepository userTaskEdgeRepository;
    @Autowired
    private TokenMetaQueryFacadeEntity tokenMetaQueryFacadeEntity;

    // ------------------------------------------------------取得ProcessDTO-----------------------------------------------------

    /**
     * 取得所有流程Id对应的Name
     *
     * @return k:process id,V:process name
     */
    public Map<String, String> findAllProcess() {
        List<ProcessFileDto> processFileDaos = processFileRepository.findAllProcess();
        return new HashMap<String, String>() {
            {
                for (ProcessFileDto dao : processFileDaos) {
                    put(dao.getProcessId(), dao.getProcessName());
                }
            }
        };
    }

    /**
     * 通过UserTaskId查找UserTask
     *
     * @param userTaskId user task id
     * @return user task
     */
    public UserTaskDto findByUserTaskId(String userTaskId) {
        return userTaskRepository.findByUserTaskId(userTaskId);
    }

    /**
     * 待办详细:表头业务信息 TaskDTO
     *
     * @param tokenId    token id
     * @param userTaskId 流程节点ID
     * @return 库所
     */
    public PlaceDTO getProcessUserTaskDTO(String tokenId, String userTaskId) {
        // 查询user task，如果为空报错
        UserTaskDto userTaskDto = userTaskRepository.findByUserTaskId(userTaskId);
        if (userTaskDto == null) {
            throw new TopException(TopErrorCode.PROC_NODE_NOT_FOUND);
        }
        // 待办详细:表头分录业务信息
        return tokenMetaQueryFacadeEntity.buildCachePlace(tokenId, userTaskDto.getMetaId());

    }

    /**
     * 根据流程Id获取流程xml
     *
     * @param processId 流程ID
     * @return 流程xml
     */
    public String getBPMNXmlString(String processId) {
        ProcessFileDto processFileDto = processFileRepository.findByProcessId(processId);
        return processFileDto != null ? processFileDto.getBpmnFile() : null;
    }

    public List<UserTaskEdgeDto> findNextUserTask(String userTaskId) {
        return userTaskEdgeRepository.findNextUserTask(userTaskId);
    }

    /**
     * 根据流程ID取得指定节点信息
     *
     * @param processId      流程Id
     * @param vertexTypeEnum STARTEVENT, USERTASK, ENDEVENT
     * @return user task 一览
     */
    public List<UserTaskDto> findByProcessIdAndType(String processId, VertexTypeEnum vertexTypeEnum) {
        List<UserTaskDto> userTaskDtos = userTaskRepository.findByProcessIdAndType(processId, vertexTypeEnum);
        if (userTaskDtos.isEmpty()) {
            throw new TopException(TopErrorCode.PROC_NODE_STATUS_ERR);
        } else {
            return userTaskDtos;
        }
    }

    public List<UserTaskDto> findAllByUserTaskId(String userTaskId) {
        return userTaskRepository.findAllByUserTaskId(userTaskId);
    }


}
