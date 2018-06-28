package com.miracle.utils;

import com.miracle.base.BaseTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Description:{@link ListUtils}的测试类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class ListUtilsTest extends BaseTest {

    @Test
    public void getFirst() {
        // 1.有值且唯一
        final Item item = new Item("item", 1);
        final List<Item> singletonList = Collections.singletonList(item);
        assertEquals(item, ListUtils.getFirst(singletonList));

        // 2.有值但不唯一
        final List<Item> commonList = Arrays.asList(item, new Item("grace", 2));
        assertEquals(item, ListUtils.getFirst(commonList));

        // 3.list is empty
        assertNull(ListUtils.getFirst(Collections.emptyList()));

        // 4.list is null
        assertNull(ListUtils.getFirst(null));
    }

    private static class Item {

        private String name;

        private int id;

        Item(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;
            Item item = (Item) o;
            return id == item.id &&
                    Objects.equals(name, item.name);
        }

        @Override
        public int hashCode() {

            return Objects.hash(name, id);
        }
    }
}