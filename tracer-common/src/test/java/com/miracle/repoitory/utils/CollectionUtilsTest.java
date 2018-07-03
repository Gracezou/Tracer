package com.miracle.repoitory.utils;

import com.miracle.base.BaseTest;
import com.miracle.common.utils.CollectionUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Description:{@link CollectionUtils}的测试类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class CollectionUtilsTest extends BaseTest {

    @Test
    public void isNullOrEmpty() {
        assertTrue(CollectionUtils.isNullOrEmpty(null));
        assertTrue(CollectionUtils.isNullOrEmpty(Collections.emptyList()));
        assertTrue(CollectionUtils.isNullOrEmpty(Collections.emptySet()));
        assertFalse(CollectionUtils.isNullOrEmpty(Collections.singletonList("yes")));
        assertFalse(CollectionUtils.isNullOrEmpty(Collections.singleton("yes")));
    }

    @Test
    public void notNullOrEmpty() {
        assertFalse(CollectionUtils.notNullOrEmpty(null));
        assertFalse(CollectionUtils.notNullOrEmpty(Collections.emptyList()));
        assertFalse(CollectionUtils.notNullOrEmpty(Collections.emptySet()));
        assertTrue(CollectionUtils.notNullOrEmpty(Collections.singletonList("yes")));
        assertTrue(CollectionUtils.notNullOrEmpty(Collections.singleton("yes")));
    }

    @Test
    public void toHashMapWithoutMerger() {
        // 1. list to map
        final List<LocalDateTime> list = Stream.iterate(LocalDateTime.of(2018, 6, 28, 0, 0, 0), t -> t.plusHours(1L))
                .limit(10)
                .collect(Collectors.toList());
        final Map<LocalDate, LocalTime> mapFromList = CollectionUtils.toHashMap(list, LocalDateTime::toLocalDate, LocalDateTime::toLocalTime);
        assertNotNull(mapFromList);
        assertEquals(1, mapFromList.size());
        // 默认策略下相同的key永远保留最后的value
        assertEquals(LocalTime.of(9, 0, 0), mapFromList.get(LocalDate.of(2018, 6, 28)));

        // 2. null list or set
        final Map<LocalDate, LocalTime> mapFromNull = CollectionUtils.toHashMap(null, LocalDateTime::toLocalDate, LocalDateTime::toLocalTime);
        assertNull(mapFromNull);

        // 3.emptyList
        final Map<LocalDate, LocalTime> mapFromEmptyList = CollectionUtils.toHashMap(Collections.emptyList(), LocalDateTime::toLocalDate, LocalDateTime::toLocalTime);
        assertNotNull(mapFromEmptyList);
        assertTrue(mapFromEmptyList.isEmpty());

        // 4.set to map
        final Set<LocalDateTime> set = Stream.iterate(LocalDateTime.of(2018, 6, 28, 0, 0, 0), t -> t.plusHours(1L))
                .limit(10)
                .collect(Collectors.toSet());
        final Map<LocalDate, LocalTime> mapFromSet = CollectionUtils.toHashMap(list, LocalDateTime::toLocalDate, LocalDateTime::toLocalTime);
        assertNotNull(mapFromList);
        assertEquals(1, mapFromList.size());
        // 默认策略下相同的key永远保留最后的value
        assertEquals(LocalTime.of(9, 0, 0), mapFromList.get(LocalDate.of(2018, 6, 28)));

        // 5.empty set
        final Map<LocalDate, LocalTime> mapFromEmptySet = CollectionUtils.toHashMap(Collections.emptyList(), LocalDateTime::toLocalDate, LocalDateTime::toLocalTime);
        assertNotNull(mapFromEmptySet);
        assertTrue(mapFromEmptySet.isEmpty());
    }

    @Test
    public void toHashMapWithMerger() {
        // merge的情况只有在list情况下才会发生,所以只测list
        // 1. list to map
        final List<LocalDateTime> list = Stream
                .iterate(LocalDateTime.of(2018, 6, 28, 0, 0, 0), t -> t.plusHours(1L))
                .limit(10)
                .collect(Collectors.toList());
        // 设置策略为保留最旧数据
        final Map<LocalDate, LocalTime> mapFromList =
                CollectionUtils.toHashMap(list, LocalDateTime::toLocalDate, LocalDateTime::toLocalTime, (o1, o2) -> o1);
        assertNotNull(mapFromList);
        assertEquals(1, mapFromList.size());
        // 默认策略下相同的key永远保留最后的value
        assertEquals(LocalTime.of(0, 0, 0), mapFromList.get(LocalDate.of(2018, 6, 28)));

        // 2. null list or set
        final Map<LocalDate, LocalTime> mapFromNull = CollectionUtils
                .toHashMap(null, LocalDateTime::toLocalDate, LocalDateTime::toLocalTime, (o1, o2) -> o1);
        assertNull(mapFromNull);

        // 3.emptyList
        final Map<LocalDate, LocalTime> mapFromEmptyList = CollectionUtils
                .toHashMap(Collections.emptyList(), LocalDateTime::toLocalDate, LocalDateTime::toLocalTime, (o1, o2) -> o1);
        assertNotNull(mapFromEmptyList);
        assertTrue(mapFromEmptyList.isEmpty());
    }
}