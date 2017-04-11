package com.hyperchain.controller.vo;

/**
 * Created by liangyue on 2017/4/10.
 */
public class RepoOverVo {

    private String repoSerialNo;
    private long repoGenerateTime;
    private String payerRepoCompany;
    private String payeeRepoCompany;
    private String repoCertNo;
    private int repoLatestStatus;
    private long recoUpdateTime;

    public String getRepoSerialNo() {
        return repoSerialNo;
    }

    public void setRepoSerialNo(String repoSerialNo) {
        this.repoSerialNo = repoSerialNo;
    }

    public long getRepoGenerateTime() {
        return repoGenerateTime;
    }

    public void setRepoGenerateTime(long repoGenerateTime) {
        this.repoGenerateTime = repoGenerateTime;
    }

    public String getPayerRepoCompany() {
        return payerRepoCompany;
    }

    public void setPayerRepoCompany(String payerRepoCompany) {
        this.payerRepoCompany = payerRepoCompany;
    }

    public String getPayeeRepoCompany() {
        return payeeRepoCompany;
    }

    public void setPayeeRepoCompany(String payeeRepoCompany) {
        this.payeeRepoCompany = payeeRepoCompany;
    }

    public String getRepoCertNo() {
        return repoCertNo;
    }

    public void setRepoCertNo(String repoCertNo) {
        this.repoCertNo = repoCertNo;
    }

    public int getRepoLatestStatus() {
        return repoLatestStatus;
    }

    public void setRepoLatestStatus(int repoLatestStatus) {
        this.repoLatestStatus = repoLatestStatus;
    }

    public long getRecoUpdateTime() {
        return recoUpdateTime;
    }

    public void setRecoUpdateTime(long recoUpdateTime) {
        this.recoUpdateTime = recoUpdateTime;
    }
}
