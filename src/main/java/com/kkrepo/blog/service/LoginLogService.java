package com.kkrepo.blog.service;

import com.kkrepo.blog.common.Constant;
import com.kkrepo.blog.common.util.AddressUtil;
import com.kkrepo.blog.common.util.HttpContextUtil;
import com.kkrepo.blog.common.util.IPUtil;
import com.kkrepo.blog.domain.LoginLog;
import com.kkrepo.blog.domain.LoginLog.Column;
import com.kkrepo.blog.domain.LoginLogExample;
import com.kkrepo.blog.repository.LoginLogMapper;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @create 2020-05-27 5:37 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class LoginLogService {

    @Resource
    private LoginLogMapper mapper;

    public int add(String username) {
        LoginLog loginLog = new LoginLog();
        //获取HTTP请求
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setUsername(username);
        loginLog.setLocation(AddressUtil.getAddress(ip));
        loginLog.setCreateTime(new Date());
        String header = request.getHeader(Constant.USER_AGENT);
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        loginLog.setDevice(browser.getName() + " -- " + operatingSystem.getName());
        return add(loginLog);
    }

    public int add(LoginLog loginLog) {
        return mapper.insert(loginLog);
    }

    public Date queryLastLoginTime() {
        LoginLogExample example = new LoginLogExample()
            .orderBy(Column.createTime.desc())
            .limit(2);
        List<LoginLog> loginLogList = mapper.selectByExample(example);
        if (loginLogList == null || loginLogList.get(1) == null) {
            return null;
        } else {
            return loginLogList.get(1).getCreateTime();
        }
    }
}
