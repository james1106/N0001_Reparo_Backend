package com.hyperchain.dal.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by ldy on 2017/4/5.
 */
@Entity
@Table(name = "permission")
public class PermissionEntity {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "用户角色")
    private Integer roleCode;
    @ApiModelProperty(value = "角色权限url")
    private String url;

    public PermissionEntity() {
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

    @Column(nullable = false, columnDefinition = "tinyint")
    public Integer getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(Integer roleCode) {
        this.roleCode = roleCode;
    }

    @Column(nullable = false, length = 200)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PermissionEntity{" +
                "id=" + id +
                ", roleCode=" + roleCode +
                ", url='" + url + '\'' +
                '}';
    }
}
