package com.kkrepo.blog.configuration.shiro.realm;

import com.kkrepo.blog.domain.User;
import lombok.Builder;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author WangRuofei
 * @create 2020-07-02 4:58 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@Builder
public class JwtToken implements AuthenticationToken {

    private User principal;

    private String token;

    @Override
    public User getPrincipal() {
        return principal;
    }

    @Override
    public String getCredentials() {
        return token;
    }
}
