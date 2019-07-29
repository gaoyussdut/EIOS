package top.toptimus.repository.token.MetaRelation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.dao.token.MetaTokenRelationDao;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.place.BillTokenSaveResultDTO;
import top.toptimus.relation.BillAndEntryMetaDTO;
import top.toptimus.repository.meta.BillEntryMetaRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class TokenRelationRopository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存token关系
     */
    public void saveMetaTokenRelation(MetaTokenRelationDao metaTokenRelationDao) {
        String sql = "INSERT INTO r_bill_entry_token(id, bill_meta_id, bill_token_id, entry_meta_id, entry_token_id)"
                + " values ('" + metaTokenRelationDao.getId() + "','"
                + metaTokenRelationDao.getBillMetaId() + "','"
                + metaTokenRelationDao.getBillTokenId() + "','"
                + metaTokenRelationDao.getEntryMetaId() + "','"
                + metaTokenRelationDao.getEntryTokenId() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("保存关系失败！");
        }

    }

    /**
     * 新建分录时保存表头和分录的关系
     */
    public void saveMetaTokenRelation(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        String sql = "INSERT INTO r_bill_entry_token(id, bill_meta_id, bill_token_id, entry_meta_id, entry_token_id)"
                + " values ('" + UUID.randomUUID().toString() + "','"
                + billTokenSaveResultDTO.getBillTokenResultBody().getBillMetaId() + "','"
                + billTokenSaveResultDTO.getBillTokenResultBody().getBillTokenId() + "','"
                + billTokenSaveResultDTO.getBillTokenResultBody().getEntryMetaId() + "','"
                + billTokenSaveResultDTO.getBillTokenResultBody().getEntryTokenData().getTokenId() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("保存关系失败！");
        }

    }


    /**
     * 删除token关系
     */
    public void deleteMetaTokenRelation(MetaTokenRelationDao metaTokenRelationDao) {
        String sql = "DELETE FROM r_bill_entry_token"
                + " WHERE id = '" + metaTokenRelationDao.getId() + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("删除关系失败！");
        }

    }

    /**
     * 保存分录时删除旧关系
     */
    public void deleteMetaTokenRelation(BillTokenSaveResultDTO billTokenSaveResultDTO) {
        String sql = "DELETE FROM r_bill_entry_token"
                + " WHERE entry_token_id = '"
                + billTokenSaveResultDTO.getBillTokenResultBody().getEntryTokenData().getTokenId() + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("删除关系失败！");
        }

    }

    /**
     * 根据表头meta获取分录token
     *
     * @param billMetaId 表头meta
     * @return List<TokenMetaInformationDto> 分录metaId
     */
    @Transactional(readOnly = true)
    public List<BillAndEntryMetaDTO> findAllByBillTokenIdAndBillMetaIdAndEntryMetaId(String billTokenId, String billMetaId, String entryMetaId) {
        String sql = "SELECT bill_meta_id,entry_meta_id,bill_token_id,entry_token_id from r_bill_entry_token"
                + " WHERE bill_meta_id = '" + billMetaId + "' AND bill_token_id = '" + billTokenId + "' AND entry_meta_id = '" + entryMetaId + "'";
        return jdbcTemplate.query(sql, new MetaTokenRelationDaoMapper());
    }

    class MetaTokenRelationDaoMapper implements RowMapper<BillAndEntryMetaDTO> {
        @Override
        public BillAndEntryMetaDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
            return new BillAndEntryMetaDTO(
                    rs.getString("bill_meta_id")
                    , rs.getString("bill_token_id")
                    , rs.getString("entry_meta_id")
                    , rs.getString("entry_token_id")
            );
        }
    }
}
