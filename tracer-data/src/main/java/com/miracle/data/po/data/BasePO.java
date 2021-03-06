package com.miracle.data.po.data;

import com.miracle.data.Jsonable;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Map;

/**
 * Description:所有po类的父类
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public abstract class BasePO implements Serializable, Jsonable {

    private static final long serialVersionUID = -3034569703573083989L;

    public static class Column {

        public static final String ID = "_id";

        public static final String CREATE_TIME = "create_time";

        public static final String LAST_MODIFIED = "last_modified";
    }

    /**
     * 数据创建时间
     */
    @Field(Column.CREATE_TIME)
    protected Long createTime;

    /**
     * 数据最近一次的修改时间
     */
    @Field(Column.LAST_MODIFIED)
    protected Long lastModified;

    /**
     * 转化成字段-值的字符串map
     * @return 字段-值的字符串map
     */
    public abstract Map<String, String> toStringMap();

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "BasePO{" +
                "createTime=" + createTime +
                ", lastModified=" + lastModified +
                '}';
    }
}
