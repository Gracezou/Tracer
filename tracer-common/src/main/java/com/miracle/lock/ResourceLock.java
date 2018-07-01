package com.miracle.lock;

import java.util.concurrent.TimeUnit;

/**
 * Description:资源锁接口
 *
 * @author guobin On date 2018/7/1.
 * @version 1.0
 * @since jdk 1.8
 */
public interface ResourceLock {

    void lock(String key);

    boolean tryLock(String key);

    boolean tryLock(String key, long time, TimeUnit unit);

    void unlock(String key);
}
