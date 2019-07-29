package top.toptimus.processIns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 项目实例
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProcessInsDto {

    private String processInsId;
    private String processId;
    private String processInsName;//流程实例名
    private String tokenId;


    public ProcessInsDto build(String processInsId, String processId) {
        this.processInsId = processInsId;
        this.processId = processId;
        return this;
    }

}
