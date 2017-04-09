package com.hyperchain.service;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;

/**
 * Created by liangyue on 2017/4/9.
 */
public interface OrderService {
    BaseResult<Object> createOrder(ContractKey contractKey, Object[] contractParams, String orderId);
    BaseResult<Object> queryOrderDetail(ContractKey contractKey, Object[] contractParams);

}
