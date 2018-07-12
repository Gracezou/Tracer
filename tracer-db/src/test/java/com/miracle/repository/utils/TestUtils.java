package com.miracle.repository.utils;

import com.alibaba.fastjson.JSON;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:test工具类
 *
 * @author guobin On date 2018/7/10.
 * @version 1.0
 * @since jdk 1.8
 */
public class TestUtils {

    /**
     * 不允许实例化
     */
    private TestUtils() {}

    /**
     * 解析已经准备好的JSON文件,获取数据
     * @param path 文件路径
     * @param clazz 解析的类
     * @param <T> 类型
     * @return 解析出来的list
     */
    public static <T> List<T> parsePrepareData(String path , Class<T> clazz) {
        return Optional.ofNullable(path).map(pathUrl -> {

            List<T> dataList = null;

            try {
                String url = TestUtils.class.getResource(pathUrl).getPath();
                Stream<String> stringStream = Files.lines(Paths.get(url), Charset.defaultCharset());
                String dataStr = stringStream.filter( str -> str != null && !str.isEmpty()).collect(Collectors.joining());

                dataList = JSON.parseArray(dataStr, clazz);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return dataList;
        }).orElse(null);
    }
}
