package top.toptimus.businessmodel.memorandvn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 凭证一览用凭证DTO
 * Created by JiangHao on 2018/10/15.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CertificateDTO {
    private String certificateId; // 凭证id
    private String certificateName; // 凭证名称
    private String certificateMetaId; //凭证的metaId
}
