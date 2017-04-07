package com.hyperchain.service;

import com.hyperchain.service.base.BaseInterface;

/**
 * Created by liangyue on 2017/4/7.
 */
public interface QueryOrderDetailService extends BaseInterface {
    String contractMethodName = "queryOrderDetail";
    String[] contractMethodReturns = new String[]{"address1", "address2", "uint[]", "string", "int", "int"};
}
