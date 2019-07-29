package top.toptimus.model.tokenModel.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * token单条任务实例查询model
 *
 * @author gaoyu
 * @since 2018-12-12
 */
@NoArgsConstructor
public class TokenSingleDataQueryModel {
    @Getter
    private Map<String, String> tokenIdAndMetaIdMap = new HashMap<>();  //  K:token id, V:meta id

    public TokenSingleDataQueryModel(List<String> tokenIdList, String metaId) {
        tokenIdList.forEach(tokenId -> this.tokenIdAndMetaIdMap.put(tokenId, metaId));
    }
}
