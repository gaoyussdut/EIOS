package top.toptimus.repository.Memorandvn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemorandvnRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 资源类备查帐接收
     *
     * @param memorandvnMetaId 备查账meta id
     * @param lotNo            批号
     * @param tokenIds         token id列表
     */
    public void updateResourceMemorandvn(String memorandvnMetaId, String lotNo, List<String> tokenIds) {
        String sql = "UPDATE " + memorandvnMetaId
                + " SET lotno = '" + lotNo
                + "' WHERE id in (" + "'" + String.join("','", tokenIds) + "'" + ");";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存备查账失败");
        }
    }
}