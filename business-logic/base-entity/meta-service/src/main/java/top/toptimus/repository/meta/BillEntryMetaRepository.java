package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.meta.MetaTablesRelation.MetaTablesRelationDTO;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.meta.relation.MetaRelDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BillEntryMetaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据表头meta获取分录meta
     *
     * @param billMetaId 表头meta
     * @return List<TokenMetaInformationDto> 分录metaId
     */
    @Transactional(readOnly = true)
    public List<TokenMetaInformationDto> findTokenMetaInfoByBillMetaId(String billMetaId) {
        String sql = "SELECT rbem.entry_meta_id,ttmf.token_meta_name,ttmf.meta_type,ttmf.meta_data_type from r_bill_entry_meta rbem"
                + " LEFT JOIN t_token_meta_formation ttmf ON rbem.entry_meta_id = ttmf.token_meta_id"
                + " WHERE rbem.bill_meta_id = '"
                + billMetaId + "' ";
        return jdbcTemplate.query(sql, new TokenMetaInforMationDtoRowMapper());
    }

    @Transactional(readOnly = true)
    public List<MetaRelDTO> getRelMetasByBillMeta(String billMetaId) {
        String sql = "SELECT rbem.token_template_id,rbem.bill_meta_id,rbem.entry_meta_id,rbem.order_,rbem.entry_type,ttd.token_template_id as rel_ttid"
                + " FROM r_bill_entry_meta rbem"
                + " LEFT JOIN t_tokentemplate_definition ttd ON ttd.bill_meta_id = rbem.entry_meta_id"
                + " WHERE bill_meta_id = '"
                + billMetaId + "' ";
        return jdbcTemplate.query(sql, new MetaRelDTORowMapper());
    }

    class TokenMetaInforMationDtoRowMapper implements RowMapper<TokenMetaInformationDto> {
        @Override
        public TokenMetaInformationDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
            return new TokenMetaInformationDto(
                    rs.getString("entry_meta_id")
                    , rs.getString("token_meta_name")
                    , rs.getString("meta_type")
            );
        }
    }

    class MetaRelDTORowMapper implements RowMapper<MetaRelDTO> {
        @Override
        public MetaRelDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
            return new MetaRelDTO(
                    rs.getString("bill_meta_id")
                    , rs.getString("entry_meta_id")
                    , MetaTypeEnum.valueOf(rs.getString("entry_type"))
                    , rs.getInt("order_")
                    , rs.getString("token_template_id")
                    , rs.getString("rel_ttid")
            );
        }
    }
}
