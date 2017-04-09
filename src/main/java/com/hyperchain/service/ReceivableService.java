package com.hyperchain.service;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;

/**
 * Created by YanYufei on 2017/4/9.
 */
public interface ReceivableService {
    BaseResult<Object> signOutApply(ContractKey contractKey, Object[] contractParams, String receivableNo);
    BaseResult<Object> signOutReply(ContractKey contractKey, Object[] contractParams);

}
