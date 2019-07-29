package top.toptimus.place.placeSaveResult;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.event.PlaceBusinessContextEnum;
import top.toptimus.place.PlaceDTO;

import java.io.Serializable;

/**
 * 库所更新返回值
 */
@NoArgsConstructor
@Data
public class PlaceSaveResultBody implements Serializable {
    private static final long serialVersionUID = -7671205786776731147L;
    private String processId; // 流程Id
    private String userTaskId; // 流程节点Id
    private String billMetaId;
    private String tokenTemplateName; // TTname赋值,如果是代办传空，是AO的话需要传值前端显示
    private String billTokenId;
    private String poTokenId; // 任务tokenId
    private String lotNo; // lotNo
    private String orgId; // 工作中心ID
    private int lotSelfIndex; // lotSelfIndex
    private String wbsInsId; // WBSid
    private PlaceBusinessContextEnum placeBusinessContextEnum;// palce保存类型
    private String userId; //操作用户Id

    PlaceSaveResultBody(PlaceDTO placeDTO) {
        this.billMetaId = placeDTO.getBillMetaId();
        this.tokenTemplateName = placeDTO.getTokenTemplateName();
        this.billTokenId = placeDTO.getBillTokenId();
        this.userId = placeDTO.getUserId();
        this.placeBusinessContextEnum = placeDTO.getPlaceBusinessContextEnum();
    }
}
