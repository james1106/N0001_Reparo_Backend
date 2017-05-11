package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.controller.base.BaseController;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
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

import javax.servlet.http.HttpServletRequest;

/**
 * Created by YanYufei on 2017/4/9.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Receivable", description = "应收款", position = 1)
@Controller
@RequestMapping("/v1/receivable")
public class ReceivableController extends BaseController{
    @Autowired
    ReceivableService receivableService;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    AccountEntityRepository accountEntityRepository;

    @LogInterceptor
    @ApiOperation(value = "签发申请", notes = "签发申请")
    @ResponseBody
    @RequestMapping(value = "sign", method = RequestMethod.POST)//路径
    public BaseResult<Object> signOutApply(
            //@ApiParam(value = "收款人私钥", required = true) @RequestParam String pyeeAddress,//收款人即签发人
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo,
            @ApiParam(value = "付款人账号", required = true) @RequestParam String pyer,//承兑人 = 付款人
            @ApiParam(value = "收款人账号", required = true) @RequestParam String pyee,//签发人 = 收款人
            @ApiParam(value = "票面金额", required = true) @RequestParam double isseAmt,
            @ApiParam(value = "到期日", required = true) @RequestParam long dueDt,
            @ApiParam(value = "带息利率", required = true) @RequestParam String rate,
            @ApiParam(value = "合同编号", required = true) @RequestParam String contractNo,
            @ApiParam(value = "发票号", required = true) @RequestParam String invoiceNo,
            HttpServletRequest request
    ) throws Exception {
        // 调用合约查询账户，获取返回结果
        return receivableService.signOutApply(orderNo, pyer, pyee, isseAmt, dueDt, rate, contractNo, invoiceNo, request);
    }

    @LogInterceptor
    @ApiOperation(value = "签发回复——承兑", notes = "签发回复")
    @ResponseBody
    @RequestMapping(value = "accept", method = RequestMethod.POST)//路径
    public BaseResult<Object> signOutReply(
            //@ApiParam(value = "回复人私钥", required = true) @RequestParam String replyerAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            @ApiParam(value = "回复人账号", required = true) @RequestParam String replyerAcctId,
            @ApiParam(value = "回复意见", required = true) @RequestParam int response,
            HttpServletRequest request
    ) throws Exception {

        return receivableService.signOutReply(receivableNo, replyerAcctId, response, request);
    }

    @LogInterceptor
    @ApiOperation(value = "贴现申请", notes = "贴现申请")
    @ResponseBody
    @RequestMapping(value = "discountApply", method = RequestMethod.POST)//路径
    public BaseResult<Object> discountApply(
            //@ApiParam(value = "申请人私钥", required = true) @RequestParam String applicantAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,//应收款编号
            @ApiParam(value = "申请人账号", required = true) @RequestParam String applicantAcctId,//申请人账号
            @ApiParam(value = "回复人账号", required = true) @RequestParam String replyerAcctId,//回复人账号
            @ApiParam(value = "申请贴现金额", required = true) @RequestParam double discountApplyAmount,//申请贴现金额
            @ApiParam(value = "贴现利率", required = true) @RequestParam double discountedRate,//贴现利率
            HttpServletRequest request
    ) throws Exception {
        // 调用合约查询账户，获取返回结果
        return receivableService.discountApply(receivableNo, applicantAcctId, replyerAcctId, discountApplyAmount, discountedRate, request);

    }

    @LogInterceptor
    @ApiOperation(value = "贴现回复", notes = "贴现回复")
    @ResponseBody
    @RequestMapping(value = "discountReply", method = RequestMethod.POST)//路径
    public BaseResult<Object> discountReply(
//            @ApiParam(value = "回复人私钥", required = true) @RequestParam String ReplyerAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,//应收款编号
            @ApiParam(value = "回复人账号", required = true) @RequestParam String replyerAcctId,//回复人账号 todo
            @ApiParam(value = "回复人意见", required = true) @RequestParam int response,//回复人意见
            @ApiParam(value = "回复到手金额", required = true) @RequestParam double discountInHandAmount,//回复到手金额
            @ApiParam(value = "贴现利率", required = true) @RequestParam double discountRate,//利率
            @ApiParam(value = "申请贴现金额金额", required = true) @RequestParam double discountApplyAmount,//票面金额
//            @ApiParam(value = "贴现申请时的流水号", required = true) @RequestParam long discountApplySerialNo,//贴现申请时的流水号
            HttpServletRequest request//http请求实体
    ) throws Exception {

        // 调用合约查询账户，获取返回结果
        return receivableService.discountReply(receivableNo, replyerAcctId, response, discountInHandAmount, discountRate, discountApplyAmount, request);

    }

    @LogInterceptor
    @ApiOperation(value = "兑付", notes = "兑付")
    @ResponseBody
    @RequestMapping(value = "cash", method = RequestMethod.POST)//路径
    public BaseResult<Object> cash(
            //@ApiParam(value = "回复人私钥", required = true) @RequestParam String replyerAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            @ApiParam(value = "兑付金额", required = true) @RequestParam double cashedAmount,
            @ApiParam(value = "回复意见", required = true) @RequestParam int response,
            HttpServletRequest request
    ) throws Exception {

        // 调用合约查询账户，获取返回结果
        return receivableService.cash(receivableNo, cashedAmount, response, request);

    }

    @LogInterceptor
    @ApiOperation(value = "买方／卖方应收款列表", notes = "买方／卖方应收款列表")
    @ResponseBody
    @RequestMapping(value = "receivableSimpleDetailList", method = RequestMethod.POST)//路径
    public BaseResult<Object> receivableSimpleDetailList(
            //@ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "用户角色", required = true) @RequestParam int roleCode,//0是买家、1是卖家
            HttpServletRequest request
    ) throws Exception {

        // 调用合约查询账户，获取返回结果
        return receivableService.receivableSimpleDetailList(roleCode, request);

    }

    @LogInterceptor
    @ApiOperation(value = "根据应收款编号查应收款详情(包含流水信息)", notes = "根据应收款编号查应收款详情(包含流水信息)")
    @ResponseBody
    @RequestMapping(value = "receivableInfoWithSerial", method = RequestMethod.POST)//路径
    public BaseResult<Object> getReceivableAllInfoWithSerial(
            //@ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            @ApiParam(value = "操作人账号", required = true) @RequestParam String operatorAcctId,
            HttpServletRequest request//http请求实体
    ) throws Exception {

        // 调用合约查询账户，获取返回结果
        return receivableService.getReceivableAllInfoWithSerial(receivableNo, operatorAcctId, request);

    }

}
