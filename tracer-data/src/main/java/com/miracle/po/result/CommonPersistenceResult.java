package com.miracle.po.result;

import com.miracle.constant.ResultConstant;
import com.miracle.utils.ListUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Description:po层的返回集基础类
 * 每一个继承于该类的子类应该只负载一种数据list,数据将会被封装在{@link #values}之中
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 * @param <E> 负载的数据类型
 */
public class CommonPersistenceResult<E> {

    /**
     * 操作的状态码
     * @see com.miracle.constant.ResultConstant.Code
     */
    private int code;

    /**
     * 返回结果的信息
     * @see com.miracle.constant.ResultConstant.Message
     */
    private String message;

    /**
     * 数据持有列表
     */
    private List<E> values;

    /**
     * {@link #values}的数量
     */
    private int count;

    /**
     * 符合条件的数据总数量
     */
    private int totalCount;

    /**
     * 异常
     */
    private Exception exception;

    /**
     * 表示当前数据所在页码数
     */
    private int page;

    /**
     * 返回结果的构造者
     * 所有该类的子类,其属性的setter方法均只提供包私有权限,外界即便自行new出也无法正常使用
     * 所有结果均需要采用这个方法返回的建造者进行构造
     * @param supplier 返回结果的构造函数
     * @param <V> 返回结果持有数据类型
     * @param <R> 返回结果类型
     * @return 返回结果
     */
    public static <V, R extends CommonPersistenceResult<V>> ResultBuilder<V, R> resultBuilder(Supplier<R> supplier) {
        return new ResultBuilder<>(supplier);
    }

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
        return new ResultBuilder<>(supplier)
                .code(ResultConstant.Code.SUCCESS)
                .page(page)
                .totalCount(totalCount)
                .values(values)
                .buildResult();
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
        return new ResultBuilder<>(supplier)
                .code(ResultConstant.Code.ERROR)
                .message(errMsg)
                .exception(ex)
                .buildResult();
    }

    void setCode(int code) {
        this.code = code;
    }

    void setMessage(String message) {
        this.message = message;
    }

    void setValues(List<E> values) {
        this.values = values;
    }

    void setCount(int count) {
        this.count = count;
    }

    void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    void setException(Exception exception) {
        this.exception = exception;
    }

    void setPage(int page) {
        this.page = page;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<E> getValues() {
        return values;
    }

    public int getCount() {
        return count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Exception getException() {
        return exception;
    }

    public int getPage() {
        return page;
    }

    /**
     * 获取当前持有数据的第一个对象
     * @return 返回元素
     */
    public E getValue() {
        return ListUtils.getFirst(this.values);
    }

    /**
     * 判断结果是否成功
     * @return 判断结果
     */
    public boolean isSuccess() {
        return this.code == ResultConstant.Code.SUCCESS;
    }

    /**
     * 判断是否失败
     * @return 判断结果
     */
    public boolean isFailed() {
        return this.code != ResultConstant.Code.SUCCESS;
    }

    /**
     * 返回结果的构造类
     * @param <V> 持有数据类型
     * @param <R> 返回结果的类型
     */
    static class ResultBuilder<V, R extends CommonPersistenceResult<V>> {

        /**
         * 返回结果
         */
        private final R result;

        ResultBuilder(Supplier<R> supplier) {
            this.result = supplier.get();
        }

        /**
         * 设置操作码
         * @param code 操作码
         * @return 建造者
         */
        public ResultBuilder<V, R> code(int code) {
            this.result.setCode(code);
            return this;
        }

        /**
         * 设置信息,如果是一条成功的返回结果可以跳过这个步骤,因为在设置{@code values}的时候会一并设置{@code message}
         * @param message 返回信息
         * @return 建造者
         */
        public ResultBuilder<V, R> message(String message) {
            this.result.setMessage(message);
            return this;
        }

        /**
         * 设置返回结果的页码数
         * @param page 页码数
         * @return 建造者
         */
        public ResultBuilder<V, R> page(int page) {
            this.result.setPage(page);
            return this;
        }

        /**
         * 设置数据总数
         * @param totalCount 总数
         * @return 建造者
         */
        public ResultBuilder<V, R> totalCount(int totalCount) {
            this.result.setTotalCount(totalCount);
            return this;
        }

        /**
         * 设置异常,一般只是在错误结果中设置
         * @param ex 异常
         * @return 建造者
         */
        public ResultBuilder<V, R> exception(Exception ex) {
            this.result.setException(ex);
            return this;
        }

        /**
         * 设置返回持有list,在{@link #result}中会被设置成只读list,同时会一并设置{@code count}和{@code message}
         * @param values 传入的数据list
         * @return 建造者
         */
        public ResultBuilder<V, R> values(List<V> values) {
            final List<V> checkedList = Optional.ofNullable(values).orElseGet(Collections::emptyList);
            final String message = checkedList.isEmpty() ?
                    ResultConstant.Message.NOT_FOUND : ResultConstant.Message.SUCCESS;
            // 存入result中的列表是只读的,不能被修改
            this.result.setValues(Collections.unmodifiableList(checkedList));
            this.result.setCount(checkedList.size());
            this.result.setMessage(message);
            return this;
        }

        /**
         * 得到最终的结果
         * @return 结果
         */
        public R buildResult() {
            return this.result;
        }
    }
}
