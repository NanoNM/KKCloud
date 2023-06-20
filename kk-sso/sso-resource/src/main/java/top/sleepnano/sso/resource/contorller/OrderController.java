package top.sleepnano.sso.resource.contorller;

import com.alibaba.cloud.commons.lang.StringUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.service.RemoteOrderService;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.sso.resource.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RemoteOrderService remoteOrderService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('sys:user:add:order')")
    public Result addOrder(@RequestBody List<RestOrderProduct> products, HttpServletRequest httpServletRequest){
        String header=httpServletRequest.getHeader("Authorization");
        String token= StringUtils.substringAfter(header,"Bearer");
        try {
            Map<String, ?> infos = (Map<String, ?>) JwtUtil.parseJWT(token).get("info");

            return remoteOrderService.addOrder(products, infos.get("userNo").toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasAuthority('sys:user:cancel:order')")
    public Result cancelOrder(@RequestParam("orderNo")String orderNo,
                              HttpServletRequest httpServletRequest){

        String header=httpServletRequest.getHeader("Authorization");
        String token= StringUtils.substringAfter(header,"Bearer");
        try {
            Map<String, ?> infos = (Map<String, ?>) JwtUtil.parseJWT(token).get("info");
            String userNo = infos.get("userNo").toString();
            return remoteOrderService.cancelOrder(orderNo,userNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping ("/list")
    @PreAuthorize("hasAuthority('sys:user:query:order')")
    public Result queryOrder(@Nullable @RequestParam("pos") String pos,
                             HttpServletRequest httpServletRequest,
                             @Nullable @RequestParam("page") Integer page){

        String header=httpServletRequest.getHeader("Authorization");
        String token= StringUtils.substringAfter(header,"Bearer");

        try {
            Map<String, ?> infos = (Map<String, ?>) JwtUtil.parseJWT(token).get("info");
            String userNo = infos.get("userNo").toString();
            return remoteOrderService.queryOrder(pos, page,userNo );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




}
