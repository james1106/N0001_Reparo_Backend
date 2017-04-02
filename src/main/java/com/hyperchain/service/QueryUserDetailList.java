package com.hyperchain.service;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.entity.User;
import com.hyperchain.service.base.BaseInterface;

import java.util.List;

/**
 * by chenyufeng on 2017/4/1 .
 */
public interface QueryUserDetailList extends BaseInterface {
    String contractMethodName = "queryUserDetailList";
    String[] contractMethodReturns = new String[]{"ids", "nickname", "password", "phoneNum"};

    List<User> callContract(ContractKey contractKey, Object[] contractMethodParams) throws Exception;
}
