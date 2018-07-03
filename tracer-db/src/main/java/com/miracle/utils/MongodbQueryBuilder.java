package com.miracle.utils;

import com.miracle.data.po.data.BasePO;
import com.miracle.function.TriConsumer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Description: mongodb的工具类,用于构造{@link Query}
 *
 * @author guobin On date 2018/7/2.
 * @version 1.0
 * @since jdk 1.8
 */
public class MongodbQueryBuilder {

    /**
     * mongdb的查询对象
     */
    private final Query query;

    /**
     * 外界不能直接构造
     */
    private MongodbQueryBuilder() {
        this.query = new Query();
    }

    private MongodbQueryBuilder(Object idValue) {
        this.query = new Query(Criteria.where(BasePO.Column.ID).is(idValue));
    }

    /**
     * 构造一个新的mongodb查询类
     * @return mongodb查询类
     */
    public static MongodbQueryBuilder newQuery() {
        return new MongodbQueryBuilder();
    }

    /**
     * 构造一个带id的mongodb查询类
     * @param idValue id的值
     * @return builder
     */
    public static MongodbQueryBuilder newQueryWithId(Object idValue) {
        return idValue == null ? new MongodbQueryBuilder() : new MongodbQueryBuilder(idValue);
    }

    /**
     * 构造id查询器
     * @param idValue id的值
     * @return 查询器
     */
    public static Query buildIdQuerier(Object idValue) {
        return idValue == null ? new Query() : new Query(Criteria.where(BasePO.Column.ID).is(idValue));
    }

    /**
     * 增加一个等值查询条件
     * @param columnName 字段名
     * @param value 比较的值
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder is(String columnName, Object value) {
        Optional.ofNullable(value)
                .map(Criteria.where(columnName)::is)
                .ifPresent(this.query::addCriteria);
        return this;
    }

    /**
     * 增加一个小于查询条件
     * @param columnName 字段名
     * @param value 比较的值
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder lt(String columnName, Object value) {
        Optional.ofNullable(value)
                .map(Criteria.where(columnName)::lt)
                .ifPresent(this.query::addCriteria);
        return this;
    }

    /**
     * 增加一个不大于查询条件
     * @param columnName 字段名
     * @param value 比较的值
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder lte(String columnName, Object value) {
        Optional.ofNullable(value)
                .map(Criteria.where(columnName)::lte)
                .ifPresent(this.query::addCriteria);
        return this;
    }

    /**
     * 增加一个大于查询条件
     * @param columnName 字段名
     * @param value 比较的值
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder gt(String columnName, Object value) {
        Optional.ofNullable(value)
                .map(Criteria.where(columnName)::gt)
                .ifPresent(this.query::addCriteria);
        return this;
    }

    /**
     * 增加一个不小于查询条件
     * @param columnName 字段名
     * @param value 比较的值
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder gte(String columnName, Object value) {
        Optional.ofNullable(value)
                .map(Criteria.where(columnName)::gte)
                .ifPresent(this.query::addCriteria);
        return this;
    }

    /**
     * 增加一个左右闭区间查询条件,如果左/右端点为{@code null}会分别被视作负无穷/正无穷
     * @param columnName 字段名
     * @param min 左端点
     * @param max 右端点
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder gteAndLte(String columnName, Object min, Object max) {
        this.range(Criteria::gte, Criteria::lte).accept(columnName, min, max);
        return this;
    }

    /**
     * 增加一个左开右闭区间查询条件,如果左/右端点为{@code null}会分别被视作负无穷/正无穷
     * @param columnName 字段名
     * @param min 左端点
     * @param max 右端点
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder gtAndLte(String columnName, Object min, Object max) {
        this.range(Criteria::gt, Criteria::lte).accept(columnName, min, max);
        return this;
    }

    /**
     * 增加一个左开右闭区间查询条件,如果左/右端点为{@code null}会分别被视作负无穷/正无穷
     * @param columnName 字段名
     * @param min 左端点
     * @param max 右端点
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder gteAndLt(String columnName, Object min, Object max) {
        this.range(Criteria::gte, Criteria::lt).accept(columnName, min, max);
        return this;
    }

    /**
     * 增加一个左右开区间查询条件,如果左/右端点为{@code null}会分别被视作负无穷/正无穷
     * @param columnName 字段名
     * @param min 左端点
     * @param max 右端点
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder gtAndLt(String columnName, Object min, Object max) {
        this.range(Criteria::gt, Criteria::lt).accept(columnName, min, max);
        return this;
    }

    /**
     * 分页查询
     * @param pageable 分页请求
     * @return 返回构造者自身
     */
    public MongodbQueryBuilder pageQuery(Pageable pageable) {
        Optional.ofNullable(pageable).ifPresent(this.query::with);
        return this;
    }

    /**
     * 返回{@link Query}
     * @return 构造完毕的mongodb查询对象
     */
    public Query get() {
        return this.query;
    }

    /**
     * 根据给定的边界定义进行范围查询
     * @param left 左边界定义
     * @param right 右边界定义
     * @return 消费三个参数的函数
     */
    private TriConsumer<String, Object, Object> range(BiConsumer<Criteria, Object> left,
                                                      BiConsumer<Criteria, Object> right) {
        return (columnName, min, max) -> {
            final Criteria criteria = Criteria.where(columnName);
            Optional.ofNullable(min).ifPresent(v -> left.accept(criteria, v));
            Optional.ofNullable(max).ifPresent(v -> right.accept(criteria, v));
            this.query.addCriteria(criteria);
        };
    }

}
