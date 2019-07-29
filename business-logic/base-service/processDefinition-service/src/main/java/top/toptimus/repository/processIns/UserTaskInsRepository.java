package top.toptimus.repository.processIns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.PaginationHelper;
import top.toptimus.common.enums.process.UserTaskStatusEnum;
import top.toptimus.processDefinition.UserTaskInsDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("NullableProblems")
@Repository
public class UserTaskInsRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<UserTaskInsDTO> userTaskInsDaos) {
        try {
            String sql = "insert into t_user_task_ins(process_id,user_task_id,meta_id,token_id,status) "
                    + " values(?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return userTaskInsDaos.size();
                }

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, userTaskInsDaos.get(i).getProcessId());
                    ps.setString(2, userTaskInsDaos.get(i).getUserTaskId());
                    ps.setString(3, userTaskInsDaos.get(i).getMetaId());
                    ps.setString(4, userTaskInsDaos.get(i).getTokenId());
                    ps.setString(5, userTaskInsDaos.get(i).getStatus());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存UserTaskInsDao失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateStatus(UserTaskInsDTO dao) {
        String sqlStr = "update t_user_task_ins set  status=? " +
                " WHERE token_id=? AND user_task_id=? ";
        try {
            jdbcTemplate.update(sqlStr, dao.getStatus(), dao.getTokenId(), dao.getUserTaskId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新UserTaskInsDao失败！");
        }
    }

    @Transactional(readOnly = true)
    public UserTaskInsDTO findByUserTaskIdAndTokenId(String userTaskId, String tokenId) {
        String sql = " SELECT id,process_id,user_task_id,meta_id,token_id,status FROM t_user_task_ins" +
                " WHERE user_task_id=? AND token_id=? ";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userTaskId, tokenId}, new UserTaskInsDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


//    /**
//     * 通过TTId查找UserTaskIns
//     *
//     * @param tokenTemplateId
//     * @param userTaskStatusEnum
//     * @return
//     */
//    @Transactional(readOnly = true)
//    public List<UserTaskInsDao> findByTTId(String tokenTemplateId ,UserTaskStatusEnum userTaskStatusEnum) {
//        try {
//            String sql = "SELECT tuti.id,tuti.process_id,tuti.user_task_id,tuti.token_template_id,tuti.token_id,tuti.status,tpi.process_ins_name FROM t_user_task_ins tuti" +
//                    " LEFT JOIN t_process_ins tpi ON tpi.process_id = tuti.process_id AND tpi.token_id = tuti.token_id"+
//                    " WHERE tuti.token_template_id  = ? AND tuti.status= ? ";
//            return jdbcTemplate.query(sql, new Object[]{tokenTemplateId, userTaskStatusEnum.name()}, new UserTaskInsDaoRowMapper());
//        } catch (EmptyResultDataAccessException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 通过userTaskId查找UserTaskIns
     *
     * @param userTaskId         user task id
     * @param userTaskStatusEnum 节点状态枚举
     * @param pageNo             页码
     * @param pageSize           页宽
     * @return 节点实例
     */
    @Transactional(readOnly = true)
    public CurrentPage<UserTaskInsDTO> findByUserTaskId(String userTaskId, UserTaskStatusEnum userTaskStatusEnum, int pageNo, int pageSize) {
        try {
            //TODO
            String strSQL = "SELECT DISTINCT tuti.id,tuti.process_id,tuti.user_task_id,tuti.meta_id,tuti.token_id,tuti.status,tpi.process_ins_name,tut.user_task_name,rtm.token_template_name FROM t_user_task_ins tuti";
            String countSql = " SELECT Count(DISTINCT tuti.id) FROM t_user_task_ins tuti";
            String sqlCommon = " LEFT JOIN t_process_ins tpi ON tpi.process_id = tuti.process_id AND tpi.token_id = tuti.token_id" +
                    " LEFT JOIN t_user_task tut ON  tut.user_task_id =  tuti.user_task_id" +
                    " LEFT JOIN r_tokentemplate_meta rtm ON tut.token_template_id = rtm.token_template_id" +
                    " WHERE tuti.user_task_id  = ? AND tuti.status= ?";
            PaginationHelper<UserTaskInsDTO> ph = new PaginationHelper<>();
            return ph.fetchPage(jdbcTemplate, countSql + sqlCommon, strSQL + sqlCommon, new Object[]{userTaskId, userTaskStatusEnum.name()}, // 参数
                    pageNo, pageSize, new UserTaskInsRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据表头tokenId和状态查询节点id
     *
     * @param billTokenId        表头tokenId
     * @param userTaskStatusEnum 节点状态
     * @return 节点id
     */
    public List<UserTaskInsDTO> findByBillTokenIdAndStatus(String billTokenId, UserTaskStatusEnum userTaskStatusEnum) {
        String sql = "SELECT id,process_id,user_task_id,meta_id,token_id,status " +
                "FROM t_user_task_ins WHERE token_id = ? AND status = ?";
        return jdbcTemplate.query(sql
                , new Object[]{billTokenId, userTaskStatusEnum.name()}, new UserTaskInsDtoRowMapper());
    }


    /**
     * 根据表头tokenId和状态查询节点id
     *
     * @param billTokenId 表头tokenId
     * @return 节点id
     */
    public List<UserTaskInsDTO> findAllByBillTokenId(String billTokenId) {
        String sql = "SELECT id,process_id,user_task_id,meta_id,token_id,status " +
                "FROM t_user_task_ins WHERE token_id = ?";
        return jdbcTemplate.query(sql
                , new Object[]{billTokenId}, new UserTaskInsDtoRowMapper());
    }

    /**
     * 根据userTaskId和roleId 查看tokenIds
     *
     * @param userTaskId
     * @param roleId
     * @return
     */
    public List<String> findUserTaskTokenId(String userTaskId ,String userTaskStatus, String roleId) {
        String sql  =" SELECT tuti.token_id FROM t_user_task_ins tuti LEFT JOIN t_user_task tut ON tuti.user_task_id = tut.user_task_id   WHERE tut.role_id = ? AND status= ?  AND tut.user_task_id = ? " ;
        return jdbcTemplate.queryForList(sql
                , new Object[]{roleId, userTaskStatus,userTaskId}, String.class);
    }

}


@SuppressWarnings("NullableProblems")
class UserTaskInsRowMapper implements RowMapper<UserTaskInsDTO> {
    @Override
    public UserTaskInsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserTaskInsDTO(
                rs.getString("id")
                , rs.getString("process_id")
                , rs.getString("user_task_id")
                , rs.getString("meta_id")
                , rs.getString("token_id")
                , rs.getString("status")
                , rs.getString("process_ins_name")
                , rs.getString("token_template_name")
                , rs.getString("user_task_name")
        );
    }
}

@SuppressWarnings("NullableProblems")
class UserTaskInsDtoRowMapper implements RowMapper<UserTaskInsDTO> {
    @Override
    public UserTaskInsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserTaskInsDTO(
                rs.getString("id")
                , rs.getString("process_id")
                , rs.getString("user_task_id")
                , rs.getString("meta_id")
                , rs.getString("token_id")
                , rs.getString("status")
        );
    }
}
