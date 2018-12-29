package com.pjb.springbootjjwt.service;

import com.pjb.springbootjjwt.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by pj on 2018/12/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    public void findByUsername() throws Exception {
        User user = userService.findUserById("1");
        user.getPassword();
    }

    @Test
    public void findUserById() throws Exception {
    }

}