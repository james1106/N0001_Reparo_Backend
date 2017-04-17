package com.hyperchain.controller.vo;

/**
 * Created by liangyue on 2017/4/10.
 */
public class ReceOverVo {
    private String receNo;
    private long receGenerateTime;
    private String receivingSide;
    private String payingSide;
    private long receAmount;
    private long coupon;
    private long dueDate;
    private int receLatestStatus;
    private long receUpdateTime;

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public String getReceNo() {
        return receNo;
    }

    public void setReceNo(String receNo) {
        this.receNo = receNo;
    }

    public long getReceGenerateTime() {
        return receGenerateTime;
    }

    public void setReceGenerateTime(long receGenerateTime) {
        this.receGenerateTime = receGenerateTime;
    }

    public String getReceivingSide() {
        return receivingSide;
    }

    public void setReceivingSide(String receivingSide) {
        this.receivingSide = receivingSide;
    }

    public String getPayingSide() {
        return payingSide;
    }

    public void setPayingSide(String payingSide) {
        this.payingSide = payingSide;
    }

    public long getReceAmount() {
        return receAmount;
    }

    public void setReceAmount(long receAmount) {
        this.receAmount = receAmount;
    }

    public long getCoupon() {
        return coupon;
    }

    public void setCoupon(long coupon) {
        this.coupon = coupon;
    }

    public int getReceLatestStatus() {
        return receLatestStatus;
    }

    public void setReceLatestStatus(int receLatestStatus) {
        this.receLatestStatus = receLatestStatus;
    }

    public long getReceUpdateTime() {
        return receUpdateTime;
    }

    public void setReceUpdateTime(long receUpdateTime) {
        this.receUpdateTime = receUpdateTime;
    }
}
