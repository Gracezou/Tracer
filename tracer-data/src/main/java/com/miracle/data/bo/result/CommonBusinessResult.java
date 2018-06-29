package com.miracle.data.bo.result;

import com.miracle.data.common.CommonResult;

/**
 * Description:bo层的基础数据返回封装
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
public class CommonBusinessResult extends CommonResult {

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
