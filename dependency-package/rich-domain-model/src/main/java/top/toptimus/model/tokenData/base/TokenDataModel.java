package top.toptimus.model.tokenData.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.baseModel.query.BaseQueryModel;
import top.toptimus.dynamicSQLModel.queryFacadeModel.ViewQueryMainTable;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.List;

/**
 * token数据存储的充血模型
 *
 * @author gaoyu
 * @since 2018-6-28
 */
@Getter
@NoArgsConstructor
public abstract class TokenDataModel extends BaseQueryModel {
    protected List<TokenDataDto> tokenDataDtos = new ArrayList<>();   //  token数据
    //  token是否存在的充血模型
    //  封装全部token id列表、已存在的token id列表、数据库中不存在的token id列表
    protected TokenIdModel tokenIdModel;
    protected TokenMetaInfoDTO tokenMetaInfoDTO;  //  token meta信息
    protected String metaId;
    protected List<String> tableNameList = new ArrayList<>(); // 要返回的tokenIdList
    private List<TokenDataDto> existTokenDataDtos = new ArrayList<>();   //  存在的token数据
    private List<TokenDataDto> notExistTokenDataDtos = new ArrayList<>();   //  不存在的token数据

//    /**
//     * 数据清洗构造函数
//     *
//     * @param tokenDataDtos    token数据
//     * @param tokenMetaInfoDTO token meta信息
//     * @param billTokenId      表头token id
//     */
//    public TokenDataModel(List<TokenDataDto> tokenDataDtos, TokenMetaInfoDTO tokenMetaInfoDTO, String billTokenId) {
//        //  赋值
//        this.tokenDataDtos = tokenDataDtos;
//        //  token是否存在的充血模型，token数据校验，并生成token id列表
//        this.tokenIdModel = new TokenIdModel(tokenDataDtos);
//        //  清洗tonken数据，如果token id不存在，生成token id
//        this.autoGenerateTokenIdsByTokenDataDtos();
//        //  token meta信息
//        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
//        // 表头token id
//        this.billTokenId = billTokenId;
//
//    }

    /**
     * 清洗数据，区分存在的token和不存在的token
     *
     * @param tokenIdsExisted 存在的token id列表
     */
    public void build(List<String> tokenIdsExisted) {
        //  拆分数据库中已存在的token id列表和不存在的token id列表
        this.tokenIdModel.build(tokenIdsExisted);
        //  根据token id拆分token数据
        this.splitTokenDataById();
    }

    /**
     * 清洗数据，区分存在的token和不存在的token
     *
     * @param tokenIdModel token是否存在的充血模型
     *                     封装全部token id列表、已存在的token id列表、数据库中不存在的token id列表
     */
    public void build(TokenIdModel tokenIdModel) {
        //  拆分数据库中已存在的token id列表和不存在的token id列表
        this.tokenIdModel = tokenIdModel;
        //  根据token id拆分token数据
        this.splitTokenDataById();
    }

    /**
     * 根据token id拆分token数据
     */
    private void splitTokenDataById() {
        //  根据token id清洗数据
        for (TokenDataDto tokenDataDto : this.tokenDataDtos) {
            if (this.tokenIdModel.getTokenIdsExisted().contains(tokenDataDto.getTokenId())) {
                this.existTokenDataDtos.add(tokenDataDto);  //  token存在
            } else if (this.tokenIdModel.getTokenIdsNotExisted().contains(tokenDataDto.getTokenId())) {
                this.notExistTokenDataDtos.add(tokenDataDto);   //  token不存在
            } else {
                throw new TopException(TopErrorCode.TOKEN_ID_ABNORRMAL);
            }
        }
    }

    public TokenDataModel build(String tableName) {
        super.mainTable = new ViewQueryMainTable(this.tokenMetaInfoDTO, tableName);
        return this;
    }

    /**
     * 生成查询基类
     *
     * @return 查询基类
     */
    public BaseQueryModel buildViewQuery() {
        return super.buildStrSQL(this.tokenIdModel.getTokenIds());
    }

}
