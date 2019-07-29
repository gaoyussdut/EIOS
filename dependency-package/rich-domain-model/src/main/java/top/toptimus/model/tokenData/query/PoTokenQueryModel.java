package top.toptimus.model.tokenData.query;

import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.model.tokenData.base.TokenIdModel;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取poMetaId下任务封装model
 *
 * @author gaoyu
 * @since 2018-12-26
 */
public class PoTokenQueryModel extends TokenDataModel {

    /**
     * 构建meta和token id列表
     *
     * @param metaId        meta id
     * @param tokenIds      token id列表
     * @param tokenDataDtos token数据
     * @apiNote 用于top.toptimus.service.domainService.PoService#findPoByPoMetaId(java.lang.String, top.toptimus.common.enums.process.UserTaskStatusEnum, int, int)
     */
    public PoTokenQueryModel(String metaId, List<String> tokenIds, List<TokenDataDto> tokenDataDtos, int pageSize, int pageNo, int total) {
        if (tokenIds != null && tokenIds.size() > 0) {
            try {
                this.metaId = metaId;
                this.tokenIdModel = new TokenIdModel().buildTokenIds(tokenIds);
                this.buildCleanTokenDataDto(tokenDataDtos);
                this.tokenDataPageableDto.build(pageSize, pageNo, total);
            } catch (Exception e) {
                this.tokenDataPageableDto.build(new ArrayList<>());
            }
        } else {
            this.tokenDataPageableDto = new TokenDataPageableDto().build(new ArrayList<>());
        }
    }
}
