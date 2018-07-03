package com.miracle.common.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:用于一个进程内的资源锁
 *
 * @author guobin On date 2018/7/1.
 * @version 1.0
 * @since jdk 1.8
 */
public class LocalResourceLock extends AbstractResourceLock {

    private final Map<String, String> lockPool = new ConcurrentHashMap<>(16);

    public LocalResourceLock() {
        super();
    }

    public LocalResourceLock(long retryDelay) {
        super(retryDelay);
    }

    @Override
    protected boolean doLock(String key, String lockerId) {
        return this.lockPool.putIfAbsent(key, lockerId) == null;
    }

    @Override
    protected boolean doUnlock(String key) {
        return this.lockPool.remove(key) != null;
    }

    @Override
    protected String getLockingThreadId(String key) {
        return this.lockPool.get(key);
    }
}
