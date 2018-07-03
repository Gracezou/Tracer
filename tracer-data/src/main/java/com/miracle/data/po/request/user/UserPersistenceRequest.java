package com.miracle.data.po.request.user;

import com.miracle.data.po.request.CommonPersistenceRequest;

/**
 * Description:user的dao层请求集
 *
 * @author guobin On date 2018/7/2.
 * @version 1.0
 * @since jdk 1.8
 */
public class UserPersistenceRequest extends CommonPersistenceRequest {

    /**
     * 主键id
     */
    private String id;

    /**
     * 电子邮箱地址
     */
    private String emailAddress;

    /**
     * 用户名字
     */
    private String username;

    /**
     * 手机号码
     */
    private String cellphoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }
}
