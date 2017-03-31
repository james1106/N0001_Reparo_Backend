package com.hyperchain.dal.entity;

import cn.hyperchain.sdk.rpc.account.Account;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import javax.persistence.*;
/**
 * Created by chenxiaoyang on 2017/3/30.
 */
@Entity
@Table(name = "user")
public class Demo_UserEntity {
    private Long id;
    private String nick_name;
    private String password;
    private String phone_number;

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

    @Column(nullable = true, length = 50)
    public String getNick_name() {
        return nick_name;
    }


    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    @Column(nullable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = true, length = 50)
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return super.toString() + "\nDemo_UserEntity{" +
                "id='" + id + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", phone_number='" + phone_number +
                '}';
    }
}
