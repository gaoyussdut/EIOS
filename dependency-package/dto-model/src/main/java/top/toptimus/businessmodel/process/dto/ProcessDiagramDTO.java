package top.toptimus.businessmodel.process.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessDiagramDTO implements Serializable {
    private static final long serialVersionUID = 1682203516366578024L;

    private String userTaskId;     // 当前跑到的节点
    private String bpmnDiagramXml; // bpmn的xml
}
