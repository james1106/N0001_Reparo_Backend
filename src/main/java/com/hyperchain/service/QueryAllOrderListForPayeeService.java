package com.hyperchain.service;

import com.hyperchain.service.base.BaseInterface;

/**
 * Created by liangyue on 2017/4/7.
 */
public interface QueryAllOrderListForPayeeService extends BaseInterface{
    String contractMethodName = "queryAllOrderListForPayee";
    String[] contractMethodReturns = new String[]{"orderList"};
}
