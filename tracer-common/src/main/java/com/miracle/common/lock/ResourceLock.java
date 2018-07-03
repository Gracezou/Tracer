package com.miracle.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * Description:资源锁接口
 *
 * @author guobin On date 2018/7/1.
 * @version 1.0
 * @since jdk 1.8
 */
public interface ResourceLock {

    /**
     * 尝试获取给定key的资源锁
     * @param key 要锁定的资源key
     * @return true代表获取成功,false代表获取失败
     */
    boolean tryLock(String key);

    /**
     * 尝试在给定时间内获取给定key的资源锁
     * @param key 要锁定的资源key
     * @param time 最大等待时间
     * @param unit 延时的时间单位
     * @return true代表获取锁成功,false失败
     */
    boolean tryLock(String key, long time, TimeUnit unit);

    /**
     * 释放给定key的资源锁
     * @param key 要释放锁的资源key
     * @return 解锁是否成功
     */
    boolean unlock(String key);

    /**
     * 强制释放资源锁,不会校验当前解锁线程是否为加锁线程
     * 一般用于加锁线程无法解锁之时的一个补救措施
     * @param key 要释放锁的资源key
     */
    void unlockForcedly(String key);

    /**
     * 锁定给定key的资源,如果无法获取锁那么线程会不停自旋,直到获取锁或者自旋时间毫秒超过{@link Long#MAX_VALUE}为止
     * @param key 要锁定的资源
     */
    default void lock(String key) {
        this.tryLock(key, Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
}
