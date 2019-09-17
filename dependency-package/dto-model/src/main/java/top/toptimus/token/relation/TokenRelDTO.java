package top.toptimus.token.relation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 表头和分录/关联单据的token关系
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenRelDTO {
    private String billMetaId;
    private String billTokenId;
    private String entryMetaId;
    private String entryTokenId;
}
