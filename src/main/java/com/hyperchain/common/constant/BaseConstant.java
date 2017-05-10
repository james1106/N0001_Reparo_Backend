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

    public static final String CONTRACT_NAME_ACCOUNT = "AccountContract";
    public static final String CONTRACT_NAME_WAYBILL = "WayBillContract";
    public static final String CONTRACT_NAME_RECEIVABLE = "ReceivableContract";
    public static final String CONTRACT_NAME_REPOSITORY = "RepositoryContract";
    public static final String CONTRACT_NAME_ORDER = "OrderContract";

    public static final String REPO_BUSI_WATING_INCOME_RESPONSE= "1";//入库待响应
    public static final String REPO_BUSI_WATING_INCOME= "2";//待入库
    public static final String REPO_BUSI_INCOMED = "3";//已入库
    public static final String REPO_BUSI_WATING_OUTCOME = "5";//待出库
    public static final String REPO_BUSI_OUTCOMED = "6";//已出库

    public static final String DEFAULT_RATE = "5"; //金融机构默认利率

    public static final String TOKEN_NAME = "token";
    public static final String COOKIE_PATH = "/";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_ROLECODE = "roleCode";

    public static final String RECEIVABLE_NO_GENERATE = "120";
    public static final String SIGNOUT_APPLY_SERIALNO = "121";
    public static final String SIGNOUT_REPLY_SERIALNO = "122";
    public static final String DISCOUNT_APPLY_SERIALNO = "123";
    public static final String DISCOUNT_REPLY_SERIALNO = "124";
    public static final String CASH_SERIALNO = "125";
}
