package com.hyperchain.dal.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by ldy on 2017/4/9.
 */
@Entity
@Table(name = "account")
public class AccountEntity {

    @ApiModelProperty(value = "账号id")
    private Long id;
    @ApiModelProperty(value = "用户地址")
    private String address;
    @ApiModelProperty(value = "证件类型")
    private String certType;
    @ApiModelProperty(value = "证件号码")
    private String certNo;
    @ApiModelProperty(value = "开户行账号")
    private String acctId;
    @ApiModelProperty(value = "开户行别")
    private String svcrClass;
    @ApiModelProperty(value = "开户行行号")
    private String acctSvcr;
    @ApiModelProperty(value = "开户行名称")
    private String acctSvcrName;

    public AccountEntity() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", allocationSize = 1, sequenceName = "SEQ_role")
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false, length = 50)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(nullable = false, length = 50)
    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    @Column(nullable = false, length = 200)
    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    @Column(nullable = false, length = 200)
    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    @Column(nullable = false, length = 50)
    public String getSvcrClass() {
        return svcrClass;
    }

    public void setSvcrClass(String svcrClass) {
        this.svcrClass = svcrClass;
    }

    @Column(nullable = false, length = 50)
    public String getAcctSvcr() {
        return acctSvcr;
    }

    public void setAcctSvcr(String acctSvcr) {
        this.acctSvcr = acctSvcr;
    }

    @Column(nullable = false, length = 50)
    public String getAcctSvcrName() {
        return acctSvcrName;
    }

    public void setAcctSvcrName(String acctSvcrName) {
        this.acctSvcrName = acctSvcrName;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", certType='" + certType + '\'' +
                ", certNo='" + certNo + '\'' +
                ", acctId='" + acctId + '\'' +
                ", svcrClass='" + svcrClass + '\'' +
                ", acctSvcr='" + acctSvcr + '\'' +
                ", acctSvcrName='" + acctSvcrName + '\'' +
                '}';
    }
}
