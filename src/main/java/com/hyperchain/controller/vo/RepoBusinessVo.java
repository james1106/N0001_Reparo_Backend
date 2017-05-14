package com.hyperchain.controller.vo;

import java.util.List;

/**
 * Created by chenxiaoyang on 2017/4/12.
 * 仓储详情及仓储流水数据
 */
public class RepoBusinessVo {
    /*private String businessTransNo;
        private String repoBusiStatus;
        private String operateOperateTime;*/
    private String repoBusiNo; //仓储业务编号
    private int curRepoBusiStatus;//最新仓储状态
    private Long opgTimeOfCurStatus;//最新仓储操作时间
    private String repoEnterpriceName;//仓储机构名称
    private String repoCertNo;//仓单编号
    private String productName;//货品名称
    private Long productQuantity;//货品数量
    private String measureUnit;//计量单位
    private String productTotalPrice;//货品总价
    private String inLogisticsEntepsName;//入库物流公司名称
    private String inWaybillNo;//入库运单号
    private String outLogisticsEntepsName;//出库物流公司名称
    private String outWaybillNo;//出库运单号
    private List<OperationRecordVo> operationRecordVoList;//仓储操作历史列表
    private String orderNo;//订单号
    private int repoCertStatus;//仓单状态
    private String holderEnterpriseName;//持有人
    private String storeEnterpriseName;//存货人

    public String getInLogisticsEntepsName() {
        return inLogisticsEntepsName;
    }

    public void setInLogisticsEntepsName(String inLogisticsEntepsName) {
        this.inLogisticsEntepsName = inLogisticsEntepsName;
    }

    public String getInWaybillNo() {
        return inWaybillNo;
    }

    public void setInWaybillNo(String inWaybillNo) {
        this.inWaybillNo = inWaybillNo;
    }

    public String getOutLogisticsEntepsName() {
        return outLogisticsEntepsName;
    }

    public void setOutLogisticsEntepsName(String outLogisticsEntepsName) {
        this.outLogisticsEntepsName = outLogisticsEntepsName;
    }

    public String getOutWaybillNo() {
        return outWaybillNo;
    }

    public void setOutWaybillNo(String outWaybillNo) {
        this.outWaybillNo = outWaybillNo;
    }
    public String getStoreEnterpriseName() {
        return storeEnterpriseName;
    }

    public void setStoreEnterpriseName(String storeEnterpriseName) {
        this.storeEnterpriseName = storeEnterpriseName;
    }

    public String getHolderEnterpriseName() {
        return holderEnterpriseName;
    }

    public void setHolderEnterpriseName(String holderEnterpriseName) {
        this.holderEnterpriseName = holderEnterpriseName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public int getRepoCertStatus() {
        return repoCertStatus;
    }

    public void setRepoCertStatus(int repoCertStatus) {
        this.repoCertStatus = repoCertStatus;
    }


    public Long getOpgTimeOfCurStatus() {
        return opgTimeOfCurStatus;
    }

    public void setOpgTimeOfCurStatus(Long opgTimeOfCurStatus) {
        this.opgTimeOfCurStatus = opgTimeOfCurStatus;
    }



    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }
    public String getRepoBusiNo() {
        return repoBusiNo;
    }

    public void setRepoBusiNo(String repoBusiNo) {
        this.repoBusiNo = repoBusiNo;
    }

    public int getCurRepoBusiStatus() {
        return curRepoBusiStatus;
    }

    public void setCurRepoBusiStatus(int curRepoBusiStatus) {
        this.curRepoBusiStatus = curRepoBusiStatus;
    }


    public String getRepoEnterpriceName() {
        return repoEnterpriceName;
    }

    public void setRepoEnterpriceName(String repoEnterpriceName) {
        this.repoEnterpriceName = repoEnterpriceName;
    }

    public String getRepoCertNo() {
        return repoCertNo;
    }

    public void setRepoCertNo(String repoCertNo) {
        this.repoCertNo = repoCertNo;
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

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }



    public List<OperationRecordVo> getOperationRecordVoList() {
        return operationRecordVoList;
    }

    public void setOperationRecordVoList(List<OperationRecordVo> operationRecordVoList) {
        this.operationRecordVoList = operationRecordVoList;
    }



    /*public String getBusinessTransNo() {
        return businessTransNo;
    }

    public void setBusinessTransNo(String businessTransNo) {
        this.businessTransNo = businessTransNo;
    }

    public String getRepoBusiStatus() {
        return repoBusiStatus;
    }

    public void setRepoBusiStatus(String repoBusiStatus) {
        this.repoBusiStatus = repoBusiStatus;
    }

    public String getOperateOperateTime() {
        return operateOperateTime;
    }

    public void setOperateOperateTime(String operateOperateTime) {
        this.operateOperateTime = operateOperateTime;
    }*/


}
