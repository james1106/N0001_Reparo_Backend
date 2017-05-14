package com.hyperchain.controller.vo;

/**
 * Created by YanYufei on 2017/5/4.
 */
public class ReceivableBankListVo {
    private int bankSvcr;
    private String bankName;
    private String bankDicountRate;

    public String getBankDicountRate() {
        return bankDicountRate;
    }

    public void setBankDicountRate(String bankDicountRate) {
        this.bankDicountRate = bankDicountRate;
    }

    public int getBankSvcr() {
        return bankSvcr;
    }

    public void setBankSvcr(int bankSvcr) {
        this.bankSvcr = bankSvcr;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
