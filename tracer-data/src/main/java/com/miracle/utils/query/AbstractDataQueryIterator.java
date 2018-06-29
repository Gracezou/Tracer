package com.miracle.utils.query;

import com.miracle.data.common.BaseCommonRequest;
import com.miracle.data.common.BaseExceptionResult;

import java.util.*;
import java.util.function.Function;

/**
 * Description:用于数据查询的迭代器抽象实现
 * 会从第0页开始查询数据
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 * @param <E> 数据的类型
 * @param <REQ> 查询数据的请求类类型
 * @param <RES> 查询出得到的返回结果
 */
public abstract class AbstractDataQueryIterator<E, REQ extends BaseCommonRequest, RES extends BaseExceptionResult>
        implements Iterator<E> {

    /**
     * 代理的数据列表迭代器
     */
    protected Iterator<E> delegate;

    /**
     * 查询的页数
     */
    protected int page;

    /**
     * 查询是否已经结束
     */
    protected boolean finish;

    /**
     * 每一页查询的数量
     */
    protected final int pageSize;

    /**
     * 查询的请求
     */
    protected final REQ request;

    /**
     * 查询的函数
     */
    protected final Function<REQ, RES> queryMapper;

    /**
     * 从{@link RES}中获取{@link E}的函数
     */
    protected final Function<RES, List<E>> valuesGetter;

    public AbstractDataQueryIterator(REQ request,
                                     Function<REQ, RES> queryMapper,
                                     Function<RES, List<E>> valuesGetter) {
        this.pageSize = request.getPageSize();
        this.request = request;
        this.queryMapper = queryMapper;
        this.valuesGetter = valuesGetter;
        this.finish = false;
        this.delegate = Collections.emptyIterator();
    }

    @Override
    public boolean hasNext() {
        if (!this.delegate.hasNext() && !this.finish) {
            this.queryData();
        }

        return this.delegate.hasNext();
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements available for page " + this.page + ".");
        }

        return this.delegate.next();
    }

    /**
     * 查询数据的默认实现
     * 页数在查询之后会+1
     * 并将代理的list用新的数据进行替换
     * 设置{@link this#finish}的值,如果已经没有更多的数据可查询,就会被设置成true
     * 子类可重载该方法
     *
     * @throws RuntimeException 查询失败则会抛出异常
     */
    protected void queryData() {
        this.request.setPage(this.page++);
        RES result = this.queryMapper.apply(this.request);

        if (result.isSuccess()) {
            this.delegate = Optional.ofNullable(this.valuesGetter.apply(result))
                    .orElseGet(Collections::emptyList)
                    .iterator();
            this.finish = this.page * this.pageSize >= result.getTotalCount();
        } else if (result.getException() != null) {
            throw new RuntimeException(result.getException());
        } else {
            throw new RuntimeException("Failed to query data:" + result.getMessage());
        }
    }
}
