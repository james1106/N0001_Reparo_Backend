//package com.hyperchain.controller;
//
//
//import cn.hyperchain.common.log.LogInterceptor;
//import com.hyperchain.ESDKUtil;
//import com.hyperchain.contract.ContractKey;
//import com.hyperchain.controller.base.BaseController;
//import com.hyperchain.controller.vo.BaseResult;
//import com.hyperchain.service.AddUserDemo;
//import com.hyperchain.service.QueryUserDemo;
//import com.hyperchain.service.QueryUserDetailListDemo;
//import com.hyperchain.service.QueryUserListDemo;
//import com.wordnik.swagger.annotations.Api;
//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * app用户接口层
// *
// * @Author Lizhong Kuang
// * @Modify 16/11/2 上午1:01
// */
//@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
//@Api(value = "AccountDemo", description = "用户账号", position = 10)
//@Controller
//@RequestMapping("/v1/account-demo")
//public class UserControllerDemo extends BaseController {
//
//    @Autowired
//    QueryUserDemo queryUser;
//
//    @Autowired
//    AddUserDemo addUser;
//
//    @Autowired
//    QueryUserListDemo queryUserList;
//
//    @Autowired
//    QueryUserDetailListDemo queryUserDetailList;
//
//    @LogInterceptor
//    @ApiOperation(value = "添加用户", notes = "通用")
//    @ResponseBody
//    @RequestMapping(value = "addUser",method = RequestMethod.GET)
//    public BaseResult<Object> addUser(
//            @ApiParam(value = "ID", required = true) @RequestParam String id,
//            @ApiParam(value = "用户名", required = true) @RequestParam String nickName,
//            @ApiParam(value = "密码", required = true) @RequestParam String password,
//            @ApiParam(value = "手机", required = true) @RequestParam String phoneNum
//    ) throws Exception {
//
//        List<String> keyInfos = ESDKUtil.newAccount();
//        String privateKey = keyInfos.get(1);
//
//        // 合约的公私钥
//        ContractKey contractKey = new ContractKey(privateKey);
//
//        // 合约方法参数（公钥，角色代码，物流交换码）
//        Object[] contractParams = new Object[4];
//        contractParams[0] = id;
//        contractParams[1] = nickName;
//        contractParams[2] = password;
//        contractParams[3] = phoneNum;
//
//        // 调用合约查询账户，获取返回结果
//        return addUser.invokeContract(contractKey, contractParams);
//    }
//
//    @LogInterceptor
//    @ApiOperation(value = "查询用户", notes = "通用")
//    @ResponseBody
//    @RequestMapping(value = "queryUser",method = RequestMethod.GET)
//    public BaseResult<Object> queryUser(
//            @ApiParam(value = "ID", required = true) @RequestParam String id
//    ) throws Exception {
//
//        List<String> keyInfos = ESDKUtil.newAccount();
//        String privateKey = keyInfos.get(1);
//
//        // 合约的公私钥
//        ContractKey contractKey = new ContractKey(privateKey);
//
//        // 合约方法参数（公钥，角色代码，物流交换码）
//        Object[] contractParams = new Object[1];
//        contractParams[0] = id;
//
//        // 调用合约查询账户，获取返回结果
//        return queryUser.invokeContract(contractKey, contractParams);
//    }
//
//    @LogInterceptor
//    @ApiOperation(value = "查询用户列表", notes = "通用")
//    @ResponseBody
//    @RequestMapping(value = "queryUserList",method = RequestMethod.GET)
//    public BaseResult<Object> queryUserList(
//    ) throws Exception {
//
//        List<String> keyInfos = ESDKUtil.newAccount();
//        String privateKey = keyInfos.get(1);
//
//        // 合约的公私钥
//        ContractKey contractKey = new ContractKey(privateKey);
//
//        // 合约方法参数（公钥，角色代码，物流交换码）
//        Object[] contractParams = new Object[0];
//
//        // 调用合约查询账户，获取返回结果
//        return queryUserList.invokeContract(contractKey, contractParams);
//    }
//
//    @LogInterceptor
//    @ApiOperation(value = "查询用户详情列表", notes = "通用")
//    @ResponseBody
//    @RequestMapping(value = "queryUserDetailList",method = RequestMethod.GET)
//    public BaseResult<Object> queryUserDetailList(
//    ) throws Exception {
//
//        List<String> keyInfos = ESDKUtil.newAccount();
//        String privateKey = keyInfos.get(1);
//
//        // 合约的公私钥
//        ContractKey contractKey = new ContractKey(privateKey);
//
//        // 合约方法参数（公钥，角色代码，物流交换码）
//        Object[] contractParams = new Object[0];
//
//        // 调用合约查询账户，获取返回结果
//        return queryUserDetailList.invokeContract(contractKey, contractParams);
//    }
//}
//
