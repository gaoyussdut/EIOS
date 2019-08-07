package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.businessUnit.BusinessUnitEdgeDTO;
import top.toptimus.common.enums.BusinessUnitEdgeTypeEnum;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.schema.SchemaDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lzs on 2019/8/7.
 */
@Repository
public class SchemaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 根据id找出schema
     * @param id
     * @return
     */
    public SchemaDTO findSchemaById(String id) {
        String sql = "SELECT ts.id,ts.schema_id,ts.bill_token_id,tsd.bill_meta_id FROM t_schema ts LEFT JOIN t_schema_definition tsd on ts.schema_id = tsd.schema_id " +
                "WHERE ts.id = '" + id + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new SchemaDTOMapper());
        } catch (Exception e) {
            return new SchemaDTO();
        }
    }

    class SchemaDTOMapper implements RowMapper<SchemaDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public SchemaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            SchemaDTO schemaDTO = new SchemaDTO(
                    rs.getString("id")
                    , rs.getString("schema_id")
            );
            schemaDTO.build(rs.getString("bill_meta_id"),rs.getString("bill_token_id"), MetaTypeEnum.BILL);
            return schemaDTO;
        }
    }
}
