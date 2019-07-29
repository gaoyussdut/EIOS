package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.businessUnit.HandoverInsDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HandoverInsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存多条fkey到t_token_meta_fkey中
     *
     * @param handoverInsDTOs fkey信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<HandoverInsDTO> handoverInsDTOs) {
        try {
            final List<HandoverInsDTO> handoverInsDTOList = handoverInsDTOs;
            String sql = "insert into t_business_handover_ins(from_org_id,from_business_unit_code,to_org_id,to_business_unit_code," +
                    "certificate_meta_id,certificate_token_id,is_recept,project_id,from_meta_id,from_token_id,to_meta_id,to_token_id)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?)";

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return handoverInsDTOList.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, handoverInsDTOList.get(i).getFromOrgId());
                    ps.setString(2, handoverInsDTOList.get(i).getFromBusinessUnitCode());
                    ps.setString(3, handoverInsDTOList.get(i).getToOrgId());
                    ps.setString(4, handoverInsDTOList.get(i).getToBusinessUnitCode());
                    ps.setString(5, handoverInsDTOList.get(i).getCertificateMetaId());
                    ps.setString(6, handoverInsDTOList.get(i).getCertificateTokenId());
                    ps.setBoolean(7, handoverInsDTOList.get(i).isRecept());
                    ps.setString(8, handoverInsDTOList.get(i).getProjectId());
                    ps.setString(9, handoverInsDTOList.get(i).getFromMetaId());
                    ps.setString(10, handoverInsDTOList.get(i).getFromTokenId());
                    ps.setString(11, handoverInsDTOList.get(i).getToMetaId());
                    ps.setString(11, handoverInsDTOList.get(i).getToTokenId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存HandoverInsDTO失败");
        }

    }
}
