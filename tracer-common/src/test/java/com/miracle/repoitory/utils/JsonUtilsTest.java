package com.miracle.repoitory.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.miracle.base.BaseTest;
import com.miracle.common.utils.JsonUtils;
import com.miracle.data.JsonTestItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Description:{@link JsonUtils}的测试类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class JsonUtilsTest extends BaseTest {

    private JsonTestItem item;

    @Before
    public void setUp() {
        this.item = buildItem();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void toJSONString() {
        String jsonString = JsonUtils.toJSONString(this.item);
        System.out.println(jsonString);
        assertNotNull(jsonString);
        JsonTestItem newItem = JSON.parseObject(jsonString, JsonTestItem.class);
        assertEquals(this.item, newItem);
    }

    @Test
    public void parseObject() {
        LocalDateTime now = LocalDateTime.now();
        String str = JsonUtils.toJSONString(now);
        LocalDateTime that = JsonUtils.parseObject(str, LocalDateTime.class);
        assertEquals(now, that);
    }

    @Test
    public void parseList() {
        List<LocalDateTime> localDateTimes = JsonUtils.parseList(JSON.toJSONString(this.item.getList()), LocalDateTime.class);
        assertEquals(localDateTimes, this.item.getList());
    }

    @Test
    public void parseMap() {
        Map<String, LocalDateTime> map = JsonUtils.parseMap(JSON.toJSONString(this.item.getMap()),
                new TypeReference<Map<String, LocalDateTime>>(){});
        assertEquals(this.item.getMap(), map);
    }

    private JsonTestItem buildItem() {
        final JsonTestItem item = new JsonTestItem();
        item.setStrValue("tracer");
        item.setIntValue(1);
        item.setLongValue(2L);
        item.setBoolValue(true);
        item.setBytes(new byte[]{0, 1, 1, 0});
        item.setDate(new Date());
        LocalDateTime time = LocalDateTime.now();
        List<LocalDateTime> timeList = Stream.iterate(time, t -> t.plusHours(1L))
                .limit(5)
                .collect(Collectors.toList());
        item.setList(timeList);
        Map<String, LocalDateTime> map = Stream.iterate(time, t -> t.plusHours(2L))
                .limit(5)
                .collect(Collectors.toMap(LocalDateTime::toString, Function.identity(), (o1, o2) -> o2));
        item.setMap(map);
        return item;
    }
}