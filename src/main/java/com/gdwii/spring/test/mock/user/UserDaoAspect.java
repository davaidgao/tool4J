package com.gdwii.spring.test.mock.user;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * UserDao的AOP,用于证明mock对象不影响原来的AOP
 */
@Aspect
public class UserDaoAspect {
    @Pointcut("execution(* com.gdwii.spring.test.mock.user.UserDao.*(..))")
    private void anyMethod(){}//定义一个切入点

    @Before("anyMethod()")
    public void doAccessCheck(){
        System.out.println("前置通知");
    }

    @AfterReturning("anyMethod()")
    public void doAfter(){
        System.out.println("后置通知");
    }
}
