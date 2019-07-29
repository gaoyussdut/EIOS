package top.toptimus.model.tokenData.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * token是否存在的充血模型
 * 封装全部token id列表、已存在的token id列表、数据库中不存在的token id列表
 *
 * @author gaoyu
 * @since 2018-6-29
 */
@NoArgsConstructor
@Getter
public class TokenIdModel {
    private List<String> tokenIds;
    private List<String> tokenIdsExisted = new ArrayList<>();
    private List<String> tokenIdsNotExisted = new ArrayList<>();

    /**
     * 构造函数，token数据校验，并生成token id列表
     *
     * @param tokenDataDtos token数据
     */
    public TokenIdModel(List<TokenDataDto> tokenDataDtos) {
        //  清洗token数据，如果token id不存在，生成token id
        this.tokenIds = TokenIdModel.autoGenerateTokenIdsByTokenDataDtos(tokenDataDtos);
    }

    /**
     * 找到不存在的token id列表
     *
     * @param partTokenIds  部分token id列表
     * @param totalTokenIds 总的token id列表
     * @return 不存在的token列表
     */
    private static List<String> getNotExistedTokenIds(List<String> partTokenIds, List<String> totalTokenIds) {
        List<String> retTokenIds = new ArrayList<>();
        for (String tokenId : partTokenIds) {
            if (!totalTokenIds.contains(tokenId)) {
                retTokenIds.add(tokenId);
            }
        }
        return retTokenIds;
    }

    /**
     * 清洗tonken数据，如果token id不存在，生成token id
     *
     * @param tokenDataDtos token数据
     * @return token id列表
     */
    private static List<String> autoGenerateTokenIdsByTokenDataDtos(List<TokenDataDto> tokenDataDtos) {
        List<String> tokenIds = new ArrayList<>();
        tokenDataDtos.forEach(tokenDataDto -> {
            if (null == tokenDataDto.getFields() /*|| tokenDataDto.getFields().size() == 0*/) {// TODO 是否需要
                throw new TopException(TopErrorCode.TOKEN_DATA_IS_EMPTY);
            }
            if (StringUtils.isEmpty(tokenDataDto.getTokenId())) {
                tokenDataDto.build(UUID.randomUUID().toString());
            }
            tokenIds.add(tokenDataDto.getTokenId());
        });
        return tokenIds;
    }

    public TokenIdModel buildTokenIds(List<String> tokenIds) {
        this.tokenIds = tokenIds;
        return this;
    }

    /**
     * 拆分数据库中已存在的token id列表和不存在的token id列表
     *
     * @param tokenIdsExisted 数据库中已存在的token id
     */
    public void build(List<String> tokenIdsExisted) {
        this.tokenIdsExisted.addAll(tokenIdsExisted);
        //  传入了数据库里找不到的tokenId
        this.tokenIdsNotExisted = TokenIdModel.getNotExistedTokenIds(tokenIds, tokenIdsExisted);
    }
}
