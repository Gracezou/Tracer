package com.miracle.common.validate;

import com.miracle.base.BaseTest;
import com.miracle.data.JsonTestItem;
import org.junit.Test;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * Description:{@link AppliableValidator}的测试类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class AppliableValidatorTest extends BaseTest {

    private static final Function<JsonTestItem, String> SUCCESS_HANDLER = i -> null;
    private static final BiFunction<JsonTestItem, String, String> ERROR_HANDLER = (i, s) -> s;

    @Test
    public void nullSuccessHandler() {
        JsonTestItem item = new JsonTestItem();
        try {
            AppliableValidator.of(item)
                    .validate(null, ERROR_HANDLER);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void nullErrorHandler() {
        JsonTestItem item = new JsonTestItem();
        try {
            AppliableValidator.of(item)
                    .validate(SUCCESS_HANDLER, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void nullValueCode() {
        JsonTestItem item = new JsonTestItem();
        try {
            AppliableValidator.of(item, null)
                    .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void testNonNullParam() {
        JsonTestItem item = null;
        // 1.参数本身为空,不给定错误码
        String errMsg = AppliableValidator.of(item)
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        System.out.println(errMsg);

        // 1.参数本身为空,给定错误码
        errMsg = AppliableValidator.of(item, "001")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("001"));
    }

    @Test
    public void testNonNullField() {
        final JsonTestItem item = new JsonTestItem();
        // 缺失字段信息
        String errMsg = AppliableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("日期不能为空"));

        // 缺失字段,设置错误码
        errMsg = AppliableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空", "002")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("日期不能为空"));
        assertTrue(errMsg.contains("002"));

        // 补全对应字段后
        item.setDate(new Date());
        errMsg = AppliableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNull(errMsg);
    }

    @Test
    public void testOnCondition() {
        final JsonTestItem item = new JsonTestItem();
        item.setIntValue(20);
        // 未满足校验条件
        String errMsg = AppliableValidator.of(item)
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("int值必须小于10"));

        // 未满足校验条件,设置错误码
        errMsg = AppliableValidator.of(item)
                .on(i -> i.getIntValue() < 10, "int值必须小于10", "003")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("int值必须小于10"));
        assertTrue(errMsg.contains("003"));

        // 满足校验条件,校验通过
        item.setIntValue(5);
        errMsg = AppliableValidator.of(item)
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNull(errMsg);
    }

    @Test
    public void testOnIfCondition() {
        final JsonTestItem item = new JsonTestItem();

        // 不满足判断条件
        String errMsg = AppliableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNull(errMsg);

        // 满足判断条件,但校验条件不满足
        item.setStrValue("check");
        item.setIntValue(1);
        errMsg = AppliableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("int值和long值必须在有字符串的情况下相等"));

        // 满足判断条件,但校验条件不满足,设置错误码
        errMsg = AppliableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null, "004")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("int值和long值必须在有字符串的情况下相等"));
        assertTrue(errMsg.contains("004"));

        // 满足判断条件,满足校验条件,校验通过
        item.setLongValue(1);
        errMsg = AppliableValidator.of(item)
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null, "004")
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNull(errMsg);
    }

    @Test
    public void testFastValidate() {
        final JsonTestItem item = new JsonTestItem();
        item.setIntValue(20);
        item.setStrValue("check");

        // 正常的校验模式
        String errMsg = AppliableValidator.of(item)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("日期不能为空"));
        assertTrue(errMsg.contains("int值必须小于10"));
        assertTrue(errMsg.contains("int值和long值必须在有字符串的情况下相等"));

        // 快速校验模式
        errMsg = AppliableValidator.of(item, true)
                .notNull(JsonTestItem::getDate, "日期不能为空")
                .on(i -> i.getIntValue() < 10, "int值必须小于10")
                .onIf(i -> i.getIntValue() == (int) i.getLongValue(), "int值和long值必须在有字符串的情况下相等", i -> i.getStrValue() != null)
                .validate(SUCCESS_HANDLER, ERROR_HANDLER);
        assertNotNull(errMsg);
        assertTrue(errMsg.contains("日期不能为空"));
        assertFalse(errMsg.contains("int值必须小于10"));
        assertFalse(errMsg.contains("int值和long值必须在有字符串的情况下相等"));
    }
}