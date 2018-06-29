package com.miracle.po.result;

import com.miracle.common.ResultConstant;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Description:{@link CommonPersistenceResult}及其子类的构建工厂类
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public class PersistenceResultFactory {

    /**
     * 不允许实例化
     */
    private PersistenceResultFactory() {}

    /**
     * 构造成功的返回结果
     * @param supplier 返回结果的构建函数
     * @param values 持有对象list
     * @param page 当前页码
     * @param totalCount 数据总数
     * @param <V> 数据类型
     * @param <R> 返回结果类型
     * @return 返回结果
     */
    public static <V, R extends CommonPersistenceResult<V>> R successResult(Supplier<R> supplier,
                                                                            List<V> values,
                                                                            int page,
                                                                            int totalCount) {
        final R result = supplier.get();
        final List<V> checkedList = Optional.ofNullable(values).orElseGet(Collections::emptyList);
        final String message = checkedList.isEmpty() ?
                ResultConstant.Message.NOT_FOUND : ResultConstant.Message.SUCCESS;
        result.setCode(ResultConstant.Code.SUCCESS);
        result.setMessage(message);
        result.setPage(page);
        result.setTotalCount(totalCount);
        result.setCount(checkedList.size());
        result.setValues(checkedList);

        return result;
    }

    /**
     * 构造错误的返回结果
     * @param supplier 返回结果的构造函数
     * @param <V> 数据类型
     * @param <R> 返回结果类型
     * @return 返回结果
     */
    public static <V, R extends CommonPersistenceResult<V>> R errorResult(Supplier<R> supplier) {
        return errorResult(supplier, ResultConstant.Message.ERROR, null);
    }

    /**
     * 构造错误的返回结果
     * @param supplier 返回结果的构造函数
     * @param errMsg 错误信息
     * @param <V> 数据类型
     * @param <R> 返回结果类型
     * @return 返回结果
     */
    public static <V, R extends CommonPersistenceResult<V>> R errorResult(Supplier<R> supplier,
                                                                          String errMsg) {
        return errorResult(supplier, errMsg, null);
    }

    /**
     * 构造错误的返回结果
     * @param supplier 返回结果的构造函数
     * @param ex 错误异常
     * @param <V> 数据类型
     * @param <R> 返回结果类型
     * @return 返回结果
     */
    public static <V, R extends CommonPersistenceResult<V>> R errorResult(Supplier<R> supplier,
                                                                          Exception ex) {
        return errorResult(supplier, ResultConstant.Message.ERROR, ex);
    }

    /**
     * 构造错误的返回结果
     * @param supplier 返回结果的构造函数
     * @param errMsg 错误信息
     * @param ex 错误异常
     * @param <V> 数据类型
     * @param <R> 返回结果类型
     * @return 返回结果
     */
    public static <V, R extends CommonPersistenceResult<V>> R errorResult(Supplier<R> supplier,
                                                                          String errMsg,
                                                                          Exception ex) {
        final R result = supplier.get();
        result.setMessage(errMsg);
        result.setCode(ResultConstant.Code.ERROR);
        result.setException(ex);
        return result;
    }
}
