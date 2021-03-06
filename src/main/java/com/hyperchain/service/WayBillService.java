package com.hyperchain.service;

import com.hyperchain.common.exception.*;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ldy on 2017/4/12.
 */
public interface WayBillService {

    BaseResult<Object> generateUnConfirmedWaybill(String orderNo,
                                                  String logisticsEnterpriseName,
                                                  String senderEnterpriseName,
                                                  String receiverEnterpriseName,
                                                  String productName,
                                                  long productQuantity,
                                                  double productValue,
                                                  String senderRepoEnterpriseName,
                                                  String senderRepoCertNo,
                                                  String receiverRepoEnterpriseName,
                                                  String receiverRepoBusinessNo,
                                                  HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException;

    BaseResult<Object> generateConfirmedWaybill(String orderNo, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException;

    BaseResult<Object> getAllRelatedWayBillDetail(HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException, QueryNotExistUserException;

    BaseResult<Object> getWayBillDetailByOrderNo(String orderNo, HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException, QueryNotExistUserException;

    BaseResult<Object> updateWayBillStatusToReceived(String orderNo, HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException;

    BaseResult<Object> getAllWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;

    BaseResult<Object> getWaitingWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;

    BaseResult<Object> getRequestingWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;

    BaseResult<Object> getSendingWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;

}
