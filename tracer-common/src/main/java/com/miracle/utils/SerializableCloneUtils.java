package com.miracle.utils;

import java.io.*;

/**
 * Description: 利用{@link Serializable}来深拷贝的工具类
 * 通过序列化进行copy的速度是比较慢的
 * 可以用于dozer中对无默认构造器类的转化
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class SerializableCloneUtils {

    private SerializableCloneUtils(){}

    /**
     * 深度拷贝一份数据
     * @param src 源数据
     * @param <T> 数据的类型
     * @return 拷贝出来的数据
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepClone(T src) {
        //为空直接返回
        if (src == null) {
            return null;
        }

        T dist = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeObject(src);
            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                dist = (T) in.readObject();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dist;
    }
}
