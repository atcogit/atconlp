package org.xm.xmnlp.seg.nshortpath;

/**
 * 一个维护了上次访问位置的优先级队列（最小堆）
 */
public class CQueue {
    private QueueElement pHead = null;
    private QueueElement pLastAccess = null;

    /**
     * 将QueueElement根据eWeight由小到大的顺序插入队列
     *
     * @param newElement
     */
    public void enQueue(QueueElement newElement) {
        QueueElement pCur = pHead, pPre = null;

        while (pCur != null && pCur.weight < newElement.weight) {
            pPre = pCur;
            pCur = pCur.next;
        }

        newElement.next = pCur;

        if (pPre == null)
            pHead = newElement;
        else
            pPre.next = newElement;
    }

    /**
     * 从队列中取出前面的一个元素
     *
     * @return
     */
    public QueueElement deQueue() {
        if (pHead == null)
            return null;

        QueueElement pRet = pHead;
        pHead = pHead.next;

        return pRet;
    }

    /**
     * 读取第一个元素，但不执行DeQueue操作
     *
     * @return
     */
    public QueueElement GetFirst() {
        pLastAccess = pHead;
        return pLastAccess;
    }

    /**
     * 读取上次读取后的下一个元素，不执行DeQueue操作
     *
     * @return
     */
    public QueueElement GetNext() {
        if (pLastAccess != null)
            pLastAccess = pLastAccess.next;

        return pLastAccess;
    }

    /**
     * 是否仍然有下一个元素可供读取
     *
     * @return
     */
    public boolean CanGetNext() {
        return (pLastAccess.next != null);
    }

    /**
     * 清除所有元素
     */
    public void clear() {
        pHead = null;
        pLastAccess = null;
    }
}
