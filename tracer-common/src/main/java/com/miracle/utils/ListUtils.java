package com.miracle.utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Description:{@link List}的工具类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class ListUtils {

    /**
     * 不允许实例化
     */
    private ListUtils() {}

    /**
     * 获取list的第一个元素
     * @param list 源list
     * @param <T> list中元素的类型
     * @return 取出的元素
     */
    public static <T> T getFirst(List<T> list) {
        return Optional.ofNullable(list)
                .filter(l -> !l.isEmpty())
                .map(values -> values.get(0))
                .orElse(null);
    }

    /**
     * 得到一个非{@code null}的list
     * 如果传入的list不为{@code null}则返回自身,为{@code null}则返回{@link Collections#emptyList()}
     * @param list 传入的list
     * @param <T> 数据类型
     * @return 校验后的list
     */
    public static <T> List<T> notNullOrEmptyList(List<T> list) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList);
    }
}
