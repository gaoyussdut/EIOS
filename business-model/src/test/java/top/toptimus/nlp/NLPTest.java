package top.toptimus.nlp;

import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.BusinessModelApplication;
import top.toptimus.CustomerDictionary.NLPSegment;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class NLPTest {
    @Autowired
    private NLPSegment nlpSegment;

    @Test
    public void getDoubleArrayTrieSegment() {
        //  插入字典
        nlpSegment.insertCustomDictionary("我卧槽你妈了个逼xxxx", "nz 1024");
        List<Term> terms = nlpSegment.getSegment("赵威是个大血逼,我卧槽你妈了个逼xxxx");
        terms.forEach(term -> System.out.println(
                "term.word = " + term.word
                        + ",term.nature = " + term.nature
                        + ",term.offset = " + term.offset
        ));
    }

    @Test
    public void getDoubleArrayTrieSegment2() {
        // vertex billTokenId 分词
        //  插入字典
        nlpSegment.insertCustomDictionary("接受的情况", "nz 1024");
        nlpSegment.insertCustomDictionary("当然在分词", "nz 1024");
        List<Term> terms = nlpSegment.getSegment("当然在分词准确度可以接受的情况下，很多细节问题NLPIR、腾讯文智同时提供了实体识别、情感分析、新闻分类等其他扩展服务。下表给出了各家系统在应用方面的详细对比。");
        terms.forEach(term -> System.out.println(
                "term.word = " + term.word
                        + ",term.nature = " + term.nature
                        + ",term.offset = " + term.offset
        ));
    }
}
