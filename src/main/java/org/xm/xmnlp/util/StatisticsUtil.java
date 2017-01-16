package org.xm.xmnlp.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 词频统计工具
 *
 * @author xuming
 */
public class StatisticsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsUtil.class);
    private String path = Xmnlp.Config.StatisticsPath;
    private Map<Term, AtomicInteger> statisticsMap = new ConcurrentHashMap<>();
    private Segment segment = Xmnlp.newSegment();

    public StatisticsUtil() {
    }

    public StatisticsUtil(String path) {
        this.path = path;
    }

    public StatisticsUtil(String path, Segment segment) {
        this.path = path;
        this.segment = segment;
    }

    private void statistics(Term word, int times, Map<Term, AtomicInteger> container) {
        container.putIfAbsent(word, new AtomicInteger());
        container.get(word).addAndGet(times);
    }

    private void dump(Map<Term, AtomicInteger> map, String path) {
        try {
            // score rank
            List<String> list = map.entrySet()
                    .parallelStream()
                    .sorted((a, b) -> new Integer(b.getValue().get()).compareTo(a.getValue().intValue()))
                    .map(entry ->entry.getKey().toString("\t") + "\t" + entry.getValue().get())
                    .collect(Collectors.toList());
            Files.write(Paths.get(path), list);
            if (list.size() < 100) {
                LOGGER.info("word statistics result:");
                AtomicInteger i = new AtomicInteger();
                list.forEach(item -> LOGGER.info("\t" + i.incrementAndGet() + "、" + item));
            }
            LOGGER.info("statistic result save：" + path);
        } catch (Exception e) {
            LOGGER.error("dump error!", e);
        }
    }

    public void seg(String text){
        segment.seg(text).parallelStream().forEach(i->{
            statistics(i,1,statisticsMap);
        });
    }
    /**
     * 将词频统计结果保存到文件
     * @param resultPath 词频统计结果保存路径
     */
    public void dump(String resultPath) {
        this.path = resultPath;
        dump();
    }
    /**
     * 将词频统计结果保存到文件
     */
    public void dump() {
        dump(this.statisticsMap, this.path);
    }
    /**
     * 清除之前的统计结果
     */
    public void reset() {
        this.statisticsMap.clear();
    }

    public Map<Term, AtomicInteger> getStatisticsMap() {
        return statisticsMap;
    }

    public void setStatisticsMap(Map<Term, AtomicInteger> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }
}
