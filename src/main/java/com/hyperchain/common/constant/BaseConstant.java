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

    public static final String DEFAULT_PRIVATE_KEY_PASSWORD = "123"; //默认加密私钥的密码
    public static final String SALT_FOR_PASSWORD = "qdqfesg12312dsdsdee21aas"; //密码盐值
    public static final String SALT_FOR_PRIVATE_KEY = "fsas1231238uu2sjkfnsdfcs"; //私钥盐值

    public static int ACCOUNT_LOCK_SECOND = -300; //用户连续输错三次密码后的锁定时间（秒）：300秒 （设为非final：方便测试用例）

    public static final String CONTRACT_NAME_ACCOUNT = "ReparoAccount";
    public static final String CONTRACT_NAME_WAYBILL = "ReparoWayBill";

}
