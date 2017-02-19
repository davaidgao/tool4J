package com.gdwii.spring.test.mock.user;

import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public String insertUser(UserEntity user) {
        return "simple UserDaoImpl impl";
    }
}
