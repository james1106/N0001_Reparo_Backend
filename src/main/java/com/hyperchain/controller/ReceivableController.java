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
            @ApiParam(value = "收款人账号", required = true) @RequestParam String pyee,//签发人
            @ApiParam(value = "付款人账号", required = true) @RequestParam String pyer,//承兑人
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo,
            @ApiParam(value = "票面金额", required = true) @RequestParam int isseAmt,
            @ApiParam(value = "到期日", required = true) @RequestParam int dueDt,
            @ApiParam(value = "带息利率", required = true) @RequestParam int rate,
            @ApiParam(value = "合同编号", required = true) @RequestParam String contractNo,
            @ApiParam(value = "发票号", required = true) @RequestParam String invoiceNo
    ) throws Exception {

        long receivableGenerateTime = System.currentTimeMillis();
        String receivableNo = "120" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String serialNo = "121" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
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

}
