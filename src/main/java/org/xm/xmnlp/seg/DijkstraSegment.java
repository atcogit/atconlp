package org.xm.xmnlp.seg;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.math.ahocorasick.DijkstraState;
import org.xm.xmnlp.recognition.organ.OrganizationRecognition;
import org.xm.xmnlp.recognition.person.JapanesePersonRecognition;
import org.xm.xmnlp.recognition.person.PersonRecognition;
import org.xm.xmnlp.recognition.person.TranslatedPersonRecognition;
import org.xm.xmnlp.recognition.place.PlaceRecognition;
import org.xm.xmnlp.seg.domain.EdgeFrom;
import org.xm.xmnlp.seg.domain.Graph;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.seg.domain.Vertex;
import org.xm.xmnlp.seg.domain.WordNet;

/**
 * 最短路径分词，最短路求解采用Dijkstra算法
 */
public class DijkstraSegment extends WordBasedModelSegment {
    @Override
    public List<Term> segSentence(char[] sentence) {
        WordNet wordNetOptimum = new WordNet(sentence);
        WordNet wordNetAll = new WordNet(wordNetOptimum.charArray);
        ////////////////生成词网////////////////////
        GenerateWordNet(wordNetAll);
        ///////////////生成词图////////////////////
        Graph graph = GenerateBiGraph(wordNetAll);
        if (Xmnlp.Config.DEBUG) {
            System.out.printf("粗分词图：%s\n", graph.printByTo());
        }
        List<Vertex> vertexList = dijkstra(graph);
        //fixResultByRule(vertexList);

        if (config.useCustomDictionary) {
            combineByCustomDictionary(vertexList);
        }

        if (Xmnlp.Config.DEBUG) {
            System.out.println("粗分结果" + convert(vertexList, false));
        }

        // 数字识别
        if (config.numberQuantifierRecognize) {
            mergeNumberQuantifier(vertexList, wordNetAll, config);
        }

        // 实体命名识别
        if (config.ner) {
            wordNetOptimum.addAll(vertexList);
            int preSize = wordNetOptimum.size();
            if (config.nameRecognize) {
                PersonRecognition.recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.translatedNameRecognize) {
                TranslatedPersonRecognition.recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.japaneseNameRecognize) {
                JapanesePersonRecognition.recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.placeRecognize) {
                PlaceRecognition.recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.organizationRecognize) {
                // 层叠隐马模型——生成输出作为下一级隐马输入
                graph = GenerateBiGraph(wordNetOptimum);
                vertexList = dijkstra(graph);
                wordNetOptimum.clear();
                wordNetOptimum.addAll(vertexList);
                preSize = wordNetOptimum.size();
                OrganizationRecognition.recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (wordNetOptimum.size() != preSize) {
                graph = GenerateBiGraph(wordNetOptimum);
                vertexList = dijkstra(graph);
                if (Xmnlp.Config.DEBUG) {
                    System.out.printf("细分词网：\n%s\n", wordNetOptimum);
                    System.out.printf("细分词图：%s\n", graph.printByTo());
                }
            }
        }

        // 如果是索引模式则全切分
        if (config.indexMode) {
            return decorateResultForIndexMode(vertexList, wordNetAll);
        }

        // 是否标注词性
        if (config.speechTagging) {
            speechTagging(vertexList);
        }

        return convert(vertexList, config.offset);
    }

    /**
     * dijkstra最短路径
     *
     * @param graph
     * @return
     */
    private static List<Vertex> dijkstra(Graph graph) {
        List<Vertex> resultList = new LinkedList<Vertex>();
        Vertex[] vertexes = graph.getVertexes();
        List<EdgeFrom>[] edgesTo = graph.getEdgesTo();
        double[] d = new double[vertexes.length];
        Arrays.fill(d, Double.MAX_VALUE);
        d[d.length - 1] = 0;
        int[] path = new int[vertexes.length];
        Arrays.fill(path, -1);
        PriorityQueue<DijkstraState> que = new PriorityQueue<DijkstraState>();
        que.add(new DijkstraState(0, vertexes.length - 1));
        while (!que.isEmpty()) {
            DijkstraState p = que.poll();
            if (d[p.vertex] < p.cost) continue;
            for (EdgeFrom edgeFrom : edgesTo[p.vertex]) {
                if (d[edgeFrom.from] > d[p.vertex] + edgeFrom.weight) {
                    d[edgeFrom.from] = d[p.vertex] + edgeFrom.weight;
                    que.add(new DijkstraState(d[edgeFrom.from], edgeFrom.from));
                    path[edgeFrom.from] = p.vertex;
                }
            }
        }
        for (int t = 0; t != -1; t = path[t]) {
            resultList.add(vertexes[t]);
        }
        return resultList;
    }

}
