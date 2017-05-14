package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.exception.*;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.controller.base.BaseController;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
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

import javax.servlet.http.HttpServletRequest;
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
public class AccountController extends BaseController{

    @Autowired
    AccountService accountService;

    @LogInterceptor
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @ResponseBody
    @RequestMapping(value = "/securityCode", method = RequestMethod.GET)
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
            @ApiParam(value = "用户名(不能重复)", required = true) @RequestParam("accountName") String accountName,
            @ApiParam(value = "密码", required = true) @RequestParam("password") String password,
            @ApiParam(value = "企业名称（不能重复）", required = true) @RequestParam("enterpriseName") String enterpriseName,
            @ApiParam(value = "手机号（不能重复）", required = true) @RequestParam("phone") String phone,
            @ApiParam(value = "角色code", required = true) @RequestParam("roleCode") int roleCode,
            @ApiParam(value = "验证码", required = true) @RequestParam("securityCode") String securityCode,
            @ApiParam(value = "验证码id", required = true) @RequestParam("securityCodeId") long securityCodeId,
            @ApiParam(value = "证件类型", required = true) @RequestParam("certType") String certType,
            @ApiParam(value = "证件号码", required = true) @RequestParam("certNo") String certNo,
            @ApiParam(value = "开户行账号（不能重复）", required = true) @RequestParam("acctIds") String acctIds,
            @ApiParam(value = "开户行别", required = true) @RequestParam("svcrClass") String svcrClass,
            @ApiParam(value = "开户行行号", required = true) @RequestParam("acctSvcr") String acctSvcr,
            @ApiParam(value = "开户行名称", required = true) @RequestParam("acctSvcrName") String acctSvcrName,
            HttpServletRequest request,
            HttpServletResponse response) throws PasswordIllegalParam, GeneralSecurityException, PrivateKeyIllegalParam, ContractInvokeFailException, IOException, ValueNullException, ReadFileException, PropertiesLoadException, UserInvalidException {
        return accountService.register(accountName, password, enterpriseName, phone, roleCode, securityCode, securityCodeId, certType, certNo, acctIds, svcrClass, acctSvcr, acctSvcrName, request, response);
    }

    @LogInterceptor
    @ApiOperation(value = "登录", notes = "登录")
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResult<Object> login(
            @ApiParam(value = "用户名", required = true) @RequestParam("accountName") String accountName,
            @ApiParam(value = "密码", required = true) @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException, UserInvalidException {
        return accountService.login(accountName, password,request , response);
    }

    @LogInterceptor
    @ApiOperation(value = "找回密码", notes = "找回密码")
    @ResponseBody
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public BaseResult<Object> forgetPassword(
            @ApiParam(value = "手机号", required = true) @RequestParam("phone") String phone,
            @ApiParam(value = "新密码", required = true) @RequestParam("newPassword") String newPassword,
            @ApiParam(value = "验证码", required = true) @RequestParam("securityCode") String securityCode,
            @ApiParam(value = "验证码id", required = true) @RequestParam("securityCodeId") String securityCodeId) {
        //TODO 找回密码controller
        return null;
    }

    @LogInterceptor
    @ApiOperation(value = "根据角色获取所有企业名称", notes = "获取所有企业名称")
    @ResponseBody
    @RequestMapping(value = "/allEnterpriseName", method = RequestMethod.GET)
    public BaseResult<Object> getAllEnterpriseNameByRoleCode(
            @ApiParam(value = "角色code", required = true) @RequestParam("roleCode") int roleCode,
            HttpServletRequest request) {
        return accountService.findAllEnterpriseNameByRoleCode(roleCode);
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有金融机构的企业名称和相应利率", notes = "查询所有金融机构的企业名称和相应利率")
    @ResponseBody
    @RequestMapping(value = "/allFinancialEnterpriseNameAndRate", method = RequestMethod.GET)
    public BaseResult<Object> getAllFinancialEnterpriseNameAndRate() {
        return accountService.getAllFinancialEnterpriseNameAndRate();
    }

    @LogInterceptor
    @ApiOperation(value = "金融机构设置利率", notes = "金融机构设置利率")
    @ResponseBody
    @RequestMapping(value = "/rate", method = RequestMethod.PUT)
    public BaseResult<Object> setRateForFinancial(
            @ApiParam(value = "利率", required = true) @RequestParam("rate") String rate,
            HttpServletRequest request)
            throws ContractInvokeFailException, ValueNullException, PrivateKeyIllegalParam, PasswordIllegalParam, UserInvalidException {
        String address = TokenUtil.getAddressFromCookie(request);
        return accountService.setRateForFinancial(rate, address);
    }


    @LogInterceptor
    @ApiOperation(value = "测试从cookie获取address", notes = "测试从cookie获取address")
    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.PUT)
    public BaseResult<Object> testLogin(HttpServletRequest request) {
        System.out.println("进入");
        String address = TokenUtil.getAddressFromCookie(request);
        System.out.println("拿到地址：" + address);
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.setCode(0);
        baseResult.setMessage("获取address成功");
        return baseResult;
    }

}
