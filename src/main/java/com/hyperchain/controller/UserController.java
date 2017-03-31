package com.hyperchain.controller;


import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.common.constant.BaseConstant.Code;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.contract.ContractKey;
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
            @ApiParam("私钥") @RequestParam("private_key") String privateKey
    ) throws Exception {

        // 检查私钥格式是否输入正确
        if (!ParamsCheck.checkPrivateKey(privateKey)) {
            throw new PrivateKeyIllegalParam(Code.INVALID_PARAM_PRIVATE_KEY, privateKey);
        }

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        // 合约方法参数（公钥，角色代码，物流交换码）
        Object[] contractParams = new Object[0];

        // 调用合约查询账户，获取返回结果
        return queryUser.invokeContract(contractKey, contractParams);
    }


}

