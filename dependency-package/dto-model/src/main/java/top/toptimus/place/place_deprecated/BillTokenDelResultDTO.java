package top.toptimus.place.place_deprecated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 库所保存结果
 *
 * @author gaoyu
 */
@NoArgsConstructor
@Getter
@Setter
public class BillTokenDelResultDTO implements Serializable {
    private static final long serialVersionUID = 8217151552614088012L;
    private String msg;
    private int resultCode;

    private BillTokenResultBody billTokenResultBody;


    /**
     * 返回错误信息
     *
     * @param msg 错误信息
     * @return this
     */
    public BillTokenDelResultDTO buildErrorMessage(String msg) {
        this.resultCode = -1;
        this.msg = msg;
        return this;
    }

    /**
     * 正常返回数据体
     *
     * @param billTokenId billTokenId
     * @param tokenId     被提交数据的tokenId
     * @param metaId      被提交数据的metaId
     * @param authId      authId
     * @return BillTokenSaveResultDTO
     */
//    public BillTokenDelResultDTO buildSuccessBody(String billTokenId, String tokenId, String metaId, String authId) {
//        this.resultCode = 0;
//        this.billTokenResultBody = new BillTokenResultBody(billTokenId,tokenId,metaId,authId);
//        return this;
//    }

}
