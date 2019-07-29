package top.toptimus.meta.MetaRelation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.meta.TokenMetaInformationDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单据表单主数据与关联表单的的关系
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MasterBillMetaRelationDTO implements Serializable {
    private static final long serialVersionUID = -4385000334040110500L;
    private String billMasterMetaId; //单据主数据meta
    private String metaName; //meta名称
    private List<MasterMetaInfoDTO> masterMetaInfoDTOS = new ArrayList<>();//单据关联表单的信息

    public MasterBillMetaRelationDTO(String billMasterMetaId, List<MasterMetaInfoDTO> masterMetaInfoDTOS) {
        this.billMasterMetaId = billMasterMetaId;
        this.masterMetaInfoDTOS = masterMetaInfoDTOS;
    }

    /**
     * 给单据添加名称
     */
    public MasterBillMetaRelationDTO build(List<TokenMetaInformationDto> tokenMetaInformationDtos) {
        for (TokenMetaInformationDto tokenMetaInformationDto : tokenMetaInformationDtos) {
            if (this.billMasterMetaId.equals(tokenMetaInformationDto.getTokenMetaId())) {
                this.metaName = tokenMetaInformationDto.getTokenMetaName();
            }
            for (MasterMetaInfoDTO masterMetaInfoDTO : this.masterMetaInfoDTOS) {
                if (masterMetaInfoDTO.getEntryMasterMetaId().equals(tokenMetaInformationDto.getTokenMetaId())) {
                    masterMetaInfoDTO.build(tokenMetaInformationDto.getTokenMetaName());
                    break;
                }
            }
        }
        return this;
    }

}
