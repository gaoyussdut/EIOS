package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.enums.StockTypeEnum;
import top.toptimus.stock.StockDetailDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 库存repo
 */
@Repository
public class StockDetailRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<StockDetailDTO> stockDetailDTOS) {
        try {
            final List<StockDetailDTO> stockDetailDTOList = stockDetailDTOS;
            String sql = "insert into storage_detail(id,materiel_id,amount,type,tokenid,created_at,created_user,storage)" +
                    " values(?,?,?,?,?,?,?,?)  ";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return stockDetailDTOList.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i)
                        throws SQLException {
                    ps.setString(1, stockDetailDTOList.get(i).getId());
                    ps.setString(2, stockDetailDTOList.get(i).getMaterielId());
                    ps.setBigDecimal(3, stockDetailDTOList.get(i).getAmount());
                    ps.setString(4, stockDetailDTOList.get(i).getType().name());
                    ps.setString(5, stockDetailDTOList.get(i).getTokenId());
                    ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    ps.setString(7, stockDetailDTOList.get(i).getCreateUser());
                    ps.setString(8, stockDetailDTOList.get(i).getStorage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存StockDetailDTO失败!");
        }
    }

    /**
     * 更新库存如出库类型
     *
     * @param ids     数据Id
     * @param userId  用户Id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateType( List<String> ids ,String userId ) {
        try {
            String tokenIds = "'" + String.join("','", ids) + "'";
            String sqlStr = "update storage_detail set type='OUT' , update_user = ? , update_at = ?" +
                    " WHERE id IN (" + tokenIds + ")";
            jdbcTemplate.update(sqlStr, userId ,new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新StockDetailDTO失败！");
        }
    }

    /**
     * 获取库存明细帐
     *
     * @param stockTypeEnum 出入库类型
     * @return  库存明细帐
     */
    public List<StockDetailDTO> findStockDetailByType(StockTypeEnum stockTypeEnum ) {
        String sql = "SELECT sd.id,sd.materiel_id,sd.amount,sd.type,sd.tokenid,sd.created_at,cu.name AS created_user,sd.update_at,uu.name AS update_user,sd.storage from storage_detail sd" +
                " LEFT JOIN users cu ON sd.created_user = cu.id " +
                " LEFT JOIN users uu ON sd.update_user = uu.id " +
                " WHERE type = ?";
        return jdbcTemplate.query(sql, new Object[]{stockTypeEnum.name()} , new StockDetailRowMapper());
    }

    /**
     * 获取指定tokenIds的库存明细帐
     *
     * @param ids            数据tokenIds
     * @param stockTypeEnum  出入库类型
     * @return List<StockDetailDTO>
     */
    public List<StockDetailDTO> findStockDetailByContract(List<String> ids ,StockTypeEnum stockTypeEnum ) {
        String tokenIds = "'" + String.join("','", ids) + "'";
        String sql = "SELECT sd.id,sd.materiel_id,sd.amount,sd.type,sd.tokenid,sd.created_at,cu.name AS created_user,sd.update_at,uu.name AS update_user,sd.storage, " +
                " m.biaodibianma , m.biaodimingcheng,m.jiliangdanwei ,m.shuilv " +
                " FROM storage_detail sd" +
                " LEFT JOIN users cu ON sd.created_user = cu.id " +
                " LEFT JOIN users uu ON sd.update_user = uu.id " +
                " LEFT JOIN materiel m ON m.id = sd.materiel_id  " +
                " WHERE type = ? AND sd.tokenid IN (" + tokenIds + ")";
        return jdbcTemplate.query(sql, new Object[]{stockTypeEnum.name()} , new StockDetailDTOMapper());
    }

}
class StockDetailRowMapper implements RowMapper<StockDetailDTO> {
    @Override
    public StockDetailDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new StockDetailDTO (
                rs.getString("id")
                , rs.getString("materiel_id")
                , rs.getBigDecimal("amount")
                , StockTypeEnum.valueOf(rs.getString("type"))
                , rs.getString("tokenid")
                , rs.getTimestamp("created_at")
                , rs.getString("created_user")
                , rs.getTimestamp("update_at")
                , rs.getString("update_user")
                , rs.getString("storage")
        );
    }
}

class StockDetailDTOMapper implements RowMapper<StockDetailDTO> {
    @Override
    public StockDetailDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new StockDetailDTO (
                rs.getString("id")
                , rs.getString("materiel_id")
                , rs.getBigDecimal("amount")
                , StockTypeEnum.valueOf(rs.getString("type"))
                , rs.getString("tokenid")
                , rs.getTimestamp("created_at")
                , rs.getString("created_user")
                , rs.getTimestamp("update_at")
                , rs.getString("update_user")
                , rs.getString("storage")
                , rs.getString("biaodibianma")
                , rs.getString("biaodimingcheng")
                , rs.getString("jiliangdanwei")
                , rs.getString("shuilv")
        );
    }
}