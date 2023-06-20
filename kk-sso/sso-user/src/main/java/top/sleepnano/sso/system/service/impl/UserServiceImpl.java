package top.sleepnano.sso.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.sleepnano.sso.system.dao.UserMapper;
import top.sleepnano.sso.system.pojo.User;
import top.sleepnano.sso.system.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public List<String> selectUserPermissions(String userId) {
        return userMapper.selectUserPermissions(userId);
    }
}
