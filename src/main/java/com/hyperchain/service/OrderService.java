package com.hyperchain.service;

import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;

/**
 * Created by liangyue on 2017/4/9.
 */
public interface OrderService {
    BaseResult<Object> createOrder(ContractKey contractKey, Object[] contractParams, String orderNo) throws ContractInvokeFailException, ValueNullException, PasswordIllegalParam;
    BaseResult<Object> queryOrderDetail(ContractKey contractKey, Object[] contractParams) throws ContractInvokeFailException, ValueNullException, PasswordIllegalParam;
    BaseResult<Object> queryAllOrderOverViewInfoList(ContractKey contractKey, Object[] contractParams) throws ContractInvokeFailException, ValueNullException, PasswordIllegalParam;
    BaseResult<Object> confirmOrder(ContractKey contractKey, Object[] contractParams) throws ContractInvokeFailException, ValueNullException, PasswordIllegalParam;

}
