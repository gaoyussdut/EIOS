package top.toptimus.place;

import com.google.common.base.Strings;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.relation.MetaRelDTO;
import top.toptimus.place.basePlace.BasePlaceDTO;
import top.toptimus.schema.BillPreviewDTO;
import top.toptimus.token.relation.TokenRelDTO;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * 构造，初始化表头
     *
     * @param metaId        表头metaid
     * @param billTokenData billTokenData
     */
    public PlaceDTO(String metaId, TokenDataDto billTokenData) {
        this.billMetaId = metaId;
        this.billTokenId = billTokenData.getTokenId();
        this.datas.put(metaId, new ArrayList<TokenDataDto>() {

            private static final long serialVersionUID = 1563767562589956971L;

            {
                add(billTokenData);
            }
        });
    }

    /**
     * 构造userid
     *
     * @param userId userId
     * @return PlaceDTO
     */
    public PlaceDTO buildUserId(String userId) {
        this.userId = userId;
        return this;
    }

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

    /**
     * 校验数据，如果tokenId不存在报错
     * 如果tokenId为空 存入了redis  会取不到数据
     */
    public void intendedEffectData() {
        Map<String, List<TokenDataDto>> datas = this.datas;
        for (String metaId : datas.keySet()) {
            for (TokenDataDto tokenDataDto : datas.get(metaId)) {
                if (Strings.isNullOrEmpty(tokenDataDto.getTokenId())) {
                    throw new TopException(TopErrorCode.TOKEN_ID_NOT_EXIST);
                }
            }
        }
    }
}
