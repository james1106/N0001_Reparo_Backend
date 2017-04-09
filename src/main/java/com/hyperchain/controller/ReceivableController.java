package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YanYufei on 2017/4/9.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Receivable", description = "应收款", position = 1)
@Controller
@RequestMapping("/v1/receivable")
public class ReceivableController {
    @Autowired
    ReceivableService receivableService;

    @LogInterceptor
    @ApiOperation(value = "签发申请", notes = "签发申请")
    @ResponseBody
    @RequestMapping(value = "sign",method = RequestMethod.POST)//路径
    public BaseResult<Object> createOrder(
            @ApiParam(value = "买方私钥地址", required = true) @RequestParam String payerPrivateKey,
            @ApiParam(value = "卖方公钥地址", required = true) @RequestParam String payeeAccount,//企业名称，从数据库查address
            @ApiParam(value = "货品名称", required = true) @RequestParam String productName,
            @ApiParam(value = "货品单价", required = true) @RequestParam int productUnitPrice,
            @ApiParam(value = "货品数量", required = true) @RequestParam int productQuantity,
            @ApiParam(value = "货品总价", required = true) @RequestParam int productTotalPrice,
            @ApiParam(value = "付款人申请仓储公司", required = true) @RequestParam String payerRepo,
            @ApiParam(value = "付款人开户行", required = true) @RequestParam String payerBank,
            @ApiParam(value = "开户行别", required = true) @RequestParam String payerBankClss,
            @ApiParam(value = "付款账户", required = true) @RequestParam String payerAccount,
            @ApiParam(value = "付款方式", required = true) @RequestParam int payingMethod
    ) throws Exception {

        long orderGenerateTime = System.currentTimeMillis();
        String orderId = "000" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String repoBusinessNo = "010" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        List<String> list= new ArrayList<>();
        list.add(orderId);
        list.add(productName);
        list.add(payerRepo);
        list.add(repoBusinessNo);
        list.add(payerBank);
        list.add(payerBankClss);
        list.add(payerAccount);

        ContractKey contractKey = new ContractKey(payerPrivateKey);


        Object[] params = new Object[7];
        params[0] = payeeAccount;
        params[1] = productUnitPrice; //单价
        params[2] = productQuantity; //
        params[3] = productTotalPrice; //
        params[4] = list;
        params[5] = 0;
        params[6] = orderGenerateTime;
        // 调用合约查询账户，获取返回结果
        return receivableService.signOutApply(contractKey, params, orderId);
    }

}
