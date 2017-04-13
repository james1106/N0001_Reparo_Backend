package com.hyperchain.controller.vo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ldy on 2017/4/13.
 */
public class WayBillDetailVo {
    private String orderNo;
    private String wayBillNo;
    private String logisticsEnterpriseName;
    private String senderEnterpriseName;
    private String receiverEnterpriseName;
    private String senderRepoEnterpriseName;
    private String receiverRepoEnterpriseName;
    private String senderRepoCertNo;
    private String receiverRepoBusinessNo;
    private String productName;
    private Long productQuantity;
    private Long productValue;
    private int waybillStatusCode;
    private Long[] allOperateTime;
    private int[] allStatusCode;
    private List<String> logisticsInfo;

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

    public Long getProductValue() {
        return productValue;
    }

    public void setProductValue(Long productValue) {
        this.productValue = productValue;
    }

    public int getWaybillStatusCode() {
        return waybillStatusCode;
    }

    public void setWaybillStatusCode(int waybillStatusCode) {
        this.waybillStatusCode = waybillStatusCode;
    }

    public Long[] getAllOperateTime() {
        return allOperateTime;
    }

    public void setAllOperateTime(Long[] allOperateTime) {
        this.allOperateTime = allOperateTime;
    }

    public int[] getAllStatusCode() {
        return allStatusCode;
    }

    public void setAllStatusCode(int[] allStatusCode) {
        this.allStatusCode = allStatusCode;
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
                ", allOperateTime=" + Arrays.toString(allOperateTime) +
                ", allStatusCode=" + Arrays.toString(allStatusCode) +
                ", logisticsInfo=" + logisticsInfo +
                '}';
    }
}
