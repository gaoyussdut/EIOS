package top.toptimus.token.relation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenRelDTO {

    private String billMetaId;
    private String billTokenId;
    private String entryMetaId;
    private String entryTokenId;
}
