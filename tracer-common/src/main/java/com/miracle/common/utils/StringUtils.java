package com.miracle.common.utils;

import java.util.Optional;
import java.util.Random;

/**
 * Description: String 工具类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class StringUtils {

    /**
     * 字符数组
     */
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'f', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'
    };

    private static final char LEFT_BRACE = '{';
    private static final char RIGHT_BRACE = '}';

    /**
     * 不允许实例化
     */
    private StringUtils() { }


    /**
     * 字符串是否为空或者null
     * @param str 字符串
     * @return 判断结果
     */
    public static boolean isEmptyOrNull(String str) {
        return Optional.ofNullable(str)
                .map(String::trim)
                .map(String::isEmpty)
                .orElse(true);
    }

    /**
     * 判断字符串是否非空以及非null
     * @param str 字符串
     * @return 判断结果
     */
    public static boolean notEmptyOrNull(String str) {
        return Optional.ofNullable(str)
                .map(String::trim)
                .map(string -> !string.isEmpty())
                .orElse(false);
    }

    /**
     * 获取指定长度的字符串
     * @param length 长度
     * @return 生成的字符串
     */
    public static String randomString(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        while (length-- > 0) {
            int index = new Random().nextInt(CHARS.length);
            sb.append(CHARS[index]);
        }
        return sb.toString();
    }

    /**
     * 用给定的{@code args}来替换给定基础字符串中的占位符
     * 占位符形式为"{1}",大括号中的数字对应的是{@code args}中数据的索引
     * {@code
     *      String newString = StringUtils.format("{0}-{1}-2018", "Grace", "Miracle");
     *      // newString = "Grace-Miracle-2018
     * }
     * 之所以不用{@link String#replaceAll(String, String)}等一类的方法,是因为该方法用了正则表达式
     * @param baseString 基础字符串,带占位符
     * @param args 变量数组
     * @return 替换占位符之后的字符串
     */
    public static String format(String baseString, Object... args) {
        if (baseString == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        final char[] chars = baseString.toCharArray();
        for (int i = 0 ; i < chars.length ; i++) {
            char c = chars[i];
            if (c == LEFT_BRACE) {
                final StringBuilder indexBuffer = new StringBuilder();
                int j;
                for (j = i + 1; j < chars.length && chars[j] != RIGHT_BRACE ; j++) {
                    indexBuffer.append(chars[j]);
                }
                final String s = indexBuffer.toString();
                final int index = notEmptyOrNull(s) ? Integer.parseInt(s) : -1;
                if (index >= 0 && index < args.length) {
                    sb.append(args[index]);
                } else {
                    sb.append(LEFT_BRACE)
                            .append(indexBuffer)
                            .append(RIGHT_BRACE);
                }
                // 从"}"所在索引继续遍历
                i = j;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
