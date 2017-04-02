package com.hyperchain.service;

import com.hyperchain.service.base.BaseInterface;

/**
 * by chenyufeng on 2017/4/1 .
 */
public interface QueryUserListDemo extends BaseInterface {
    String contractMethodName = "queryUserList";
    String[] contractMethodReturns = new String[]{"userList"};
}
