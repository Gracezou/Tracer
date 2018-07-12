package com.miracle.repository.base;

import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:tracer-repository项目的基础测试类,是基于mongodb的测试类
 *
 * @author guobin On date 2018/7/10.
 * @version 1.0
 * @since jdk 1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:ctx-base-test.xml", "classpath:ctx-mongo-test.xml"})
public abstract class BaseTracerMongodbTest {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 插入测试用的准备数据
     * @param preparedList 准备插入的数据
     * @param count 要插入的数据数量
     * @param <T> 数据类型
     */
    protected <T> void prepareTestData(List<T> preparedList, int count) {
        preparedList.stream().limit(count).forEach(this.mongoTemplate::insert);
    }

    /**
     * 删除测试用的数据
     * @param preparedList 先前为了测试而插入数据库的数据
     * @param <T> 数据类型
     */
    protected <T> void deleteTestData(List<T> preparedList) {
        preparedList.forEach(this.mongoTemplate::remove);
    }

    /**
     * 根据给定的期望元素list进行数据库查询匹配,查看数据库的返回结果是否与期望元素list全部匹配
     * @param expectedList 期望元素list
     * @param idSelector 期望元素的id选择器
     * @param expectedType 期望元素的类型对象
     * @param <T> 期望元素的类型
     * @return 判断结果
     */
    protected <T> boolean allEqualsToExepectedElements(List<T> expectedList,
                                                       Function<T, String> idSelector,
                                                       Class<T> expectedType) {
        return expectedList.stream().
                allMatch(expected -> this.equalsToExpectedElement(expected, idSelector, expectedType));
    }

    /**
     * 根据给定的期望元素进行数据库查询匹配,查看数据库的返回结果是否与期望元素list匹配
     * @param expected 期望元素对象
     * @param idSelector 期望元素的id选择器
     * @param expectedType 期望元素的类型对象
     * @param <T> 期望元素的类型
     * @return 判断结果
     */
    protected <T> boolean equalsToExpectedElement(T expected,
                                                  Function<T, String> idSelector,
                                                  Class<T> expectedType) {
        return Objects.equals(expected, this.mongoTemplate.findById(idSelector.apply(expected), expectedType));
    }

    /**
     * 根据给定的id列表来查询数据
     * @param ids id列表
     * @param expectedType 期望元素的类型对象
     * @param <T> 期望元素的类型
     * @return 判断结果
     */
    protected <T> List<T> queryDataByIds(List<String> ids, Class<T> expectedType) {
        return ids.stream()
                .map(id -> this.mongoTemplate.findById(id, expectedType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
