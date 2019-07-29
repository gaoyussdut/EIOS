package top.toptimus.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.baseModel.query.BasePageQueryModel;
import top.toptimus.common.search.SearchInfo;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.entity.tokendata.query.TokenDataSqlRetrieveEntity;
import top.toptimus.merkle.MerkleBasicModel;
import top.toptimus.model.tokenModel.query.baseQueryFacade.TokenQueryWithPageModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 备查账线程查询
 * Created by JiangHao on 2019/1/7.
 */
@Component
public class TokenDataThreadQueryFacadeEntity {

    private static final Logger logger = LoggerFactory.getLogger(TokenDataThreadQueryFacadeEntity.class);

    @Autowired
    private TokenDataSqlRetrieveEntity tokenDataSqlRetrieveEntity;

    /**
     * 备查账查询merkle
     *
     * @param metaId       备查账MetaId
     * @param pageNumber   当前页
     * @param pageSize     当前页大小
     * @param mainTableMap 缓存的MainTable
     * @param searchInfo   检索项目定义
     * @return MerkleBasicModel
     */
    public MerkleBasicModel generateMerkleModel(String metaId, Integer pageNumber, Integer pageSize
            , Map<String, MainTable> mainTableMap, SearchInfo searchInfo) {
        BasePageQueryModel basePageQueryModel =
                generateBasePageQueryModel(metaId, pageNumber, pageSize, mainTableMap, searchInfo);
        if (null == basePageQueryModel) {
            throw new RuntimeException("备查账查询merkle失败!");
        }
        return new MerkleBasicModel(
                basePageQueryModel.getTokenDataPageableDto()
                , new HashMap<String, MainTable>() {
            private static final long serialVersionUID = -1414642972731454701L;

            {
                if (null != searchInfo && null != searchInfo.getSearchItem()) {
                    // K: 备查账簿metaId的type页 V: MainTable
                    put(searchInfo.getSearchItem().getId(), basePageQueryModel.getMainTable());
                }
            }
        }
        );
    }

    /**
     * 取得查询的结果
     *
     * @param metaId       备查账MetaId
     * @param pageNumber   当前页
     * @param pageSize     当前页大小
     * @param mainTableMap 缓存的MainTable
     * @param searchInfo   检索项目定义
     * @return BasePageQueryModel
     */
    private BasePageQueryModel generateBasePageQueryModel(String metaId, Integer pageNumber, Integer pageSize
            , Map<String, MainTable> mainTableMap, SearchInfo searchInfo) {
        if (null != searchInfo && null != searchInfo.getSearchItem() && mainTableMap.containsKey(searchInfo.getSearchItem().getId())) {   //  main table存在的情况
            /*
                缓存sql查询  根据缓存的SQL只需要再拼接limit的信息和注入userId等数据即可
             */
            try {
                logger.info("取缓存中的SQL");
                return tokenDataSqlRetrieveEntity.generateQuerySqlByCacheMainTable(
                        new TokenQueryWithPageModel(
                                mainTableMap.get(searchInfo.getSearchItem().getId())  //  main table
                                , pageNumber
                                , pageSize
                                , searchInfo
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            /*
                正常查询，不走缓存
             */
            logger.info("正常查询");
            BasePageQueryModel basePageQueryModel = tokenDataSqlRetrieveEntity.generateQuerySql(
                    new TokenQueryWithPageModel(
                            metaId, searchInfo, pageNumber, pageSize
                    )
            );
            //  不走缓存的查询，建立main table
            if (null != (searchInfo != null ? searchInfo.getSearchItem() : null)) {
                mainTableMap.put(
                        searchInfo.getSearchItem().getId() // 备查账簿metaId的type页
                        , basePageQueryModel.getMainTable() //  主表信息，带sql
                );
            }

            return basePageQueryModel;
        }
        return null;
    }
}
