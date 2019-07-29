package top.toptimus.dynamicSQLModel.queryFacadeModel;

import org.springframework.util.StringUtils;
import top.toptimus.common.search.SearchItem;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.meta.TokenMetaInfoDTO;


public class ViewQueryPageAbleMainTable extends MainTable {
    public ViewQueryPageAbleMainTable(TokenMetaInfoDTO tokenMetaInfoDTO, String tableName) {
        super(tokenMetaInfoDTO, tableName);
    }

    /**
     * 根据缓存的MainTable准备查询的数据
     *
     * @param mainTable 缓存的mainTable
     */
    public ViewQueryPageAbleMainTable(MainTable mainTable) {
        this.tableName = mainTable.getTableName();
        this.tokenMetaInfoDTO = mainTable.getTokenMetaInfoDTO();
        this.commonSql = mainTable.getCommonSql();
        this.countSql = mainTable.getCountSql();
        this.cacheSql = mainTable.getCacheSql();
    }

    /**
     * 根据视图查询
     *
     * @return sql
     */
    public MainTable buildViewQueryPageAble(Integer pageNumber, Integer pageSize) {
        this.strSql = this.buildSelect(this.tableName, this.foreignAttributeSuites, this.tokenMetaInfoDTO)
                + this.buildSelfQuery(this.tableName) + this.buildJoinViewQuery(this.tableName) + "limit " + pageSize
                + " offset " + (pageSize * (pageNumber - 1));
        logger.info(this.strSql);
        return this;
    }

    /**
     * 根据视图和检索项目查询
     *
     * @param searchItem 检索的项目
     */
    public void buildViewQueryPageAble(SearchItem searchItem) {
        this.cacheSql = this.buildSelect(this.tableName, this.foreignAttributeSuites, this.tokenMetaInfoDTO);
        // 如果commonSql存在的场合直接拼接，不存在取得
        if (StringUtils.isEmpty(this.commonSql)) {
            this.cacheSql += this.buildSelfQuery(this.tableName)
                    + this.buildJoinViewQuery(this.tableName)
                    + this.buildWhereSQL(searchItem);
        } else {
            this.cacheSql += this.commonSql;
        }
        logger.info("缓存的SQL信息: " + this.cacheSql);
    }

    /**
     * 时序上必须放在最后执行
     * 构造分页用的limit
     *
     * @param pageNumber 当前页
     * @param pageSize   当前页大小
     */
    public void buildPaging(Integer pageNumber, Integer pageSize) {
        //  根据缓存的SQL拼出完整的执行SQL
        this.strSql = this.cacheSql + " limit " + pageSize + " offset " + (pageSize * (pageNumber - 1));
        logger.info("执行的SQL: " + this.strSql);
    }


}
