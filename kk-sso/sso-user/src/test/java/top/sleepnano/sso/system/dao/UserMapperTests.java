package top.sleepnano.sso.system.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.sleepnano.sso.system.pojo.User;

import java.util.List;

@SpringBootTest
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelectUserByUsername(){
        User user =
                userMapper.selectUserByUsername("admin");
        System.out.println(user);
    }
    @Test
    void testSelectUserPermissions(){
        List<String> permission=
                userMapper.selectUserPermissions("");
        System.out.println(permission);
    }
}
