package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.controller.vo.BaseResult;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ldy on 2017/4/5.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Account", description = "用户管理", position = 1)
@Controller
@RequestMapping("/v1/account")
public class AccountController {

    @LogInterceptor
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @ResponseBody
    @RequestMapping(value = "/security_code", method = RequestMethod.GET)
    public BaseResult<Object> getSecurityCode(
            @ApiParam(value = "手机号", required = true) @RequestParam("phone") String phone) {
        return null;
    }

    @LogInterceptor
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResult<Object> register(
            @ApiParam(value = "用户名", required = true) @RequestParam("account") String account,
            @ApiParam(value = "密码", required = true) @RequestParam("password") String password,
            @ApiParam(value = "企业名称", required = true) @RequestParam("company_name") String companyName,
            @ApiParam(value = "手机号", required = true) @RequestParam("phone") String phone,
            @ApiParam(value = "角色code", required = true) @RequestParam("role_code") int roleCode,
            @ApiParam(value = "验证码", required = true) @RequestParam("security_code") int securityCode,
            @ApiParam(value = "验证码id", required = true) @RequestParam("security_code_id") long securityCodeId) {
        return null;
    }

    @LogInterceptor
    @ApiOperation(value = "登录", notes = "登录")
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResult<Object> login(
            @ApiParam(value = "用户名", required = true) @RequestParam("account") String account,
            @ApiParam(value = "密码", required = true) @RequestParam("password") String password) {
        return null;
    }

    @LogInterceptor
    @ApiOperation(value = "找回密码", notes = "找回密码")
    @ResponseBody
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public BaseResult<Object> forgetPassword(
            @ApiParam(value = "手机号", required = true) @RequestParam("phone") String phone,
            @ApiParam(value = "新密码", required = true) @RequestParam("new_password") String newPassword,
            @ApiParam(value = "验证码", required = true) @RequestParam("security_code") String securityCode,
            @ApiParam(value = "验证码id", required = true) @RequestParam("security_code_id") String securityCodeId) {
        return null;
    }

}
