package top.toptimus.businessmodel.process.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 业务审批实例
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessTaskDTO implements Serializable {

    private static final long serialVersionUID = 851897440977161015L;

    private String metaId;          // 单据的metaId
    private String tokenId;         // 单据的tokenId
    private String taskId;          //  任务Id
    private String processId;       // 流程Id
    private String processTokenId;  // 流程tokenId
}
