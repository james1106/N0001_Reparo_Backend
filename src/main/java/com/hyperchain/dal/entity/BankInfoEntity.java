package com.hyperchain.dal.entity;

/**
 * Created by YanYufei on 2017/4/17.
 */
public class BankInfoEntity {
    private int bankSvcr;//行号

    private String bankName;

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

    public BankInfoEntity(int bankSvcr, String bankName) {
        this.bankSvcr = bankSvcr;
        this.bankName = bankName;
    }
}
