package com.hyperchain.common.constant;

import java.util.List;

/**
 * 常量枚举公用类
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:27
 */
@SuppressWarnings("JavaDoc")
public class BaseConstant {

    public static final String SIGNATURE_KEY = "signature";

    public enum Code {
        SUCCESS(0, "成功"),
        PERMISSION_DENIED(1, "权限拒绝"),
        INVALID_USER(2, "无效的用户，该用户的公钥可能未注册或已失效"),
        INVALID_PARAM_ROLE(3, "参数输入错误：参与方功能代码party-function-code"),
        INVALID_PARAM_STATUS(4, "参数输入错误：保单状态status"),
        BILL_ALREADY_EXISTED(5, "投保申请单已经存在"),
        BILL_DO_NOT_EXISTED(6, "投保申请单不存在"),
        BILL_STATUS_UPDATE_ILLEGAL(7, "投保申请单状态更新未按流程"),
        BILL_STATUS_WAS_ALREADY_UPDATED(8, "投保申请单状态重复更新"),
        REPLY_ALREADY_EXISTED(9, "投保反馈单已经存在"),
        REPLY_DO_NOT_EXISTED(10, "投保反馈单不存在"),
        REPLY_STATUS_UPDATE_ILLEGAL(11, "投保反馈单状态更新非法"),
        REPLY_STATUS_WAS_ALREADY_UPDATED(12, "投保反馈单状态重复更新"),
        REPLY_CANNOT_BE_CREATED_UNTIL_BILL_HAS_BEEN_RECEIVED(13, "投保反馈单不能在保险服务商收到投保申请单前创建"),
        POLLING_BILL_REPLY_WITH_ILLEGAL_STATUS(14,"轮询投保申请单或投保反馈单的状态不符合流程"),
        HYPERCHAIN_ERROR(15, "区块链异常"),
        INVALID_PARAM_PRIVATE_KEY(16, "参数输入错误：私钥private-key"),
        INVALID_PASSWORD(17, "参数输入错误：密码password"),
        SIGNATURE_VALIDATION_ERROR(18, "签名验证错误"),
        SYSTEM_ERROR(19, "系统异常，请稍后重试"),
        JSON_TRANSFER_ERROR(20, "JSON转化异常"),

        ORDER_ALREDY_EXISTED(21,"订单已存在"),
        ORDER_COMFIRM_ERR(22,"仅订单的供应商可进行确认操作");
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
    }

    /**
     * 用户角色
     */
    public enum Role {
        IFC("IFC"),
        BM("BM"),
        OJ("OJ");

        String role;

        Role(String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return role;
        }
    }

    /**
     * 投保申请单和投保反馈单的状态
     */
    public static String[] Status = new String[]{"-2", "-1", "1", "2", "4", "8"};

}
