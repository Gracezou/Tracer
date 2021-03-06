package com.miracle.repository.dao.user;

import com.miracle.data.po.data.user.UserPO;
import com.miracle.data.po.request.user.UserPersistenceRequest;
import com.miracle.data.po.result.user.UserPersisitenceResult;

/**
 * Description:对用户{@link UserPO}的dao层操作接口
 *
 * @author guobin On date 2018/7/2.
 * @version 1.0
 * @since jdk 1.8
 */
public interface UserDao {

    /**
     * user对应的缓存前缀
     */
    String USER_CACHE_PREFIX = "user";

    /**
     * 插入一条{@link UserPO}数据
     * @param user 用户数据
     * @return 操作结果
     */
    UserPersisitenceResult insert(UserPO user);

    /**
     * 更新一条{@link UserPO}数据,{@link UserPO#id}不允许为空
     * @param user 用户数据
     * @return 操作结果
     */
    UserPersisitenceResult update(UserPO user);

    /**
     * 根据id删除一条数据
     * @param id 要删除的数据id
     * @return 操作结果
     */
    UserPersisitenceResult remove(String id);

    /**
     * 根据条件进行查询
     * @param request 请求
     * @return 操作结果
     */
    UserPersisitenceResult queryByRequest(UserPersistenceRequest request);
}
