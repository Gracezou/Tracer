package com.miracle.data;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Description:用于测试的类
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class JsonTestItem {

    private String strValue;

    private int intValue;

    private long longValue;

    private boolean boolValue;

    private Date date;

    private byte[] bytes;

    private List<LocalDateTime> list;

    private Map<String, LocalDateTime> map;

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public List<LocalDateTime> getList() {
        return list;
    }

    public void setList(List<LocalDateTime> list) {
        this.list = list;
    }

    public Map<String, LocalDateTime> getMap() {
        return map;
    }

    public void setMap(Map<String, LocalDateTime> map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonTestItem)) return false;
        JsonTestItem that = (JsonTestItem) o;
        return intValue == that.intValue &&
                longValue == that.longValue &&
                boolValue == that.boolValue &&
                Objects.equals(strValue, that.strValue) &&
                Objects.equals(date, that.date) &&
                Arrays.equals(bytes, that.bytes) &&
                Objects.equals(list, that.list) &&
                Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(strValue, intValue, longValue, boolValue, date, list, map);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}
