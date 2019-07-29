package top.toptimus.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CertificateReceiveDTO {
    private String receiveMetaId;
    private String storedProcedure;
    private String billTokenId;
    private String certificateTokenId;

    public CertificateReceiveDTO(String receiveMetaId,String storedProcedure){
        this.receiveMetaId = receiveMetaId;
        this.storedProcedure = storedProcedure;
    }

    public CertificateReceiveDTO buildBillTokenId(String billTokenId){
        this.billTokenId = billTokenId;
        return this;
    }

    public CertificateReceiveDTO buildCertificateTokenIdTokenId(String certificateTokenId){
        this.certificateTokenId = certificateTokenId;
        return this;
    }
}
