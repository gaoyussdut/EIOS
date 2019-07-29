package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.PaginationHelper;
import top.toptimus.formula.FormulaDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by JiangHao on 2018/9/7.
 */
@Repository
public class FormulaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据业务纬度id 取公式
     *
     * @param businessAspectId 业务纬度id
     * @return 公式
     */
    public List<FormulaDTO> findByBusinessAspectId(String businessAspectId) {
        String sql = "SELECT id,business_aspect_id,business_aspect_name,formula,key_list,value_key" +
                " FROM t_formula_formation WHERE business_aspect_id = ?";
        return jdbcTemplate.query(sql, new Object[]{businessAspectId}, new FormulaDTORowMapper());
    }

    /**
     * 查看全部公式
     *
     * @param pageNo   当前页
     * @param pageSize 每页的大小
     * @return 全部公式 分页
     */
    public CurrentPage<FormulaDTO> findAll(Integer pageNo, Integer pageSize) {
        PaginationHelper<FormulaDTO> ph = new PaginationHelper<>();
        String strSQL = "SELECT id,business_aspect_id,business_aspect_name,formula,key_list,value_key FROM t_formula_formation";
        String countSql = "select COUNT(1) from t_formula_formation ";
        return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{}, // 参数
                pageNo, pageSize, new FormulaDTORowMapper());
    }


    /**
     * 保存全部业务纬度下的公式
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<FormulaDTO> formulaDTOS) {
        String sql = "INSERT INTO t_formula_formation(business_aspect_id,business_aspect_name,formula,key_list,value_key)" +
                " VALUES (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public int getBatchSize() {
                        return formulaDTOS.size();
                    }

                    @SuppressWarnings("NullableProblems")
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, formulaDTOS.get(i).getBusinessAspectId());
                        ps.setString(2, formulaDTOS.get(i).getBusinessAspectName());
                        ps.setString(3, formulaDTOS.get(i).getFormula());
                        ps.setString(4, formulaDTOS.get(i).getKeyList());
                        ps.setString(5, formulaDTOS.get(i).getValueKey());
                    }
                }
        );
    }

    /**
     * 根据业务纬度id  删除关联的所有公式
     *
     * @param businessAspectId 业务纬度id
     */
    public void delectByBusinessAspectId(String businessAspectId) {
        String sql = "DELETE FROM t_formula_formation WHERE business_aspect_id = ?";
        jdbcTemplate.update(sql, businessAspectId);
    }

    /**
     * 根据主键删除公式
     *
     * @param id 主键
     */
    public void delectById(Integer id) {
        String sql = "DELETE FROM t_formula_formation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    @SuppressWarnings("NullableProblems")
    class FormulaDTORowMapper implements RowMapper<FormulaDTO> {
        @Override
        public FormulaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FormulaDTO(
                    rs.getInt("id")
                    , rs.getString("business_aspect_id")
                    , rs.getString("business_aspect_name")
                    , rs.getString("formula")
                    , rs.getString("key_list")
                    , rs.getString("value_key")
            );
        }
    }


}


