package com.hyperchain.controller;


import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant.Code;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.base.BaseController;
import com.hyperchain.common.util.ParamsCheck;
import com.hyperchain.controller.vo.BaseResult;
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

    @LogInterceptor
    @ApiOperation(value = "查询用户账号", notes = "通用")
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public BaseResult<Object> create(
            @ApiParam("Id") @RequestParam("My_Id") String Id
    ) throws Exception {

        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        // 合约方法参数（公钥，角色代码，物流交换码）
        Object[] contractParams = new Object[4];
        contractParams[0] = 1;
        contractParams[1] = "Jack";
        contractParams[2] = "123456";
        contractParams[3] = "110";

        String[] resultParams = new String[1];

        ContractResult contractResult = ContractUtil.invokeContract(contractKey,"addUser", contractParams, resultParams);
        System.out.println("结果11：" + contractResult.getValue());

        // 调用合约查询账户，获取返回结果
        return queryUser.invokeContract(contractKey, contractParams);
    }
}

