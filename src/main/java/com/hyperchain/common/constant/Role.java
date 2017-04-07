package com.hyperchain.common.constant;

/**
 * Created by ldy on 2017/4/6.
 */
public enum Role {
    COMPANY(0, "融资企业"),
    LOGISTICS(1, "物流公司"),
    STORAGE(2, "仓储公司"),
    FINANCE(3, "金融机构");

    private String role;
    private int code;

    Role(int code, String role) {
        this.code = code;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
