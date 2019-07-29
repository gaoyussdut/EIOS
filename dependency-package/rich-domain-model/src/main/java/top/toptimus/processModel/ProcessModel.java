package top.toptimus.processModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.CurrentPage;
import top.toptimus.processDefinition.UserTaskInsDTO;
import top.toptimus.tokendata.ReducedTokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程model
 *
 * @author gaoyu
 */
@NoArgsConstructor
@Getter
public class ProcessModel {
    private TokenDataPageableDto tokenDataPageableDto;
    private String metaId;
    private List<String> tokenIds = new ArrayList<>();

    /**
     * 封装token id列表和ttid
     *
     * @param userTaskInsDaos user task实例列表
     * @param pageSize        页宽
     * @param pageNo          页码
     * @return this
     */
    public ProcessModel build(CurrentPage<UserTaskInsDTO> userTaskInsDaos, int pageSize, int pageNo) {
        this.tokenDataPageableDto = new TokenDataPageableDto().build(pageSize, pageNo);
        for (UserTaskInsDTO dao : userTaskInsDaos.getPageItems()) {
            tokenIds.add(dao.getTokenId()); // tokenIds取得
            this.metaId = dao.getMetaId();    // metaId
        }
        tokenDataPageableDto.build(userTaskInsDaos.getTotal());  // 总数
        return this;
    }

    /**
     * 封装token data
     *
     * @param reducedTokenDataDtos token data
     * @return this
     */
    public ProcessModel build(List<ReducedTokenDataDto> reducedTokenDataDtos) {
        this.tokenDataPageableDto.build(reducedTokenDataDtos);
        return this;
    }
}
