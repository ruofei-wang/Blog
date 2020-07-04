package com.kkrepo.blog.configuration.shiro.realm;

import com.kkrepo.blog.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author WangRuofei
 * @create 2019-01-16 10:10
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private Long expiration;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String TOKEN_BLACKLIST_PREFIX = "tokenBlacklist";

    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_CREATED = "created";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成token
     * @param user 用户
     * @return
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        String token = generateToken(claims);
        //构建jwt
        JwtToken jwtToken = JwtToken.builder().token(token).principal(user).build();

        Subject subject = SecurityUtils.getSubject();
        //该方法会调用JwtRealm中的doGetAuthenticationInfo方法
        subject.login(jwtToken);

        //认证通过
        if (subject.isAuthenticated()) {
            //登录成功后token信息保存到shiro session中
            subject.getSession(true).setAttribute("token",token);
            return token;
        }
        return null;
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS512, this.secret)
            .compact();
    }

    /**
     * 生成token时间 = 当前时间 + expiration（properties中配置的失效时间）
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 根据token获取用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断token失效时间是否到了
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取设置的token失效时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 将token加入黑名单
     * @param token
     */
    public boolean tokenToBlacklists(String token) {
        //将原来的token存入redis中, key的格式 tokenBlocklist:token
        stringRedisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + ":" + token,new Date().toString(),expiration.longValue(),TimeUnit.SECONDS);
        String flag = stringRedisTemplate.opsForValue().get(TOKEN_BLACKLIST_PREFIX + ":" + token);
        if (flag != null) {
            return true;
        }
        return false;
    }
}
