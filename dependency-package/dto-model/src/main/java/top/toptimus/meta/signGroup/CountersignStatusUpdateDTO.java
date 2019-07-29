package top.toptimus.meta.signGroup;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//汇签信息涉及到的单据token,用于汇签信息提交时批量更改单据任务状态
@NoArgsConstructor
@Data
public class CountersignStatusUpdateDTO implements Serializable {
    private static final long serialVersionUID = 983527990921415589L;

    private String billMetaId;  //单据metaId
    private String billTokenId; //单据tokenId

    public CountersignStatusUpdateDTO(String billMetaId,String billTokenId){
        this.billMetaId = billMetaId;   //单据metaId
        this.billTokenId = billTokenId; //单据tokenId
    }
}
