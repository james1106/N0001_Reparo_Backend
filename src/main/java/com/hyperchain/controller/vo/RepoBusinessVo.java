package com.hyperchain.controller.vo;

import java.util.List;

/**
 * Created by chenxiaoyang on 2017/4/12.
 */
public class RepoBusinessVo {
    /*private String businessTransNo;
        private String repoBusiStatus;
        private String operateOperateTime;*/
    private String repoBusiNo;
    private int curRepoBusiStatus;
    private String opgTimeOfCurStatus;
    private String repoEnterpriceName;
    private String repoCertNo;
    private String productName;
    private Long productQuantity;
    private String measureUnit;
    private Long productTotalPrice;
    private String logisticsEntepsName;
    private String waybillNo;
    private List<OperationRecordVo> operationRecordVoList;


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

    public String getOpgTimeOfCurStatus() {
        return opgTimeOfCurStatus;
    }

    public void setOpgTimeOfCurStatus(String opgTimeOfCurStatus) {
        this.opgTimeOfCurStatus = opgTimeOfCurStatus;
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

    public Long getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(Long productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getLogisticsEntepsName() {
        return logisticsEntepsName;
    }

    public void setLogisticsEntepsName(String logisticsEntepsName) {
        this.logisticsEntepsName = logisticsEntepsName;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
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
