package com.miracle.validate;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Description:参数校验器
 * 在使用时除了设置校验条件,还必须设置校验成功执行函数,这个函数会返回值
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 * @param <T> 被校验的参数类型
 */
public class AppliableValidator<T> extends AbstractValidator<T> {

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
     * 判断mapper返回值非空
     * @param mapper 传入的mapper
     * @param errorMsg 错误信息
     * @param <R> mapper返回的类型
     * @return 返回更新后的校验者
     */
    public <R> AppliableValidator<T> notNull(Function<? super T, ? extends R> mapper, String errorMsg) {
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
    public <R> AppliableValidator<T> notNull(Function<? super T, ? extends R> mapper,
                                                String errorMsg,
                                                String errorCode) {
        this.checkValue();
        if (this.keepValidating() && this.value != null && mapper.apply(this.value) == null) {
            this.setError(errorCode, errorMsg);
        }
        return this;
    }

    /**
     * 自定义校验方式
     * 当传入的{@code predicate}通过时视作校验成功
     * @param predicate 传入的断言
     * @param errorMsg 错误信息
     * @return 返回更新后的校验者
     */
    public AppliableValidator<T> on(Predicate<? super T> predicate, String errorMsg) {
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
    public AppliableValidator<T> on(Predicate<? super T> predicate, String errorMsg, String errorCode) {
        this.checkValue();
        if (this.keepValidating() && !predicate.test(this.value)) {
            this.setError(errorCode, errorMsg);
        }
        return this;
    }

    /**
     * 自定义校验方式,当满足条件时才进行校验
     * @param predicate 校验断言
     * @param errorMsg 错误信息
     * @param condition 校验的条件
     * @return 返回更新后的校验者
     */
    public AppliableValidator<T> onIf(Predicate<? super T> predicate, String errorMsg, Predicate<? super T> condition) {
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
    public AppliableValidator<T> onIf(Predicate<? super T> predicate,
                                       String errorMsg,
                                       Predicate<? super T> condition,
                                      String errorCode) {
        this.checkValue();
        if (this.keepValidating() && condition.test(this.value) && !predicate.test(this.value)) {
            this.setError(errorCode, errorMsg);
        }
        return this;
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
