package com.hyperchain.service.interfaces;

import com.hyperchain.service.base.BaseInterface;

/**
 * Created by martin on 2017/3/17.
 */
public interface QueryUser extends BaseInterface{
    String contractMethodName = "queryUser";
    String[] contractMethodReturns = new String[]{"logistics_exchange_code","role", "role2", "role3"};
}
