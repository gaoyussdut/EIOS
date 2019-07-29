package top.toptimus.businessUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.TaskStatusEnum;

import java.util.UUID;

/**
 * 任务实例dto
 *
 * @author lizongsheng
 * @since 2019-04-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskInsDTO {

    private String businessUnitCode;     //业务单元ID
    private String processInsId;      //任务流程实例ID
    private String taskMetaId;      //任务meta
    private String taskTokenId;     //任务token
    private TaskStatusEnum status;    //任务状态

    public TaskInsDTO(String businessUnitCode,String taskMetaId,String taskTokenId){
        this.businessUnitCode = businessUnitCode;
        this.processInsId = UUID.randomUUID().toString();
        this.taskMetaId = taskMetaId;
        this.taskTokenId = taskTokenId;
        this.status = TaskStatusEnum.OPEN;
    }

}
