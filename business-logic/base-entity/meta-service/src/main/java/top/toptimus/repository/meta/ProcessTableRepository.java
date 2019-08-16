package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.TokenMetaInformationDto;

@Repository
public class ProcessTableRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(String  tableName,String metaId) {
        String sql = "INSERT INTO t_process_table(table_name,meta_id)"
                + "VALUES ('" + tableName + "','"
                + metaId + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存t_process_table失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String metaId) {
        String sql = "DELETE FROM t_process_table WHERE meta_id = '" + metaId + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除t_process_table失败");
        }
    }
}
