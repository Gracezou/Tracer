package com.miracle.aop.user;

import com.miracle.data.po.data.user.UserPO;
import com.miracle.data.po.result.PersistenceResultFactory;
import com.miracle.data.po.result.user.UserPersisitenceResult;
import com.miracle.utils.AspectUtils;
import com.miracle.validate.AppliableValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Description: 对{@link com.miracle.dao.user.UserDao}进行验证的切面
 * @author guobin On date 2018/7/3.
 * @since jdk 1.8
 * @version 1.0
 */
@Component("userDaoParamValidateAspect")
@Aspect
public class UserDaoParamValidateAspect {

    @Pointcut("execution(public * com.miracle.dao.user.UserDao(..))")
    public void validate() { }

    @Around("validate() && args(com.miracle.data.po.data.user.UserPO)")
    public UserPersisitenceResult aroundSaving(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        final UserPO user = (UserPO) args[0];
        return AppliableValidator.of(user)
                .notNull(UserPO::getUsername, "用户名不能为空")
                .notNull(UserPO::getPassword, "密码不能为空")
                .notNull(UserPO::getEmailAddress, "电子邮箱不能为空")
                .validate(po -> AspectUtils.proceedJoinPoint(joinPoint, UserPersisitenceResult::new),
                        (po, errMsg) -> PersistenceResultFactory.errorResult(UserPersisitenceResult::new, errMsg));
    }
}
