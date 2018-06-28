package com.miracle.utils;

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
}
