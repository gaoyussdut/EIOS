package top.toptimus.repository.meta.tokentemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.common.enums.TokenTemplateTypeEnum;
import top.toptimus.meta.TalendModelMetaDto;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TokenTemplateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据ttid获取tt定义
     *
     * @param tokenTemplateId
     */
    public TokenTemplateDefinitionDTO findById(String tokenTemplateId){
        String sql = "SELECT token_template_id,token_template_name,token_template_type,bill_meta_id FROM t_tokentemplate_definition" +
                " WHERE token_template_id = '"+tokenTemplateId+"'";
        try{
            return jdbcTemplate.queryForObject(sql,new TokenTemplateDTORowMapper());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("TTID不存在");
        }
    }

    class TokenTemplateDTORowMapper implements RowMapper<TokenTemplateDefinitionDTO> {
        @Override
        public TokenTemplateDefinitionDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
            return new TokenTemplateDefinitionDTO(
                    rs.getString("token_template_id")
                    , rs.getString("token_template_name")
                    , rs.getString("bill_meta_id")
                    , TokenTemplateTypeEnum.valueOf(rs.getString("token_template_type"))
            );
        }
    }
}
