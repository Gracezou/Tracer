package com.miracle.repository.dao.user.impl;

import com.miracle.common.utils.CollectionUtils;
import com.miracle.common.utils.SerializableCloneUtils;
import com.miracle.data.common.ResultConstant;
import com.miracle.data.po.data.user.UserPO;
import com.miracle.data.po.request.user.UserPersistenceRequest;
import com.miracle.data.po.result.user.UserPersisitenceResult;
import com.miracle.repository.base.BaseTracerMongodbTest;
import com.miracle.repository.dao.user.UserDao;
import com.miracle.repository.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Description:{@link UserDaoImpl}的测试类
 *
 * @author guobin On date 2018/7/10.
 * @version 1.0
 * @since jdk 1.8
 */
public class UserDaoImplTest extends BaseTracerMongodbTest {

    private static final String JSON_PATH = "/user-test.json";

    @Resource
    private UserDao userDao;

    private List<UserPO> userList;

    @Before
    public void setUp() {
        this.userList = TestUtils.parsePrepareData(JSON_PATH, UserPO.class);
        // 向数据库中插入准备测试的数据
        this.prepareTestData(this.userList, this.userList.size() - 1);
    }

    @After
    public void tearDown() {
        this.deleteTestData(this.userList);
    }

    @Test
    public void insert() {
        final UserPO user4Insert = this.userList.get(this.userList.size() - 1);
        UserPersisitenceResult result = this.userDao.insert(user4Insert);
        assertEquals(ResultConstant.Code.SUCCESS, result.getCode());
        assertEquals(ResultConstant.Message.SUCCESS, result.getMessage());
        assertTrue(this.equalsToExpectedElement(user4Insert, UserPO::getId, UserPO.class));
    }

    @Test
    public void update() {
        final List<UserPO> updateList = this.userList.stream()
                .limit(this.userList.size() - 1)
                .map(insertUser -> {
                    final UserPO updateUser = SerializableCloneUtils.deepClone(insertUser);
                    updateUser.setUsername("-" + insertUser.getUsername() + "-");
                    updateUser.setPassword("-" + insertUser.getPassword() + "-");
                    updateUser.setCellphoneNumber("-" + insertUser.getCellphoneNumber() + "-");
                    updateUser.setEmailAddress("-" + insertUser.getEmailAddress() + "-");
                    updateUser.setHeadshot("-" + insertUser.getHeadshot() + "-");
                    return updateUser;
                })
                .collect(Collectors.toList());
        updateList.forEach(updateUser -> {
            final UserPersisitenceResult result = this.userDao.update(updateUser);
            assertEquals(ResultConstant.Code.SUCCESS, result.getCode());
            assertEquals(ResultConstant.Message.SUCCESS, result.getMessage());
            assertTrue(this.equalsToExpectedElement(updateUser, UserPO::getId, UserPO.class));
        });
    }

    @Test
    public void remove() {
        this.userList.stream()
                .limit(this.userList.size() - 1)
                .map(UserPO::getId)
                .map(this.userDao::remove)
                .peek(result -> assertEquals(ResultConstant.Code.SUCCESS, result.getCode()))
                .forEach(result -> assertEquals(ResultConstant.Message.SUCCESS, result.getMessage()));
        final List<String> ids = this.userList.stream().map(UserPO::getId).collect(Collectors.toList());
        final List<UserPO> queryList = this.queryDataByIds(ids, UserPO.class);
        assertTrue(CollectionUtils.isNullOrEmpty(queryList));
    }

    @Test
    public void queryByRequest() {
        this.userList.stream().limit(this.userList.size() - 1).forEach(expected -> {
            final UserPersisitenceResult result = this.userDao.queryByRequest(this.buildRequest(expected));
            assertEquals(ResultConstant.Code.SUCCESS, result.getCode());
            assertEquals(ResultConstant.Message.SUCCESS, result.getMessage());
            assertEquals(1, result.getCount());
            assertEquals(1, result.getTotalCount());
            assertTrue(CollectionUtils.notNullOrEmpty(result.getValues()));
            assertEquals(expected, result.getValue());
        });

        // 查无记录
        final UserPersistenceRequest noDataRequest = new UserPersistenceRequest();
        noDataRequest.setId("NO DATA");
        final UserPersisitenceResult noDataResult = this.userDao.queryByRequest(noDataRequest);
        assertEquals(ResultConstant.Code.SUCCESS, noDataResult.getCode());
        assertEquals(ResultConstant.Message.NOT_FOUND, noDataResult.getMessage());
        assertEquals(0, noDataResult.getCount());
        assertEquals(0, noDataResult.getTotalCount());
        assertTrue(CollectionUtils.isNullOrEmpty(noDataResult.getValues()));

        // 分页查询
        final UserPersistenceRequest pageRequest = new UserPersistenceRequest();
        pageRequest.setPage(0);
        pageRequest.setPageSize(10);
        final UserPersisitenceResult pageResult = this.userDao.queryByRequest(pageRequest);
        assertEquals(ResultConstant.Code.SUCCESS, pageResult.getCode());
        assertEquals(ResultConstant.Message.SUCCESS, pageResult.getMessage());
        assertEquals(10, pageResult.getCount());
        assertEquals(this.userList.size() - 1, pageResult.getTotalCount());
        assertTrue(CollectionUtils.notNullOrEmpty(pageResult.getValues()));
        assertTrue(this.allEqualsToExepectedElements(this.userList.subList(0, this.userList.size() - 1), UserPO::getId, UserPO.class));
    }

    /**
     * 根据一条{@link UserPO}构建查询请求
     * @param user 用户信息数据
     * @return 查询请求
     */
    private UserPersistenceRequest buildRequest(UserPO user) {
        final UserPersistenceRequest request = new UserPersistenceRequest();
        request.setId(user.getId());
        request.setCellphoneNumber(user.getCellphoneNumber());
        request.setUsername(user.getUsername());
        request.setEmailAddress(user.getEmailAddress());

        return request;
    }
}