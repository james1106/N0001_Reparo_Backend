package com.hyperchain.controller.vo;

import java.util.List;

/**
 * Created by ldy on 2017/5/8.
 */
public class FinancialRateListVo {
    private List<String> enterpriseNameList;
    private List<String> rateList;
    private List<String> acctIdList;

    public FinancialRateListVo(List<String> enterpriseNameList, List<String> rateList, List<String> acctIdList) {
        this.enterpriseNameList = enterpriseNameList;
        this.rateList = rateList;
        this.acctIdList = acctIdList;
    }

    public List<String> getEnterpriseNameList() {
        return enterpriseNameList;
    }

    public void setEnterpriseNameList(List<String> enterpriseNameList) {
        this.enterpriseNameList = enterpriseNameList;
    }

    public List<String> getRateList() {
        return rateList;
    }

    public void setRateList(List<String> rateList) {
        this.rateList = rateList;
    }

    public List<String> getAcctIdList() {
        return acctIdList;
    }

    public void setAcctIdList(List<String> acctIdList) {
        this.acctIdList = acctIdList;
    }

    @Override
    public String toString() {
        return "FinancialRateListVo{" +
                "enterpriseNameList=" + enterpriseNameList +
                ", rateList=" + rateList +
                ", acctIdList=" + acctIdList +
                '}';
    }
}
