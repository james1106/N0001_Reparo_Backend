package com.hyperchain.controller.vo;

import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.UserEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by ldy on 2017/4/9.
 */
public class UserVo {
    @ApiModelProperty(value = "用户名")
    private String accountName;
    @ApiModelProperty(value = "企业名称")
    private String company_name;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "用户角色（0：融资企业、1：物流公司；2：仓储公司；3：金融机构）")
    private int roleCode;
    @ApiModelProperty(value = "证件类型")
    private String certType;
    @ApiModelProperty(value = "证件号码")
    private String certNo;
    @ApiModelProperty(value = "开户行账号（多个，使用:拼接)")
    private String acctIds;
    @ApiModelProperty(value = "开户行别")
    private String svcrClass;
    @ApiModelProperty(value = "开户行行号")
    private String acctSvcr;
    @ApiModelProperty(value = "开户行名称")
    private String acctSvcrName;
    @ApiModelProperty(value = "金融机构利率")
    private String rate;

    public UserVo(UserEntity userEntity, AccountEntity accountEntity) {
        this.accountName = userEntity.getAccountName();
        this.company_name = userEntity.getCompanyName();
        this.phone = userEntity.getPhone();
        this.roleCode = userEntity.getRoleCode();
        this.certType = accountEntity.getCertType();
        this.certNo = accountEntity.getCertNo();
        this.acctIds = accountEntity.getAcctId();
        this.svcrClass = accountEntity.getSvcrClass();
        this.acctSvcr = accountEntity.getAcctSvcr();
        this.acctSvcrName = accountEntity.getAcctSvcrName();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(int roleCode) {
        this.roleCode = roleCode;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getAcctIds() {
        return acctIds;
    }

    public void setAcctIds(String acctIds) {
        this.acctIds = acctIds;
    }

    public String getSvcrClass() {
        return svcrClass;
    }

    public void setSvcrClass(String svcrClass) {
        this.svcrClass = svcrClass;
    }

    public String getAcctSvcr() {
        return acctSvcr;
    }

    public void setAcctSvcr(String acctSvcr) {
        this.acctSvcr = acctSvcr;
    }

    public String getAcctSvcrName() {
        return acctSvcrName;
    }

    public void setAcctSvcrName(String acctSvcrName) {
        this.acctSvcrName = acctSvcrName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "accountName='" + accountName + '\'' +
                ", company_name='" + company_name + '\'' +
                ", phone='" + phone + '\'' +
                ", roleCode=" + roleCode +
                ", certType='" + certType + '\'' +
                ", certNo='" + certNo + '\'' +
                ", acctIds='" + acctIds + '\'' +
                ", svcrClass='" + svcrClass + '\'' +
                ", acctSvcr='" + acctSvcr + '\'' +
                ", acctSvcrName='" + acctSvcrName + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
