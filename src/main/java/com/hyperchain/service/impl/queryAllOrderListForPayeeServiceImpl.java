package com.hyperchain.service.impl;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.QueryAllOrderListForPayeeService;
import org.springframework.stereotype.Service;

/**
 * Created by liangyue on 2017/4/7.
 */
@Service("QueryAllOrderListForPayeeService")
public class queryAllOrderListForPayeeServiceImpl implements QueryAllOrderListForPayeeService {
    @Override
    public BaseResult<Object> invokeContract(ContractKey contractKey, Object[] contractMethodParams) throws Exception {
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractMethodReturns);
        // 将合约结果转化为接口返回数据
        return ContractUtil.convert2BaseResult(contractResult);
    }
}
