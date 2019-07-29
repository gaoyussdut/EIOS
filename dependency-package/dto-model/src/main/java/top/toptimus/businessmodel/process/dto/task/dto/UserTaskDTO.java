package top.toptimus.businessmodel.process.dto.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 节点定义
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserTaskDTO {

    private String id;
    private String processId;
    private String userTaskId;
    private String userTaskName;
    private String tokenTemplateId;
    private String status;

}
