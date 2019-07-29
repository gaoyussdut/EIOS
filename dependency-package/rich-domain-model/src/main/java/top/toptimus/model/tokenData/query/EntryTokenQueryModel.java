package top.toptimus.model.tokenData.query;

import lombok.Getter;
import top.toptimus.common.CurrentPage;
import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.tokendata.TokenDataDto;

/**
 * 分录数据查询model
 *
 * @author gaoyu
 * @since 2018-12-26
 */
@Getter
public class EntryTokenQueryModel extends TokenDataModel {
    private String billMetaId;
    private String entryMetaId;
    private String billTokenId;
    private int pageNo;
    private int pageSize;

    public EntryTokenQueryModel(String billMetaId, String entryMetaId, String billTokenId, int pageNo, int pageSize) {
        this.billMetaId = billMetaId;
        this.entryMetaId = entryMetaId;
        this.billTokenId = billTokenId;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public EntryTokenQueryModel buildCleanTokenDataDto(CurrentPage<TokenDataDto> cleanTokenDataPage) {
        this.buildCleanTokenDataDto(cleanTokenDataPage.getPageItems());
        this.tokenDataPageableDto.build(cleanTokenDataPage.getPageSize(), cleanTokenDataPage.getPageNo(), cleanTokenDataPage.getTotal());
        return this;
    }
}
