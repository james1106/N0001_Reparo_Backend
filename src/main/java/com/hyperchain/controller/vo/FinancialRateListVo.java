package com.hyperchain.controller.vo;

import java.util.List;

/**
 * Created by ldy on 2017/5/8.
 */
public class FinancialRateListVo {
    private List<String> enterpriseNameList;
    private List<String> rateList;

    public FinancialRateListVo(List<String> enterpriseNameList, List<String> rateList) {
        this.enterpriseNameList = enterpriseNameList;
        this.rateList = rateList;
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

    @Override
    public String toString() {
        return "FinancialRateListVo{" +
                "enterpriseNameList=" + enterpriseNameList +
                ", rateList=" + rateList +
                '}';
    }
}
