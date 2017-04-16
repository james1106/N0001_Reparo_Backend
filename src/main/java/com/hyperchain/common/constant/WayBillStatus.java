package com.hyperchain.common.constant;

/**
 * Created by ldy on 2017/4/11.
 */
public enum WayBillStatus {
    UNDEFINED(0, "未定义"),
    WAITING(1, "待发货"),
    REQUESTING(2, "发货待响应"),
    REJECTED(3, "发货被拒绝"),
    SENDING(4, "已发货"),
    RECEIVED(5, "已送达");

    private String msg;
    private int code;

    WayBillStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
