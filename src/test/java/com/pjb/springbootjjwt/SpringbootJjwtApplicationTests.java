package com.pjb.springbootjjwt;

import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.tools.JWTTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootJjwtApplicationTests {

    @Test
    public void contextLoads() {
        User user = new User();
        user.setId("1");
        user.setUsername("张三");
        user.setPassword("123456");
        String token = JWTTool.createJWT(10*60*1000,user);

        String s = JWTTool.getEncodePwd("123456",1252);

        String p = JWTTool.getDecodePwd("1dbX0NHS",1252);


        System.out.println(token);

    }

}
