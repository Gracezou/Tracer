package com.miracle.repoitory.utils;

import com.miracle.data.po.data.BasePO;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

/**
 * Description: mongodb的工具类,用于构造{@link Update}
 *
 * @author guobin On date 2018/7/3.
 * @version 1.0
 * @since jdk 1.8
 */
public class MongodbUpdateBuilder {

    /**
     * mongodb中的数据更新对象
     */
    private final Update update;

    private MongodbUpdateBuilder() {
        this.update = new Update();
    }

    /**
     * 构造一个新的mongodb更新器建造者
     * @return mongodb更新器建造者
     */
    public static MongodbUpdateBuilder newUpdate() {
        return new MongodbUpdateBuilder();
    }

    /**
     * 设置某个更新字段的值
     * @param columnName 字段名
     * @param value 要更新的值
     * @return builder自身
     */
    public MongodbUpdateBuilder set(String columnName, Object value) {
        Optional.ofNullable(value).ifPresent(v -> this.update.set(columnName, value));
        return this;
    }

    /**
     * 得到构造完毕的{@link Update}类
     * @return mongodb的更新对象
     */
    public Update get() {
        final long currentTimestamp = System.currentTimeMillis();
        return this.update
                .setOnInsert(BasePO.Column.CREATE_TIME, currentTimestamp)
                .set(BasePO.Column.LAST_MODIFIED, currentTimestamp);
    }
}
