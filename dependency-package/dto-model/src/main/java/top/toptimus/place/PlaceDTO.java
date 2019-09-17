package top.toptimus.place;

import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.meta.relation.MetaRelDTO;
import top.toptimus.place.basePlace.BasePlaceDTO;
import top.toptimus.schema.BillPreviewDTO;
import top.toptimus.token.relation.TokenRelDTO;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;

import java.util.List;

/**
 * 库所新设计
 *
 * @author gaoyu
 * @since 2019-09-17
 */
public class PlaceDTO extends BasePlaceDTO {
    private TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO;  //  tokenTemplate定义
    private List<TokenRelDTO> tokenRelDTOS;
    private List<MetaRelDTO> metaRelDTOS;

    /**
     * 构建库所表头分录的ttid信息、token数据关系、meta关系
     *
     * @param tokenTemplateDefinitionDTO ttid信息
     * @param tokenRelDTOS               token数据关系
     * @param metaRelDTOS                meta数据关系
     * @return this
     */
    public PlaceDTO build(
            TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO
            , List<TokenRelDTO> tokenRelDTOS
            , List<MetaRelDTO> metaRelDTOS
    ) {
        this.tokenTemplateDefinitionDTO = tokenTemplateDefinitionDTO;
        this.tokenRelDTOS = tokenRelDTOS;
        this.metaRelDTOS = metaRelDTOS;
        return this;
    }

    /**
     * [
     * <p>
     * {
     * <p>
     * //    表头
     * <p>
     * tokenId;
     * <p>
     * metaId;
     * <p>
     * metaType=表头;
     * <p>
     * }
     * <p>
     * {
     * <p>
     * //    分录
     * <p>
     * [tokenId];
     * <p>
     * metaId;
     * <p>
     * metaType=分录;
     * <p>
     * }
     * <p>
     * {
     * <p>
     * //    关联单据
     * <p>
     * tokenId;
     * <p>
     * metaId;
     * <p>
     * metaType=关联单据;
     * <p>
     * }
     * <p>
     * ]
     *
     * @return 前端预览页面DTO
     */
    public BillPreviewDTO generateBillPreviewDTO() {
        return new BillPreviewDTO()
                .build(
                        this.tokenTemplateDefinitionDTO.getTokenTemplateId()
                        , this.tokenTemplateDefinitionDTO.getBillMetaId()
                        , this.billTokenId
                        , MetaTypeEnum.BILL
                )        // 首先找出schemaId
                .buildMetaRels(this.metaRelDTOS)
                .buildTokenRels(this.billTokenId, this.tokenRelDTOS);
    }
}
