package org.xm.xmnlp.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 用固定容量的优先队列模拟的最大堆，用于解决求topN大的问题
 *
 * @author hankcs
 */
public class MaxHeap<E> {
    /**
     * 优先队列
     */
    private PriorityQueue<E> queue;
    /**
     * 堆的最大容量
     */
    private int maxSize;

    /**
     * 构造最大堆
     *
     * @param maxSize    保留多少个元素
     * @param comparator 比较器，生成最大堆使用o1-o2，生成最小堆使用o2-o1，并修改 e.compareTo(peek) 比较规则
     */
    public MaxHeap(int maxSize, Comparator<E> comparator) {
        if (maxSize <= 0)
            throw new IllegalArgumentException();
        this.maxSize = maxSize;
        this.queue = new PriorityQueue<E>(maxSize, comparator);
    }

    /**
     * 添加一个元素
     *
     * @param e 元素
     * @return 是否添加成功
     */
    public boolean add(E e) {
        if (queue.size() < maxSize) { // 未达到最大容量，直接添加
            queue.add(e);
            return true;
        } else { // 队列已满
            E peek = queue.peek();
            if (queue.comparator().compare(e, peek) > 0) { // 将新元素与当前堆顶元素比较，保留较小的元素
                queue.poll();
                queue.add(e);
                return true;
            }
        }
        return false;
    }

    /**
     * 添加许多元素
     *
     * @param collection
     */
    public MaxHeap<E> addAll(Collection<E> collection) {
        for (E e : collection) {
            add(e);
        }

        return this;
    }

    /**
     * 转为有序列表，自毁性操作
     *
     * @return
     */
    public List<E> toList() {
        ArrayList<E> list = new ArrayList<E>(queue.size());
        while (!queue.isEmpty()) {
            list.add(0, queue.poll());
        }

        return list;
    }
}
