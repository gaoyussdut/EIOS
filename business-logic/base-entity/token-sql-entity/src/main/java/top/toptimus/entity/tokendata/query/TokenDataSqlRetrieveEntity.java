package top.toptimus.entity.tokendata.query;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.baseModel.query.BasePageQueryModel;
import top.toptimus.baseModel.query.BaseQueryModel;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.meta.MetaTablesRelation.MetaTablesRelationDTO;
import top.toptimus.model.tokenModel.query.baseQueryFacade.TokenQueryModel;
import top.toptimus.model.tokenModel.query.baseQueryFacade.TokenQueryWithPageModel;
import top.toptimus.place.PlaceDTO;
import top.toptimus.repository.meta.MetaTablesRelation.MetaTablesRelationRepository;
import top.toptimus.repository.token.dynamicTokenQuery.DynamicTokenQueryRepository;
import top.toptimus.tokendata.TokenDataDto;

import java.util.List;

/**
 * token sql查询实体
 *
 * @author gaoyu
 * @since 2018-11-26
 */
@Component
public class TokenDataSqlRetrieveEntity {
    @Autowired
    private DynamicTokenQueryRepository dynamicTokenQueryRepository;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private MetaTablesRelationRepository metaTablesRelationRepository;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;

    /**
     * 根据meta和token id列表生成查询语句，并给helper的data赋值
     *
     * @param baseQueryModel token数据查询model
     */
    public BaseQueryModel generateQuerySql(BaseQueryModel baseQueryModel) {
        return baseQueryModel.buildTokenDatas(
                // 查询结果集
                dynamicTokenQueryRepository.getTokenDataDto(
                        baseQueryModel.buildViewQuery()    //  生成查询基类
                )
        );
    }

    /**
     * 根据meta列表生成查询语句
     *
     * @param tokenQueryWithPageModel token数据查询model，带分页
     * @return TokenDataPageableDto
     */
    public BasePageQueryModel generateQuerySql(TokenQueryWithPageModel tokenQueryWithPageModel) {
        // 返回值
        return tokenQueryWithPageModel.buildTokenDataPageableDto(
                dynamicTokenQueryRepository.getReducedTokenDataDtos(
                        tokenQueryWithPageModel
                                .build(tokenQueryWithPageModel.getSearchInfo(), userQueryFacadeEntity.findByAccessToken())// 构建检索参数
                                .build(
                                        metaQueryFacadeEntity.getMetaInfo(tokenQueryWithPageModel.getMetaId())
                                        , this.getTableName(tokenQueryWithPageModel.getMetaId())
                                )   //  构建meta信息和构建主表信息
                                .build(
                                        dynamicTokenQueryRepository.getTokenDataCount(tokenQueryWithPageModel)   //  查询数据总条数
                                )  //  构建数据总条数，检查分页
                                .buildBasePageQueryModel(tokenQueryWithPageModel.getSearchInfo().getSearchItem())
                                .buildPagingSQL() // 构造分页的信息
                ) // 查询token数据并封装返回
        );
    }

    /**
     * 根据缓存的MainTable生成查询语句并查询数据
     *
     * @param tokenQueryWithPageModel token数据查询model，带分页
     * @return BasePageQueryModel
     */
    public BasePageQueryModel generateQuerySqlByCacheMainTable(TokenQueryWithPageModel tokenQueryWithPageModel) {
        // 返回值
        return tokenQueryWithPageModel.buildTokenDataPageableDto(
                dynamicTokenQueryRepository.getReducedTokenDataDtos(
                        tokenQueryWithPageModel
                                .build(tokenQueryWithPageModel.getSearchInfo(), userQueryFacadeEntity.findByAccessToken())// 构建检索参数
                                .build(
                                        dynamicTokenQueryRepository.getTokenDataCount(tokenQueryWithPageModel)   //  查询数据总条数
                                )  //  构建数据总条数，检查分页
                                .buildPagingSQL()  // 构造分页的信息
                ) // 查询token数据并封装返回
        );
    }

    /**
     * 查找指定metaId的tableName TODO    这事总放数据库里面干有点傻逼，加缓存
     *
     * @param metaId meta id
     * @return 表名
     */
    public String getTableName(String metaId) {
        String tab_name = null;
        List<MetaTablesRelationDTO> metaTablesRelationDaos;
        try {
            metaTablesRelationDaos = metaTablesRelationRepository.getProcessTablesDaoByMetaId(metaId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("meta不存在");
        }
        for (MetaTablesRelationDTO metaTablesRelationDao : metaTablesRelationDaos) {
            if (metaTablesRelationDao.getMetaId().equals(metaId)) {
                tab_name = metaTablesRelationDao.getTableName();
                break;
            }
        }

        if (StringUtils.isEmpty(tab_name))
            throw new RuntimeException("table_name是空！metaId:" + metaId);
        return tab_name;
    }

    /**
     * 检查token id是否存在
     *
     * @param tableName 表名
     * @param tokenIds  token id 列表
     * @return 存在的token id 列表
     */
    public List<String> getExistTokenIds(String tableName, List<String> tokenIds) {
        return dynamicTokenQueryRepository.getExistTokenIds(tableName, tokenIds);
    }

    /**
     * 根据tokenId和metaId获取指定单条数据
     *
     * @param tokenId token id
     * @param metaId  meta id
     * @return token单条数据
     */
    public PlaceDTO getPlace(String tokenId, String metaId) {
        List<TokenDataDto> tokenDataDtoList =
                this.generateQuerySql(
                        new TokenQueryModel(
                                Lists.newArrayList(tokenId)    //  token ids
                                , this.getTableName(metaId)   //  获取表名
                                , metaQueryFacadeEntity.getMetaInfo(metaId)  //  获取TokenMetaInfoDto
                        )
                ).getTokenDataDtos();
        if (tokenDataDtoList != null && tokenDataDtoList.size() > 0) {
            return new PlaceDTO(
                    metaId
                    , tokenId
                    , tokenDataDtoList.get(0)
            );
        }
        return null;
    }
}
