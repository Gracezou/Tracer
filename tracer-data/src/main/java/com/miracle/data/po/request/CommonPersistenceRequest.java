package com.miracle.data.po.request;

import com.miracle.data.common.BaseCommonRequest;
import com.miracle.data.po.data.BasePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:po层的基础请求封装类
 * 因为项目用的是mongodb作为数据库,所以这里查询条件直接实现{@link Pageable}接口
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public class CommonPersistenceRequest extends BaseCommonRequest implements Pageable {

    /**
     * 排序的字段名列表
     */
    private List<String> sortingColumns;

    /**
     * 是否是顺序
     */
    private boolean asc;

    public CommonPersistenceRequest() {
        super();
        this.sortingColumns = Collections.singletonList(BasePO.Column.CREATE_TIME);
    }

    @Override
    public int getPageNumber() {
        return this.getPage();
    }

    @Override
    public long getOffset() {
        return this.getStart();
    }

    @Override
    public Sort getSort() {
        final Sort.Direction direction = this.asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new Sort(direction, this.sortingColumns);
    }

    @Override
    public Pageable next() {
        this.setPage(this.getPage() + 1);
        return this;
    }

    @Override
    public Pageable previousOrFirst() {
        if (this.getPage() > 0) {
            this.setPage(this.getPage() - 1);
        }
        return this;
    }

    @Override
    public Pageable first() {
        this.setPage(0);
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return this.getPage() > 0;
    }

    @Override
    public boolean isPaged() {
        return this.isPaging();
    }

    public List<String> getSortingColumns() {
        return sortingColumns;
    }

    public void setSortingColumns(String... columns) {
        this.sortingColumns = Arrays.stream(columns).collect(Collectors.toList());
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
