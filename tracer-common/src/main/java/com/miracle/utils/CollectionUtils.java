package com.miracle.utils;

import java.util.Collection;
import java.util.Optional;

/**
 * Description:{@link Collection}的工具类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class CollectionUtils {

    /**
     * 不允许实例化
     */
    private CollectionUtils() {}

    /**
     * 判断一个集合是否为{@code null}或者空
     * @param collection 集合
     * @return true表示集合为{@null}或者是空列表,false反之
     */
    public static boolean isNullOrEmpty(Collection collection) {
        return Optional.ofNullable(collection)
                .map(Collection::isEmpty)
                .orElse(true);
    }

    /**
     * 判断一个集合是否不为{@code null}或者不为空
     * @param collection 集合
     * @return true表示集合不为{@null}或者不是空列表,false反之
     */
    public static boolean notNullOrEmpty(Collection collection) {
        return Optional.ofNullable(collection)
                .map(c -> !c.isEmpty())
                .orElse(false);
    }
}
