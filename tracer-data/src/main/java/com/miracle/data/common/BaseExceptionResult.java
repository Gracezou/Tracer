package com.miracle.data.common;

/**
 * Description:带有异常的返回结果
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public abstract class BaseExceptionResult extends BaseCommonResult {

    /**
     * 表示出现的异常
     */
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
