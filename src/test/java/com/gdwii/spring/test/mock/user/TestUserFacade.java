package com.gdwii.spring.test.mock.user;

import com.gdwii.spring.test.MockSpringJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

/**
 * 普通Spring MockSpringJUnitRunner
 */
@RunWith(MockSpringJUnitRunner.class)
@ContextConfiguration("classpath:test-mock-user.xml")
public class TestUserFacade {
    @Resource
    private UserFacade userFacade;
    @Mock
    private UserDao userDao;

    @Before
    public void mockObject(){
        Mockito.when(userDao.insertUser(Mockito.any())).thenReturn("这是一个mock返回值");
    }

    @Test
    public void testAddUser() {
        System.out.println(userFacade.addUser(new UserEntity()));
    }
}
