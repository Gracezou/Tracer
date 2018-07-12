package com.miracle.repository.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description:表示数据库写入操作
 *
 * @author guobin On date 2018/7/3.
 * @version 1.0
 * @since jdk 1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteAction {
}
