package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.SelfDefiningMetaDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SelfDefiningMetaRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @param userId           用户ID
     * @param memorandvnMetaId 备查帐meta
     * @return selfDefiningMeta
     */
    public SelfDefiningMetaDTO getSelfDefiningMeta(String userId, String memorandvnMetaId) {
        try {
            String sql = "SELECT tusdm.memorandvn_meta_id,tusdm.user_id,tusdm.self_defining_meta_id,tdmi.default_meta_id from t_default_meta_id tdmi" +
                    " LEFT JOIN t_user_self_defining_meta tusdm ON tusdm.memorandvn_meta_id = tdmi.memorandvn_meta_id AND tusdm.user_id = '" + userId + "'" +
                    "  WHERE tdmi.memorandvn_meta_id = '" + memorandvnMetaId + "'";
            return jdbcTemplate.queryForObject(sql, new SelfDefiningMetaDTORowMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyOrderDao失败");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(SelfDefiningMetaDTO selfDefingMetaDTO) {
        String sql = "INSERT INTO t_user_self_defining_meta(memorandvn_meta_id,user_id,self_defining_meta_id)"
                + "VALUES('" + selfDefingMetaDTO.getMemorandvnMetaId() + "','"
                + selfDefingMetaDTO.getUserId() + "','"
                + selfDefingMetaDTO.getSelfDefiningMeta() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存SelfDefingMetaDTO失败");
        }
    }

    class SelfDefiningMetaDTORowMapper implements RowMapper<SelfDefiningMetaDTO> {

        @Override
        public SelfDefiningMetaDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNumber) throws SQLException {
            return new SelfDefiningMetaDTO(
                    rs.getString("memorandvn_meta_id")
                    , rs.getString("user_id")
                    , rs.getString("self_defining_meta_id")
                    , rs.getString("default_meta_id")
            );
        }
    }

}
