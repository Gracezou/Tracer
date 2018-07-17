package com.miracle.common.validate;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Description:参数校验器
 * 在使用时除了设置校验条件,还必须设置校验成功执行函数,这个函数会返回值
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 * @param <T> 被校验的参数类型
 */
public class AppliableValidator<T> extends AbstractValidator<AppliableValidator<T>, T> {

    /**
     * 构造器
     * @param value 待校验的值
     * @param nullValueCode 空值错误码
     * @param fastValidate 是否是快速验证模式
     */
    private AppliableValidator(T value, String nullValueCode, boolean fastValidate) {
        super(value, nullValueCode, fastValidate);
    }

    /**
     * 返回一个带传入值的{@code AcceptableValidator}
     *
     * @param value 带校验的对象
     * @param <P> 对象的类型
     * @return 一个校验者
     */
    public static <P> AppliableValidator<P> of(P value) {
        return of(value, false);
    }

    /**
     * 返回一个带传入值的{@code AcceptableValidator}
     * @param value 带校验的对象
     * @param nullValueCode 当校验对象为空时返回的错误码
     * @param <P> 对象的类型
     * @return 一个校验者
     */
    public static <P> AppliableValidator<P> of(P value, String nullValueCode) {
        return of(value, nullValueCode, false);
    }

    /**
     * 返回一个带传入值的{@code AcceptableValidator}
     *
     * @param value 带校验的对象
     * @param fastValidate 是否是快速校验模式
     * @param <P> 对象的类型
     * @return 一个校验者
     */
    public static <P> AppliableValidator<P> of(P value, boolean fastValidate) {
        return of(value, NO_ERROR_CODE, fastValidate);
    }

    /**
     * 返回一个带传入值的{@code AcceptableValidator}
     *
     * @param value 带校验的对象
     * @param <P> 对象的类型
     * @param nullValueCode 当校验对象为空时返回的错误码
     * @param fastValidate 是否是快速校验模式
     * @return 一个校验者
     * @throws NullPointerException 当传入的空对象错误码为空时抛出异常
     */
    public static <P> AppliableValidator<P> of(P value, String nullValueCode, boolean fastValidate) {
        if (nullValueCode == null) {
            throw new NullPointerException(NULL_ERROR_CODE_MESSAGE);
        }
        return new AppliableValidator<>(value, nullValueCode, fastValidate);
    }

    /**
     * 执行验证逻辑
     * @param successHandler 验证成功的执行函数
     * @param errorHandler 验证失败的执行函数
     * @param <R> 函数执行的返回结果类型
     * @return 返回结果
     * @throws NullPointerException 当{@code successHandler}为{@code null}时抛出
     * @throws NullPointerException 当{@code #errorHandler}为{@code null}时抛出
     */
    public <R> R validate(Function<T, R> successHandler, BiFunction<T, String, R> errorHandler) {
        if (successHandler == null) {
            throw new NullPointerException("The success mapper must not be null.");
        }
        if (errorHandler == null) {
            throw new NullPointerException("The error mapper must not be null.");
        }
        this.checkValue();
        return this.isValid() ? successHandler.apply(this.value) : errorHandler.apply(this.value, this.getErrMsg());
    }
}
