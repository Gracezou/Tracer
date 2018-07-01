package com.miracle.round;

import com.miracle.lock.LocalResourceLock;
import com.miracle.lock.ResourceLock;
import com.miracle.tree.BinaryTree;
import com.miracle.tree.RedBlackTree;
import com.miracle.utils.CollectionUtils;
import com.miracle.utils.ListUtils;
import com.miracle.utils.TreeUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.ToLongFunction;

/**
 * Description: 模拟时间片轮转调度算法
 * 采用红黑树作为数据的存储
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public class RoundRobinDispatcher {

    /**
     * 默认时间片大小{@value}
     */
    private static final long DEFAULT_TIME_SLICE = 100L;

    /**
     * 任务队列的资源锁
     */
    private static final ResourceLock QUEUE_RESOURCE_LOCK = new LocalResourceLock();

    /**
     * 每个时间片的大小
     */
    private final long timeSlice;

    /**
     * 周期性任务执行者
     */
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE =
            new ScheduledThreadPoolExecutor(1, r -> {
                final Thread thread = new Thread(r);
                thread.setName("RoundRobinDispatcher");
                return thread;
            });

    /**
     * 工作队列集合
     */
    private final Map<String, TaskQueue<?>> taskQueueMap;

    /**
     * 每个队列与元素集合的对应关系
     */
    private final Map<String, Set<Object>> queueObjectsMap;

    public RoundRobinDispatcher() {
        this(DEFAULT_TIME_SLICE);
    }

    public RoundRobinDispatcher(long timeSlice) {
        if (timeSlice <= 0) {
            throw new IllegalArgumentException("Time slice must greater that 0.");
        }
        this.timeSlice = timeSlice;
        this.taskQueueMap = new HashMap<>(16);
        this.queueObjectsMap = new HashMap<>(16);
       SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(this::runPeriodically, 0, this.timeSlice, TimeUnit.MILLISECONDS);
    }

    /**
     * 注册一个任务
     * @param queueName 队列名字
     * @param notification 任务过期时的通知任务
     * @param <T> 任务中负载的数据类型
     */
    public <T> void registerTaskQueue(String queueName, ToLongFunction<T> notification) {
        synchronized (this.taskQueueMap) {
            this.taskQueueMap.putIfAbsent(queueName, new TaskQueue<>(queueName, notification));
        }
    }

    /**
     * 将一个元素添加进指定的工作队列之中
     * @param obj 元素
     * @param taskDuration 元素在时间片轮中的存活时长
     * @param queueName 工作队列名
     * @param <T> 元素的类型
     */
    @SuppressWarnings("unchecked")
    public <T> void add(T obj, long taskDuration, String queueName) {
        if (!this.taskQueueMap.containsKey(queueName)) {
            throw new NullPointerException("The task queue which named," + queueName + ", has not been registered yet. "
                    + "Please invoke registerTaskQueue() before you invoking add().");
        }
        final long expireTime = System.currentTimeMillis() + taskDuration;
        final Task<T> task = new Task<>(obj, expireTime, queueName);
        QUEUE_RESOURCE_LOCK.lock(queueName);
        try {
            final TaskQueue<T> taskQueue = (TaskQueue<T>) this.taskQueueMap.get(queueName);
            this.queueObjectsMap.computeIfAbsent(queueName, k -> new HashSet<>()).add(task.obj);
            taskQueue.offerTask(task);
        } finally {
            QUEUE_RESOURCE_LOCK.unlock(queueName);
        }
    }

    /**
     * 删除一个元素所对应的任务
     * @param obj 元素对象
     * @param queueName 任务队列名
     */
    public void remove(Object obj, String queueName) {
        QUEUE_RESOURCE_LOCK.lock(queueName);
        try {
            Optional.ofNullable(this.queueObjectsMap.get(queueName))
                    .ifPresent(set -> set.remove(obj));
        } finally {
            QUEUE_RESOURCE_LOCK.unlock(queueName);
        }
    }

    /**
     * 周期性执行的任务
     */
    private void runPeriodically() {
        // 计算此次执行的过期时间
        final long currentExpireTime = System.currentTimeMillis() + this.timeSlice;
        this.taskQueueMap.values().forEach(taskQueue -> this.processOnTaskQueue(currentExpireTime, taskQueue));
    }

    /**
     * 对任务队列所周期性执行的操作
     * @param expireTime 当前=数据过期时间
     * @param taskQueue 任务队列
     * @param <T> 任务队列中数据的类型
     */
    private <T> void processOnTaskQueue(long expireTime, TaskQueue<T> taskQueue) {
        QUEUE_RESOURCE_LOCK.lock(taskQueue.queueName);
        try {
            final List<BinaryTree.Node<Long, Task<T>>> nodes = taskQueue.pollAllExpireNode(expireTime);
            nodes.forEach(node -> this.processSubTree(node, taskQueue.notification));
        } finally {
            QUEUE_RESOURCE_LOCK.unlock(taskQueue.queueName);
        }
    }

    /**
     * 处理子树
     * @param node 子树中的一个叶节点
     * @param taskExecutor 任务执行器
     * @param <T> 节点中任务持有的数据类型
     */
    private <T> void processSubTree(BinaryTree.Node<Long, Task<T>> node, ToLongFunction<T> taskExecutor) {
        if (node == null) {
            return;
        }
        final BinaryTree.Node<Long, Task<T>> rootNode = TreeUtils.findRootNode(node);
        // 前遍历的方式遍历子树
        TreeUtils.acceptPreOrderTraversal(rootNode, n -> this.processNode(n, taskExecutor));
    }

    /**
     * 处理单个节点
     * @param node 节点
     * @param taskExecutor 任务执行器
     * @param <T> 节点中任务持有的数据类型
     */
    private <T> void processNode(BinaryTree.Node<Long, Task<T>> node, ToLongFunction<T> taskExecutor) {
        ListUtils.notNullOrEmptyList(node.values())
                .forEach(task -> CompletableFuture.runAsync(() -> processTask(task, taskExecutor)));
    }

    /**
     * 执行任务
     * @param task 任务
     * @param taskExecutor 任务执行器
     * @param <T> 节点中任务持有的数据类型
     */
    @SuppressWarnings("unchecked")
    private <T> void processTask(Task<T> task, ToLongFunction<T> taskExecutor) {
        // 不存在在queueObjectsMap中对应value(set)中的元素没有拿出来执行的必要,因为已经被删除任务了
        final Set<T> objSet = (Set<T>) this.queueObjectsMap.get(task.queueName);
        if (!CollectionUtils.isContaining(objSet, task.obj)) {
            return;
        }
        // 执行之前,将这个对象从任务队列所对应的objSet中删除
        objSet.remove(task.obj);
        long result = taskExecutor.applyAsLong(task.obj);
        // result > 0说明任务没有异步执行成功,以result作为存活时间再度放回时间轮之中
        if (result > 0) {
            this.add(task.obj, result, task.queueName);
        }
    }

    /**
     * 任务队列
     * @param <T> 队列中每个任务所消费的数据类型
     */
    private static class TaskQueue<T> {

        /**
         * 队列名
         */
        private final String queueName;

        /**
         * 存储任务的红黑树
         */
        private final BinaryTree<Long, Task<T>> tree;

        /**
         * 任务完成时调用的函数
         * 当函数返回值 > 0时会被重新以返回值作为过期时间放入时间片之中
         * 当函数返回值 = 0时视作任务完成
         */
        private final ToLongFunction<T> notification;

        TaskQueue(String queueName, ToLongFunction<T> notification) {
            this.queueName = queueName;
            this.notification = notification;
            this.tree = new RedBlackTree<>();
        }

        /**
         * 提交一份任务到任务队列之中
         * @param task 任务
         */
        void offerTask(Task<T> task) {
            this.tree.insert(task, t -> t.expireTime);
        }

        /**
         * 弹出所有过期失效的节点
         * @param expireTime 过期时间
         * @return 弹出的节点列表
         */
        List<BinaryTree.Node<Long, Task<T>>> pollAllExpireNode(long expireTime) {
            return this.tree.popPreNodes(expireTime);
        }
    }


    /**
     * 任务
     * @param <T> 任务持有的数据类型
     */
    private static class Task<T> {

        /**
         * 持有的数据对象
         */
        private final T obj;

        /**
         * 任务到期时间
         */
        private final long expireTime;

        /**
         * 任务所在队列名
         */
        private final String queueName;

        Task(T obj, long expireTime, String queueName) {
            this.obj = obj;
            this.expireTime = expireTime;
            this.queueName = queueName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Task)) {
                return false;
            }
            Task<?> task = (Task<?>) o;
            return expireTime == task.expireTime &&
                    Objects.equals(obj, task.obj) &&
                    Objects.equals(queueName, task.queueName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(obj, expireTime, queueName);
        }
    }
}
