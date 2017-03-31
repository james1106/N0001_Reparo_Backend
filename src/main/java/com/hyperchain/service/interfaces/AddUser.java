package com.hyperchain.service.interfaces;

import com.hyperchain.service.base.BaseInterface;

/**
 * by chenyufeng on 2017/3/31 .
 */
public interface AddUser extends BaseInterface{
    String contractMethodName = "addUser";
    String[] contractMethodReturns = new String[]{"logistics_exchange_code","role"};
}
