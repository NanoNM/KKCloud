package top.sleepnano.sso.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import top.sleepnano.sso.auth.pojo.LoginUser;
import top.sleepnano.sso.auth.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * 自定义Jwt转换器
 */
public class JwtCustomerAccessTokenConverter extends JwtAccessTokenConverter {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> info = new HashMap<>();
        map.put("author","laowang");
        map.put("date",new Date());
        map.put("site","top.sleepnano");
        map.put("userNo", ((LoginUser)authentication.getPrincipal()).getUser().getUserNo());
        info.put("info",map);
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);

        return super.enhance(accessToken,authentication);
    }
}
