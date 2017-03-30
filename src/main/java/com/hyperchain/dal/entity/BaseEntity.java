package com.hyperchain.dal.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.util.CommonUtil;

/**
 * 基础实体类
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:27
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态，0为无效，1为正常,3删除 参看BaseConstant.Status")
    private Integer status;

    @ApiModelProperty(value = "版本,hibernate维护")
    private Integer version;

    @ApiModelProperty(value = "@Fields createUser : 创建者")
    private Long createUser;

    @ApiModelProperty(value = "@Fields createTime : 创建时间")
    private Long createTime;

    @ApiModelProperty(value = "@Fields modifyUser : 修改者")
    private Long modifyUser;

    @ApiModelProperty(value = "@Fields modifyTime : 修改时间")
    private Long modifyTime;

    @ApiModelProperty(value = "@Fields modifyDescription : 修改描述")
    private String modifyDescription;


    /**
     * 为确保赋值增加默认值1:正常
     */
    @Column(nullable = false, columnDefinition = "int default 1")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {

        this.status = status;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(name = "createUser", updatable = false)
    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    @Column(name = "createTime")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "modifyUser")
    public Long getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(Long modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Column(name = "modifyTime")
    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "modifyDescription", length = 500)
    public String getModifyDescription() {
        return modifyDescription;
    }

    public void setModifyDescription(String modifyDescription) {
        this.modifyDescription = modifyDescription;
    }

    /**
     * 数据插入前的操作
     */
    @PrePersist
    public void setInsertBefore() {
        Long temp = new Date().getTime();
        if (CommonUtil.isEmpty(this.createTime)) {

            this.createTime = temp;
        }
        this.modifyTime = temp;
        if (status == null) {
            this.status = BaseConstant.Status.valid;
        }
    }

    /**
     * 数据修改前的操作
     */
    @PreUpdate
    public void setUpdateBefore() {
        this.modifyTime = new Date().getTime();
    }

    public static String getLongToString(Long longTime) {
        if (longTime == null) {
            return null;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(longTime);
        return sf.format(date);
    }

    @Transient
    @ApiModelProperty(value = "创建时间的字符串表示")
    public String getCreateAtStr() {
        return getLongToString(createTime);
    }

    @Transient
    @ApiModelProperty(value = "修改时间的字符串表示")
    public String getUpdatedAtStr() {
        return getLongToString(modifyTime);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "status=" + status +
                ", version=" + version +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", modifyUser=" + modifyUser +
                ", modifyTime=" + modifyTime +
                ", modifyDescription='" + modifyDescription + '\'' +
                '}';
    }
}
