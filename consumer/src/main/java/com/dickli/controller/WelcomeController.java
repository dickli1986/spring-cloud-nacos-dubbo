package com.dickli.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.dickli.provider.api.welcome.WelcomeRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {
    @DubboReference(version = "1.0.0")
    private WelcomeRpcService welcomeService;

    @RequestMapping("/welcome")
    @ResponseBody
    @SentinelResource(value = "welcomeController")
    public String welcome(String userName){
        return welcomeService.welcome(userName);
    }
}


