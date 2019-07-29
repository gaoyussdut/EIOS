package top.toptimus.model.tokenModel.query.baseQueryFacade;

import lombok.Getter;
import top.toptimus.baseModel.query.BaseQueryModel;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.dynamicSQLModel.queryFacadeModel.ViewQueryMainTable;
import top.toptimus.meta.TokenMetaInfoDTO;

import java.util.List;

/**
 * 取分录的model
 *
 * @author gaoyu
 * @since 2018-11-26
 */
@Getter
public class EntryTokenIdQueryModel extends BaseQueryModel {
    private String tableName;   //  表名
    private List<String> tokenIds;

    /**
     * 构建主表
     *
     * @param tokenMetaInfoDTO meta info
     * @param tableName        表名
     */
    public EntryTokenIdQueryModel(
            TokenMetaInfoDTO tokenMetaInfoDTO
            , String tableName
    ) {
        this.tableName = tableName;
        super.mainTable = new ViewQueryMainTable(tokenMetaInfoDTO, tableName);
    }

    /**
     * 构建token id list
     *
     * @param tokenIds token id list
     * @return this
     */
    public EntryTokenIdQueryModel build(List<String> tokenIds) {
        this.tokenIds = tokenIds;
        return this;
    }

    /**
     * 生成查询基类
     *
     * @return 查询基类
     */
    public EntryTokenIdQueryModel buildViewQuery() {
        super.buildStrSQL(this.tokenIds);
        return this;
    }
}
