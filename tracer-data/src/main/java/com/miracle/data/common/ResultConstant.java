package com.miracle.data.common;

/**
 * Description:封装结果集常量
 *
 * @author guobin On date 2018/6/28.
 * @version 1.0
 * @since jdk 1.8
 */
public class ResultConstant {

    /**
     * 状态码常量
     */
    public static class Code {

        /**
         * 操作成功
         */
        public static final int SUCCESS = 200;

        /**
         * 表示失败
         */
        public static final int ERROR = 400;

        /**
         * 没有权限
         */
        public static final int UN_AUTH = 600;
    }

    /**
     * 返回结果中的信息常量
     */
    public static class Message {

        /**
         * 处理成功的信息
         */
        public static final String SUCCESS = "操作成功";

        /**
         * 操作失败的信息
         */
        public static final String ERROR = "操作失败";

        /**
         * 查无数据的信息
         */
        public static final String NOT_FOUND = "查无记录";

        /**
         * 参数非法的信息
         */
        public static final String INVALID_PARAM = "非法参数";
    }
}
