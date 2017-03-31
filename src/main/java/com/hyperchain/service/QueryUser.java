package com.hyperchain.service;

import com.hyperchain.service.base.BaseInterface;

/**
 * Created by martin on 2017/3/17.
 */
public interface QueryUser extends BaseInterface{
    String contractMethodName = "addUser";
    String[] contractMethodReturns = new String[]{"logistics_exchange_code","role"};
}
