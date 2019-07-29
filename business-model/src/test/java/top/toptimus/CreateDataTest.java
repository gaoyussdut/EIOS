package top.toptimus;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CreateDataTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据文件执行sql
     *
     * @param pathName
     */
    private void executeSql(String pathName) {
        String path = getClass().getClassLoader().getResource(pathName).toString();
        path = path.replace("\\", "/");
        if (path.contains(":")) {
            path = path.replace("file:", "");// 2
        }
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String data = "";
            for (String tempString = ""; (tempString = reader.readLine()) != null; data += tempString) {
                logger.info("tempString:+++++=" + tempString);
                jdbcTemplate.execute(tempString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createData() {
        executeSql("TokenData/fpdb_test_warehouse.sql");
    }
}
