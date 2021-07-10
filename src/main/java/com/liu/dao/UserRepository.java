package com.liu.dao;

import com.liu.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tianse
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username, String password);
}
