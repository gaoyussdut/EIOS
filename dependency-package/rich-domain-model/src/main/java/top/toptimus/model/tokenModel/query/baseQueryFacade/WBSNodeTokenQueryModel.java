package top.toptimus.model.tokenModel.query.baseQueryFacade;

import lombok.Getter;
import top.toptimus.baseModel.query.BaseQueryModel;
import top.toptimus.businessmodel.memorandvn.dto.MemorandvnDTO;
import top.toptimus.common.enums.event.MemorandvnEnum;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.dynamicSQLModel.queryFacadeModel.ViewQueryMainTable;
import top.toptimus.meta.TokenMetaInfoDTO;

/**
 * WBS节点token详情model
 *
 * @author gaoyu
 * @since 2018-11-26
 */
public class WBSNodeTokenQueryModel extends BaseQueryModel {
    @Getter
    private MemorandvnDTO memorandvnDTO; // meta id
    private String lotNo; // lot no
    private String orgId; // 任务中心id

    @Getter
    private TokenMetaInfoDTO tokenMetaInfoDTO; // meta信息

    /**
     * 封装WBS节点，为了获取top.toptimus.merkle.MerkleModel
     * 最终得到库所、查询信息、token的byte数据
     *
     * @param lotNo               批号
     * @param orgId               任务中心id
     * @param memorandvnDTO       备查账簿dto
     * @param memorandvnFkey      当前节点存储过程外键名称
     * @param childMemorandvnFkey 子节点存储过程外键名称
     * @param memorandvnType      备查账类型
     */
    public WBSNodeTokenQueryModel(
            String lotNo
            , String orgId
            , MemorandvnDTO memorandvnDTO
            , String memorandvnFkey
            , String childMemorandvnFkey
            , MemorandvnEnum memorandvnType
    ) {
        this.lotNo = lotNo;
        this.orgId = orgId;
        switch (memorandvnType) {
            case BUSINESS:
                this.memorandvnDTO = memorandvnDTO.build(memorandvnFkey, memorandvnType);
                break;
            case RESOURCE:
                this.memorandvnDTO = memorandvnDTO.build(memorandvnFkey, childMemorandvnFkey, memorandvnType);
        }
    }

    /**
     * 根据缓存的Main table进行车讯
     *
     * @param mainTable 缓存的主表结构
     */
    public WBSNodeTokenQueryModel(MainTable mainTable) {
        this.mainTable = (ViewQueryMainTable) mainTable;
    }

    /**
     * 取得WBS节点详情并更新
     *
     * @return this
     */
    public WBSNodeTokenQueryModel build() {

        return this;
    }

    /**
     * 给meta信息赋值
     *
     * @param tokenMetaInfoDTO meta信息
     * @return this
     */
    public WBSNodeTokenQueryModel build(TokenMetaInfoDTO tokenMetaInfoDTO) {
        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
        return this;
    }

    /**
     * 生成主表结构，并生成查询token的sql
     *
     * @param tableName 表名
     * @return this
     */
    public WBSNodeTokenQueryModel build(String tableName) {
        this.mainTable = new ViewQueryMainTable(this.tokenMetaInfoDTO, tableName);   // 生成主表结构
        mainTable.buildViewQuery(
                this.orgId
                , this.lotNo
                , this.memorandvnDTO
        );  // 生成sql
        return this;
    }

    public BaseQueryModel buildViewQuery() {
        super.buildStrSQL(this.mainTable.getStrSql());
        return this;
    }
}
