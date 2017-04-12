package com.hyperchain.service;

import com.hyperchain.controller.vo.BaseResult;
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
                                                  int productName,
                                                  String productQuantity,
                                                  long productValue,
                                                  String senderRepoEnterpriseName,
                                                  String senderRepoCertNo,
                                                  String receiverRepoEnterpriseName,
                                                  String receiverRepoBusinessNo,
                                                  HttpServletRequest request);

    BaseResult<Object> generateConfirmedWaybill(String orderNo, HttpServletRequest request);

    BaseResult<Object> getAllRelatedWayBillDetail(HttpServletRequest request);

    BaseResult<Object> updateWayBillStatusToReceived(String orderNo, HttpServletRequest request);

}
