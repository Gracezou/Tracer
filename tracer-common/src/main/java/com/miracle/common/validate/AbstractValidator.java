package com.miracle.common.validate;

import com.miracle.common.utils.JsonUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:参数校验器抽象实现
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 * @param <T> 被校验的参数类型
 */
abstract class AbstractValidator<T> {

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
    private final List<ErrorEntry> errorEntries;

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
        this.errorEntries = new LinkedList<>();
        this.fastValidate = fastValidate;
    }

    /**
     * 是否继续验证,不跳过后续的验证步骤
     * @return true代表继续, false反之
     */
    boolean keepValidating() {
        return this.errorEntries.isEmpty() || !this.fastValidate;
    }

    /**
     * 设置错误信息
     * @param errCode 错误码
     * @param errMsg 错误信息
     */
    void setError(String errCode, String errMsg) {
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
    }
}
