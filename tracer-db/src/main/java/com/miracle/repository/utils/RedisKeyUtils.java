package com.miracle.repository.utils;

/**
 * Description:redis-key的工具类
 *
 * @author guobin On date 2018/7/3.
 * @version 1.0
 * @since jdk 1.8
 */
public class RedisKeyUtils {

    /**
     * 项目缓存名
     */
    private static final String PROJECT_NAME = "tracer_cache";

    /**
     * key的分隔符
     */
    private static final String DIVIDER = "/";

    /**
     * 不允许实例化
     */
    private RedisKeyUtils() { }

    /**
     * 构造redis的key
     * @param args 参数数组
     * @return 构造出来的key
     */
    public static String buildKey(String... args) {
        return String.join(DIVIDER, PROJECT_NAME, String.join(DIVIDER, args));
    }
}
