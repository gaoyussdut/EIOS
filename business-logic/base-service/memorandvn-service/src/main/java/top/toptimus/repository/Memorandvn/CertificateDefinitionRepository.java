package top.toptimus.repository.Memorandvn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.businessmodel.memorandvn.dto.BalanceAccountDTO;
import top.toptimus.businessmodel.memorandvn.dto.CertificateDTO;
import top.toptimus.businessmodel.memorandvn.dto.MemorandvnDTO;
import top.toptimus.businessmodel.process.dto.ProcessDTO;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.PaginationHelper;
import top.toptimus.common.enums.process.ProcessEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 凭证定义
 * Created by JiangHao on 2018/10/15.
 */
@SuppressWarnings("ALL")
@Repository
public class CertificateDefinitionRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查看全部备查账信息
     *
     * @return 全部备查账信息
     * @throws DataAccessException DataAccessException
     */
    public List<MemorandvnDTO> getAllMemorandvn() throws DataAccessException {
        String strSQL = "SELECT memorandvn_id,memorandvn_name,memorandvn_meta_id" +
                " FROM t_memorandvn_definition";
        return jdbcTemplate.query(strSQL, new MemorandvnRowMapper());
    }

    /**
     * 根据metaId查询备查账信息
     *
     * @return MemorandvnDTO
     * @throws DataAccessException DataAccessException
     */
    public MemorandvnDTO findMemorandvnByMetaId(String metaId) throws DataAccessException {
        String strSQL = "SELECT memorandvn_id,memorandvn_name,memorandvn_meta_id" +
                " FROM t_memorandvn_definition WHERE memorandvn_meta_id = ?";
        return jdbcTemplate.queryForObject(strSQL, new Object[]{metaId}, new MemorandvnRowMapper());
    }

    /**
     * 根据备查账ID查可以启动的流程信息
     *
     * @param memorandvnId 备查账ID
     * @return 全部备查账信息
     */
    public List<ProcessDTO> getProcessDTOByMemorandvnId(String memorandvnId, ProcessEnum processEnum, String orgId) throws DataAccessException {
        String strSQL;

        if (StringUtils.isEmpty(orgId)) {
            strSQL = "SELECT process_id,process_name " +
                    "FROM r_memorandvn_process WHERE memorandvn_id = '" + memorandvnId + "' AND process_enum = '" + processEnum.toString() + "'";

        } else {
            strSQL = "SELECT mp.process_id,mp.process_name "
                    + " FROM r_memorandvn_process mp"
                    + " LEFT JOIN r_process_orgid po on mp.process_id = po.process_id"
                    + " WHERE mp.memorandvn_id = '" + memorandvnId
                    + "' AND mp.process_enum = '"
                    + processEnum.toString() + "'"
                    + " AND po.org_id = '" + orgId + "'";

        }

        return jdbcTemplate.query(strSQL, new ProcessDTORowMapper());

        //return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{}, // 参数
        //      pageNo, pageSize, new MemorandvnRowMapper());
    }

    /**
     *
     */
    public String getProcessDTOByMemorandvnId(String processId) throws DataAccessException {
        String strSQL = "SELECT task_meta_id " +
                " FROM r_memorandvn_process WHERE process_id = '" + processId + "'";
        return jdbcTemplate.queryForList(strSQL, String.class).get(0);

    }

    /**
     * 根据备查帐meta查找备查帐DTO
     *
     * @param memorandvnMetaIds
     * @return MemorandvnDTO
     */
    public List<MemorandvnDTO> getMemorandvnDTOByMemorandvnMetaId(List<String> memorandvnMetaIds) {
        try {
            String sql = "SELECT memorandvn_id,memorandvn_meta_id,memorandvn_name \n" + "FROM r_baseline_memorandvn WHERE baseline_id IN ('" + String.join("','", memorandvnMetaIds) + "') ";
            return jdbcTemplate.query(sql, new MemorandvnRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    class CertificateRowMapper implements RowMapper<CertificateDTO> {
        @Override
        public CertificateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CertificateDTO(
                    rs.getString("certificate_id")
                    , rs.getString("certificate_name")
                    , rs.getString("certificate_meta_id")
            );
        }
    }

    class BalanceAccountRowMapper implements RowMapper<BalanceAccountDTO> {
        @Override
        public BalanceAccountDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BalanceAccountDTO(
                    rs.getString("balance_account_id")
                    , rs.getString("balance_account_name")
                    , rs.getString("balance_account_meta_id")
            );
        }
    }

    class MemorandvnRowMapper implements RowMapper<MemorandvnDTO> {
        @Override
        public MemorandvnDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemorandvnDTO().buildMemorandvnTable(rs);
        }
    }

    class ProcessDTORowMapper implements RowMapper<ProcessDTO> {
        @Override
        public ProcessDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ProcessDTO(
                    rs.getString("process_id")
                    , rs.getString("process_name")

            );
        }
    }


}
