package top.sleepnano.sso.system.service;

import top.sleepnano.sso.system.pojo.User;

import java.util.List;

public interface UserService {
    User selectUserByUsername(String username);
    List<String> selectUserPermissions(String userId);
}
