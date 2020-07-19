package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.common.exception.ErrorCode;
import com.kkrepo.blog.common.util.Sha256Salt;
import com.kkrepo.blog.configuration.shiro.realm.JwtTokenUtil;
import com.kkrepo.blog.domain.User;
import com.kkrepo.blog.service.LoginLogService;
import com.kkrepo.blog.service.UserService;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangRuofei
 * @create 2020-05-27 5:20 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public R login(
        @RequestParam(value = "username") String username,
        @RequestParam(value = "password") String password,
        HttpServletResponse response
    ) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResultWrap.error(ErrorCode.PARAM_ERROR);
        }
        // 验证用户名
        User user = userService.findByName(username);
        if (user == null) {
            return ResultWrap.error(ErrorCode.NOT_EXIST_ERROR);
        }
        // 验证密码
        if (!Sha256Salt.checkSha256EncryptSalt(password, user.getPassword())) {
            return ResultWrap.error(ErrorCode.LOGIN_ERROR);
        }
        // 生成token同时登录
        String token = jwtTokenUtil.generateToken(user);
        //记录登录日志
        loginLogService.add(super.getCurrentUser().getUsername());
        // 返回前端的数据  //前端好像没用到这个数据
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", this.getCurrentUser());
//        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
//        request.getSession().setAttribute("user", this.getCurrentUser());
        return ResultWrap.ok(result);
    }
}
