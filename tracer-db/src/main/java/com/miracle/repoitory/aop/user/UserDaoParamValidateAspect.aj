package com.miracle.repoitory.aop.user;

import com.miracle.data.po.data.user.UserPO;
import com.miracle.data.po.request.user.UserPersistenceRequest;
import com.miracle.data.po.result.PersistenceResultFactory;
import com.miracle.data.po.result.user.UserPersisitenceResult;
import com.miracle.common.utils.AspectUtils;
import com.miracle.common.validate.AppliableValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Description: 对{@link com.miracle.repoitory.dao.user.UserDao}进行验证的切面
 * @author guobin On date 2018/7/3.
 * @since jdk 1.8
 * @version 1.0
 */
@Component("userDaoParamValidateAspect")
@Aspect
public class UserDaoParamValidateAspect {

    @Pointcut("execution(public com.miracle.data.po.result.user.UserPersisitenceResult com.miracle.repoitory.dao.user.UserDao.*(..))")
    public void validate() { }

    /**
     * 校验{@link UserPO}的合法性
     * @param joinPoint 连接点
     * @return 操作结果
     */
    @Around("validate() && args(com.miracle.data.po.data.user.UserPO)")
    public UserPersisitenceResult validateUser(ProceedingJoinPoint joinPoint) {
        final UserPO user = AspectUtils.getFirstArgFromJoinPoint(joinPoint);
        return AppliableValidator.of(user)
                .notNull(UserPO::getUsername, "用户名不能为空")
                .notNull(UserPO::getPassword, "密码不能为空")
                .notNull(UserPO::getEmailAddress, "电子邮箱不能为空")
                .validate(po -> AspectUtils.proceedJoinPoint(joinPoint, UserPersisitenceResult::new),
                        (po, errMsg) -> PersistenceResultFactory.errorResult(UserPersisitenceResult::new, errMsg));
    }

    /**
     * 校验{@code id}的合法性
     * @param joinPoint 连接点
     * @return 操作结果
     */
    @Around("validate() && args(String)")
    public UserPersisitenceResult validateId(ProceedingJoinPoint joinPoint) {
        final String id = AspectUtils.getFirstArgFromJoinPoint(joinPoint);
        return AppliableValidator.of(id)
                .validate(value -> AspectUtils.proceedJoinPoint(joinPoint, UserPersisitenceResult::new),
                        (value, errMsg) -> PersistenceResultFactory.errorResult(UserPersisitenceResult::new, errMsg));
    }

    /**
     * 校验{@link UserPersistenceRequest}的合法性
     * @param joinPoint 连接点
     * @return 操作结果
     */
    @Around("validate() && args(com.miracle.data.po.request.user.UserPersistenceRequest)")
    public UserPersisitenceResult validateRequest(ProceedingJoinPoint joinPoint) {
        final UserPersistenceRequest request = AspectUtils.getFirstArgFromJoinPoint(joinPoint);
        return AppliableValidator.of(request)
                .validate(r -> AspectUtils.proceedJoinPoint(joinPoint, UserPersisitenceResult::new),
                        (r, errMsg) -> PersistenceResultFactory.errorResult(UserPersisitenceResult::new, errMsg));
    }
}
