//package com.hyperchain.controller;
//
//import cn.hyperchain.common.log.LogInterceptor;
//import com.hyperchain.contract.ContractKey;
//import com.hyperchain.controller.vo.BaseResult;
//import com.hyperchain.service.OrderService;
//import com.wordnik.swagger.annotations.Api;
//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
///**
// * Created by chenxiaoyang on 2017/4/11.
// */
//
//@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
//@Api(value = "Repository", description = "仓储管理", position = 1)
//@Controller
//@RequestMapping("/v1/Repository")
//public class RepositoryController {
//
//    @Autowired
//    OrderService orderService;
//
//    @LogInterceptor
//    @ApiOperation(value = "添加订单", notes = "添加订单")
//    @ResponseBody
//    @RequestMapping(value = "creation",method = RequestMethod.POST)
//    public BaseResult<Object> createOrder(
//            @ApiParam(value = "买方私钥地址", required = true) @RequestParam String payerPrivateKey,
//            @ApiParam(value = "卖方公钥地址", required = true) @RequestParam String payeeAccount,
//            @ApiParam(value = "货品名称", required = true) @RequestParam String productName,
//            @ApiParam(value = "货品单价", required = true) @RequestParam long productUnitPrice,
//            @ApiParam(value = "货品数量", required = true) @RequestParam long productQuantity,
//            @ApiParam(value = "货品总价", required = true) @RequestParam long productTotalPrice,
//            @ApiParam(value = "付款人申请仓储公司", required = true) @RequestParam String payerRepo,
//            @ApiParam(value = "付款人开户行", required = true) @RequestParam String payerBank,
//            @ApiParam(value = "开户行别", required = true) @RequestParam String payerBankClss,
//            @ApiParam(value = "付款账户", required = true) @RequestParam String payerAccount,
//            @ApiParam(value = "付款方式", required = true) @RequestParam int payingMethod
//    ) throws Exception {
//
//        long orderGenerateTime = System.currentTimeMillis();
//        String orderId = "100" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (new Random().nextInt(900)+100);
//        String txSerialNo = orderId + 00;
//        String repoBusinessNo = "130" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);
//        List<String> list= new ArrayList<>();
//        list.add(orderId);
//        list.add(productName);
//        list.add(payerRepo);
//        list.add(repoBusinessNo);
//        list.add(payerBank);
//        list.add(payerBankClss);
//        list.add(payerAccount);
//        list.add(txSerialNo);
//
//        ContractKey contractKey = new ContractKey(payerPrivateKey);
//
//
//        Object[] params = new Object[7];
//        params[0] = payeeAccount;
//        params[1] = productUnitPrice*100; //单价
//        params[2] = productQuantity; //
//        params[3] = productTotalPrice*100; //
//        params[4] = list;
//        params[5] = payingMethod;
//        params[6] = orderGenerateTime;
//        // 调用合约查询账户，获取返回结果
//        return orderService.createOrder(contractKey, params, orderId);
//    }
//
//
//    @LogInterceptor
//    @ApiOperation(value = "确认订单", notes = "确认订单")
//    @ResponseBody
//    @RequestMapping(value = "confirmation",method = RequestMethod.POST)
//    public BaseResult<Object> confirmOrder(
//            @ApiParam(value = "卖方私钥地址", required = true) @RequestParam String payeePrivateKey,
//            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo,
//            @ApiParam(value = "仓储公司", required = true) @RequestParam String payeeRepo,
//            @ApiParam(value = "仓单编号", required = true) @RequestParam String payeeRepoCertNo
//    ) throws Exception {
//
//        ContractKey contractKey = new ContractKey(payeePrivateKey);
//        long txConfirmTime = System.currentTimeMillis();
//        String txSerialNo = orderNo + "01";
//        Object[] contractParams = new Object[1];
//        contractParams[0] = orderNo;
//        contractParams[1] = payeeRepo;
//        contractParams[2] = payeeRepoCertNo;
//        contractParams[3] = txSerialNo;
//        contractParams[4] = txConfirmTime;
//        // 调用合约确认订单，获取返回结果
//        return orderService.confirmOrder(contractKey, contractParams);
//    }
//
//
//    @LogInterceptor
//    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
//    @ResponseBody
//    @RequestMapping(value = "detail",method = RequestMethod.GET)
//    public BaseResult<Object> queryOrderDetail(
//            @ApiParam(value = "用户地址", required = true) @RequestParam String account,
//            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo
//    ) throws Exception {
//
//        ContractKey contractKey = new ContractKey(account);
//        Object[] contractParams = new Object[1];
//        contractParams[0] = orderNo;
//
//        return orderService.queryOrderDetail(contractKey, contractParams);
//    }
//
//    @LogInterceptor
//    @ApiOperation(value = "用户查询订单编号列表", notes = "用户查询订单编号列表")
//    @ResponseBody
//    @RequestMapping(value = "order_list/{role}",method = RequestMethod.GET)
//    public BaseResult<Object> queryAllOrderList(
//            @ApiParam(value = "买方私钥地址", required = true) @RequestParam String payeePrivateKey,
//            @ApiParam(value = "公司角色", required = true) @PathVariable(value = "role") int companyRole
//    ) throws Exception {
//
//        ContractKey contractKey = new ContractKey(payeePrivateKey);
//        Object[] contractParams = new Object[1];
//        contractParams[0] = companyRole;
//        // 调用合约确认订单，获取返回结果
//        return orderService.queryAllOrderOverViewInfoList(contractKey, contractParams);
//    }
//
//}
