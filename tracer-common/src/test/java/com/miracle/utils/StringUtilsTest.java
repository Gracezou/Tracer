package com.miracle.utils;

import com.miracle.base.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Description:
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class StringUtilsTest extends BaseTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void isEmptyOrNull() {
        assertTrue(StringUtils.isEmptyOrNull(null));
        assertTrue(StringUtils.isEmptyOrNull(""));
        assertTrue(StringUtils.isEmptyOrNull("         "));
        assertFalse(StringUtils.isEmptyOrNull("not empty"));
        assertFalse(StringUtils.isEmptyOrNull(" not empty"));
    }

    @Test
    public void notEmptyOrNull() {
        assertTrue(StringUtils.notEmptyOrNull("not empty"));
        assertTrue(StringUtils.notEmptyOrNull(" not empty"));
        assertFalse(StringUtils.notEmptyOrNull(null));
        assertFalse(StringUtils.notEmptyOrNull(""));
        assertFalse(StringUtils.notEmptyOrNull("        "));
    }

    @Test
    public void randomString() {
        int length = new Random().nextInt(512);
        String str = StringUtils.randomString(length);
        System.out.println(str);
        assertEquals(length, str.length());

        assertEquals(0 , StringUtils.randomString(0).length());
        assertEquals(0, StringUtils.randomString(-1).length());
    }

    @Test
    public void format() {
        // 1.基础字符串为null,没有参数
        assertNull(StringUtils.format(null));
        // 2.基础字符串为null,有参数
        assertNull(StringUtils.format(null, "no base str"));
        // 3.基础字符串为null,有参数但是为null
        assertNull(StringUtils.format(null, null, null));
        // 4.基础字符串没有占位符,没有参数
        final String noPlaceholder = "no_placeholder";
        assertEquals(noPlaceholder, StringUtils.format(noPlaceholder));
        // 5.基础字符串没有占位符,有参数
        assertEquals(noPlaceholder, StringUtils.format(noPlaceholder, "args"));
        // 6.基础字符串没有占位符,有参数但是全部为null
        assertEquals(noPlaceholder, StringUtils.format(noPlaceholder, null, null));
        // 7.基础字符串有占位符但是占位符中没有数字,不是有效占位符,没有参数
        final String hasInvalidPlaceholder = "has_{}_placeholder";
        assertEquals(hasInvalidPlaceholder, StringUtils.format(hasInvalidPlaceholder));
        // 8.基础字符串有占位符但是占位符中没有数字,不是有效占位符,有参数
        assertEquals(hasInvalidPlaceholder, StringUtils.format(hasInvalidPlaceholder, "args"));
        // 9.基础字符串有占位符但是占位符中没有数字,不是有效占位符,有参数但都是null
        assertEquals(hasInvalidPlaceholder, StringUtils.format(hasInvalidPlaceholder, null, null));
        // 10.基础字符串有占位符,没有参数
        final String hasValidPlaceholder = "has_{0}_{1}_placeholder";
        assertEquals(hasValidPlaceholder, StringUtils.format(hasValidPlaceholder));
        // 11.基础字符串有占位符,有参数但是参数数量少于占位符数量
        assertEquals("has_one_{1}_placeholder", StringUtils.format(hasValidPlaceholder, "one"));
        // 12.基础字符串有占位符,有参数但是参数数量等于占位符数量
        assertEquals("has_one_two_placeholder", StringUtils.format(hasValidPlaceholder, "one", "two"));
        // 13.基础字符串有占位符,有参数但是有一个参数是null
        assertEquals("has_one_null_placeholder", StringUtils.format(hasValidPlaceholder, "one", null));
        // 14.基础字符串有占位符,有参数但是所有都是null
        assertEquals("has_null_null_placeholder", StringUtils.format(hasValidPlaceholder, null, null));
        // 15.基础字符串有占位符,有参数,参数数量多于占位符
        assertEquals("has_one_two_placeholder", StringUtils.format(hasValidPlaceholder, "one", "two", "three"));
        // 16.基础字符串有占位符,但是占位符中有重复索引,有参数
        final String hasDuplicateIndexPlaceholder = "has_{0}_{1}_{1}_placeholder";
        assertEquals("has_one_two_two_placeholder", StringUtils.format(hasDuplicateIndexPlaceholder, "one", "two"));
    }
}