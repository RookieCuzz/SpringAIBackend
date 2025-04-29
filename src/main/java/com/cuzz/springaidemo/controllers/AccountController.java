package com.cuzz.springaidemo.controllers;

import cn.dev33.satoken.stp.StpUtil;
import com.cuzz.springaidemo.models.Result;
import com.cuzz.springaidemo.models.TestAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/test")
public class AccountController {


    @PostMapping("/reg")
    @ResponseBody
    public Result reg(@RequestBody TestAccount account){

        StpUtil.login("22112312");
        return Result.ok("成功");

    }
}
