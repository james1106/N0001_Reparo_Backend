package com.hyperchain.common.constant;

/**
 * Created by ldy on 2017/4/5.
 */
public enum Code {
    //通用部分
    SUCCESS(0, "成功"),
    PERMISSION_DENIED(1, "权限拒绝"),
    INVALID_USER(2, "账户不存在，该用户可能未注册或已失效"),
    PARAMETER_EMPTY(3, "入参为空"),

    HYPERCHAIN_ERROR(15, "区块链异常"),
    INVALID_PARAM_PRIVATE_KEY(16, "参数输入错误：私钥private-key"),
    INVALID_PASSWORD(17, "参数输入错误：密码password"),
    SIGNATURE_VALIDATION_ERROR(18, "签名验证错误"),
    SYSTEM_ERROR(19, "系统异常，请稍后重试"),
    JSON_TRANSFER_ERROR(20, "JSON转化异常"),

    //订单管理部分
    ORDER_NOT_EXIST(2001, "账单不存在"),
    QEURY_ORDER_PERMISSION_DENIED(2002, "无权限查询该账单"),

    //    //账户管理部分
    PHONE_ALREADY_EXIST(5001, "手机号码已注册"),
    ACCOUNT_ALREADY_EXIST(5002, "账户已存在"),
    INVALID_SECURITY_CODE(5003, "验证码失效，请重新获取验证码"),
    NON_EXIST_SECURITY_CODE(5004, "验证码不存在，请获取验证码"),
    WRONG_SECURITY_CODE(5005, "验证码错误，还可以尝试："),
    ACCOUNT_STATUS_LOCK(5006, "用户已锁定，请稍后重试"),
    ERROR_PASSWORD(5007, "密码错误，请重新输入密码");


    private int code;
    private String msg;

    // 构造方法
    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static String getMsgByCode(int code) {
        for (Code e : Code.values()) {
            if (e.getCode() == code) {
                return e.msg;
            }
        }
        return null;
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

    public static Code fromInt(int code) {
        switch (code) {
            case 0:
                return SUCCESS;
            case 2001:
                return ORDER_NOT_EXIST;
            case 2002:
                return ORDER_NOT_EXIST;
            default:
                return SUCCESS;
        }
    }
}
