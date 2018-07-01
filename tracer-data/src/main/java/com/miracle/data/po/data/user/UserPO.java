package com.miracle.data.po.data.user;

import com.miracle.data.po.data.BasePO;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Description:用于的PO对象
 *
 * @author guobin On date 2018/6/29.
 * @version 1.0
 * @since jdk 1.8
 */
@Document(collection = "c_user")
public class UserPO extends BasePO {

    private static final long serialVersionUID = 2434105134900272335L;

    public static class Column {

        public static final String EMAIL_ADDRESS = "email_address";

        public static final String USERNAME = "username";

        public static final String CELLPHONE_NUMBER = "cellphone_number";

        public static final String PASSWORD = "password";

        public static final String HEADSHOT = "headshot";
    }

    /**
     * 电子邮箱地址
     */
    @Field(Column.EMAIL_ADDRESS)
    private String emailAddress;

    /**
     * 用户名字
     */
    @Field(Column.USERNAME)
    private String username;

    /**
     * 手机号码
     */
    @Field(Column.CELLPHONE_NUMBER)
    private String cellphoneNumber;

    /**
     * 密码
     */
    @Field(Column.PASSWORD)
    private String password;

    /**
     * 头像图片
     */
    @Field(Column.HEADSHOT)
    private String headshot;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }
}
