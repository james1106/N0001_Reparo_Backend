package com.hyperchain.dal.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:24
 */
@Entity
@Table(name = "zb_bank_card")
@ApiModel(value = "app用户信息定义表")
public class BankCard extends BaseEntity {


    @ApiModelProperty(value = "卡号")
    private String bankCardNumber;
    @ApiModelProperty(value = "卡的id")
    private String bankId;
    @ApiModelProperty(value = "银行名称")
    private String bankName;
    @ApiModelProperty(value = "1：绑定 ，2：签收")
    private String type;

    @ApiModelProperty(value = "账户id")
    private Long accountId;
    private Account account;

    @ApiModelProperty(value = "@Fields lastUsedTime : 最后使用时间")
    private Long lastUsedTime;

    private Long tid;

    public BankCard() {
    }

    public BankCard(String bankId, String bankAccount, Long accountId, String type) {
        super();
        this.bankId = bankId;
        this.bankCardNumber = bankAccount;
        this.accountId = accountId;
        this.type = type;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", allocationSize = 1, sequenceName = "SEQ_role")
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    @Column(nullable = true, length = 50)
    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    @Column(nullable = true, length = 50)
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Column(nullable = true, length = 50)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(nullable = true, length = 50)
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "accountId", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Column(nullable = true, length = 1)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "lastUsedTime")
    public Long getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(Long lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    @Override
    public String toString() {
        return super.toString() + "\nBankCard{" +
                "bankCardNumber='" + bankCardNumber + '\'' +
                ", bankId='" + bankId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", type='" + type + '\'' +
                ", accountId=" + accountId +
                ", account=" + account +
                ", lastUsedTime=" + lastUsedTime +
                ", tid=" + tid +
                '}';
    }
}
