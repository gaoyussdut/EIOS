package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.filter.FilterDTO;
import top.toptimus.target.TargetDataDTO;
import top.toptimus.target.TargetPageableDTO;
import top.toptimus.target.TargetTypeDTO;
import top.toptimus.target.TargetTypePageableDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzs on 2019/2/19.
 */
@Repository
public class TargetRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TargetPageableDTO getTargetGeneralView(Integer pageSize, Integer pageNo, List<FilterDTO> filterCondition) {

        TargetPageableDTO targetPageableDTO = new TargetPageableDTO(pageSize, pageNo);
        String sql = "SELECT target_data_id,target_data_table,target_type_id,is_split,zhongzhou,hengzhou,status," +
                "create_time,target_unit_id,target_bill,target_bill_key,task_cycle,year FROM t_target";
        String querySql = targetPageableDTO.buildQuerySql(filterCondition);
        sql += querySql;
        sql += "LIMIT " + pageSize + " OFFSET " + (pageNo - 1) * pageSize + "";
        String countSql = "SELECT COUNT(target_data_id) FROM t_target ";
        countSql += querySql;
        try {
            targetPageableDTO.build(jdbcTemplate.queryForObject(sql, Integer.class));
        }catch (Exception e){
            targetPageableDTO.build(0);
        }
        try {
            targetPageableDTO.build(jdbcTemplate.query(sql, new TargetDataDTOMapper()));
        }catch (Exception e){
            targetPageableDTO.build(new ArrayList<>());
        }
        return targetPageableDTO;
    }

    public TargetDataDTO getTargetDetail(String targetDataId) {
        try {
            String sql = "SELECT target_data_id,target_data_table,target_type_id,is_split,zhongzhou,hengzhou,status" +
                    ",create_time,target_unit_id,target_bill,target_bill_key,task_cycle,year FROM t_target " +
                    "WHERE target_data_id = '" + targetDataId + "'";
            return jdbcTemplate.queryForObject(sql, new TargetDataDTOMapper());
        } catch (Exception e) {
            return new TargetDataDTO();
        }
    }

    public void saveTarget(TargetDataDTO targetDataDTO) {
        String sql = "INSERT INTO t_target (target_data_id,target_data_table," +
                "target_type_id,is_split,zhongzhou,hengzhou,status,create_time," +
                "target_unit_id,target_bill,target_bill_key,task_cycle,year) " +
                "VALUES(" +
                "'" + targetDataDTO.getTargetDataId()
                + "','" + targetDataDTO.getTargetDataTableName()
                + "','" + targetDataDTO.getTargetTypeId()
                + "'," + targetDataDTO.isSplit()
                + "," + targetDataDTO.getChaijiejici()
                + ",'" + targetDataDTO.getRiqikeli()
                + "','" + targetDataDTO.getStatus()
                + "','" + targetDataDTO.getCreateTime()
                + "','" + targetDataDTO.getTargetUnitId()
                + "','" + targetDataDTO.getTargetBill()
                + "','" + targetDataDTO.getTargetBillKey()
                + "','" + targetDataDTO.getTaskCycle()
                + "','" + targetDataDTO.getYear() + "')";
        jdbcTemplate.execute(sql);
    }

    public void updateTarget(TargetDataDTO targetDataDTO) {
        String sql = "UPDATE t_target" +
                " SET target_data_id = '" + targetDataDTO.getTargetDataId() + "'," +
                " target_data_table = '" + targetDataDTO.getTargetDataTableName() + "'," +
                " target_type_id = '" + targetDataDTO.getTargetTypeId() + "'," +
                " is_split = " + targetDataDTO.isSplit() + "," +
                " zhongzhou = " + targetDataDTO.getChaijiejici() + "," +
                " hengzhou = '" + targetDataDTO.getRiqikeli() + "'," +
                " status = '" + targetDataDTO.getStatus() + "'," +
                " create_time = '" + targetDataDTO.getCreateTime() + "'," +
                " target_unit_id = '" + targetDataDTO.getTargetUnitId() + "'," +
                " target_bill = '" + targetDataDTO.getTargetBill() + "'," +
                " target_bill_key = '" + targetDataDTO.getTargetBillKey() + "'," +
                " task_cycle = '" + targetDataDTO.getTaskCycle() + "'," +
                " year = '" + targetDataDTO.getYear() + "'" +
                " WHERE target_data_id = '" + targetDataDTO.getTargetDataId() + "'";
        jdbcTemplate.execute(sql);
    }

    public void deleteTarget(String targetDataId) {
        String sql = "DELETE FROM t_target WHERE target_data_id = '" + targetDataId + "'";
        jdbcTemplate.execute(sql);
    }

    public List<TargetDataDTO> getTargetByTargetTypeId(String targetTypeId) {
        try {
            String sql = "SELECT target_data_id,target_data_table,target_type_id,is_split,zhongzhou,hengzhou,status" +
                    ",create_time,target_unit_id,target_bill,target_bill_key,task_cycle,year FROM t_target " +
                    "WHERE target_type_id = '" + targetTypeId + "'";
            return jdbcTemplate.query(sql, new TargetDataDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    class TargetDataDTOMapper implements RowMapper<TargetDataDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public TargetDataDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TargetDataDTO(
                    rs.getString("target_data_id")
                    , rs.getString("target_data_table")
                    , rs.getString("target_type_id")
                    , rs.getBoolean("is_split")
                    , rs.getInt("zhongzhou")
                    , rs.getString("hengzhou")
                    , rs.getString("status")
                    , rs.getDate("create_time")
                    , rs.getString("target_unit_id")
                    , rs.getString("target_bill")
                    , rs.getString("target_bill_key")
                    , rs.getString("task_cycle")
                    , rs.getString("year")
            );
        }
    }
}
