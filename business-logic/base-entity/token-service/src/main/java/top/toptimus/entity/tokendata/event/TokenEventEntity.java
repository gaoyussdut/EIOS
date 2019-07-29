package top.toptimus.entity.tokendata.event;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.dao.token.MetaTokenRelationDao;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.tokendata.query.TokenDataSqlRetrieveEntity;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.meta.signGroup.CountersignSubmitDTO;
import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.model.tokenData.event.EntryTokenSaveModel;
import top.toptimus.place.BillTokenSaveResultDTO;
import top.toptimus.repository.meta.CountersignRepository;
import top.toptimus.repository.token.MetaRelation.MetaTokenRelationRepository;
import top.toptimus.repository.token.MetaRelation.TokenRelationRopository;
import top.toptimus.repository.token.TokenRepository;
import top.toptimus.task.TaskDto;
import top.toptimus.token.TokenDto;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理token event的实体类
 *
 * @author gaoyu
 * @since 2018-6-20
 */
@Component
public class TokenEventEntity {
    @Autowired
    private TokenDataSqlCUDEntity tokenDataSqlCUDEntity;
    @Autowired
    private TokenDataSqlRetrieveEntity tokenDataSqlRetrieveEntity;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private MetaTokenRelationRepository metaTokenRelationRepository;
    @Autowired
    private CountersignRepository countersignRepository;
    @Autowired
    private TokenRelationRopository tokenRelationRopository;

    /**
     * 插入指定表中的指定数据
     *
     * @param tokenDao token数据
     */
    public void insertTokenValueById(TokenDto tokenDao) {
        tokenRepository.insertTokenValueById(tokenDao);
    }

    /**
     * 插入指定表中的指定tokenId
     *
     * @param tokenDao token数据
     */
    public void insertTokenId(TokenDto tokenDao) {
        tokenRepository.insertTokenId(tokenDao);
    }

    /**
     * 保存数据
     */
    public void saveTokenDatas(TokenDataModel tokenDataModel) {
        //  判断token id是否存在，直接构建tokenDataModel
        tokenDataModel.build(
                tokenDataSqlRetrieveEntity.getExistTokenIds(   //  查询已存在的token
                        tokenDataSqlRetrieveEntity.getTableName(tokenDataModel.getTokenMetaInfoDTO().getTokenMetaId())    //  找表名
                        , tokenDataModel.getTokenIdModel().getTokenIds()
                )
        );

        if (!tokenDataModel.getNotExistTokenDataDtos().isEmpty()) {  //  token 不存在，插入
            this.tokenDataPersistence(tokenDataModel);
        }
        if (!tokenDataModel.getExistTokenDataDtos().isEmpty()) {  //  token 存在，更新
            this.updateTokenDataDtos(tokenDataModel);
        }
    }

    /**
     * token数据持久化
     *
     * @param tokenDataModel token数据存储的充血模型
     */
    private void tokenDataPersistence(TokenDataModel tokenDataModel) {
        String tableName;
        try {
            //  找表名
            tableName = tokenDataSqlRetrieveEntity.getTableName(
                    tokenDataModel.getTokenMetaInfoDTO().getTokenMetaId()
            );
        } catch (Exception e) {
            throw new RuntimeException("找不到表名");
        }
        //保存数据
        this.insertTokenDataDtos(tableName, tokenDataModel);
    }

    /**
     * 更新token数据
     *
     * @param tokenDataModel 保存token的充血模型
     */
    private void updateTokenDataDtos(TokenDataModel tokenDataModel) {
        // 保存和更新数据到数据表
        tokenDataSqlCUDEntity.updateTokenData(
                tokenDataSqlRetrieveEntity.getTableName(tokenDataModel.getTokenMetaInfoDTO().getTokenMetaId())  // 获取table
                , tokenDataModel.getTokenDataDtos()
                , tokenDataModel.getTokenMetaInfoDTO().getMetaFields()  // 根据metaId 获取 List<MetaField>
        );
    }

    /**
     * 插入token数据
     *
     * @param tableName      表名
     * @param tokenDataModel token数据存储的充血模型
     */
    private void insertTokenDataDtos(String tableName, TokenDataModel tokenDataModel) {
        // 根据metaId 获取 List<MetaField>
        List<MetaFieldDTO> metaFields = tokenDataModel.getTokenMetaInfoDTO().getMetaFields();
        // 保存和更新数据到数据表
        tokenDataSqlCUDEntity.insertTokenData(tableName, tokenDataModel.getTokenDataDtos(), metaFields);
    }

    /**
     * 根据tokenId更新指定表的字段
     *
     * @param tokenDao token数据
     */
    public void updateTokenValueById(TokenDto tokenDao) {
        tokenRepository.updateTokenValueById(tokenDao);
    }

