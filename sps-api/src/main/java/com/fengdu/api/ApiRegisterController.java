package com.fengdu.api;

import com.fengdu.annotation.IgnoreAuth;
import com.fengdu.service.ApiUserService;
import com.fengdu.utils.R;
import com.fengdu.validator.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册
 *
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
@RestController
@RequestMapping("/api/register")
public class ApiRegisterController {
    @Autowired
    private ApiUserService userService;

    /**
     * 注册
     */
    @IgnoreAuth
    @RequestMapping("register")
    public R register(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        userService.save(mobile, password);

        return R.ok();
    }
}
