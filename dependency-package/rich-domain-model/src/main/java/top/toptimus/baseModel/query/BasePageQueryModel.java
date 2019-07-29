package top.toptimus.baseModel.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.common.search.SearchInfo;
import top.toptimus.common.search.SearchItem;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.dynamicSQLModel.queryFacadeModel.ViewQueryPageAbleMainTable;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.tokendata.ReducedTokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础分页查询model
 */
@Getter
@NoArgsConstructor
public abstract class BasePageQueryModel extends BaseModel {
    protected boolean checkPage = true;    // 判断所查数据下标收否超过数据条数
    protected Integer pageNumber;   // 页码
    protected Integer pageSize; // 页宽
    protected int count;    //  结果集总条数
    protected ViewQueryPageAbleMainTable mainTable;  //  主表
//    protected SearchItem searchItem; // 检索项目定义
    protected SearchInfo searchInfo; // 检索信息
    protected List<Object> param = new ArrayList<>(); // 参数
    protected TokenDataPageableDto tokenDataPageableDto;

    /**
     * 判断所查数据下标收否超过数据条数，构造sql文
     *
     * @param strSql sql文
     */
    public BasePageQueryModel build(String strSql) {
        // 判断所查数据下标收否超过数据条数
        if (!this.checkPage)
            throw new TopException(TopErrorCode.ARRAY_INDEX_OUT_BOUNDS);// 所查数据下标超过数据条数
        this.mainTable.buildStrSQL(strSql);
        return this;
    }

    /**
     * 构建基础分页查询model
     *
     * @return this
     */
    public BasePageQueryModel buildBasePageQueryModel() {
        this.mainTable.buildViewQueryPageAble(this.pageNumber, this.pageSize);  //  分页sql
        // 判断所查数据下标收否超过数据条数
        if (!this.checkPage)
            throw new TopException(TopErrorCode.ARRAY_INDEX_OUT_BOUNDS);// 所查数据下标超过数据条数
        return this;
    }

    /**
     * 构建基础分页查询model
     *
     * @return this
     */
    public BasePageQueryModel buildBasePageQueryModel(SearchItem searchItem) {
        this.mainTable.buildViewQueryPageAble(searchItem);  // 构造SQL
        // 判断所查数据下标收否超过数据条数
        if (!this.checkPage)
            throw new TopException(TopErrorCode.ARRAY_INDEX_OUT_BOUNDS);// 所查数据下标超过数据条数
        return this;
    }

    /**
     * 拼接分页信息
     * (使用场景：缓存中有MainTable 只需要拼接分页信息就可以)
     *
     * @return BasePageQueryModel
     */
    public BasePageQueryModel buildPagingSQL() {
        this.mainTable.buildPaging(this.pageNumber, this.pageSize);
        return this;
    }

    /**
     * 构建token list返回结构
     *
     * @param reducedDtos token list
     * @return this
     */
    public TokenDataPageableDto buildTokenDataPageableDto(List<ReducedTokenDataDto> reducedDtos) {
        return new TokenDataPageableDto(
                this.pageSize
                , this.pageNumber
                , this.count
                , reducedDtos
        );
    }

    public BasePageQueryModel buildTokenDataPageableDto(TokenDataPageableDto tokenDataPageableDto) {
        this.tokenDataPageableDto = tokenDataPageableDto;
        return this;
    }
}
