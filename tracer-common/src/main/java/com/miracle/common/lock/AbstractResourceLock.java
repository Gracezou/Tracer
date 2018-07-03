package com.miracle.common.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description:资源锁的抽象实现
 * 大致思路是每个资源key会记录对应一个由{@link ThreadLocal<String>}所保存的自定义线程id,这个id通过{@link UUID}生成
 * 每个线程在第一次进入资源锁之时都会被初始化一个自定义线程id
 * 该资源锁是一个非公平锁
 *
 * @author guobin On date 2018/7/1.
 * @version 1.0
 * @since jdk 1.8
 */
public abstract class AbstractResourceLock implements ResourceLock {

    /**
     * 默认在争抢锁失败之后每{@value}毫秒重试一次
     */
    private static final long DEFAULT_RETRY_DELAY = 50L;

    /**
     * 资源锁id的前缀{@value}
     */
    private static final String LOCK_ID_PREFIX = "lock.mark#";

    /**
     * 线程的信息,用于为每个线程赋值一个id
     */
    private final ThreadLocal<String> localThreadInfo = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    /**
     * 锁获取失败之时的重试间隔
     */
    private final long retryDelay;

    public AbstractResourceLock() {
        this(DEFAULT_RETRY_DELAY);
    }

    public AbstractResourceLock(long retryDelay) {
        this.retryDelay = retryDelay;
    }

    @Override
    public boolean tryLock(String key) {
        return this.doLock(this.buildResourceKey(key), this.getCurrentThreadId());
    }

    @Override
    public boolean tryLock(String key, long time, TimeUnit unit) {
        if (time <= 0) {
            return tryLock(key);
        }
        boolean result;
        final String storeKey = this.buildResourceKey(key);
        final String lockingThreadId = this.getCurrentThreadId();
        final long start = System.currentTimeMillis();
        while (!(result = this.doLock(storeKey, lockingThreadId))) {
            this.await();
            if ((System.currentTimeMillis() - start) < unit.toMillis(time)) {
                break;
            }
        }
        return result;
    }

    @Override
    public boolean unlock(String key) {
        final String storeKey = this.buildResourceKey(key);
        final String unlockingThreadId = this.getCurrentThreadId();
        final String lockingThreadId = this.getLockingThreadId(storeKey);
        if (lockingThreadId == null) {
            // 说明资源并没有被锁定
            return true;
        }
        if (!lockingThreadId.equals(unlockingThreadId)) {
            // 解锁线程不是加锁线程
            return false;
        }
        final boolean result = this.doUnlock(storeKey);
        if (result) {
            this.localThreadInfo.remove();
        }
        return result;
    }

    @Override
    public void unlockForcedly(String key) {
        if (this.doUnlock(this.buildResourceKey(key))) {
            this.localThreadInfo.remove();
        }
    }

    /**
     * 构造资源锁的key
     * @param key 业务key
     * @return 资源锁的key
     */
    private String buildResourceKey(String key) {
        return LOCK_ID_PREFIX + key;
    }

    /**
     * 获取当前自定义线程id
     * @return 自定义线程id
     */
    private String getCurrentThreadId() {
        return this.localThreadInfo.get();
    }

    /**
     * 当前线程进行休眠
     */
    private void await() {
        try {
            Thread.sleep(this.retryDelay);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 执行加锁
     * @param key 锁定的key
     * @param lockerId 锁定线程id
     * @return true表示锁定成功,false失败
     */
    protected abstract boolean doLock(String key, String lockerId);

    /**
     * 执行解锁
     * @param key 解锁的key
     * @return true表示解锁成功,false失败
     */
    protected abstract boolean doUnlock(String key);

    /**
     * 获取加锁线程的id
     * @param key 查询的key
     * @return 加锁线程的id,为{@code null}说明并未加锁
     */
    protected abstract String getLockingThreadId(String key);

}
