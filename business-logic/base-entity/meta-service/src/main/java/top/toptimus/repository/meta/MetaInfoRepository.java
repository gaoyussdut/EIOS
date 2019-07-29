package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MetaInfoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据meta id查询meta info
     *
     * @param metaId meta id
     * @return meta信息
     */
    @Transactional(readOnly = true)
    public List<MetaInfoDTO> findByMetaId(String metaId) {
        String sql = "SELECT meta_id,token_meta_name,key,caption,fkeytypeenum,visible,readonly,required,validation,meta_name,fkey,meta_key,order_ from v_meta_edit WHERE meta_id = '"
                + metaId + "'";
        return jdbcTemplate.query(sql, new MetaEditRowMapper());
    }

    @Transactional(readOnly = true)
    public List<MetaInfoDTO> findByMetaIds(List<String> metaIds) {
        String metaIdList = "'" + String.join("','", metaIds) + "'";
        String strSQL = "select meta_id,token_meta_name,key,caption,fkeytypeenum,visible,readonly,required,validation,meta_name,fkey,meta_key,order_ from v_meta_edit" +
                " WHERE meta_id IN (" + metaIdList + ");";
        return jdbcTemplate.query(strSQL, new MetaEditRowMapper());
    }


}

@SuppressWarnings("NullableProblems")
class MetaEditRowMapper implements RowMapper<MetaInfoDTO> {

    @Override
    public MetaInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MetaInfoDTO(rs);
    }
}

