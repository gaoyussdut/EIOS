package top.toptimus.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;

/**
 * 任务流定义
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskDefinitionDTO {

    private String billMetaId;   //表头meta
    private String taskId;      //任务id
    private String taskName;    //任务名称
    private int parentIndex;
    private int selfIndex;
    private TaskTypeEnum taskTypeEnum;  //任务类型
    private String metaId;  //关联单据meta
    private String processId;   //审批流程

}
