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
 * 分录保存充血模型
 *
 * @author gaoyu
 * @since 2018-12-26
 */
public class EntryTokenSaveModel extends TokenDataModel {

    /**
     * 数据清洗构造函数，给token数据、meta赋值，检验token数据，生成token id
     *
     * @param tokenDataDtos    token数据
     * @param tokenMetaInfoDTO token meta信息
     */
    public EntryTokenSaveModel(List<TokenDataDto> tokenDataDtos, TokenMetaInfoDTO tokenMetaInfoDTO) {
        //  赋值
        this.tokenDataDtos = tokenDataDtos;
        //  token是否存在的充血模型，token数据校验，并生成token id列表
        this.tokenIdModel = new TokenIdModel(tokenDataDtos);
        //  清洗tonken数据，如果token id不存在，生成token id
        this.autoGenerateTokenIdsByTokenDataDtos();
        //  token meta信息
        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
    }

    /**
     * 清洗分录token数据，如果token id不存在，生成token id
     */
    private void autoGenerateTokenIdsByTokenDataDtos() {
        this.tokenDataDtos.forEach(tokenDataDto -> {
            if (null == tokenDataDto.getFields() /*|| tokenDataDto.getFields().size() == 0*/) { //TODO 是否需要
                throw new TopException(TopErrorCode.TOKEN_DATA_IS_EMPTY);
            }
            if (StringUtils.isEmpty(tokenDataDto.getTokenId())) {
                tokenDataDto.build(UUID.randomUUID().toString());
            }
        });
    }
}
