package com.hyperchain.common.constant;

/**
 * Created by ldy on 2017/4/5.
 */
public enum Code {
    //通用部分
    UNDEFINED(-1, "未定义"),
    SUCCESS(0, "成功"),
    PERMISSION_DENIED(1, "权限拒绝"),
    INVALID_USER(2, "账户不存在，该用户可能未注册或已失效"),
    PARAMETER_EMPTY(3, "入参为空"),
    UNKNOWN_ABNORMAL(4,"未知异常"),

    HYPERCHAIN_ERROR(15, "区块链异常"),
    INVALID_PARAM_PRIVATE_KEY(16, "参数输入错误：私钥private-key"),
    INVALID_PASSWORD(17, "参数输入错误：密码password"),
    SIGNATURE_VALIDATION_ERROR(18, "签名验证错误"),
    SYSTEM_ERROR(19, "系统异常，请稍后重试"),
    JSON_TRANSFER_ERROR(20, "JSON转化异常"),

    //订单管理部分
    ORDER_NOT_EXIST(2001, "订单不存在"),
    QEURY_ORDER_PERMISSION_DENIED(2002, "无权限查询该账单"),
    COMPANY_NOT_BE_REGISTERED(2003, "卖方公司名称未注册"),
    ORDER_ALREADY_EXSIT(2004, "该订单号已经存在"),
    NOT_TELATIVE_ORDER(2005, "该订单与用户无关"),
    ORDER_ALREADY_CONFIRMED(2006, "该订单已经确认"),
    NOT_PAYEE_FOR_ORDER(2007, "用户不是订单的卖方"),
    BANKACCOUNT_NOT_EXIST(2008, "用户无该银行账号"),

    //账户管理部分
    PHONE_ALREADY_EXIST(5001, "手机号码已注册"),
    ACCOUNT_ALREADY_EXIST(5002, "账户已存在"),
    INVALID_SECURITY_CODE(5003, "验证码失效，请重新获取验证码"),
    NON_EXIST_SECURITY_CODE(5004, "验证码不存在，请获取验证码"),
    WRONG_SECURITY_CODE(5005, "验证码错误，还可以尝试："),
    ACCOUNT_STATUS_LOCK(5006, "用户已锁定，请稍后重试"),
    ERROR_PASSWORD(5007, "密码错误，请重新输入密码"),
    INVALID_TOKEN(5008, "token无效"),

    //运单部分
    WAY_BILL_ALREADY_EXIST(3000, "订单对应的运单已经存在"),
    WAY_BILL_NO_DATA(3001, "该用户暂无数据"),
    WAY_BILL_CONTENT_INVALID(3050, "运单内容不合法"),

    //应收款部分
    SERIALNO_EXIST(1032, "流水号已存在"),
    ISSEAMT_ERROR(1019, "票面金额为负值或0"),
    ACCTID_ADDRESS_ERROR(1007,"账号与操作者不匹配"),
    REPLYER_ACCOUNT_ERROR(1004,"回复人账户不存在"),
    REPLYER_ACCOUNT_INVALID(1031,"回复人账户无效"),
    RECEIVABLENO_EXITS(1030,"应收款编号已经存在"),
    RESPONSETYPE_ERROR(1020,"回复意见不是同意或拒绝"),
    RECCEIVABLE_STATUS_ERROR(1006,"应收款状态错误"),
    CASHEDAMOUNT_ERROR(1016,"已兑付金额为负值或0"),
    RECEIVABLE_NOT_EXITS(1005,"应收款不存在"),
    CASH_TIME_ERROR(1010,"兑付时应收款还未到期"),
    RECEIVABLE_RECORD_NOT_EXITS(1013,"未找到该流水号对应的交易记录"),
    TXSTATE_ERROR(1002,"订单状态错误"),
    PAYERREPOBUSISTATE(1003,"买方仓储状态错误"),
    NOT_RECEIVABLE_FIRSTOWNER(1008,"申请人不是本手持有人");

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
            case 1:
                return PERMISSION_DENIED;
            case 2:
                return INVALID_USER;
            case 3:
                return PARAMETER_EMPTY;

            case 1032:
                return SERIALNO_EXIST;
            case 1019:
                return ISSEAMT_ERROR;
            case 1007:
                return ACCTID_ADDRESS_ERROR;
            case 1004:
                return REPLYER_ACCOUNT_ERROR;
            case 1031:
                return REPLYER_ACCOUNT_INVALID;
            case 1030:
                return RECEIVABLENO_EXITS;
            case 1020:
                return RESPONSETYPE_ERROR;
            case 1006:
                return RECCEIVABLE_STATUS_ERROR;
            case 1016:
                return CASHEDAMOUNT_ERROR;
            case 1005:
                return RECEIVABLE_NOT_EXITS;
            case 1008:
                return NOT_RECEIVABLE_FIRSTOWNER;
            case 1010:
                return CASH_TIME_ERROR;
            case 1013:
                return RECEIVABLE_RECORD_NOT_EXITS;

            case 2001:
                return ORDER_NOT_EXIST;
            case 2002:
                return QEURY_ORDER_PERMISSION_DENIED;
            case 2003:
                return COMPANY_NOT_BE_REGISTERED;
            case 2004:
                return ORDER_ALREADY_EXSIT;
            case 2005:
                return NOT_TELATIVE_ORDER;
            case 2006:
                return ORDER_ALREADY_CONFIRMED;
            case 2007:
                return NOT_PAYEE_FOR_ORDER;
            case 2008:
                return BANKACCOUNT_NOT_EXIST;

            case 3000:
                return WAY_BILL_ALREADY_EXIST;
            case 3001:
                return WAY_BILL_NO_DATA;
            case 3050:
                return WAY_BILL_CONTENT_INVALID;

            case 5002:
                return ACCOUNT_ALREADY_EXIST;

            default:
                return UNDEFINED;
        }
    }

}
