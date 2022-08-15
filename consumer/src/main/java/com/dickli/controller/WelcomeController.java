package com.dickli.controller;

import com.dickli.provider.api.WelcomeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {
    @DubboReference(version = "1.0.0")
    private WelcomeService welcomeService;

    @RequestMapping("/welcome")
    @ResponseBody
    public String welcome(String userName){
        return welcomeService.welcome(userName);
    }
}
