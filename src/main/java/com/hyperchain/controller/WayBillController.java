package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.common.exception.*;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.controller.base.BaseController;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
import com.hyperchain.service.WayBillService;
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
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ldy on 2017/4/12.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "waybill", description = "运单管理", position = 10)
@Controller
@RequestMapping("/v1/waybill")
public class WayBillController extends BaseController{

    @Autowired
    WayBillService wayBillService;

    @LogInterceptor
    @ApiOperation(value = "企业申请发货运单", notes = "企业申请发货运单")
    @ResponseBody
    @RequestMapping(value = "/unConfirmedWaybill", method = RequestMethod.POST)
    public BaseResult<Object> postUnConfirmedWaybill(
            @ApiParam(value = "订单编号", required = true) @RequestParam("orderNo") String orderNo,
            @ApiParam(value = "物流公司企业名称", required = true) @RequestParam("logisticsEnterpriseName") String logisticsEnterpriseName,
            @ApiParam(value = "发货人企业名称（当前账户企业名称）", required = true) @RequestParam("senderEnterpriseName") String senderEnterpriseName,
            @ApiParam(value = "收货人企业名称", required = true) @RequestParam("receiverEnterpriseName") String receiverEnterpriseName,
            @ApiParam(value = "货品名称", required = true) @RequestParam("productName") String productName,
            @ApiParam(value = "货品数量", required = true) @RequestParam("productQuantity") long productQuantity,
            @ApiParam(value = "货品价值", required = true) @RequestParam("productValue") double productValue,
            @ApiParam(value = "货品所在仓储公司名称", required = true) @RequestParam("senderRepoEnterpriseName") String senderRepoEnterpriseName,
            @ApiParam(value = "发货货品仓单编号", required = true) @RequestParam("senderRepoCertNo") String senderRepoCertNo,
            @ApiParam(value = "收货仓储公司名称", required = true) @RequestParam("receiverRepoEnterpriseName") String receiverRepoEnterpriseName,
            @ApiParam(value = "收货仓储业务编号", required = true) @RequestParam("receiverRepoBusinessNo") String receiverRepoBusinessNo,
            HttpServletRequest request,
            HttpServletResponse response)
            throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException, UserInvalidException {
        LogUtil.debug("logisticsEnterpriseName：" + logisticsEnterpriseName);
        LogUtil.debug("receiverEnterpriseName：" + receiverEnterpriseName);
        LogUtil.debug("senderEnterpriseName：" + senderEnterpriseName);
        LogUtil.debug("senderRepoEnterpriseName：" + senderRepoEnterpriseName);
        LogUtil.debug("receiverRepoEnterpriseName：" + receiverRepoEnterpriseName);
        return wayBillService.generateUnConfirmedWaybill(orderNo, logisticsEnterpriseName, senderEnterpriseName, receiverEnterpriseName, productName, productQuantity, productValue, senderRepoEnterpriseName, senderRepoCertNo, receiverRepoEnterpriseName, receiverRepoBusinessNo, request);
    }


    @LogInterceptor
    @ApiOperation(value = "物流公司确认发货", notes = "物流公司确认发货")
    @ResponseBody
    @RequestMapping(value = "/confirmedWaybill", method = RequestMethod.POST)
    public BaseResult<Object> postConfirmedWaybill(
            @ApiParam(value = "订单编号", required = true) @RequestParam("orderNo") String orderNo,
            HttpServletRequest request,
            HttpServletResponse response)
            throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException, UserInvalidException {
        return wayBillService.generateConfirmedWaybill(orderNo, request);
    }

    @LogInterceptor
    @ApiOperation(value = "物流公司更新运单状态为已送达", notes = "物流公司更新运单状态为已送达")
    @ResponseBody
    @RequestMapping(value = "/receivedStatus", method = RequestMethod.PUT)
    public BaseResult<Object> updateWayBillStatusToReceived(
            @ApiParam(value = "订单编号", required = true) @RequestParam("orderNo") String orderNo,
            HttpServletRequest request,
            HttpServletResponse response)
            throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException, UserInvalidException {
        return wayBillService.updateWayBillStatusToReceived(orderNo, request);
    }

    @LogInterceptor
    @ApiOperation(value = "根据订单号查询所有运单信息", notes = "根据订单号查询所有运单信息")
    @ResponseBody
    @RequestMapping(value = "/wayBillDetail", method = RequestMethod.GET)
    public BaseResult<Object> getWayBillDetailByOrderNo(
            @ApiParam(value = "订单编号", required = true) @RequestParam("orderNo") String orderNo,
            HttpServletRequest request,
            HttpServletResponse response)
            throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException, UserInvalidException, QueryNotExistUserException {
        return wayBillService.getWayBillDetailByOrderNo(orderNo, request);
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有与自己相关的运单信息", notes = "查询所有与自己相关的运单信息")
    @ResponseBody
    @RequestMapping(value = "/allWayBillDetail", method = RequestMethod.GET)
    public BaseResult<Object> getAllRelatedWayBillDetail(
            HttpServletRequest request,
            HttpServletResponse response)
            throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException, UserInvalidException, QueryNotExistUserException {
        return wayBillService.getAllRelatedWayBillDetail(request);
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有与自己相关的运单数量", notes = "查询所有与自己相关的运单数量")
    @ResponseBody
    @RequestMapping(value = "/allWayBillCount", method = RequestMethod.GET)
    public BaseResult<Object> getAllWaybillCount(
            HttpServletRequest request) throws UserInvalidException, ValueNullException, PasswordIllegalParam, PrivateKeyIllegalParam, ContractInvokeFailException {
        String address = TokenUtil.getAddressFromCookie(request);
        return wayBillService.getAllWayBillCount(address);
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有与自己相关的待发货运单数量", notes = "查询所有与自己相关的待发货运单数量")
    @ResponseBody
    @RequestMapping(value = "/waitingWayBillCount", method = RequestMethod.GET)
    public BaseResult<Object> getWaitingWaybillCount(
            HttpServletRequest request) throws UserInvalidException, ValueNullException, PasswordIllegalParam, PrivateKeyIllegalParam, ContractInvokeFailException {
        String address = TokenUtil.getAddressFromCookie(request);
        return wayBillService.getWaitingWayBillCount(address);
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有与自己相关的发货待响应运单数量", notes = "查询所有与自己相关的发货待响应运单数量")
    @ResponseBody
    @RequestMapping(value = "/requestingWayBillCount", method = RequestMethod.GET)
    public BaseResult<Object> getRequestingWaybillCount(
            HttpServletRequest request) throws UserInvalidException, ValueNullException, PasswordIllegalParam, PrivateKeyIllegalParam, ContractInvokeFailException {
        String address = TokenUtil.getAddressFromCookie(request);
        return wayBillService.getRequestingWayBillCount(address);
    }

    @LogInterceptor
    @ApiOperation(value = "查询所有与自己相关的已发货（发货中）运单数量", notes = "查询所有与自己相关的已发货（发货中）运单数量")
    @ResponseBody
    @RequestMapping(value = "/sendingWayBillCount", method = RequestMethod.GET)
    public BaseResult<Object> getSendingWaybillcount(
            HttpServletRequest request) throws UserInvalidException, ValueNullException, PasswordIllegalParam, PrivateKeyIllegalParam, ContractInvokeFailException {
        String address = TokenUtil.getAddressFromCookie(request);
        return wayBillService.getSendingWayBillCount(address);
    }
}
