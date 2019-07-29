package top.toptimus.processDefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.PoEnum;

import java.io.Serializable;

/**
 * 节点定义
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserTaskDto implements Serializable {

    private static final long serialVersionUID = 659788780233875638L;

    private String id;
    private String processId;
    private String userTaskId;
    private String userTaskName;
    private String metaId; // metaId
    private String status;


    public UserTaskDto build(String processId, String userTaskId, String userTaskName, String metaId, String status) {
        this.processId = processId;
        this.userTaskId = userTaskId;
        this.userTaskName = userTaskName;
        this.metaId = metaId;
        this.status = status;
        return this;
    }


}
