package com.miracle.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Description:对JSON类型数据的工具类
 * 当前采用Jackson进行JSON的解析与序列化
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class JsonUtils {

    /**
     * jackson的解析类
     */
    private static final ObjectMapper JACKSON_MAPPER;

    /*
     * 初始化jackson的解析类
     */
    static {
        JACKSON_MAPPER = new ObjectMapper();
        // 如果属性没有值，那么Json是会处理的，int类型为0，String类型为null，数组为[]，设置这个特性可以忽略空值属性
        JACKSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

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
        try {
            return JACKSON_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * 将一个JSON字符串解析成指定对象数据
     * @param jsonString json字符串
     * @param <T> 指定对象的类型
     * @return 指定对象
     */
    public static <T> T parseObject(String jsonString) {
        try {
            return JACKSON_MAPPER.readValue(jsonString, new TypeReference<T>(){});
        } catch (Exception ex) {
            throw new JsonParseException(ex);
        }
    }

    /**
     * 将JSON字符串解析成{@link List},默认会解析成{@link java.util.ArrayList}
     * @param jsonString 字符串
     * @param <E> list中的元素类型
     * @return 解析出来的list
     */
    public static <E> List<E> parseList(String jsonString) {
        try {
            return JACKSON_MAPPER.readValue(jsonString, new TypeReference<List<E>>(){});
        } catch (Exception ex) {
            throw new JsonParseException(ex);
        }
    }

    /**
     * 将JSON字符串解析成{@link Map},默认会被解析成{@link java.util.HashMap}
     * @param jsonString 字符串
     * @param <K> key的类型
     * @param <V> value的类型
     * @return 解析出来的map
     */
    public static <K, V> Map<K, V> parseMap(String jsonString) {
        try {
            return JACKSON_MAPPER.readValue(jsonString, new TypeReference<Map<K, V>>(){});
        } catch (Exception ex) {
            throw new JsonParseException(ex);
        }
    }

    /**
     * JSON解析异常类,继承一个{@link RuntimeException}
     */
    private static class JsonParseException extends RuntimeException {
        /**
         * 提供一个默认构造器,防止某些框架解析或者构造对象失败
         */
        public JsonParseException() { }

        JsonParseException(Throwable cause) {
            super(cause);
        }
    }
}
