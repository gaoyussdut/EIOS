package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.constantConfig.Constants;
import top.toptimus.meta.signGroup.CountersignStatusUpdateDTO;
import top.toptimus.task.TaskDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CountersignRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    /**
//     * 根据单据的metaId和tokenId查询汇签的tokenId
//     *
//     * @param metaId    单据metaid
//     * @param tokenId   单据tokenId
//     * @return String   汇签tokenid
//     */
//    @Transactional(readOnly = true)
//    public String findByMetaIdAndTokenId(String metaId, String tokenId) {
//        String sql = "SELECT DISTINCT countersign_token_id from r_bill_token_and_countersign_token WHERE token_id = '"
//                + tokenId + "',"
//                + " AND meta_id = '" + metaId +"'";
//        return jdbcTemplate.queryForObject(sql, String.class);
//    }
//
//    /**
//     * 保存单据token和汇签token之间的关系
//     *
//     * @param metaId                单据metaiId
//     * @param tokenId               单据tokenId
//     * @param countersignTokenId    汇签tokenId
//     */
//    @Transactional(readOnly = true)
//    public void saveRel(String metaId, String tokenId, String countersignTokenId) {
//        String sql = "INSERT INTO  r_bill_token_and_countersign_token(meta_id,token_id,countersign_token_id) "
//        +" VALUES('"+metaId+"','"+tokenId+"','"+countersignTokenId+"')";
//        jdbcTemplate.execute(sql);
//    }

    /**
     *根据单据的metaId和tokenId查询汇签的tokenId
     *
     * @param metaId    单据metaid
     * @param tokenId   单据tokenId
     * @return String   汇签tokenid
     */
    @Transactional(readOnly = true)
    public List<String> findByMetaIdAndTokenId(String metaId, String tokenId) {
        String sql = "SELECT  countersign_token_id from r_bill_token_and_countersign_task  btct "
                + " LEFT JOIN r_countersign_task_and_countersign_token ctct "
                + " ON btct.countersign_task_token_id = ctct.countersign_task_token_id "
                + " WHERE btct.token_id = '"
                + tokenId + "' "
                + " AND btct.meta_id = '" + metaId +"'";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * 根据单据的metaId和tokenId查询和taskId查询汇签信息的tokenId
     *
     * @param metaId    单据metaid
     * @param tokenId   单据tokenId
     * @param taskId    单据任务id
     * @return String   汇签tokenid
     */
    @Transactional(readOnly = true)
    public String findByMetaIdAndTokenId(String metaId, String tokenId,String taskId) {
        String sql = "SELECT DISTINCT countersign_token_id from r_bill_token_and_countersign_task  btct "
                + " LEFT JOIN r_countersign_task_and_countersign_token ctct "
                + " ON btct.countersign_task_token_id = ctct.countersign_task_token_id"
                + " WHERE btct.token_id = '"
                + tokenId + "',"
                + " AND btct.meta_id = '" + metaId +"'"
                + " AND btct.task_id = '" + taskId + "'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
    /**
     * 保存单据token和汇签任务token之间的关系
     *
     * @param taskDto                   任务dto
     * @param countersignTaskMetaId     汇签任务metaId
     * @param countersignTaskTokenId    汇签任务tokenId
     */
    public void saveCountersignTaskRel(TaskDto taskDto, String countersignTaskMetaId, String countersignTaskTokenId) {
        String sql = "INSERT INTO  r_bill_token_and_countersign_task(meta_id,token_id,task_id,countersign_task_meta_id,countersign_task_token_id) "
        +" VALUES('"+taskDto.getBillMetaId()+"','"+taskDto.getBillTokenId()+"','"+taskDto.getTaskId()+ "','" +countersignTaskMetaId+"','"+countersignTaskTokenId+"')";
        jdbcTemplate.execute(sql);
    }

    /**
     * 保存汇签任务token和汇签信息token之间的关系
     *
     * @param countersignTaskTokenIds 汇签任务tokenId
     * @param countersignTokenId      汇签信息tokenId
     */
    public void saveCountersignRel(List<String> countersignTaskTokenIds, String countersignTokenId, String countersignTaskMetaId, String countersignMetaId) {
        String sql = "INSERT INTO  r_countersign_task_and_countersign_token(countersign_task_token_id,countersign_task_meta_id,countersign_token_id,countersign_meta_id) "
                +this.buildValueString(countersignTaskTokenIds,countersignTokenId,countersignTaskMetaId,countersignMetaId);
        jdbcTemplate.execute(sql);
    }

    /**
     * 构造values
     *
     * @param countersignTaskTokenIds   汇签任务tokenId
     * @param countersignTokenId        汇签信息tokenId
     * @return  valueStr
     */
    private String buildValueString(List<String> countersignTaskTokenIds,String countersignTokenId, String countersignTaskMetaId, String countersignMetaId){
        StringBuilder valueStr = new StringBuilder(" VALUES ");
        for (String countersignTaskTokenId : countersignTaskTokenIds) {
            valueStr.append("('")
                    .append(countersignTaskTokenId)
                    .append("','")
                    .append(countersignTaskMetaId)
                    .append("','")
                    .append(countersignTokenId)
                    .append("','")
                    .append(countersignMetaId)
                    .append("'),");
        }
        valueStr.deleteCharAt(valueStr.lastIndexOf(","));
        return valueStr.toString();
    }

    /**
     * 查询汇签任务关联的单据token,用于批量更改单据任务状态
     *
     * @param countersignTaskTokenIds 汇签任务tokenids
     * @return List<CountersignStatusUpdateDTO>  查询汇签任务关联的单据token
     */
    public List<CountersignStatusUpdateDTO> findBillInfoByTaskTokenId(List<String> countersignTaskTokenIds){
        String strCountersignTaskTokenIds = "'" + String.join("','", countersignTaskTokenIds) + "'";
        String sql = "SELECT meta_id,token_id from r_bill_token_and_countersign_task WHERE countersign_task_token_id IN ("+strCountersignTaskTokenIds+")";
        return jdbcTemplate.query(sql,new CountersignStatusUpdateDTORowMapper());
    }
}

class CountersignStatusUpdateDTORowMapper implements RowMapper<CountersignStatusUpdateDTO> {
    @SuppressWarnings("NullableProblems")
    @Override
    public CountersignStatusUpdateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CountersignStatusUpdateDTO(
                rs.getString("meta_id")
                , rs.getString("token_id")
        );
    }
}
