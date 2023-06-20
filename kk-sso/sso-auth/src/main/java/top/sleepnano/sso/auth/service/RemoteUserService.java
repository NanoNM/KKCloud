package top.sleepnano.sso.auth.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.sleepnano.sso.auth.pojo.User;

import java.util.List;

@Service
@FeignClient(name="sso-user",contextId = "remoteProviderService")
public interface RemoteUserService {

    @GetMapping("/user/login/{username}")
    User selectUserByUsername(@PathVariable("username") String username);

    @GetMapping("/user/permission/{userId}")
    List<String>  selectUserPermissions(@PathVariable("userId") String userId);
}
