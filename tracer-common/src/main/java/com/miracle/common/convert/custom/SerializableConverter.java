package com.miracle.common.convert.custom;

import com.miracle.common.utils.SerializableCloneUtils;
import org.dozer.CustomConverter;

import java.io.Serializable;

/**
 * Description: 如果转化的对象是可序列化的,那么用序列化的方式深克隆出一份数据
 *
 * @author guobin On date 2018/5/16.
 * @version 1.0
 * @since jdk 1.8
 */
public class SerializableConverter implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue,
                          Object sourceFieldValue,
                          Class<?> destinationClass,
                          Class<?> sourceClass) {
        if (destinationClass == null || sourceClass == null) {
            return existingDestinationFieldValue;
        }

        if (sourceFieldValue == null) {
            return existingDestinationFieldValue;
        }

        if (sourceFieldValue instanceof Serializable) {
            // 如果实现了可序列化接口,那么就用序列化深克隆
            existingDestinationFieldValue = SerializableCloneUtils.deepClone((Serializable) sourceFieldValue);
        }

        return existingDestinationFieldValue;
    }
}
