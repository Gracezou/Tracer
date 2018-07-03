package com.miracle.common.function;

/**
 * Description:消费三个参数的消费者函数
 *
 * @author guobin On date 2018/7/3.
 * @version 1.0
 * @since jdk 1.8
 * @param <T> 第一个参数
 * @param <U> 第二个参数
 * @param <V> 第三个参数
 */
@FunctionalInterface
public interface TriConsumer<T, U ,V> {

    /**
     * 消费数据
     * @param t 第一个参数
     * @param u 第二个参数
     * @param v 第三个参数
     */
    void accept(T t, U u, V v);
}
