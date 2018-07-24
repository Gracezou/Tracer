package com.miracle.data;

import com.alibaba.fastjson.JSON;

/**
 * Description: 表示一条数据可被转化成JSONString
 *
 * @author guobin On date 2018/7/24.
 * @version 1.0
 * @since jdk 1.8
 */
public interface Jsonable {

    /**
     * 将数据转化成JSONString
     * @return JSONString
     */
    default String toJSONString() {
        return JSON.toJSONString(this);
    }
}
