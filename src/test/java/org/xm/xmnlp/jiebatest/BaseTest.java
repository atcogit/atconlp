package org.xm.xmnlp.jiebatest;


import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.xm.xmnlp.dic.JItem;
import org.xm.xmnlp.segword.SegMode;
import org.xm.xmnlp.segword.Segmenter;

import junit.framework.TestCase;


public class BaseTest extends TestCase {
    protected Segmenter segmenter = new Segmenter();
    String[] sentences =
            new String[]{
                    "找小姐",
                    "找美女",
                    "找小妹",
                    "学生妹",
                    "职业狐狸精",
                    "男公关",
                    "上门",
                    "抽獎",
                    "好声音",
                    "好聲音",
                    "夢之声",
                    "夢之聲",
                    "訂票",
                    "改簽",
                    "熱线",
                    "熱線",
                    "热線",
                    "電话",
                    "電話",
                    "醫院",
                    "代刷",
                    "撲剋牌",
                    "137-1234-1234",
                    "这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。",
                    "我不喜欢日本和服。",
                    "雷猴回归人间。",
                    "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作",
                    "我需要廉租房",
                    "永和服装饰品有限公司",
                    "我爱北京天安门",
                    "abc",
                    "隐马尔可夫",
                    "雷猴是个好网站",
                    "“,”和“SOFTware（软件）”两部分组成",
                    "草泥马和欺实马是今年的流行词汇",
                    "伊藤洋华堂总府店",
                    "中国科学院计算技术研究所",
                    "罗密欧与朱丽叶",
                    "我购买了道具和服装",
                    "PS: 我觉得开源有一个好处，就是能够敦促自己不断改进，避免敞帚自珍",
                    "湖北省石首市",
                    "湖北省十堰市",
                    "总经理完成了这件事情",
                    "电脑修好了",
                    "做好了这件事情就一了百了了",
                    "人们审美的观点是不同的",
                    "我们买了一个美的空调",
                    "线程初始化时我们要注意",
                    "一个分子是由好多原子组织成的",
                    "但是后来我才知道你是对的",
                    "存在即合理",
                    "的的的的的在的的的的就以和和和",
                    "I love你，不以为耻，反以为rong",
                    "因",
                    "",
                    "hello你好人们审美的观点是不同的",
                    "很好但主要是基于网页形式",
                    "hello你好人们审美的观点是不同的",
                    "为什么我不能拥有想要的生活",
                    "后来我才",
                    "此次来中国是为了",
                    "使用了它就可以解决一些问题",
                    ",使用了它就可以解决一些问题",
                    "其实使用了它就可以解决一些问题",
                    "好人使用了它就可以解决一些问题",
                    "是因为和国家",
                    "老年搜索还支持",
                    "干脆就把那部蒙人的闲法给废了拉倒！RT @laoshipukong : 27日，全国人大常委会第三次审议侵权责任法草案，删除了有关医疗损害责任“举证倒置”的规定。在医患纠纷中本已处于弱势地位的消费者由此将陷入万劫不复的境地。 ",
                    "张晓梅去人民医院做了个B超然后去买了件T恤", "AT&T是一件不错的公司，给你发offer了吗？",
                    "C++和c#是什么关系？11+122=133，是吗？PI=3.14159", "你认识那个和主席握手的的哥吗？他开一辆黑色的士。", "枪杆子中出政权",
                    "鲜芋仙 3"};
    String[] longSentences = new String[]{
    };


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testCutForSearch() {
        for (String sentence : sentences) {
            List<JItem> tokens = segmenter.process(sentence, SegMode.SEARCH);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
        }
    }


    @Test
    public void testCutForIndex() {
        for (String sentence : sentences) {
            List<JItem> tokens = segmenter.process(sentence, SegMode.INDEX);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
        }
    }


    @Test
    public void testBugSentence() {
        String[] bugs =
                new String[]{
                        "UTF-8",
                        "iphone5",
                        "鲜芋仙 3",
                        "RT @laoshipukong : 27日，",
                        "AT&T是一件不错的公司，给你发offer了吗？",
                        "干脆就把那部蒙人的闲法给废了拉倒！RT @laoshipukong : 27日，全国人大常委会第三次审议侵权责任法草案，删除了有关医疗损害责任“举证倒置”的规定。在医患纠纷中本已处于弱势地位的消费者由此将陷入万劫不复的境地。 "};
        for (String sentence : bugs) {
            List<JItem> tokens = segmenter.process(sentence, SegMode.SEARCH);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
        }
    }


    @Test
    public void testSegmentSpeed() {
        long length = 0L;
        long wordCount = 0L;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 2000; ++i)
            for (String sentence : sentences) {
                segmenter.process(sentence, SegMode.INDEX);
                length += sentence.getBytes(Charset.forName("UTF-8")).length;
                wordCount += sentence.length();
            }
        long elapsed = (System.currentTimeMillis() - start);
        System.out.println(String.format(Locale.getDefault(), "time elapsed:%d, rate:%fkb/s, sentences:%.2f/s", elapsed,
                (length * 1.0) / 1024.0f / (elapsed * 1.0 / 1000.0f), wordCount * 1000.0f / (elapsed * 1.0)));
    }


    @Test
    public void testLongTextSegmentSpeed() {
        long length = 0L;
        long wordCount = 0L;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; ++i)
            for (String sentence : longSentences) {
                segmenter.process(sentence, SegMode.INDEX);
                length += sentence.getBytes(Charset.forName("UTF-8")).length;
                wordCount += sentence.length();
            }
        long elapsed = (System.currentTimeMillis() - start);
        System.out.println(String.format(Locale.getDefault(), "time elapsed:%d, rate:%fkb/s, sentences:%.2f/s", elapsed,
                (length * 1.0) / 1024.0f / (elapsed * 1.0 / 1000.0f), wordCount * 1000.0f / (elapsed * 1.0)));
    }

}
