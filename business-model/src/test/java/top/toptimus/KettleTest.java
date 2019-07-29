package top.toptimus;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kettle demo
 *
 * @author jp
 * @date 2018/05/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class KettleTest {

    /**
     * 从t_process_ins表中抽取数据写入dbtest表
     */
    @Test
    public void KettleDemo() {
        //String jobname = "/Users/jp/workspace/SpringBoot2-v2/business-model/src/test/resources/kettlejobs/kettle_demo.kjb";
        //KettleUtil.runJob(jobname);
//        String tranname = "/Users/jp/workspace/SpringBoot2-v2/business-model/src/test/resources/kettlejobs/kettle_demo.ktr";
//        KettleUtil.runTrans(tranname);

        // String filename="Users/jp/learn/elt_demo.ktr";
        // runTrans(filename);
//
//
//        //String tranname = "/home/congchenri/Downloads/20180817/2018082722.ktr";
//        String tranname = "/home/congchenri/Downloads/20180817/2018082733.ktr";
//        System.out.println("tranname:   +++++++++++++"+tranname);
//        KettleUtil.runTrans(tranname);
//
//        Map<String,String> map = new HashMap<>();
//        map.put("USERID","StartEvent_19595123123");
//        KettleUtil.runTrans(tranname,map);

    }

}
