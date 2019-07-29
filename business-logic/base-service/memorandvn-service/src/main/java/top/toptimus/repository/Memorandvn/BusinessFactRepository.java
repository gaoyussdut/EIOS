package top.toptimus.repository.Memorandvn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.exception.TopSQLException;
import top.toptimus.model.procedure.ProcedureModel;

import java.util.List;

/**
 * 业务事实表存储repo
 */
@Repository
public class BusinessFactRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 根据usertaskId查任务种类id
     *
     * @param userTaskId user task id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String fingTaskCategoryIdByUserTaskId(String userTaskId) {
        String sql = "SELECT task_category from t_user_task WHERE user_task_id = '" + userTaskId + "';";
        try {
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (TopSQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据任务id找凭证类型和调用的存储过程名
     *
     * @param taskCategoryId 任务id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> fingProcedureByTaskCategoryId(String taskCategoryId) {
        String sql = "SELECT stored_procedure from r_task_certificate WHERE task_category = '" + taskCategoryId + "';";
        try {
            return jdbcTemplate.queryForList(sql, String.class);
        } catch (TopSQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用存储过程插入备查账（WBS用）
     *
     * @param procedureModel 调用存储过程model
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ProcedureModel excuteStoredProcedure(ProcedureModel procedureModel) {
        if (procedureModel.isSuccess()) {
            procedureModel.getStoredProcedures().forEach(storedProcedure -> {
                try {
                    String sql = "SELECT " + storedProcedure + "('" + procedureModel.getBillTokenId() + "','" + procedureModel.getLotNo() + "');";
                    jdbcTemplate.execute(sql);
                } catch (TopSQLException e) {
                    e.printStackTrace();
                }
            });
        }
        return procedureModel;
    }

}

