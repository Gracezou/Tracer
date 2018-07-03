package com.miracle.common.utils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    /**
     * 判断一个集合是否包含指定元素
     * @param collection 集合
     * @param obj 指定的元素
     * @param <E> 元素类型
     * @return true代表包含,false代表不包含
     */
    public static <E> boolean isContaining(Collection<E> collection, E obj) {
        return collection != null && collection.contains(obj);
    }

    /**
     * 将一个{@link Collection}转化成{@link HashMap}
     * @param collection 集合
     * @param keyGenerator key生成器
     * @param valueGenerator value生成器
     * @param <K> key的类型
     * @param <V> value的类型
     * @param <U> 集合中的数据类型
     * @return 转化之后的map
     */
    public static <K, V, U> Map<K, V> toHashMap(Collection<U> collection,
                                                Function<U, K> keyGenerator,
                                                Function<U, V> valueGenerator) {
        return toHashMap(collection, keyGenerator, valueGenerator, CollectionUtils::defaultMerger);
    }

    /**
     * 将一个{@link Collection}转化成{@link HashMap}
     * @param collection 集合
     * @param keyGenerator key生成器
     * @param valueGenerator value生成器
     * @param merger 合并的函数
     * @param <K> key的类型
     * @param <V> value的类型
     * @param <U> 集合中的数据类型
     * @return 转化之后的map
     */
    public static <K, V, U> Map<K, V> toHashMap(Collection<U> collection,
                                                Function<U, K> keyGenerator,
                                                Function<U, V> valueGenerator,
                                                BinaryOperator<V> merger) {
        return toMap(collection, keyGenerator, valueGenerator, merger, () -> new HashMap<>(collection.size()));
    }

    /**
     * 将一个{@link Collection}转化成{@link Map}
     * @param collection 集合
     * @param keyGenerator key生成器
     * @param valueGenerator value生成器
     * @param merger 合并的函数
     * @param <K> key的类型
     * @param <V> value的类型
     * @param <U> 集合中的数据类型
     * @return 转化之后的map
     */
    public static <K, V, U> Map<K, V> toMap(Collection<U> collection,
                                            Function<U, K> keyGenerator,
                                            Function<U, V> valueGenerator,
                                            BinaryOperator<V> merger,
                                            Supplier<Map<K, V>> mapSupplier) {
        if (collection == null) {
            return null;
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(keyGenerator, valueGenerator, merger, mapSupplier));
    }

    /**
     * 默认的合并方式
     * @param oldValue 旧值
     * @param newValue 新值
     * @param <V> 值的类型
     * @return 合并之后的值
     */
    private static <V> V defaultMerger(V oldValue, V newValue) {
        return newValue;
    }
}
