package com.miracle.common.validate;

import com.miracle.common.utils.JsonUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Description:参数校验器抽象实现
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 * @param <T> 被校验的参数类型
 */
@SuppressWarnings("unchecked")
abstract class AbstractValidator<V, T> {

    /**
     * {@link #value}为{@code null}时的默认错误信息。默认为{@value}
     */
    private static final String DEFAULT_NULL_VALUE_MESSAGE = "Invalid parameter for null.";

    /**
     * 错误信息为{@code null}时的错误信息:{@value}
     */
    static final String NULL_ERROR_CODE_MESSAGE = "Error code of null param is missing!";

    /**
     * 无错误码传入时对应的默认错误码字符串,值为{@value}
     */
    static final String NO_ERROR_CODE  = "";

    /**
     * 要校验的参数对象
     */
    final T value;

    /**
     * 校验对象为空时返回的错误码
     */
    private final String nullValueCode;

    /**
     * 用于存储错误信息,如果校验无错那么这个值是{@code null}
     */
    private final Set<ErrorEntry> errorEntries;

    /**
     * 用于判断是否属于快速校验状态
     * 如果是快速校验状态,那么当{@link #errorEntries}不为empty时会跳过后续一切校验步骤
     */
    private final boolean fastValidate;

    /**
     * 构造器
     * @param value 被校验的值
     * @param nullValueCode 空值错误码
     * @param fastValidate 是否是快速校验模式
     */
    AbstractValidator(T value, String nullValueCode, boolean fastValidate) {
        this.value = value;
        this.nullValueCode = nullValueCode;
        this.errorEntries = new HashSet<>(16);
        this.fastValidate = fastValidate;
    }

    /**
     * 是否继续验证,不跳过后续的验证步骤
     * @return true代表继续, false反之
     */
    private boolean keepValidating() {
        return this.errorEntries.isEmpty() || !this.fastValidate;
    }

    /**
     * 设置错误信息
     * @param errCode 错误码
     * @param errMsg 错误信息
     */
    private void setError(String errCode, String errMsg) {
        this.errorEntries.add(new ErrorEntry(errCode, errMsg));
    }

    /**
     * 判断校验结果是否通过
     * @return true为通过,false反之
     */
    boolean isValid() {
        return this.errorEntries.isEmpty();
    }

    /**
     * 检查{@link #value}是否为{@code null}
     */
    void checkValue() {
        // 要对参数本身进行校验
        if (this.value != null) {
            return;
        }
        this.errorEntries.add(new ErrorEntry(this.nullValueCode, DEFAULT_NULL_VALUE_MESSAGE));
    }

    /**
     * 判断mapper返回值非空
     * @param mapper 传入的mapper
     * @param errorMsg 错误信息
     * @param <R> mapper返回的类型
     * @return 返回更新后的校验者
     */
    public <R> V notNull(Function<? super T, ? extends R> mapper, String errorMsg) {
        return this.notNull(mapper, errorMsg, NO_ERROR_CODE);
    }

    /**
     * 判断mapper返回值非空
     * @param mapper 传入的mapper
     * @param errorMsg 错误信息
     * @param errorCode 错误码
     * @param <R> mapper返回的类型
     * @return 返回更新后的校验者
     */
    public <R> V notNull(Function<? super T, ? extends R> mapper,
                                             String errorMsg,
                                             String errorCode) {
        this.checkValue();
        if (this.keepValidating() && this.value != null && mapper.apply(this.value) == null) {
            this.setError(errorCode, errorMsg);
        }
        return (V) this;
    }

    /**
     * 自定义校验方式
     * 当传入的{@code predicate}通过时视作校验成功
     * @param predicate 传入的断言
     * @param errorMsg 错误信息
     * @return 返回更新后的校验者
     */
    public V on(Predicate<? super T> predicate, String errorMsg) {
        return this.on(predicate, errorMsg, NO_ERROR_CODE);
    }

    /**
     * 自定义校验方式
     * 当传入的{@code predicate}通过时视作校验成功
     * @param predicate 传入的断言
     * @param errorMsg 错误信息
     * @param errorCode 错误码
     * @return 返回更新后的校验者
     */
    public V on(Predicate<? super T> predicate, String errorMsg, String errorCode) {
        this.checkValue();
        if (this.keepValidating() && !predicate.test(this.value)) {
            this.setError(errorCode, errorMsg);
        }
        return (V) this;
    }

    /**
     * 自定义校验方式,当满足条件时才进行校验
     * @param predicate 校验断言
     * @param errorMsg 错误信息
     * @param condition 校验的条件
     * @return 返回更新后的校验者
     */
    public V onIf(Predicate<? super T> predicate, String errorMsg, Predicate<? super T> condition) {
        return this.onIf(predicate, errorMsg, condition, NO_ERROR_CODE);
    }

    /**
     * 自定义校验方式,当满足条件时才进行校验
     * @param predicate 校验断言
     * @param errorMsg 错误信息
     * @param condition 校验的条件
     * @param errorCode 错误码
     * @return 返回更新后的校验者
     */
    public V onIf(Predicate<? super T> predicate,
                                      String errorMsg,
                                      Predicate<? super T> condition,
                                      String errorCode) {
        this.checkValue();
        if (this.keepValidating() && condition.test(this.value) && !predicate.test(this.value)) {
            this.setError(errorCode, errorMsg);
        }
        return (V) this;
    }

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getErrMsg() {
        return JsonUtils.toJSONString(this.errorEntries);
    }

    /**
     * 封装错误信息的实体类
     */
    private static class ErrorEntry {

        /**
         * 错误码
         */
        private final String code;

        /**
         * 错误信息
         */
        private final String message;

        ErrorEntry(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ErrorEntry)) {
                return false;
            }
            ErrorEntry that = (ErrorEntry) o;
            return Objects.equals(code, that.code) &&
                    Objects.equals(message, that.message);
        }

        @Override
        public int hashCode() {

            return Objects.hash(code, message);
        }
    }
}
