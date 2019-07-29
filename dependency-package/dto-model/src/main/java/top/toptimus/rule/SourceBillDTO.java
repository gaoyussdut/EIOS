package top.toptimus.rule;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SourceBillDTO {
    private String sourceMetaId;
    private String sourceTokenId;

    public SourceBillDTO(String sourceMetaId,String sourceTokenId){
        this.sourceMetaId = sourceMetaId;
        this.sourceTokenId = sourceTokenId;
    }
}
