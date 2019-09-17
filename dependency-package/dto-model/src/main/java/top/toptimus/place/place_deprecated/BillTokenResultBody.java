package top.toptimus.place.place_deprecated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.toptimus.tokendata.TokenDataDto;

import java.io.Serializable;

/**
 * 库所更新返回值
 */
@SuppressWarnings("ALL")
@NoArgsConstructor
@Getter
@Setter
public class BillTokenResultBody implements Serializable {

    private static final long serialVersionUID = 9148615232644242214L;

    private String billTokenId;       //表头tokenId
    private String billMetaId;        //表头metaId
    private TokenDataDto billTokenData;  //表头tokendata
    private TokenDataDto entryTokenData;           //分录的tokenid
    private String entryMetaId;            //分录的metaid
    private String authId;

    /**
     * 保存分录时调用
     * @param billTokenId   表头tokenId
     * @param billMetaId    表头metaId
     * @param entryTokenData  entryTokenData
     * @param entryMetaId   分录的metaid
     * @param authId        authid
     */
    public BillTokenResultBody(String billTokenId,String billMetaId,TokenDataDto entryTokenData,String entryMetaId,String authId){
        this.billTokenId = billTokenId;
        this.billMetaId = billMetaId;
        this.entryTokenData = entryTokenData;
        this.entryMetaId = entryMetaId;
        this.authId = authId;
    }

    /**
     * 保存表头时调用
     *
     * @param billMetaId        表头metaId
     * @param billTokenData     billTokenData
     * @param authId            authid
     */
    public BillTokenResultBody(String billMetaId, TokenDataDto billTokenData, String authId){
        this.billTokenId = billTokenData.getTokenId();
        this.billTokenData = billTokenData;
        this.billMetaId = billMetaId;
        this.authId = authId;
    }



}
