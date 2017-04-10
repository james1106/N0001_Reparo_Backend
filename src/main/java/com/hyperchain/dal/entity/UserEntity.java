package com.hyperchain.dal.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by ldy on 2017/4/5.
 */
@Entity
@Table(name = "user11")
public class UserEntity {
    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String accountName;
    @ApiModelProperty(value = "登录密码（MD5)")
    private String password;
    @ApiModelProperty(value = "私钥")
    private String privateKey;
    @ApiModelProperty(value = "用户地址")
    private String address;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "用户角色（0：融资企业、1：物流公司；2：仓储公司；3：金融机构）")
    private int roleCode;
    @ApiModelProperty(value = "连续密码错误次数")
    private int errorPasswordCount;
    @ApiModelProperty(value = "用户状态（0：有效、1：无效、2：冻结、3：锁定）")
    private int userStatus;
    @ApiModelProperty(value = "用户锁定时间（当用户状态为锁定时有效）")
    private Long lockTime;

    public UserEntity() {

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

    @Column(nullable = false, length = 50, unique = true)
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Column(nullable = false, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false, length = 200)
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Column(nullable = false, length = 50, unique = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(nullable = false, length = 50, unique = true)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(nullable = false, length = 50, unique = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(nullable = false, columnDefinition = "tinyint")
    public int getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(int roleCode) {
        this.roleCode = roleCode;
    }

    @Column(nullable = true, columnDefinition = "tinyint default 0")
    public int getErrorPasswordCount() {
        return errorPasswordCount;
    }

    public void setErrorPasswordCount(int errorPasswordCount) {
        this.errorPasswordCount = errorPasswordCount;
    }

    @Column(nullable = true, columnDefinition = "tinyint default 0")
    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    @Column(nullable = true, columnDefinition = "bigint(20) default 0")
    public Long getLockTime() {
        return lockTime;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", accountName='" + accountName + '\'' +
                ", password='" + password + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", address='" + address + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phone='" + phone + '\'' +
                ", roleCode=" + roleCode +
                ", errorPasswordCount=" + errorPasswordCount +
                ", userStatus=" + userStatus +
                ", lockTime=" + lockTime +
                '}';
    }
}
