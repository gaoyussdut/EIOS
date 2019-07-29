package top.toptimus.processDefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 流程及文件定义
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProcessFileDto {

    private String id;
    private String processId; // 流程ID
    private String processName; // 流程名称
    private String bpmnFile; // 流程bpmn文件
    private String status;

    public ProcessFileDto(String processId, String processName) {
        this.processId = processId;
        this.processName = processName;
    }

    /**
     * 构造方法
     *
     * @param processId   process id
     * @param processName process name
     * @param bpmnFile    bpmn文件
     * @param status      状态  TODO    枚举
     * @return this
     */
    public ProcessFileDto build(String processId, String processName, String bpmnFile
            , String status) {
        this.processId = processId;
        this.processName = processName;
        this.bpmnFile = bpmnFile;
        this.status = status;
        return this;
    }
}
