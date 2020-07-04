package com.kkrepo.blog.configuration.shiro.realm;

import com.kkrepo.blog.domain.User;
import com.kkrepo.blog.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author WangRuofei
 * @create 2020-05-24 10:16 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 权限校验
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 身份校验
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        // 获取token
        String token = jwtToken.getToken();

        //判断token是否为空
        if(token == null) {
            throw new UnknownAccountException("token is null!");
        }

        /**
         * 判断token是否存在黑名单中,如果存在,则返回登录异常
         */
        String tokenFlag = stringRedisTemplate.opsForValue().get(JwtTokenUtil.TOKEN_BLACKLIST_PREFIX + ":" + jwtToken.getToken());
        if (tokenFlag != null) {
            throw new UnknownAccountException("token is expired!");
        }

        // 从token中获取用户名
        String username = jwtTokenUtil.getUsernameFromToken(token);

        //token异常
        if (username == null) {
            throw new UnknownAccountException("token is error!");
        }
        User user = userService.findByName(username);
        if (user == null) {
            throw new UnknownAccountException("UnknownAccount");
        }
        user.setPassword("******");
        try {
            return new SimpleAuthenticationInfo(
                user,
                token,
                getName()
            );
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
