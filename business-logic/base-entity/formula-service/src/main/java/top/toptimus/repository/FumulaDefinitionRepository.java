package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.formula.FormulaDefinitionDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lzs on 2018/11/1.
 */
@Repository
public class FumulaDefinitionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存公式
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<FormulaDefinitionDTO> formulaDefinitionDTOS) {
        String sql = "INSERT INTO t_formula_definition (formula_id,formula,formula_name)" +
                " VALUES (?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public int getBatchSize() {
                        return formulaDefinitionDTOS.size();
                    }

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, formulaDefinitionDTOS.get(i).getFormulaId());
                        ps.setString(2, formulaDefinitionDTOS.get(i).getFormula());
                        ps.setString(3, formulaDefinitionDTOS.get(i).getFormulaName());
                    }
                }
        );
    }

}
