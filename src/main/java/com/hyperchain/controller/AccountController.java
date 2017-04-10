package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.AccountService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by ldy on 2017/4/5.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Account", description = "用户管理", position = 1)
@Controller
@RequestMapping("/v1/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @LogInterceptor
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @ResponseBody
    @RequestMapping(value = "/security_code", method = RequestMethod.GET)
    public BaseResult<Object> getSecurityCode(
            @ApiParam(value = "手机号", required = true) @RequestParam("phone") String phone) {
        //TODO 获取验证码controller
        return null;
    }

    @LogInterceptor
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ResponseBody
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public BaseResult<Object> register(
            @ApiParam(value = "用户名", required = true) @RequestParam("accountName") String accountName,
            @ApiParam(value = "密码", required = true) @RequestParam("password") String password,
            @ApiParam(value = "企业名称", required = true) @RequestParam("enterpriseName") String enterpriseName,
            @ApiParam(value = "手机号", required = true) @RequestParam("phone") String phone,
            @ApiParam(value = "角色code", required = true) @RequestParam("roleCode") int roleCode,
            @ApiParam(value = "验证码", required = false) @RequestParam("securityCode") String securityCode,
            @ApiParam(value = "验证码id", required = false) @RequestParam("securityCodeId") long securityCodeId,
            @ApiParam(value = "证件类型", required = false) @RequestParam("certType") String certType,
            @ApiParam(value = "证件号码", required = false) @RequestParam("certNo") String certNo,
            @ApiParam(value = "开户行账号（多个，使用:拼接)", required = false) @RequestParam("acctIds") String acctIds,
            @ApiParam(value = "开户行别", required = false) @RequestParam("svcrClass") String svcrClass,
            @ApiParam(value = "开户行行号", required = false) @RequestParam("acctSvcr") String acctSvcr,
            @ApiParam(value = "开户行名称", required = false) @RequestParam("acctSvcrName") String acctSvcrName,
            HttpServletResponse response) throws PasswordIllegalParam, GeneralSecurityException, PrivateKeyIllegalParam, ContractInvokeFailException, IOException, ValueNullException {
//        String securityCodeMock = "1111";
//        String securityCodeIdMock = "12";
//        String certTypeMock = "身份证";
//        String certNoMock = "123456";
//        String acctIdsMock = "12345678";
//        String svcrClassMock = "123";
//        String acctSvcrMock = "123456";
//        String acctSvcrNameMock = "中国银行";
        return accountService.register(accountName, password, enterpriseName, phone, roleCode, securityCode, securityCodeId, certType, certNo, acctIds, svcrClass, acctSvcr, acctSvcrName);
    }

    @LogInterceptor
    @ApiOperation(value = "登录", notes = "登录")
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResult<Object> login(
            @ApiParam(value = "用户名", required = true) @RequestParam("account_name") String accountName,
            @ApiParam(value = "密码", required = true) @RequestParam("password") String password,
            HttpServletResponse response) {
        return accountService.login(accountName, password, response);
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
        //TODO 找回密码controller
        return null;
    }

}
