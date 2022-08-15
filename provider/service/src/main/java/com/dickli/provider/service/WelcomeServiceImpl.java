package com.dickli.provider.service;

import com.dickli.provider.api.WelcomeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@DubboService(version = "1.0.0")
public class WelcomeServiceImpl implements WelcomeService {
    @Value("${welcome}")
    private String welcome;

    @Override
    public String welcome(String userName) {
        if (userName == null || "".equals(userName)) {
            userName = "stranger";
        }
        return welcome + " : " + userName;
    }
}
