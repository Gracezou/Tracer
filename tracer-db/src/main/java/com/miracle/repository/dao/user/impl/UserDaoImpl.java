package com.miracle.repository.dao.user.impl;

import com.miracle.repository.cache.annotation.ReadAction;
import com.miracle.repository.cache.annotation.WriteAction;
import com.miracle.repository.dao.user.UserDao;
import com.miracle.data.po.data.user.UserPO;
import com.miracle.data.po.request.user.UserPersistenceRequest;
import com.miracle.data.po.result.PersistenceResultFactory;
import com.miracle.data.po.result.user.UserPersisitenceResult;
import com.miracle.repository.utils.MongodbQueryBuilder;
import com.miracle.repository.utils.MongodbUpdateBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Supplier;

/**
 * Description:user-dao的实现类
 *
 * @author guobin On date 2018/7/2.
 * @version 1.0
 * @since jdk 1.8
 */
@Repository
public class UserDaoImpl implements UserDao {

    private static final Supplier<UserPersisitenceResult> RESULT_SUPPLIER = UserPersisitenceResult::new;

    @Resource
    private MongoTemplate mongoTemplate;

    @WriteAction
    @Override
    public UserPersisitenceResult insert(UserPO user) {
        UserPersisitenceResult result;
        try {
            // 不存在就insert,存在就update
            this.mongoTemplate.insert(user);
            result = PersistenceResultFactory.successResult(RESULT_SUPPLIER);
        } catch (Exception ex) {
            result = PersistenceResultFactory.errorResult(RESULT_SUPPLIER, ex);
        }
        return result;
    }

    @WriteAction
    @Override
    public UserPersisitenceResult update(UserPO user) {
        final Query query = MongodbQueryBuilder.buildIdQuerier(user.getId());
        final Update update = MongodbUpdateBuilder.newUpdate()
                // 设置update字段
                .set(UserPO.Column.USERNAME, user.getUsername())
                .set(UserPO.Column.EMAIL_ADDRESS, user.getEmailAddress())
                .set(UserPO.Column.CELLPHONE_NUMBER, user.getCellphoneNumber())
                .set(UserPO.Column.PASSWORD, user.getPassword())
                .set(UserPO.Column.HEADSHOT, user.getHeadshot())
                .get();

        UserPersisitenceResult result;
        try {
            this.mongoTemplate.updateMulti(query, update, UserPO.class);
            result = PersistenceResultFactory.successResult(RESULT_SUPPLIER);
        } catch (Exception ex) {
            result = PersistenceResultFactory.errorResult(RESULT_SUPPLIER, ex);
        }
        return result;
    }

    @WriteAction
    @Override
    public UserPersisitenceResult remove(String id) {
        final Query query = MongodbQueryBuilder.buildIdQuerier(id);

        UserPersisitenceResult result;
        try {
            this.mongoTemplate.findAndRemove(query, UserPO.class);
            result = PersistenceResultFactory.successResult(RESULT_SUPPLIER);
        } catch (Exception ex) {
            result = PersistenceResultFactory.errorResult(RESULT_SUPPLIER, ex);
        }
        return result;
    }

    @ReadAction
    @Override
    public UserPersisitenceResult queryByRequest(UserPersistenceRequest request) {
        final Query query = MongodbQueryBuilder.newQueryWithId(request.getId())
                .is(UserPO.Column.EMAIL_ADDRESS, request.getEmailAddress())
                .is(UserPO.Column.CELLPHONE_NUMBER, request.getCellphoneNumber())
                .is(UserPO.Column.USERNAME, request.getUsername())
                .pageQuery(request)
                .get();

        UserPersisitenceResult result;
        try {
            final List<UserPO> list = this.mongoTemplate.find(query, UserPO.class);
            final int totalCount = request.isPaging() ?
                    (int) this.mongoTemplate.count(query, UserPO.class) : list.size();
            result = PersistenceResultFactory.successResult(RESULT_SUPPLIER,
                    list, request.getPage(), totalCount);
        } catch (Exception ex) {
            result = PersistenceResultFactory.errorResult(RESULT_SUPPLIER, ex);
        }
        return result;
    }
}
