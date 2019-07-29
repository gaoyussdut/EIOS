package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.signGroup.SignGroupDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SignGroupRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据signGroupId查询SignGroupDTO信息
     *
     * @param signGroupId 汇签组id
     * @return 汇签组信息
     */
    @Transactional(readOnly = true)
    public SignGroupDTO findBySignGroupId(String signGroupId) {
        String sql = "SELECT sign_group_id,sign_group_name,role_id,role_name,enabled from t_sign_group WHERE sign_group_id = '"
                + signGroupId + "'";
        return jdbcTemplate.queryForObject(sql, new SignGroupDTORowMapper());
    }

    /**
     * 根据metaId查询SignGroupDTO信息
     *
     * @param metaId 单据metaId
     * @return 汇签组信息
     */
    @Transactional(readOnly = true)
    public SignGroupDTO findBySignMetaId(String metaId) {
        String sql = "SELECT tsp.sign_group_id,tsp.sign_group_name,tsp.role_id,tsp.role_name,tsp.enabled "
                +" FROM r_master_metaid_and_sign_group rms "
                +" LEFT JOIN t_sign_group tsp  ON tsp.sign_group_id = rms.sign_group_id"
                +" WHERE rms.master_meta_id = '"
                + metaId +"' ";
        return jdbcTemplate.queryForObject(sql, new SignGroupDTORowMapper());
    }

}
class SignGroupDTORowMapper implements RowMapper<SignGroupDTO> {

    @Override
    public SignGroupDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new SignGroupDTO(
                rs.getString("sign_group_id")
                , rs.getString("sign_group_name")
                , rs.getString("role_id")
                , rs.getString("role_name")
                , rs.getBoolean("enabled")
        );
    }

}