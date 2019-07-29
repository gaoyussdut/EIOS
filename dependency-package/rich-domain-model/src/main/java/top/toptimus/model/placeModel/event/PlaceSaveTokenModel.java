package top.toptimus.model.placeModel.event;

import lombok.Getter;
import top.toptimus.place.PlaceAlterDto;
import top.toptimus.tokendata.TokenDataDto;

import java.util.List;

/**
 * 库所增量提交token model
 */
@Getter
public class PlaceSaveTokenModel {
    private PlaceAlterDto placeAlterDto;    //  库所增量提交dto
    private List<TokenDataDto> tokenDataDtos;   //  token数据list
    private String billTokenId; //  表头token id
    private String metaId;  //  需要增量处理的meta id
    private List<String> removeTokenIds;    //删除的token id

    /**
     * @param billTokenId    表头token id
     * @param metaId         需要增量处理的meta id
     * @param metaType       meta type
     * @param billMetaId     表头meta id
     * @param tokenDataDtos  token数据list
     * @param removeTokenIds 删除的token id
     */
    public PlaceSaveTokenModel(
            String billTokenId
            , String metaId
            , String metaType
            , String billMetaId
            , List<TokenDataDto> tokenDataDtos
            , List<String> removeTokenIds
    ) {
        this.placeAlterDto = new PlaceAlterDto(billTokenId, metaId, metaType, billMetaId, removeTokenIds);
        this.tokenDataDtos = tokenDataDtos;
        this.billTokenId = billTokenId;
        this.metaId = metaId;
        this.removeTokenIds = removeTokenIds;
    }
}
