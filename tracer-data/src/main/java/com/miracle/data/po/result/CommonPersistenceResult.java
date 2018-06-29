package com.miracle.data.po.result;

import com.miracle.data.common.BaseExceptionResult;
import com.miracle.utils.ListUtils;

import java.util.Collections;
import java.util.List;

/**
 * Description:po层的返回集基础类
 * 每一个继承于该类的子类应该只负载一种数据list,数据将会被封装在{@link #values}之中
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 * @param <E> 负载的数据类型
 */
public class CommonPersistenceResult<E> extends BaseExceptionResult {

    /**
     * 数据持有列表
     */
    private List<E> values;

    public List<E> getValues() {
        return values;
    }

    public void setValues(List<E> values) {
        // 返回结果中的数据是只读的
        this.values = Collections.unmodifiableList(values);
    }

    /**
     * 获取当前持有数据的第一个对象
     * @return 返回元素
     */
    public E getValue() {
        return ListUtils.getFirst(this.values);
    }
}
