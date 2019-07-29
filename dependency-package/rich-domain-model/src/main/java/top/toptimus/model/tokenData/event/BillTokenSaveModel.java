package top.toptimus.model.tokenData.event;

import org.apache.commons.lang3.StringUtils;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.model.tokenData.base.TokenIdModel;
import top.toptimus.tokendata.TokenDataDto;

import java.util.List;
import java.util.UUID;

/**
 * 表头数据保存的充血模型
 *
 * @author gaoyu
 * @since 2018-12-26
 */
public class BillTokenSaveModel extends TokenDataModel {
    /**
     * 表头数据清洗构造函数
     *
     * @param billTokenId      表头token id
     * @param tokenDataDtos    token数据
     * @param tokenMetaInfoDTO token meta信息
     */
    public BillTokenSaveModel(
            String billTokenId
            , List<TokenDataDto> tokenDataDtos
            , TokenMetaInfoDTO tokenMetaInfoDTO
    ) {
        //  赋值
        this.tokenDataDtos = tokenDataDtos;
        //  清洗表头token数据，如果token id不存在，生成token id
        this.autoGenerateTokenIdsByTokenDataDtos(billTokenId);
        //  token是否存在的充血模型，token数据校验，并生成token id列表
        this.tokenIdModel = new TokenIdModel(tokenDataDtos);
        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
    }

    /**
     * 清洗表头token数据
     * 如果表头token id不存在，生成表头token id
     * 无论表头token id是否存在，都要重新给表头重新赋值
     *
     * @param billTokenId 表头token id
     */
    private void autoGenerateTokenIdsByTokenDataDtos(String billTokenId) {
        if (StringUtils.isEmpty(billTokenId)) {
            billTokenId = UUID.randomUUID().toString();
        }
        try {
            this.tokenDataDtos.get(0).build(billTokenId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TopException(TopErrorCode.TOKEN_BILL_DATA_IS_EMPTY);
        }
    }
}
