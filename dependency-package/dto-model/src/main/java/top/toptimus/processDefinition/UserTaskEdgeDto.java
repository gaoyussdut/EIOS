package top.toptimus.processDefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.process.UserTaskEdgeEnum;
import top.toptimus.common.enums.process.VertexTypeEnum;

/**
 * 节点弧定义
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserTaskEdgeDto {

    private String id;
    private String processId;
    private String userTaskId;
    private String incoming;
    private String outgoing;
    private String type;
    /**
     * 追加的项目
     */
    private String metaId;
    private String userTaskName;  // 节点名称

    public UserTaskEdgeDto(String userTaskId, String metaId, String userTaskName, String type) {
        this.userTaskId = userTaskId;
        this.metaId = metaId;
        this.userTaskName = userTaskName;
        this.type = type;
    }

    public UserTaskEdgeDto(UserTaskDto userTaskDto, UserTaskEdgeEnum userTaskEdgeEnum) {
        this.userTaskId = userTaskDto.getUserTaskId();
        this.metaId = userTaskDto.getMetaId();
        this.userTaskName = userTaskDto.getUserTaskName();
        this.type = userTaskEdgeEnum.name();
    }

    /**
     * 构造方法
     *
     * @param processId  process id
     * @param userTaskId user task id
     * @param incoming   入度
     * @param outgoing   出度
     * @param type       类型
     * @return this
     */
    public UserTaskEdgeDto build(String processId, String userTaskId, String incoming, String outgoing, VertexTypeEnum type) {
        this.processId = processId;
        this.userTaskId = userTaskId;
        this.incoming = incoming;
        this.outgoing = outgoing;
        this.type = type.name();
        return this;
    }

}
