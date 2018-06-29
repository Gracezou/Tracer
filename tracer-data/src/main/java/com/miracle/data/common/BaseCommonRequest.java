package com.miracle.data.common;

/**
 * Description: 基础的请求封装类
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public abstract class BaseCommonRequest {

    /**
     * 默认的每页数据量大小:{@value}
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 查询页码
     */
    private int page;

    /**
     * 每页的数据数量
     */
    private int pageSize;

    /**
     * 查询是否分页
     * {@code true}代表需要分页,{@code false}代表不需要分页
     */
    private boolean isPaging;

    /**
     * 默认构造器
     * 默认每页数量为{@link #DEFAULT_PAGE_SIZE}
     * 默认分页
     * 默认查询页码为0
     */
    public BaseCommonRequest() {
        this.page = 0;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.isPaging = false;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPaging() {
        return isPaging;
    }

    public void setPaging(boolean paging) {
        isPaging = paging;
    }

    /**
     * 在需要分页的情况下,通过该方法获取该页第一条数据的索引
     * @return 该页第一条数据的索引
     */
    public int getStart() {
        return this.page * this.pageSize;
    }
}
