package top.toptimus.model.tokenData.event;

import lombok.Getter;
import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.model.tokenData.base.TokenIdModel;

import java.util.List;

/**
 * token删除的充血模型
 *
 * @author gaoyu
 * @since 2018-12-26
 */
@Getter
public class TokenDeleteModel extends TokenDataModel {
    /**
     * 构建meta和token id列表
     *
     * @param metaId   meta id
     * @param tokenIds token id列表
     */
    public TokenDeleteModel(String metaId, List<String> tokenIds) {
        this.metaId = metaId;
        this.tokenIdModel = new TokenIdModel().buildTokenIds(tokenIds);
    }
}
