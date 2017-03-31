package com.hyperchain.service;

import com.hyperchain.service.base.BaseInterface;

/**
 * by chenyufeng on 2017/3/31 .
 */
public interface AddUser extends BaseInterface{
    String contractMethodName = "addUser";
    String[] contractMethodReturns = new String[]{"result"};
}
