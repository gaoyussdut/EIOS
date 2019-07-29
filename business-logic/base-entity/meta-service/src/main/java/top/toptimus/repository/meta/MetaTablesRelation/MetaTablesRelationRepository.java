package top.toptimus.repository.meta.MetaTablesRelation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.MetaTablesRelation.MetaTablesRelationDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 维护meta id和数据库表对应的关系
 *
 * @author gaoyu
 * @since 2018-07-03
 */
@Repository
public class MetaTablesRelationRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

//    /**
//     * 根据processId查询meta id和数据库表对应的关系
//     *
//     * @param processId 流程id
//     * @return meta id和数据库表对应的关系
//     */
//    @Transactional(readOnly = true)
//    public List<MetaTablesRelationDao> findProcessTablesDaosByProcessId(String processId) {
//        return jdbcTemplate.query(
//                "select id,process_id,table_name,vertex_id,meta_id from t_process_table where process_id=?",
//                new Object[]{processId}, new MetaTablesRelationDaooRowMapper());
//    }

    /**
     * 保存meta id和数据库表对应的关系
     *
     * @param processTablesDaoList meta id和数据库表对应的关系
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<MetaTablesRelationDTO> processTablesDaoList) {

        String sql = "insert into t_process_table(process_id,table_name,vertex_id,meta_id)" + " values(?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return processTablesDaoList.size();
            }

            @Override
            public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, processTablesDaoList.get(i).getProcessId());
                ps.setString(2, processTablesDaoList.get(i).getTableName());
                ps.setString(2, processTablesDaoList.get(i).getVertexId());
                ps.setString(2, processTablesDaoList.get(i).getMetaId());
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(MetaTablesRelationDTO metaTablesRelationDao) {
        String sql = "INSERT INTO t_process_table(table_name,meta_id) " +
                "VALUES ('" + metaTablesRelationDao.getTableName() + "','"
                + metaTablesRelationDao.getMetaId() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyDao失败");
        }

    }

    /**
     * 根据tokenMetaId查询processTablesDaos
     *
     * @param tokenMetaId token meta id
     * @return meta id和数据库表对应的关系
     */
    @Transactional(readOnly = true)
    public List<MetaTablesRelationDTO> getProcessTablesDaoByMetaId(String tokenMetaId) {
        return jdbcTemplate.query(
                "select id,process_id,table_name,vertex_id,meta_id from t_process_table where meta_id=?",
                new Object[]{tokenMetaId}, new MetaTablesRelationDaooRowMapper());
    }

    /**
     * 根据tokenMetaId查询processTablesDaos
     *
     * @param tokenMetaIds token meta id
     * @return meta id和数据库表对应的关系
     */
    @Transactional(readOnly = true)
    public List<MetaTablesRelationDTO> getProcessTablesDaoByMetaId(List<String> tokenMetaIds) {
        String tokenMetaIdList = "'" + String.join("','", tokenMetaIds) + "'";
        return jdbcTemplate.query(
                "select id,process_id,table_name,vertex_id,meta_id from t_process_table where meta_id in (" + tokenMetaIdList + ")",
                new MetaTablesRelationDaooRowMapper());
    }


}

class MetaTablesRelationDaooRowMapper implements RowMapper<MetaTablesRelationDTO> {
    @Override
    public MetaTablesRelationDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new MetaTablesRelationDTO(
                rs.getInt("id")
                , rs.getString("process_id")
                , rs.getString("table_name")
                , rs.getString("vertex_id")
                , rs.getString("meta_id")
        );
    }
}
