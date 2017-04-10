package com.hyperchain.common.constant;

/**
 * Created by ldy on 2017/4/6.
 */
public enum AccountStatus {
    VALID(0, "有效"),
    INVALID(1, "无效"),
    FROZEN(1, "无效"),
    LOCK(2, "锁定");

    private int code;
    private String status;

    AccountStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
