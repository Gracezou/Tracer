package com.miracle.round;

import com.miracle.lock.LocalResourceLock;
import com.miracle.lock.ResourceLock;
import com.miracle.utils.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.ToLongFunction;

/**
 * Description: 时间片轮转算法
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public class RoundRobinDispatcher {

    /**
     * 默认时间片大小{@value}
     */
    static final long DEFAULT_TIME_SLICE = 100L;

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
    private final Map<String, TaskQueueDelegate<?>> taskQueueMap;

    /**
     * 每个队列与元素集合的对应关系
     */
    private final Map<String, Set<Object>> queueObjectsMap;

    /**
     * 上一次的任务截止时间
     */
    private final AtomicLong lastDeadline;

    RoundRobinDispatcher() {
        this(DEFAULT_TIME_SLICE);
    }

    RoundRobinDispatcher(long timeSlice) {
        if (timeSlice <= 0) {
            throw new IllegalArgumentException("Time slice must greater that 0.");
        }
        this.timeSlice = timeSlice;
        this.taskQueueMap = new HashMap<>(16);
        this.queueObjectsMap = new HashMap<>(16);
        this.lastDeadline = new AtomicLong(System.currentTimeMillis());
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(this::runPeriodically, 0, this.timeSlice, TimeUnit.MILLISECONDS);
    }

    /**
     * 将一个元素添加进指定的工作队列之中
     * @param obj 元素
     * @param taskDuration 元素在时间片轮中的存活时长
     * @param queueName 工作队列名
     * @param <T> 元素的类型
     * @throws NullPointerException 如果obj为{@code null}时抛出
     */
    @SuppressWarnings("unchecked")
    public <T> void add(T obj, long taskDuration, String queueName, ToLongFunction<T> taskExecutor) {
        if (obj == null) {
            throw new NullPointerException("The object in the wheel cannot be null.");
        }
        final long taskCompleteTime = this.getTaskCompleteTime(taskDuration);
        final String actualQueueName = this.buildActualQueueName(obj, queueName);
        QUEUE_RESOURCE_LOCK.lock(queueName);
        try {
            TaskQueueDelegate<T> queue = (TaskQueueDelegate<T>) this.taskQueueMap.computeIfAbsent(actualQueueName,
                    k -> new TaskQueueDelegate<>(actualQueueName, taskExecutor));
            queue.offer(new Task<>(obj, taskCompleteTime, actualQueueName));
            // 加入每个队列所对应的数据set中
            this.queueObjectsMap.computeIfAbsent(actualQueueName, k -> new HashSet<>()).add(obj);
        } finally {
            QUEUE_RESOURCE_LOCK.unlock(actualQueueName);
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
        this.lastDeadline.addAndGet(this.timeSlice);
        this.taskQueueMap.values().forEach(this::processOnTaskQueue);
    }

    /**
     * 对任务队列所周期性执行的操作
     * @param taskQueue 任务队列
     * @param <T> 任务队列中数据的类型
     */
    private <T> void processOnTaskQueue(TaskQueueDelegate<T> taskQueue) {
        QUEUE_RESOURCE_LOCK.lock(taskQueue.queueName);
        try {
            this.runTaskAsync(taskQueue.poll(), taskQueue.notification);
        } finally {
            QUEUE_RESOURCE_LOCK.unlock(taskQueue.queueName);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void runTaskAsync(Task<T> task, ToLongFunction<T> taskExecutor) {
        // 如果在对应队列所持有的对象集中不存在所需要执行任务中持有的对象,说明这个任务已经被外界通过调用remove()取消掉了,不执行
        Set<Object> queueObjectsSet = this.queueObjectsMap.get(task.queueName);
        if (!CollectionUtils.isContaining(queueObjectsSet, task.obj)) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            // 到这一步队列一定不会为null,所以直接用get获取
            TaskQueueDelegate<T> queue = (TaskQueueDelegate<T>) this.taskQueueMap.get(task.queueName);
            // 如果时间还没到完成时间,那么重新放回队列之中
            if (task.completeTime > this.lastDeadline.get()) {
                queue.offer(task);
                return;
            }
            // 说明时间到了
            final long result = taskExecutor.applyAsLong(task.obj);
            // result > 0说明任务没有执行成功,以result作为任务工作时长再度放回时间轮之中
            if (result > 0) {
                Task<T> newTask = new Task<>(task.obj, this.getTaskCompleteTime(result), task.queueName);
                queue.offer(newTask);
            } else {
                // 执行成功,将这个对象从任务队列所对应的objSet中删除
                queueObjectsSet.remove(task.obj);
            }
        });
    }

    /**
     * 根据参数类型和外界指定的任务队列名来生成一个在时间轮中实际应用的队列名
     * @param obj 传入的对象
     * @param outsideQueueName 外界指定的队列名
     * @return 生成的实际队列名
     */
    private String buildActualQueueName(Object obj, String outsideQueueName) {
        return obj.getClass().getSimpleName() + "-" + outsideQueueName;
    }

    /**
     * 得到任务的完成时间
     * @param taskNeedTime 任务执行所需时间
     * @return 任务完成的时间戳
     */
    private long getTaskCompleteTime(long taskNeedTime) {
        return System.currentTimeMillis() + taskNeedTime;
    }

    /**
     * 任务队列
     * @param <E> 队列中每个任务所消费的数据类型
     */
    private static class TaskQueueDelegate<E> {

        /**
         * 队列名
         */
        private final String queueName;

        /**
         * 存储任务的红黑树
         */
        private final Queue<Task<E>> delegate;

        /**
         * 任务完成时调用的函数
         * 当函数返回值 > 0时会被重新以返回值作为过期时间放入时间片之中
         * 当函数返回值 = 0时视作任务完成
         */
        private final ToLongFunction<E> notification;

        TaskQueueDelegate(String queueName, ToLongFunction<E> notification) {
            this.queueName = queueName;
            this.notification = notification;
            this.delegate = new LinkedList<>();
        }

        private void offer(Task<E> eTask) {
            delegate.offer(eTask);
        }

        private Task<E> poll() {
            return delegate.poll();
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
        private final long completeTime;

        /**
         * 任务所在队列名
         */
        private final String queueName;

        Task(T obj, long completeTime, String queueName) {
            this.obj = obj;
            this.completeTime = completeTime;
            this.queueName = queueName;
        }
    }
}
