package top.toptimus.businessmodel.memorandvn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BalanceAccountDTO {
    private String balanceAccountId;// 余额账id
    private String balanceAccountName; // 余额账名称
    private String balanceAccountMetaId; // 余额账metaId
}
