package com.hyperchain.service;

import com.hyperchain.service.base.BaseInterface;

/**
 * by chenyufeng on 2017/4/1 .
 */
public interface QueryUserDetailListDemo extends BaseInterface {
    String contractMethodName = "queryUserDetailList";
    String[] contractMethodReturns = new String[]{"ids", "nickname", "password", "phoneNum"};
}
