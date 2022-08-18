package com.dickli.provider.service.welcome;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dickli.provider.api.welcome.WelcomeRpcService;
import com.dickli.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@DubboService(version = "1.0.0")
@Slf4j
public class WelcomeRpcServiceImpl implements WelcomeRpcService {
    @Value("${welcome}")
    private String welcome;

    @Autowired
    private IUserService userService;

    @Override
    @SentinelResource(value = "welcome", fallback = "fallback", blockHandler = "handleBlock")
    public String welcome(String userName) {
        if (userName == null || "".equals(userName)) {
            userName = "stranger";
        }
        if ("exception".equals(userName)) {
            throw new RuntimeException("模拟异常");
        }
        return welcome + " : " + userName;
    }

    public String handleBlock(String userName, BlockException ex) {
        log.error("handleBlock,userName={}", userName, ex);
        if (userName == null || "".equals(userName)) {
            userName = "stranger";
        }
        return "welcome : " + userName;
    }

    public String fallback(String userName, Throwable t) {
        log.error("fallback,userName={}", userName, t);
        return "系统异常，请稍后再试！";
    }
}
