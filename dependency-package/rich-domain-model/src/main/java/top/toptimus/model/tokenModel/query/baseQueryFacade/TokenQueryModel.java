package top.toptimus.model.tokenModel.query.baseQueryFacade;

import lombok.Getter;
import top.toptimus.baseModel.query.BaseQueryModel;
import top.toptimus.dynamicSQLModel.queryFacadeModel.ViewQueryMainTable;
import top.toptimus.meta.TokenMetaInfoDTO;

import java.util.List;

/**
 * token数据查询model
 *
 * @author gaoyu
 * @since 2018-11-26
 */
@Getter
public class TokenQueryModel extends BaseQueryModel {
    private List<String> tokenIds;

    /**
     * 构建token ids和主表
     *
     * @param tokenIds         token ids
     * @param tableName        表名
     * @param tokenMetaInfoDTO meta info
     */
    public TokenQueryModel(List<String> tokenIds, String tableName, TokenMetaInfoDTO tokenMetaInfoDTO) {
        this.tokenIds = tokenIds;
        super.mainTable = new ViewQueryMainTable(tokenMetaInfoDTO, tableName);
    }

    /**
     * 生成查询基类
     *
     * @return 查询基类
     */
    public TokenQueryModel buildViewQuery() {
        super.buildStrSQL(this.tokenIds);
        return this;
    }
}
