package com.gdwii.spring.test.mock.user;

import org.junit.Before;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现,用于测试
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public String addUser(UserEntity user) {
        return userDao.insertUser(user);
    }
}
