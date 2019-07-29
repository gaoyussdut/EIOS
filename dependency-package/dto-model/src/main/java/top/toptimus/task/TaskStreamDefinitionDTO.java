package top.toptimus.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 大任务流定义
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskStreamDefinitionDTO {

    private String taskStreamId;  // 任务流id
    private int parentIndex;        //上级索引
    private int selfIndex;          //当前索引
    private String metaId;          //当前节点大任务表头meta
    private List<TaskDefinitionDTO> taskDefinitionDTOList = new ArrayList<>();  //大任务下小任务定义
}
