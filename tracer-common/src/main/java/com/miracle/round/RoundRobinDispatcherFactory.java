package com.miracle.round;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:时间片轮转调度者工厂类
 *
 * @author guobin On date 2018/7/2.
 * @version 1.0
 * @since jdk 1.8
 */
public class RoundRobinDispatcherFactory {

    /**
     * 时间轮的池,以时间片的大小作为key
     */
    private static final Map<Long, RoundRobinDispatcher> ROUND_ROBIN_POOL = new ConcurrentHashMap<>(16);

    /**
     * 不允许实例化
     */
    private RoundRobinDispatcherFactory() {}

    /**
     * 注册一个时间轮
     * 如果给定的时间片所对应的时间轮是已经存在的那么会进行复用
     * @param timeSlice 指定的时间片大小
     * @return 时间片轮转调度者
     */
    public static RoundRobinDispatcher registerRoundRobin(long timeSlice) {
        return ROUND_ROBIN_POOL.computeIfAbsent(timeSlice, k -> new RoundRobinDispatcher(timeSlice));
    }

    /**
     * 注册一个时间轮,用默认的时间片
     * 如果给定的时间片所对应的时间轮是已经存在的那么会进行复用
     * @return 时间片轮转调度者
     */
    public static RoundRobinDispatcher registerRoundRobin() {
        return ROUND_ROBIN_POOL.computeIfAbsent(RoundRobinDispatcher.DEFAULT_TIME_SLICE, k -> new RoundRobinDispatcher());
    }
}
