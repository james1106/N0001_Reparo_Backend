package com.hyperchain.service;

import com.hyperchain.service.base.BaseInterface;

/**
 * Created by liangyue on 2017/4/7.
 */
public interface QueryAllOrderListForPayerService extends BaseInterface {
    String contractMethodName = "queryAllOrderListForPayer";
    String[] contractMethodReturns = new String[]{"orderList"};
}
