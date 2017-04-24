package com.hyperchain.controller.vo;

import com.hyperchain.common.constant.WayBillStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ldy on 2017/4/13.
 */
public class WayBillDetailVo {
    private String orderNo = "";
    private String wayBillNo = "";
    private String logisticsEnterpriseName = "";
    private String senderEnterpriseName = "";
    private String receiverEnterpriseName = "";
    private String senderRepoEnterpriseName = "";
    private String receiverRepoEnterpriseName = "";
    private String senderRepoCertNo = "";
    private String receiverRepoBusinessNo = "";
    private String productName = "";
    private Long productQuantity = new Long("0");
    private String productValue = "0.00";
    private int waybillStatusCode = WayBillStatus.UNDEFINED.getCode();
    private OperationRecordVo[] operationRecordVo = new OperationRecordVo[]{};
    private List<String> logisticsInfo = new ArrayList<>();

    public WayBillDetailVo() {
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWayBillNo() {
        return wayBillNo;
    }

    public void setWayBillNo(String wayBillNo) {
        this.wayBillNo = wayBillNo;
    }

    public String getLogisticsEnterpriseName() {
        return logisticsEnterpriseName;
    }

    public void setLogisticsEnterpriseName(String logisticsEnterpriseName) {
        this.logisticsEnterpriseName = logisticsEnterpriseName;
    }

    public String getSenderEnterpriseName() {
        return senderEnterpriseName;
    }

    public void setSenderEnterpriseName(String senderEnterpriseName) {
        this.senderEnterpriseName = senderEnterpriseName;
    }

    public String getReceiverEnterpriseName() {
        return receiverEnterpriseName;
    }

    public void setReceiverEnterpriseName(String receiverEnterpriseName) {
        this.receiverEnterpriseName = receiverEnterpriseName;
    }

    public String getSenderRepoEnterpriseName() {
        return senderRepoEnterpriseName;
    }

    public void setSenderRepoEnterpriseName(String senderRepoEnterpriseName) {
        this.senderRepoEnterpriseName = senderRepoEnterpriseName;
    }

    public String getReceiverRepoEnterpriseName() {
        return receiverRepoEnterpriseName;
    }

    public void setReceiverRepoEnterpriseName(String receiverRepoEnterpriseName) {
        this.receiverRepoEnterpriseName = receiverRepoEnterpriseName;
    }

    public String getSenderRepoCertNo() {
        return senderRepoCertNo;
    }

    public void setSenderRepoCertNo(String senderRepoCertNo) {
        this.senderRepoCertNo = senderRepoCertNo;
    }

    public String getReceiverRepoBusinessNo() {
        return receiverRepoBusinessNo;
    }

    public void setReceiverRepoBusinessNo(String receiverRepoBusinessNo) {
        this.receiverRepoBusinessNo = receiverRepoBusinessNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductValue() {
        return productValue;
    }

    public void setProductValue(String productValue) {
        this.productValue = productValue;
    }

    public int getWaybillStatusCode() {
        return waybillStatusCode;
    }

    public void setWaybillStatusCode(int waybillStatusCode) {
        this.waybillStatusCode = waybillStatusCode;
    }

    public OperationRecordVo[] getOperationRecordVo() {
        return operationRecordVo;
    }

    public void setOperationRecordVo(OperationRecordVo[] operationRecordVo) {
        this.operationRecordVo = operationRecordVo;
    }

    public List<String> getLogisticsInfo() {
        return logisticsInfo;
    }

    public void setLogisticsInfo(List<String> logisticsInfo) {
        this.logisticsInfo = logisticsInfo;
    }

    @Override
    public String toString() {
        return "WayBillDetailVo{" +
                "orderNo='" + orderNo + '\'' +
                ", wayBillNo='" + wayBillNo + '\'' +
                ", logisticsEnterpriseName='" + logisticsEnterpriseName + '\'' +
                ", senderEnterpriseName='" + senderEnterpriseName + '\'' +
                ", receiverEnterpriseName='" + receiverEnterpriseName + '\'' +
                ", senderRepoEnterpriseName='" + senderRepoEnterpriseName + '\'' +
                ", receiverRepoEnterpriseName='" + receiverRepoEnterpriseName + '\'' +
                ", senderRepoCertNo='" + senderRepoCertNo + '\'' +
                ", receiverRepoBusinessNo='" + receiverRepoBusinessNo + '\'' +
                ", productName='" + productName + '\'' +
                ", productQuantity=" + productQuantity +
                ", productValue=" + productValue +
                ", waybillStatusCode=" + waybillStatusCode +
                ", operationRecordVo=" + Arrays.toString(operationRecordVo) +
                ", logisticsInfo=" + logisticsInfo +
                '}';
    }
}
