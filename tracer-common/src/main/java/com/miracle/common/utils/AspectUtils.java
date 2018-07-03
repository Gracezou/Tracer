package com.miracle.common.utils;

import com.miracle.data.po.result.CommonPersistenceResult;
import com.miracle.data.po.result.PersistenceResultFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.Supplier;

/**
 * Description:用于aop的工具类
 *
 * @author guobin On date 2018/7/3.
 * @version 1.0
 * @since jdk 1.8
 */
public class AspectUtils {

    /**
     * 不允许实例化
     */
    private AspectUtils() {}

    /**
     * 连接点指定包装函数
     * @param joinPoint aop连接点
     * @param supplier 返回结果函数构造器
     * @param <V> 返回结果承载的数据类型
     * @param <R> 返回结果类型
     * @return 返回结果
     */
    @SuppressWarnings("unchecked")
    public static <V, R extends CommonPersistenceResult<V>> R proceedJoinPoint(ProceedingJoinPoint joinPoint,
                                                                               Supplier<R> supplier) {
        R result;
        try {
            result = (R) joinPoint.proceed();
        } catch (Throwable throwable) {
            result = PersistenceResultFactory.errorResult(supplier, new RuntimeException(throwable));
        }
        return result;
    }

    /**
     * 从连接点中获取第一个参数
     * @param joinPoint 连接点
     * @param <T> 参数类型
     * @return 返回的参数
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFirstArgFromJoinPoint(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        return (T) args[0];
    }
}
