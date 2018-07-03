package com.miracle.common.validate;

import com.miracle.base.BaseTest;
import com.miracle.data.JsonTestItem;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Description:{@link AcceptableValidator}的测试类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class AcceptableValidatorTest extends BaseTest {

    private static final int INIT_VALUE = 5;

    @Test
    public void nullSuccessHandler() {
        final Counter counter1 = new Counter();
        try {
            AcceptableValidator.of(null)
                    .validate(null, (i, s) -> {
                        counter1.msg = s;
                        counter1.count--;
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void nullErrorHandler() {
        final Counter counter1 = new Counter();
        try {
            AcceptableValidator.of(null)
                    .validate(i -> counter1.count++, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void nullValueCode() {
        final Counter counter1 = new Counter();

        try {
            AcceptableValidator.of(null, null)
                    .validate(i -> counter1.count++, (i, s) -> {
                        counter1.msg = s;
                        counter1.count--;
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void testNonNullParam() {
        final Counter counter1 = new Counter();
        // 1.参数本身为空,不给定错误码
        AcceptableValidator.of(null)
                .validate(i -> counter1.count++, (i, s) -> {
                    counter1.msg = s;
                    counter1.count--;
                });
        assertNotNull(counter1.msg);
        System.out.println(counter1.msg);
        assertEquals(INIT_VALUE - 1, counter1.count);

        final Counter counter2 = new Counter();
        // 1.参数本身为空,给定错误码
        AcceptableValidator.of(null, "001")
                .validate(i -> counter1.count++, (i, s) -> {
                    counter2.msg = s;
                    counter2.count--;
                });
        assertNotNull(counter2.msg);
        System.out.println(counter2.msg);
        assertTrue(counter2.msg.contains("001"));
        assertEquals(INIT_VALUE - 1, counter2.count);
    }

    @Test
    public void testNonNullField() {
        final Counter counter1 = new Counter();
        final JsonTestItem item = new JsonTestItem();
        // 缺失字段信息
        AcceptableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .validate(i -> counter1.count++, (i, s) -> {
                    counter1.msg = s;
                    counter1.count--;
                });
        assertNotNull(counter1.msg);
        assertTrue(counter1.msg.contains("日期不能为空"));
        assertEquals(INIT_VALUE - 1, counter1.count);

        // 缺失字段,设置错误码
        final Counter counter2 = new Counter();
        AcceptableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空", "002")
                .validate(i -> counter2.count++, (i, s) -> {
                    counter2.msg = s;
                    counter2.count--;
                });
        assertNotNull(counter2.msg);
        assertTrue(counter2.msg.contains("日期不能为空"));
        assertTrue(counter2.msg.contains("002"));
        assertEquals(INIT_VALUE - 1, counter2.count);

        final Counter counter3 = new Counter();
        // 补全对应字段后
        item.setDate(new Date());
        AcceptableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .validate(i -> counter3.count++, (i, s) -> {
                    counter3.msg = s;
                    counter3.count--;
                });
        assertNull(counter3.msg);
        assertEquals(INIT_VALUE + 1, counter3.count);
    }

    @Test
    public void testOnCondition() {
        final Counter counter1 = new Counter();
        final JsonTestItem item = new JsonTestItem();
        item.setIntValue(20);
        // 未满足校验条件
        AcceptableValidator.of(item)
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .validate(i -> counter1.count++, (i, s) -> {
                    counter1.msg = s;
                    counter1.count--;
                });
        assertNotNull(counter1.msg);
        assertTrue(counter1.msg.contains("int值必须小于10"));
        assertEquals(INIT_VALUE - 1, counter1.count);

        final Counter counter2 = new Counter();
        // 未满足校验条件,设置错误码
        AcceptableValidator.of(item)
                .on(i -> i.getIntValue() < 10, "int值必须小于10", "003")
                .validate(i -> counter2.count++, (i, s) -> {
                    counter2.msg = s;
                    counter2.count--;
                });
        assertNotNull(counter2.msg);
        assertTrue(counter2.msg.contains("int值必须小于10"));
        assertTrue(counter2.msg.contains("003"));
        assertEquals(INIT_VALUE - 1, counter2.count);

        // 满足校验条件,校验通过
        item.setIntValue(5);
        final Counter counter3 = new Counter();
        AcceptableValidator.of(item)
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .validate(i -> counter3.count++, (i, s) -> {
                    counter3.msg = s;
                    counter3.count--;
                });
        assertNull(counter3.msg);
        assertEquals(INIT_VALUE + 1, counter3.count);
    }

    @Test
    public void testOnIfCondition() {
        final JsonTestItem item = new JsonTestItem();

        // 不满足判断条件
        final Counter counter1 = new Counter();
        AcceptableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(i -> counter1.count++, (i, s) -> {
                    counter1.msg = s;
                    counter1.count--;
                });
        assertNull(counter1.msg);
        assertEquals(INIT_VALUE + 1, counter1.count);

        // 满足判断条件,但校验条件不满足
        item.setStrValue("check");
        item.setIntValue(1);
        final Counter counter2 = new Counter();
        AcceptableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(i -> counter2.count++, (i, s) -> {
                    counter2.msg = s;
                    counter2.count--;
                });
        assertNotNull(counter2.msg);
        assertTrue(counter2.msg.contains("int值和long值必须在有字符串的情况下相等"));
        assertEquals(INIT_VALUE - 1, counter2.count);

        // 满足判断条件,但校验条件不满足,设置错误码
        final Counter counter3 = new Counter();
        AcceptableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null, "004")
                .validate(i -> counter3.count++, (i, s) -> {
                    counter3.msg = s;
                    counter3.count--;
                });
        assertNotNull(counter3.msg);
        assertTrue(counter3.msg.contains("int值和long值必须在有字符串的情况下相等"));
        assertTrue(counter3.msg.contains("004"));
        assertEquals(INIT_VALUE - 1, counter3.count);

        // 满足判断条件,满足校验条件,校验通过
        item.setLongValue(1);
        final Counter counter4 = new Counter();
        AcceptableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null, "004")
                .validate(i -> counter4.count++, (i, s) -> {
                    counter4.msg = s;
                    counter4.count--;
                });
        assertNull(counter4.msg);
        assertEquals(INIT_VALUE + 1, counter4.count);
    }

    @Test
    public void testFastValidate() {
        final JsonTestItem item = new JsonTestItem();
        item.setIntValue(20);
        item.setStrValue("check");

        // 正常的校验模式
        final Counter counter1 = new Counter();
        AcceptableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(i -> counter1.count++, (i, s) -> {
                    counter1.msg = s;
                    counter1.count--;
                });
        assertNotNull(counter1.msg);
        assertTrue(counter1.msg.contains("日期不能为空"));
        assertTrue(counter1.msg.contains("int值必须小于10"));
        assertTrue(counter1.msg.contains("int值和long值必须在有字符串的情况下相等"));
        assertEquals(INIT_VALUE - 1, counter1.count);

        // 快速校验模式
        final Counter counter2 = new Counter();
        AcceptableValidator.of(item, true)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(i -> counter2.count++, (i, s) -> {
                    counter2.msg = s;
                    counter2.count--;
                });
        assertNotNull(counter2.msg);
        assertTrue(counter2.msg.contains("日期不能为空"));
        assertFalse(counter2.msg.contains("int值必须小于10"));
        assertFalse(counter2.msg.contains("int值和long值必须在有字符串的情况下相等"));
        assertEquals(INIT_VALUE - 1, counter2.count);
    }

    private static class Counter {
        int count = INIT_VALUE;
        String msg;
    }
}