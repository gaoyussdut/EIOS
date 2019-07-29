package top.toptimus.businessmodel.process.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessDTO {
    private String processId;   //流程ID
    private String processName;      // 流程名

    public ProcessDTO build(String processId, String processName) {
        this.processId = processId;
        this.processName = processName;
        return this;
    }
}
