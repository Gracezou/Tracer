package com.miracle.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description:资源锁的抽象实现
 *
 * @author guobin On date 2018/7/1.
 * @version 1.0
 * @since jdk 1.8
 */
public abstract class AbstractResourceLock implements ResourceLock {

    private final ThreadLocal<String> localThreadInfo = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    @Override
    public void lock(String key) {

    }

    @Override
    public boolean tryLock(String key) {
        return false;
    }

    @Override
    public boolean tryLock(String key, long time, TimeUnit unit) {
        return false;
    }

    @Override
    public void unlock(String key) {

    }

    protected abstract boolean doLock(String key, String lockerId);

    protected abstract boolean doUnlock(String key);

    protected abstract boolean isLocked(String key);

}
