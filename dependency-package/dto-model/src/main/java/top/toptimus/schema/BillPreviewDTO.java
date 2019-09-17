package top.toptimus.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.meta.relation.MetaRelDTO;
import top.toptimus.token.relation.TokenRelDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务描述：作为返给前端预览页面DTO。包含表头信息及相关联分录及单据信息
 *
 * @author lzs
 * @since 2019-08-06
 */
@NoArgsConstructor
@Setter
@Getter
public class BillPreviewDTO {

    private BillSchemaDTO billHeader = new BillSchemaDTO(); //表头
    private List<BillSchemaDTO> relBillDTOList = new ArrayList<>(); //关联单据列表

    public BillPreviewDTO build(String tokenTemplateId, String billMetaId, String billTokenId, MetaTypeEnum metaType) {
        this.billHeader.build(tokenTemplateId, billMetaId, billTokenId, metaType);
        return this;
    }

    public BillPreviewDTO build(String tokenId) {
        this.billHeader.setTokenId(tokenId);
        return this;
    }


    /**
     * 根据meta关系构建前端一览的meta关系
     *
     * @param metaRelDTOS meta关系
     * @return this
     */
    public BillPreviewDTO buildMetaRels(List<MetaRelDTO> metaRelDTOS) {
        if (metaRelDTOS != null && metaRelDTOS.size() > 0) {
            for (MetaRelDTO metaRelDTO : metaRelDTOS) {
                this.getRelBillDTOList()
                        .add(
                                new BillSchemaDTO(
                                        metaRelDTO.getRelTokenTemplateId()
                                        , metaRelDTO.getEntryMetaId()
                                        , metaRelDTO.getMetaType()
                                        , metaRelDTO.getOrder_())
                        );
            }
        }
        return this;
    }

    /**
     * 根据表头和分录/关联单据的token关系构建前端一览
     *
     * @param billTokenId  表头token id
     * @param tokenRelDTOS 表头和分录/关联单据的token关系
     * @return this
     */
    public BillPreviewDTO buildTokenRels(String billTokenId, List<TokenRelDTO> tokenRelDTOS) {
        if (billTokenId != null) {
            this.getRelBillDTOList().forEach(relBillDTO -> {
                tokenRelDTOS.forEach(tokenRelDTO -> {
                    if (relBillDTO.getMetaId().equals(tokenRelDTO.getEntryMetaId())) {
                        relBillDTO.build(tokenRelDTO.getEntryTokenId());
                    }
                });
            });
        }
        return this;
    }
}
