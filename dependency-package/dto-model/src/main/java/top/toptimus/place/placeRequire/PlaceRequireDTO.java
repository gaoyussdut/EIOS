package top.toptimus.place.placeRequire;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.event.PlaceBusinessContextEnum;

import java.io.Serializable;
import java.util.List;

/**
 * 请求库所结构的DTO
 *
 * @author gaoyu
 * @since 2018-11-21
 */
@Data
@NoArgsConstructor
public class PlaceRequireDTO implements Serializable {

    private static final long serialVersionUID = -8861731601729448144L;

    private String billTokenId; //  库所唯一标示
    private PlaceBusinessContextEnum placeBusinessContextEnum;  //  place库所应用上下文枚举
    private String processId;   //  流程id
    private MemorandumInfoBody memorandumInfoBody;  //  备查账信息
    private String userTokenId; //  用户令牌

    /**
     * 构造
     *
     * @param billTokenId 表头tokenId
     * @param processId   流程id
     * @param metaId      备查账meta id
     * @param tokenIds    备查账id list
     */
    public PlaceRequireDTO(String billTokenId
            , String processId
            , String metaId
            , List<String> tokenIds
    ) {
        this.billTokenId = billTokenId;
        this.processId = processId;
        this.memorandumInfoBody = new MemorandumInfoBody(metaId, tokenIds);
        judgeStartProcessWay(metaId, tokenIds);
    }

    /**
     * 判断首节点启动方式
     */
    private void judgeStartProcessWay(String metaId, List<String> tokenIds) {
        // 如何备查账metaId 或 备查账tokenIds为空 是正常的流程启动
        if (null == metaId || tokenIds.isEmpty()) {
            this.placeBusinessContextEnum = PlaceBusinessContextEnum.START_EVENT_WITHOUT_MEMORANDUM;
        } else {
            // 否则为备查账流程启动
            this.placeBusinessContextEnum = PlaceBusinessContextEnum.START_EVENT_WITH_MEMORANDUM;
        }
    }
}
