package top.sleepnano.sso.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sleepnano.sso.system.pojo.User;
import top.sleepnano.sso.system.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/login/{username}")
    public User doSelectUserByUsername(@PathVariable String username){
        return userService.selectUserByUsername(username);
    }

    @GetMapping("/permission/{userid}")
    public List<String> doSelectUserPermissions(@PathVariable String userid){
        return userService.selectUserPermissions(userid);
    }
}
