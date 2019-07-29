package top.toptimus.dynamicSQLModel.queryFacadeModel;

import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import top.toptimus.businessmodel.memorandvn.dto.MemorandvnDTO;
import top.toptimus.common.enums.event.MemorandvnEnum;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.meta.TokenMetaInfoDTO;

import java.util.List;

/**
 * 查询的model直接显式存在线程池中
 */
@NoArgsConstructor
public class ViewQueryMainTable extends MainTable {
    private String selectSql;
    private String joinSql;

    public ViewQueryMainTable(TokenMetaInfoDTO tokenMetaInfoDTO, String metaId) {
        super(tokenMetaInfoDTO, metaId);
    }

    /**
     * 根据视图查询
     *
     * @param tokenIds token id列表
     * @return sql
     */
    public MainTable buildViewQuery(List<String> tokenIds) {
        this.selectSql = this.buildSelect(this.tableName, this.foreignAttributeSuites, this.tokenMetaInfoDTO);
        this.joinSql = this.buildJoinViewQuery(this.tableName);
        this.strSql = this.selectSql
                + this.buildSelfQuery(tokenIds)
                + this.joinSql;
        logger.info("strSql:" + this.strSql);
        return this;
    }

    /**
     * 根据视图查询
     *
     * @param orgId         任务中心id
     * @param lotNo         lot no
     * @param memorandvnDTO 备查账
     * @return sql
     */
    public MainTable buildViewQuery(String orgId, String lotNo, MemorandvnDTO memorandvnDTO) {
        this.strSql = this.buildSelect(this.tableName, this.foreignAttributeSuites, this.tokenMetaInfoDTO)
                + this.buildSelfQuery(this.tableName, orgId, lotNo, memorandvnDTO)
                + this.buildJoinViewQuery(this.tableName);
        logger.info("strSql:" + this.strSql);
        return this;
    }

    /**
     * 刷新
     *
     * @param tokenIds token id列表
     * @return sql
     */
    public ViewQueryMainTable refreshSQL(List<String> tokenIds) {
        this.strSql = this.selectSql
                + this.buildSelfQuery(tokenIds)
                + this.joinSql;
        logger.info("strSql:" + this.strSql);
        return this;
    }

    /**
     * from ( select id,属性 ，businessId_colunm1，businessId_colunm2 from 主表 where id =
     * token_id_value ) as 主表
     *
     * @return 查询主表的SQL
     */
    private String buildSelfQuery(List<String> tokenIds) {
        return "from (\n" + "select \n" + "* \n" + "from " + this.tableName + " \n" + "where id in ('"
                + String.join("','", tokenIds) + "')\n" + ") as " + this.tableName + "\n";
    }

    /**
     * from ( select id,属性 ，businessId_colunm1，businessId_colunm2 from 主表 where id =
     * token_id_value ) as 主表
     *
     * @return 查询主表的SQL
     */
    private String buildSelfQuery(
            String tableName
            , String orgId
            , String lotNo
            , MemorandvnDTO memorandvnDTO
    ) {
        if (memorandvnDTO.getMemorandvnType().equals(MemorandvnEnum.RESOURCE)) {
            // 非叶节点
            if (!StringUtils.isEmpty(memorandvnDTO.getChildMemorandvnFkey())) {
                return "from (\n" + "select \n" + "* \n" + "from " + tableName + " \n" + "where "
                        + memorandvnDTO.getMemorandvnFkey() + " = '" + orgId + "' AND lotno = '" + lotNo + "' AND "
                        + memorandvnDTO.getChildMemorandvnFkey() + " is null \n" + ") as " + tableName + "\n";
            }
            // 叶节点
            else {
                return "from (\n" + "select \n" + "* \n" + "from " + tableName + " \n" + "where 1=2) as " + tableName
                        + "\n";
            }

        } else {
            return "from (\n" + "select \n" + "* \n" + "from " + tableName + " \n" + "where " + memorandvnDTO.getMemorandvnFkey() + " = " +
                    "'" + orgId + "' AND lotno = '" + lotNo + "'\n" + ") as " + tableName + "\n";
        }
    }
}
