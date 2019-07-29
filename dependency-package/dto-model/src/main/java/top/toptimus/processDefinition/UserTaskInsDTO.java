package top.toptimus.processDefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.process.UserTaskStatusEnum;

/**
 * 节点实例定义
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserTaskInsDTO {

    private String id;
    private String processId;
    private String userTaskId;
    private String metaId;
    private String tokenId;
    private String status;

    private String processInsName;
    private String userTaskName;
    private String tokenTemplateName;

    public UserTaskInsDTO(String id, String processId, String userTaskId, String metaId, String tokenId, String status) {
        this.id = id;
        this.processId = processId;
        this.userTaskId = userTaskId;
        this.metaId = metaId;
        this.tokenId = tokenId;
        this.status = status;
    }

    public UserTaskInsDTO(String processId, UserTaskDto userTaskDto, String tokenId) {
        this.processId = processId;
        this.userTaskId = userTaskDto.getUserTaskId();
        this.metaId = userTaskDto.getMetaId();
        this.tokenId = tokenId;
        this.status = UserTaskStatusEnum.RUNNING.name();
    }

    /**
     * 构建方法
     *
     * @param processId       process id
     * @param userTaskId      user task id
     * @param metaId          metaId
     * @param tokenId         token id
     * @param status          状态  TODO    枚举
     * @return this
     */
    public UserTaskInsDTO build(String processId, String userTaskId, String metaId, String tokenId, String status) {
        this.processId = processId;
        this.userTaskId = userTaskId;
        this.metaId = metaId;
        this.tokenId = tokenId;
        this.status = status;
        return this;
    }

    public UserTaskInsDTO build(String userTaskId, String tokenId, String status) {
        this.userTaskId = userTaskId;
        this.tokenId = tokenId;
        this.status = status;
        return this;
    }
}
