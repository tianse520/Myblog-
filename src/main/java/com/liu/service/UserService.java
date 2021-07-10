package com.liu.service;

import com.liu.pojo.User;

/**
 * @author tianse
 */
public interface UserService {
    User checkUser(String username, String password);
}
