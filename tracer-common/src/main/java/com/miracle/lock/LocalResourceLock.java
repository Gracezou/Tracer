package com.miracle.lock;

/**
 * Description:用于一个进程内的资源锁
 *
 * @author guobin On date 2018/7/1.
 * @version 1.0
 * @since jdk 1.8
 */
public class LocalResourceLock extends AbstractResourceLock {

    @Override
    protected boolean doLock(String key, String lockerId) {
        return false;
    }

    @Override
    protected boolean doUnlock(String key) {
        return false;
    }

    @Override
    protected boolean isLocked(String key) {
        return false;
    }
}
