package top.toptimus.entity.processIns.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.businessmodel.process.dto.ProcessTaskDTO;
import top.toptimus.common.enums.process.UserTaskStatusEnum;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.processDefinition.UserTaskInsDTO;
import top.toptimus.repository.processDefinition.UserTaskRepository;
import top.toptimus.repository.processIns.ProcessTaskRepository;
import top.toptimus.repository.processIns.UserTaskInsRepository;
import top.toptimus.tokendata.TokenDataDto;
import java.util.List;

/**
 * 流程查询实体
 *
 * @author gaoyu
 */
@Component
public class ProcessInsQueryFacadeEntity {
    @Autowired
    private UserTaskRepository userTaskRepository;
    @Autowired
    private UserTaskInsRepository userTaskInsRepository;
    @Autowired
    private ProcessTaskRepository processTaskRepository;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;


    /**
     * 查询节点对应的一览信息
     * @param userTaskId     节点ID
     * @param userTaskStatus 节点状态
     * @return Result
     */
    public List<TokenDataDto> findUserTaskList(String userTaskId, String userTaskStatus) {
        try {
            return tokenQueryFacadeEntity.getMetaTokenData(
                        userTaskRepository.findByUserTaskId(userTaskId).getMetaId()
                        ,userTaskInsRepository.findUserTaskTokenId(userTaskId, userTaskStatus, userQueryFacadeEntity.findByAccessToken().getRoleId()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据表头tokenId和状态查询节点id
     *
     * @param billTokenId        表头tokenId
     * @param userTaskStatusEnum 节点状态
     * @return 节点id
     */
    public List<UserTaskInsDTO> findByBillTokenIdAndStatus(String billTokenId, UserTaskStatusEnum userTaskStatusEnum) {
        List<UserTaskInsDTO> userTaskInsDTOS;
        // 如果节点状态不为空  根据节点状态查找  否则查找流程下所有的节点状态
        if (null != userTaskStatusEnum) {
            userTaskInsDTOS = userTaskInsRepository.findByBillTokenIdAndStatus(billTokenId, userTaskStatusEnum);
        } else {
            userTaskInsDTOS = userTaskInsRepository.findAllByBillTokenId(billTokenId);
        }
        //  没找到的话报错 正常情况下最少有一个节点的状态
        if (userTaskInsDTOS.isEmpty()) {
            throw new TopException(TopErrorCode.PROC_NODE_STATUS_ERR);
        }
        return userTaskInsDTOS;
    }

    /**
     * 根据单据metaId和tokenId查询业务审批
     *
     * @param metaId  单据metaId
     * @param tokenId 单据tokenId
     * @return ProcessTaskDTO 业务审批
     */
    public ProcessTaskDTO findByTokenIdAndMetaId(String metaId , String tokenId) {
        return processTaskRepository.findByTokenIdAndMetaId(metaId ,tokenId);
    }

    /**
     * 根据流程tokenId查询业务审批
     *
     * @param processTokenId 流程的tokenId
     * @return ProcessTaskDTO 业务审批
     */
    public ProcessTaskDTO findByProcessTokenId(String processTokenId) {
        return processTaskRepository.findByProcessTokenId(processTokenId);
    }
}
