package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.ESDKUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by liangyue on 2017/4/7.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Order", description = "订单", position = 1)
@Controller
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    CreateOrderService createOrderService;

    @Autowired
    ConfirmOrderService confirmOrderService;

    @Autowired
    QueryOrderDetailService queryOrderDetailService;

    @Autowired
    QueryAllOrderListForPayeeService queryAllOrderListForPayeeService;

    @Autowired
    QueryAllOrderListForPayerService queryAllOrderListForPayerService;


    @LogInterceptor
    @ApiOperation(value = "添加订单", notes = "添加订单")
    @ResponseBody
    @RequestMapping(value = "new",method = RequestMethod.POST)
    public BaseResult<Object> createOrder(
            @ApiParam(value = "买方私钥地址", required = true) @RequestParam String payerPrivateKey,
            @ApiParam(value = "卖方公钥地址", required = true) @RequestParam String payeeAccount,
            @ApiParam(value = "货品名称", required = true) @RequestParam String productName,
            @ApiParam(value = "货品单价", required = true) @RequestParam int productPrice,
            @ApiParam(value = "货品数量", required = true) @RequestParam int productNum,
            @ApiParam(value = "货品总价", required = true) @RequestParam int totalPrice,
            @ApiParam(value = "付款银行", required = true) @RequestParam String payerBank,
            @ApiParam(value = "开户行别", required = true) @RequestParam String payerBankClss,
            @ApiParam(value = "付款账户", required = true) @RequestParam String payerBankAccount,
            @ApiParam(value = "付款方式", required = true) @RequestParam int payingMethod,
            @ApiParam(value = "生成订单时间", required = true) @RequestParam int timeStamp
    ) throws Exception {

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(payerPrivateKey);
        String orderId = "20170405205903002";

        // 合约方法参数（公钥，角色代码，物流交换码）
        Object[] contractParams = new Object[11];
        contractParams[0] = orderId;
        contractParams[1] = payeeAccount;
        contractParams[2] = productName;
        contractParams[3] = productPrice;
        contractParams[4] = productNum;
        contractParams[5] = totalPrice;
        contractParams[6] = payerBank;
        contractParams[7] = payerBankClss;
        contractParams[8] = payerBankAccount;
        contractParams[9] = payingMethod;
        contractParams[10] = timeStamp;

        Map<String, Object> resultMap = new HashedMap();
        BaseResult<Object> result = new BaseResult<>();
        resultMap.put("orderId", orderId);
        resultMap.put("result", createOrderService.invokeContract(contractKey, contractParams));
        result.setData(resultMap);

        // 调用合约查询账户，获取返回结果
        return result;
    }


    @LogInterceptor
    @ApiOperation(value = "确认订单", notes = "确认订单")
    @ResponseBody
    @RequestMapping(value = "confirmation",method = RequestMethod.POST)
    public BaseResult<Object> confirmOrder(
            @ApiParam(value = "卖方私钥地址", required = true) @RequestParam String payeePrivateKey,
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderId
    ) throws Exception {

        ContractKey contractKey = new ContractKey(payeePrivateKey);

        Object[] contractParams = new Object[1];
        contractParams[0] = orderId;
        // 调用合约确认订单，获取返回结果
        return confirmOrderService.invokeContract(contractKey, contractParams);
    }


    @LogInterceptor
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    @ResponseBody
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public BaseResult<Object> queryOrderDetail(
            @ApiParam(value = "卖方私钥地址", required = true) @RequestParam String payeePrivateKey,
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderId
    ) throws Exception {

        ContractKey contractKey = new ContractKey(payeePrivateKey);

        Object[] contractParams = new Object[1];
        contractParams[0] = orderId;
        // 调用合约确认订单，获取返回结果
        return queryOrderDetailService.invokeContract(contractKey, contractParams);
    }

    @LogInterceptor
    @ApiOperation(value = "买方查询订单编号列表", notes = "买方查询订单编号列表")
    @ResponseBody
    @RequestMapping(value = "payer/order_list",method = RequestMethod.GET)
    public BaseResult<Object> queryAllOrderListForPayer(
            @ApiParam(value = "买方私钥地址", required = true) @RequestParam String payeePrivateKey
    ) throws Exception {

        ContractKey contractKey = new ContractKey(payeePrivateKey);
        Object[] contractParams = new Object[0];
        // 调用合约确认订单，获取返回结果
        return queryAllOrderListForPayerService.invokeContract(contractKey, contractParams);
    }

    @LogInterceptor
    @ApiOperation(value = "卖方查询订单编号列表", notes = "卖方查询订单编号列表")
    @ResponseBody
    @RequestMapping(value = "payee/order_list",method = RequestMethod.GET)
    public BaseResult<Object> queryAllOrderListForPayee(
            @ApiParam(value = "买方私钥地址", required = true) @RequestParam String payeePrivateKey
    ) throws Exception {

        ContractKey contractKey = new ContractKey(payeePrivateKey);
        Object[] contractParams = new Object[0];
        // 调用合约确认订单，获取返回结果
        return queryAllOrderListForPayeeService.invokeContract(contractKey, contractParams);
    }
}


