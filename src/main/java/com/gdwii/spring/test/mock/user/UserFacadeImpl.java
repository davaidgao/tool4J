package com.gdwii.spring.test.mock.user;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userFacade")
public class UserFacadeImpl implements UserFacade{
    @Resource
    private UserService userService;

    @Override
    public String addUser(UserEntity user) {
        return userService.addUser(user);
    }
}
