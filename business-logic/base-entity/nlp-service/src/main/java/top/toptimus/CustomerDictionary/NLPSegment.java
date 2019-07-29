package top.toptimus.CustomerDictionary;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * nlp分词实体
 */
@Component
public class NLPSegment {

    /**
     * 插入字典
     *
     * @param word                分词
     * @param natureWithFrequency 词性和频率
     */
    public void insertCustomDictionary(String word, String natureWithFrequency) {
        CustomDictionary.insert(word, natureWithFrequency);
    }

    /**
     * 取分词
     *
     * @param text 文本
     */
    public List<Term> getSegment(String text) {
        return HanLP.segment(text);
    }

}
