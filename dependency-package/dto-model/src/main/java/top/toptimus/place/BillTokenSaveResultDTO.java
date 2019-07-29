package top.toptimus.place;

import lombok.Getter;
import lombok.Setter;
import top.toptimus.tokendata.TokenDataDto;

import java.io.Serializable;

/**
 * 库所保存结果
 *
 * @author gaoyu
 */
@Getter
@Setter
public class BillTokenSaveResultDTO implements Serializable {
    private static final long serialVersionUID = 8217151552614088012L;
    private String msg;
    private int resultCode;

    private BillTokenResultBody billTokenResultBody;
    /**
     * 保存分录时调用
     * @param billTokenId   表头tokenId
     * @param billMetaId    表头metaId
     * @param entryTokenData  entryTokenData
     * @param entryMetaId   分录的metaid
     * @param authId        authid
     */
    public BillTokenSaveResultDTO(String billTokenId, String billMetaId, TokenDataDto entryTokenData, String entryMetaId, String authId) {
        this.resultCode = 0;
        this.billTokenResultBody = new BillTokenResultBody(billTokenId, billMetaId, entryTokenData,entryMetaId,authId);
    }

    /**
     * 保存表头时调用
     *
     * @param billMetaId        表头metaId
     * @param billTokenData     billTokenData
     * @param authId            authid
     */
    public BillTokenSaveResultDTO(String billMetaId, TokenDataDto billTokenData, String authId) {
        this.resultCode = 0;
        this.billTokenResultBody = new BillTokenResultBody( billMetaId, billTokenData,authId);
    }
//    /**
//     * 正常返回数据体
//     *
//     * @param placeDTO 被创建的关联单据
//     * @param authId    authId
//     */
//    public BillTokenSaveResultDTO(PlaceDTO placeDTO ,String authId){
//        this.resultCode = 0;
//        this.billTokenResultBody = new BillTokenResultBody(placeDTO, authId);
//    }


//    /**
//     * 返回错误信息
//     *
//     * @param msg 错误信息
//     */
//    public BillTokenSaveResultDTO(String msg) {
//        this.resultCode = -1;
//        this.msg = msg;
//    }
}
