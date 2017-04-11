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
    public BaseResult<Object> signOutApply(
            @ApiParam(value = "收款人私钥", required = true) @RequestParam String pyeeAddress,//收款人即签发人
            @ApiParam(value = "收款人账号", required = true) @RequestParam String pyee,//签发人 = 收款人
            @ApiParam(value = "付款人账号", required = true) @RequestParam String pyer,//承兑人 = 付款人
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo,
            @ApiParam(value = "票面金额", required = true) @RequestParam int isseAmt,
            @ApiParam(value = "到期日", required = true) @RequestParam int dueDt,
            @ApiParam(value = "带息利率", required = true) @RequestParam String rate,
            @ApiParam(value = "合同编号", required = true) @RequestParam String contractNo,
            @ApiParam(value = "发票号", required = true) @RequestParam String invoiceNo
    ) throws Exception {

        long receivableGenerateTime = System.currentTimeMillis();
        String receivableNo = "120" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//应收款编号
        String serialNo = "121" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//流水号
        List<String> list= new ArrayList<>();
        list.add(contractNo);
        list.add(invoiceNo);

        ContractKey contractKey = new ContractKey(pyeeAddress);


        Object[] params = new Object[12];
        params[0] = receivableNo;
        params[1] = orderNo;
        params[2] = pyee;
        params[3] = pyer;
        params[4] = pyee;
        params[5] = pyer;
        params[6] = isseAmt;
        params[7] = dueDt;
        params[8] = rate;
        params[9] = list;
        params[10] = serialNo;
        params[11] = receivableGenerateTime;

        // 调用合约查询账户，获取返回结果
        return receivableService.signOutApply(contractKey, params, receivableNo);
    }

    @LogInterceptor
    @ApiOperation(value = "签发回复——承兑", notes = "签发回复")
    @ResponseBody
    @RequestMapping(value = "accept",method = RequestMethod.POST)//路径
    public BaseResult<Object> signOutReply(
            @ApiParam(value = "回复人私钥", required = true) @RequestParam String replyerAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            @ApiParam(value = "回复人账号", required = true) @RequestParam String replyerAcctId,
            @ApiParam(value = "回复意见", required = true) @RequestParam String response
    ) throws Exception {

        long operateTime = System.currentTimeMillis();
        String serialNo = "122" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//签发回复流水号

        ContractKey contractKey = new ContractKey(replyerAddress);

        Object[] params = new Object[5];
        params[0] = receivableNo;
        params[1] = replyerAcctId;
        params[2] = response;
        params[3] = serialNo;
        params[4] = operateTime;

        // 调用合约查询账户，获取返回结果
        return receivableService.signOutReply(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "根据应收款编号查应收款详情", notes = "根据应收款编号查应收款详情")
    @ResponseBody
    @RequestMapping(value = "receivableInfo",method = RequestMethod.POST)//路径
    public BaseResult<Object> signOutReply(
            @ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            @ApiParam(value = "操作人账号", required = true) @RequestParam String operatorAcctId
    ) throws Exception {
        ContractKey contractKey = new ContractKey(operatorAddress);
        Object[] params = new Object[2];
        params[0] = receivableNo;
        params[1] = operatorAcctId;


        // 调用合约查询账户，获取返回结果
        return receivableService.getReceivableAllInfo(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "用户根据流水号查交易记录详情", notes = "用户根据流水号查交易记录详情")
    @ResponseBody
    @RequestMapping(value = "recordInfo",method = RequestMethod.POST)//路径
    public BaseResult<Object> getRecordBySerialNo(
            @ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "流水号", required = true) @RequestParam String serialNo
    ) throws Exception {

        ContractKey contractKey = new ContractKey(operatorAddress);

        Object[] params = new Object[1];
        params[0] = serialNo;

        // 调用合约查询账户，获取返回结果
        return receivableService.getRecordBySerialNo(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "应收款操作流水号", notes = "应收款操作流水号")
    @ResponseBody
    @RequestMapping(value = "historySerialNo",method = RequestMethod.POST)//路径
    public BaseResult<Object> getReceivableHistorySerialNo(
            @ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo
    ) throws Exception {

        ContractKey contractKey = new ContractKey(operatorAddress);

        Object[] params = new Object[1];
        params[0] = receivableNo;

        // 调用合约查询账户，获取返回结果
        return receivableService.getReceivableHistorySerialNo(contractKey, params);
    }

}