    /**
     * 保存tokenDataDto数据
     *
     * @param tokenDataDto token数据
     * @param metaId       meta信息
     */
    public void saveDatas(TokenDataDto tokenDataDto, String metaId) {
        TokenMetaInfoDTO tokenMetaInfoDTO = metaQueryFacadeEntity.getMetaInfo(metaId);
        this.saveTokenDatas(
                new EntryTokenSaveModel(Lists.newArrayList(tokenDataDto), tokenMetaInfoDTO)  //  数据清洗构造函数，给token数据、meta赋值，检验token数据，生成token id
        );
    }

    /**
     * 保存tokenDataDto数据
     *
     * @param datas K:meta id, V:token数据列表
     */
    public void saveDatas(Map<String, List<TokenDataDto>> datas) {
        for (String metaId : datas.keySet()) {
            this.saveTokenDatas(
                    new EntryTokenSaveModel(
                            datas.get(metaId)
                            , metaQueryFacadeEntity.getMetaInfo(metaId)
                    )  //  数据清洗构造函数，给token数据、meta赋值，检验token数据，生成token id
            );
        }
    }

    /**
     * 保存关系 向ES中保存关系
     *
     * @param metaTokenRelationDao 记录表头token和（分录、关联单据、引用单据）之间的token关系
     */
    public void saveRel(MetaTokenRelationDao metaTokenRelationDao) {
        //删除旧关系
        metaTokenRelationRepository.deleteAllByBillMetaIdAndBillTokenIdAndEntryMetaIdAndEntryTokenId(
                metaTokenRelationDao.getBillMetaId()
                , metaTokenRelationDao.getBillTokenId()
                , metaTokenRelationDao.getEntryMetaId()
                , metaTokenRelationDao.getEntryTokenId()
        );
        metaTokenRelationRepository.refresh();
        //存新关系
        metaTokenRelationRepository.save(metaTokenRelationDao);
        //同步关系到pg
        tokenRelationRopository.deleteMetaTokenRelation(metaTokenRelationDao);
        tokenRelationRopository.saveMetaTokenRelation(metaTokenRelationDao);
    }

    /**
     * 保存关系 向pg中存关系
     *
     * @param billTokenSaveResultDTO
     */
    public void saveRel(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        //同步关系到pg
        tokenRelationRopository.deleteMetaTokenRelation(billTokenSaveResultDTO);
        tokenRelationRopository.saveMetaTokenRelation(billTokenSaveResultDTO);
    }

    /**
     * 删除ES中分录Token的关系
     *
     * @param entryTokenId 分录tokenId
     */
    public void delRelByEntryTokenId(String entryTokenId) {
        metaTokenRelationRepository.deleteByEntryTokenId(entryTokenId);
        metaTokenRelationRepository.refresh();
    }

    /**
     * 查分录tokenids
     *
     * @param billTokenId 表头tokenid
     * @param billMetaId  表头meta
     * @param entryMetaId 分录meta
     * @return List<String>
     */
    public List<String> findRel(String billTokenId, String billMetaId, String entryMetaId) {
        return new ArrayList<String>() {
            private static final long serialVersionUID = -2689426152607310768L;

            {
                tokenRelationRopository
                        .findAllByBillTokenIdAndBillMetaIdAndEntryMetaId(billTokenId, billMetaId, entryMetaId)
                        .forEach(billAndEntryMetaDTO -> add(billAndEntryMetaDTO.getEntryTokenId()));
            }
        };
    }

    /**
     * 保存汇签任务和汇签单据信息之间的关系
     *
     * @param countersignSubmitDTO  汇签信息提交DTO
     * @param countersignTaskMetaId 汇签任务metaId
     * @param countersignMetaId     汇签信息metaId
     */
    public void saveCountersignRel(CountersignSubmitDTO countersignSubmitDTO, String countersignTaskMetaId, String countersignMetaId) {
        //添加关系
        countersignRepository.saveCountersignRel(
                countersignSubmitDTO.getCountersignTaskTokenIds()
                , countersignSubmitDTO.getCountersignTokenData().getTokenId()
                , countersignTaskMetaId
                , countersignMetaId
        );
    }

    /**
     * 保存单据token和汇签任务token之间的关系
     *
     * @param taskDto                任务dto
     * @param countersignTaskMetaId  汇签任务metaId
     * @param countersignTaskTokenId 汇签任务tokenId
     */
    public void saveCountersignTaskRel(TaskDto taskDto, String countersignTaskMetaId, String countersignTaskTokenId) {
        countersignRepository.saveCountersignTaskRel(taskDto, countersignTaskMetaId, countersignTaskTokenId);
    }
}
