package com.hyperchain.controller.vo;

/**
 * Created by liangyue on 2017/4/10.
 */
public class WayBillOverInfo {

    private String wayBillNo;
    private String logisticCompany;
    private long wayBillGenerateTime;
    private int wayBillLatestStatus;
    private long wayBillUpdateTime;
    public String getWayBillNo() {
        return wayBillNo;
    }

    public void setWayBillNo(String wayBillNo) {
        this.wayBillNo = wayBillNo;
    }

    public String getLogisticCompany() {
        return logisticCompany;
    }

    public void setLogisticCompany(String logisticCompany) {
        this.logisticCompany = logisticCompany;
    }

    public long getWayBillGenerateTime() {
        return wayBillGenerateTime;
    }

    public void setWayBillGenerateTime(long wayBillGenerateTime) {
        this.wayBillGenerateTime = wayBillGenerateTime;
    }

    public int getWayBillLatestStatus() {
        return wayBillLatestStatus;
    }

    public void setWayBillLatestStatus(int wayBillLatestStatus) {
        this.wayBillLatestStatus = wayBillLatestStatus;
    }

    public long getWayBillUpdateTime() {
        return wayBillUpdateTime;
    }

    public void setWayBillUpdateTime(long wayBillUpdateTime) {
        this.wayBillUpdateTime = wayBillUpdateTime;
    }

}
