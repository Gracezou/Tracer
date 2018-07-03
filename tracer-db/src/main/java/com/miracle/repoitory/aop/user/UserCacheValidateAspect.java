package com.miracle.repoitory.aop.user;

import com.miracle.data.po.data.user.UserPO;
import com.miracle.data.po.request.user.UserPersistenceRequest;
import com.miracle.data.po.result.user.UserPersisitenceResult;
import com.miracle.repoitory.dao.user.UserDao;
import com.miracle.repoitory.utils.RedisKeyUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Description: {@link UserPO}的缓存操作切面
 * 缓存采用redis
 *
 * @author guobin On date 2018/7/3.
 * @version 1.0
 * @since jdk 1.8
 */
@Component("userCacheValidateAspect")
@Aspect
public class UserCacheValidateAspect {

    private static final Logger LOG = LoggerFactory.getLogger(UserCacheValidateAspect.class);

    @Resource
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public com.miracle.data.po.result.user.UserPersisitenceResult com.miracle.repoitory.dao.user.UserDao.*(..))")
    public void userDaoPoint() { }

    @Pointcut("userDaoPoint() && @annotation(com.miracle.repoitory.cache.annotation.WriteAction)")
    public void writeActionPoint() { }

    @Pointcut("userDaoPoint() && @annotation(com.miracle.repoitory.cache.annotation.ReadAction)")
    public void readActionPoint() { }

    /**
     * 数据保存的缓存切面处理
     * @param user 用户
     * @param result 写入操作方法执行之后的返回结果
     */
    @AfterReturning(value = "writeActionPoint() && args(user)", returning = "result", argNames = "user, result")
    public void afterSaving(UserPO user, UserPersisitenceResult result) {
        // 数据库操作失败的话不用进行缓存处理
        if (result.isFailed()) {
            return;
        }
        // db写入成功,删除原缓存。这里不直接写入,等缓存被读取时再写入,为的是防止写多读少的情况下对缓存频繁操作
        // 如果id不存在,说明是插入操作,无需删除缓存
        Optional.ofNullable(user.getId()).ifPresent(this::evictCache);
    }

    /**
     * 数据删除的缓存切面处理
     * @param id 删除的id
     * @param result 删除操作方法执行之后的返回结果
     */
    @AfterReturning(value = "writeActionPoint() && args(id)", returning = "result", argNames = "id, result")
    public void afterRemoving(String id, UserPersisitenceResult result) {
        // 数据库操作失败的话不用进行缓存处理
        if (result.isFailed()) {
            return;
        }
        this.evictCache(id);
    }

    /**
     * 数据查询的缓存切面处理
     * @param request 查询请求
     * @param result 查询操作方法执行之后的返回结果
     */
    @AfterReturning(value = "readActionPoint() && args(request)", returning = "result", argNames = "request, result")
    public void afterQuerying(UserPersistenceRequest request, UserPersisitenceResult result) {
        // 数据库操作失败的话不用进行缓存处理
        if (result.isFailed()) {
            return;
        }
        Optional.ofNullable(result.getValues()).ifPresent(list -> list.forEach(this::buildCache));
    }

    /**
     * 删除缓存
     * @param id 要删除的用户id
     */
    private void evictCache(String id) {
        final String key = RedisKeyUtils.buildKey(UserDao.USER_CACHE_PREFIX, id);
        try {
            this.redisTemplate.delete(key);
        } catch (Exception ex) {
            LOG.error("Failed to evict cache of {}", key);
            LOG.error("Failed to evict cache.", ex);
        }
    }

    /**
     * 构建缓存
     * @param user 用户数据
     */
    private void buildCache(UserPO user) {
        final String key = RedisKeyUtils.buildKey(UserDao.USER_CACHE_PREFIX, user.getId());
        try {
            this.redisTemplate.boundHashOps(key).putAll(user.toStringMap());
        } catch (Exception ex) {
            LOG.error("Failed to build cache of {}", key);
            LOG.error("Failed to build cache.", ex);
        }
    }
}
