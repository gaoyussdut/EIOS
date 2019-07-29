package top.toptimus.repository.meta;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.FKeyOrderDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * meta的fkey保存在t_token_meta_fkey中
 * 对t_token_meta_fkey表的保存、查询
 *
 * @author JiangHao
 * @since 2018/5/22.
 */
@Repository
public class FKeyOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 保存多条fkey到t_token_meta_fkey_order中
     *
     * @param fKeyDaos fkey信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<FKeyOrderDto> fKeyDaos) {
        try {
            final List<FKeyOrderDto> fKeyOrderDtoList = fKeyDaos;
            String sql = "insert into t_token_meta_fkey_order(meta_id,key,order_)" +
                    " values(?,?,?)";

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return fKeyOrderDtoList.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, fKeyOrderDtoList.get(i).getMetaId());
                    ps.setString(2, fKeyOrderDtoList.get(i).getKey());
                    if (StringUtils.isNotEmpty(fKeyOrderDtoList.get(i).getOrder_())) {
                        ps.setInt(3, Integer.valueOf(fKeyOrderDtoList.get(i).getOrder_()));
                    } else {
                        ps.setInt(3, 0);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyOrderDao失败");
        }

    }

//    /**
//     * 根据meta id查询fkey信息
//     *
//     * @param metaId meta id
//     * @return fkey信息
//     */
//    @Transactional(readOnly = true)
//    public List<FKeyOrderDao> findByMetaId(String metaId) {
//        String sql = "SELECT id,meta_id,key,order_ from t_token_meta_fkey_order WHERE meta_id = '"
//                + metaId + "'  order by id";
//        return jdbcTemplate.query(sql, new FKeyOrderDaoRowMapper());
//    }

    /**
     * 根据metaId删除数据
     *
     * @param metaId meta id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByMetaId(String metaId) {
        String sql = "delete from t_token_meta_fkey_order where meta_id=?";
        jdbcTemplate.update(sql, metaId);
    }
}
