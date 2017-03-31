package com.hyperchain.controller;


import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.ESDKUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.base.BaseController;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.AddUser;
import com.hyperchain.service.QueryUser;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app用户接口层
 *
 * @Author Lizhong Kuang
 * @Modify 16/11/2 上午1:01
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Account", description = "用户账号", position = 10)
@Controller
@RequestMapping("/v1/account")
public class UserController extends BaseController {

    @Autowired
    QueryUser queryUser;

    @Autowired
    AddUser addUser;

    @LogInterceptor
    @ApiOperation(value = "添加用户", notes = "通用")
    @ResponseBody
    @RequestMapping(value = "addUser",method = RequestMethod.GET)
    public BaseResult<Object> addUser(
            @ApiParam(value = "ID", required = true) @RequestParam String id,
            @ApiParam(value = "用户名", required = true) @RequestParam String nickName,
            @ApiParam(value = "密码", required = true) @RequestParam String password,
            @ApiParam(value = "手机", required = true) @RequestParam String phoneNum
    ) throws Exception {

        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        // 合约方法参数（公钥，角色代码，物流交换码）
        Object[] contractParams = new Object[4];
        contractParams[0] = id;
        contractParams[1] = nickName;
        contractParams[2] = password;
        contractParams[3] = phoneNum;

//        String[] resultParams = new String[1];

//        ContractResult contractResult = ContractUtil.invokeContract(contractKey,"addUser", contractParams, resultParams);
//        System.out.println("合约返回结果：" + contractResult.getValue());

        // 调用合约查询账户，获取返回结果
        return addUser.invokeContract(contractKey, contractParams);
    }

    @LogInterceptor
    @ApiOperation(value = "查询用户", notes = "通用")
    @ResponseBody
    @RequestMapping(value = "queryUser",method = RequestMethod.GET)
    public BaseResult<Object> queryUser(
            @ApiParam(value = "ID", required = true) @RequestParam String id
    ) throws Exception {

        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        // 合约方法参数（公钥，角色代码，物流交换码）
        Object[] contractParams = new Object[1];
        contractParams[0] = id;

        String[] resultParams = new String[3];

        //ContractResult contractResult = ContractUtil.invokeContract(contractKey,"queryUser", contractParams, resultParams);
        //System.out.println("合约返回结果：" + contractResult.getValue());

        // 调用合约查询账户，获取返回结果
        return queryUser.invokeContract(contractKey, contractParams);

    }
}

