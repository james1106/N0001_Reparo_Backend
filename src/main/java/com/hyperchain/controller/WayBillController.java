package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by ldy on 2017/4/12.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "WayBill", description = "运单管理", position = 10)
@Controller
@RequestMapping("/v1/waybill")
public class WayBillController {

    @LogInterceptor
    @ApiOperation(value = "企业申请发货运单", notes = "企业申请发货运单")
    @ResponseBody
    @RequestMapping(value = "/unConfirmedWaybill", method = RequestMethod.POST)
    public BaseResult<Object> postUnConfirmedWaybill(
            @ApiParam(value = "订单编号", required = true) @RequestParam("orderNo") String orderNo,
            @ApiParam(value = "物流公司企业名称", required = true) @RequestParam("logisticsEnterpriseName") String logisticsEnterpriseName,
            @ApiParam(value = "发货人企业名称（当前账户企业名称）", required = true) @RequestParam("senderEnterpriseName") String senderEnterpriseName,
            @ApiParam(value = "收货人企业名称", required = true) @RequestParam("receiverEnterpriseName") String receiverEnterpriseName,
            @ApiParam(value = "货品名称", required = true) @RequestParam("productName") int productName,
            @ApiParam(value = "货品数量", required = false) @RequestParam("productQuantity") String productQuantity,
            @ApiParam(value = "货品价值", required = false) @RequestParam("productValue") long productValue,
            @ApiParam(value = "货品所在仓储公司名称", required = false) @RequestParam("senderRepoEnterpriseName") String senderRepoEnterpriseName,
            @ApiParam(value = "发货货品仓单编号", required = false) @RequestParam("senderRepoCertNo") String senderRepoCertNo,
            @ApiParam(value = "收货仓储公司名称", required = false) @RequestParam("receiverRepoEnterpriseName") String receiverRepoEnterpriseName,
            @ApiParam(value = "收货仓储业务编号", required = false) @RequestParam("receiverRepoBusinessNo") String receiverRepoBusinessNo,
            HttpServletRequest request,
            HttpServletResponse response) {
        return null;
    }


    @LogInterceptor
    @ApiOperation(value = "物流公司确认发货", notes = "物流公司确认发货")
    @ResponseBody
    @RequestMapping(value = "/confirmedWaybill", method = RequestMethod.POST)
    public BaseResult<Object> postConfirmedWaybill(
            @ApiParam(value = "订单编号", required = true) @RequestParam("orderNo") String orderNo,
            HttpServletRequest request,
            HttpServletResponse response) {
        return null;
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有与自己相关的运单信息", notes = "查询所有与自己相关的运单信息")
    @ResponseBody
    @RequestMapping(value = "/allWayBillDetail", method = RequestMethod.GET)
    public BaseResult<Object> getAllRelatedWayBillDetail(
            HttpServletRequest request,
            HttpServletResponse response) {
        return null;
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有与自己相关的运单信息", notes = "查询所有与自己相关的运单信息")
    @ResponseBody
    @RequestMapping(value = "/allWayBillDetail", method = RequestMethod.PUT)
    public BaseResult<Object> updateWayBillStatusToReceived(
            @ApiParam(value = "订单编号", required = true) @RequestParam("orderNo") String orderNo,
            HttpServletRequest request,
            HttpServletResponse response) {
        return null;
    }

}
