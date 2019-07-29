package top.toptimus.relation;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表头和分录关系
 */
@Data
@NoArgsConstructor
public class BillAndEntryMetaDTO {

    private String billMetaId;
    private String billTokenId;
    private String entryMetaId;
    private String entryTokenId;

    public BillAndEntryMetaDTO(String billMetaId, String billTokenId, String entryMetaId, String entryTokenId) {
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        this.entryMetaId = entryMetaId;
        this.entryTokenId = entryTokenId;
    }
}
