package top.sleepnano.sso.auth.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class TestUserDetailsService {
    @Autowired
    UserDetailsService userDetailsService;

    @Test
    void testUserDetailsService(){
        UserDetails user = userDetailsService.loadUserByUsername("admin");
        System.out.println(user.getPassword());
    }
}
