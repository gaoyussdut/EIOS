package top.toptimus;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JiangHao on 2018/6/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TestStoredProcedure {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testStoredProcedure() {
        logger.info("user" + JSON.toJSONString(
                jdbcTemplate.query("select * from getuser();", new UserMapper())
                )
        );
    }

    @Test
    public void testStoredProcedure2() {
        String sql = "select inserttest(4,'jianghao',3)";
        jdbcTemplate.execute(sql);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    class User {
        private Integer id;
        private String name;
        private Integer age;
    }

    class UserMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
//            user.setId(rs.getInt("id"));
//            user.setName(rs.getString("name"));
//            user.setAge(rs.getInt("age"));
            return user;
        }
    }


}
