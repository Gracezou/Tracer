package com.miracle.common;

/**
 * Description:基础的返回结果类
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public class CommonResult {

    /**
     * 操作的状态码
     * @see com.miracle.common.ResultConstant.Code
     */
    private int code;

    /**
     * 返回结果的信息
     * @see com.miracle.common.ResultConstant.Message
     */
    private String message;

    /**
     * 当前返回结果持有的数据数量
     */
    private int count;

    /**
     * 符合条件的数据总数量
     */
    private int totalCount;

    /**
     * 表示当前数据所在页码数
     */
    private int page;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 判断结果是否成功
     * @return 判断结果
     */
    public boolean isSuccess() {
        return this.code == ResultConstant.Code.SUCCESS;
    }

    /**
     * 判断是否失败
     * @return 判断结果
     */
    public boolean isFailed() {
        return this.code != ResultConstant.Code.SUCCESS;
    }
}
