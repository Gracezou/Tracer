package com.miracle.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * Description:对JSON类型数据的工具类
 * 当前采用fastjson进行JSON的解析与序列化
 * 这个类的意义在于日后JSON底层实现替换时更为方便,只需改这个类
 *
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class JsonUtils {

    /**
     * 不允许实例化
     */
    private JsonUtils() {}

    /**
     * 将数据转化成JSON字符串
     * @param obj 要转化的对象
     * @return JSON字符串,如果序列化出错会返回{@code null}
     */
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 将一个JSON字符串解析成指定对象数据
     * @param jsonString json字符串
     * @param valueType 元素对应的类型对象
     * @param <T> 指定对象的类型
     * @return 指定对象
     */
    public static <T> T parseObject(String jsonString, Class<T> valueType) {
        return JSON.parseObject(jsonString, valueType);
    }

    /**
     * 将JSON字符串解析成{@link List},默认会解析成{@link java.util.ArrayList}
     * @param jsonString 字符串
     * @param valueType 元素对应的类型对象
     * @param <E> list中的元素类型
     * @return 解析出来的list
     */
    public static <E> List<E> parseList(String jsonString, Class<E> valueType) {
        return JSON.parseArray(jsonString, valueType);
    }

    /**
     * 将JSON字符串解析成{@link Map},默认会被解析成{@link java.util.HashMap}
     * @param jsonString 字符串
     * @param <K> key的类型
     * @param <V> value的类型
     * @return 解析出来的map
     */
    public static <K, V> Map<K, V> parseMap(String jsonString, TypeReference<Map<K, V>> typeReference) {
        return JSON.parseObject(jsonString, typeReference);
    }
}
