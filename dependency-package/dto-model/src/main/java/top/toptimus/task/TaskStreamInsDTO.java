package top.toptimus.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 大任务流实例
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskStreamInsDTO {
    private String taskStreamId; // 任务流id
    private String taskStreamInsId;  // 任务流实例id
    private int parentIndex;
    private int selfIndex;
    private String metaId;      //表头meta
    private String tokenId;     //表头token
    private String status;      //大任务状态
    private List<TaskDto> taskDtoList = new ArrayList<>();   //大任务当前节点实例下小任务实例集合

}
