package com.hyperchain.dal.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by ldy on 2017/4/5.
 */
@Entity
@Table(name = "security_code")
public class SecurityCodeEntity {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "手机号码")
    private String phone;
    @ApiModelProperty(value = "验证码")
    private String securityCode;
    @ApiModelProperty(value = "验证码连续错误次数")
    private Integer errorCodeCount;
    @ApiModelProperty(value = "验证码创建时间")
    private Long createTime;

    public SecurityCodeEntity() {
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

    @Column(nullable = false, length = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(nullable = false, length = 6)
    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Column(nullable = true, columnDefinition = "tinyint default 0")
    public Integer getErrorCodeCount() {
        return errorCodeCount;
    }

    public void setErrorCodeCount(Integer errorCodeCount) {
        this.errorCodeCount = errorCodeCount;
    }

    @Column(nullable = false, columnDefinition = "bigint(20) default 0")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SecurityCodeEntity{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", securityCode='" + securityCode + '\'' +
                ", errorCodeCount=" + errorCodeCount +
                ", createTime=" + createTime +
                '}';
    }
}
