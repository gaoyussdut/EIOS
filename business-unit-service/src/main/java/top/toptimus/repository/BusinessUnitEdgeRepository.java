package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.businessUnit.BusinessUnitEdgeDTO;
import top.toptimus.common.enums.BusinessUnitEdgeTypeEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzs on 2019/4/14.
 */
@Repository
public class BusinessUnitEdgeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据下级meta查找流程弧
     *
     * @param businessUnitCode 业务单元id
     * @param toMetaId         下级meta
     * @return BusinessUnitEdgeDTO
     */
    public BusinessUnitEdgeDTO findEdgeByToMetaId(String businessUnitCode, String toMetaId) {
        String sql = "SELECT business_unit_code,edge_id,from_meta_id,to_meta_id,edge_type,rule_id FROM t_business_unit_edge_definition " +
                "WHERE business_unit_code = '" + businessUnitCode + "' AND to_meta_id = '" + toMetaId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new BusinessUnitEdgeDTOMapper());
        } catch (Exception e) {
            return new BusinessUnitEdgeDTO();
        }

    }

    /**
     * 根据上级meta获取业务单元流程弧
     *
     * @param businessUnitCode 业务单元Id
     * @return Result
     */
    public List<BusinessUnitEdgeDTO> findNextTaskMetaByBusinessUnitCodeAndFromMetaId(String businessUnitCode, String fromMetaId) {
        String sql = "SELECT business_unit_code,edge_id,from_meta_id,to_meta_id,edge_type,rule_id FROM t_business_unit_edge_definition " +
                "WHERE business_unit_code = '" + businessUnitCode + "' AND from_meta_id = '" + fromMetaId + "'";
        try {
            return jdbcTemplate.query(sql, new BusinessUnitEdgeDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 根据上下级节点获取弧
     *
     * @param businessUnitCode
     * @param preMetaId
     * @param metaId
     * @param edgeType
     * @return
     */
    public BusinessUnitEdgeDTO findEdgeByFromMetaIdAndToMetaId(String businessUnitCode, String preMetaId, String metaId, String edgeType) {
        String sql = "SELECT business_unit_code,edge_id,from_meta_id,to_meta_id,edge_type,rule_id FROM t_business_unit_edge_definition " +
                "WHERE business_unit_code = '" + businessUnitCode + "' AND from_meta_id = '" + preMetaId + "' " +
                "AND to_meta_id = '" + metaId + "' AND edge_type = '" + edgeType + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new BusinessUnitEdgeDTOMapper());
        } catch (Exception e) {
            return new BusinessUnitEdgeDTO();
        }
    }


    class BusinessUnitEdgeDTOMapper implements RowMapper<BusinessUnitEdgeDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public BusinessUnitEdgeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BusinessUnitEdgeDTO(
                    rs.getString("business_unit_code")
                    , rs.getString("edge_id")
                    , rs.getString("from_meta_id")
                    , rs.getString("to_meta_id")
                    , BusinessUnitEdgeTypeEnum.valueOf(rs.getString("edge_type"))
                    , rs.getString("rule_id")
            );
        }
    }
}
