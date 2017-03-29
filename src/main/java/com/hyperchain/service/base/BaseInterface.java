package com.hyperchain.service.base;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;

/**
 * Created by martin on 2017/3/17.
 */
public interface BaseInterface {
    BaseResult<Object> invokeContract(ContractKey contractKey,Object[] contractMethodParams) throws Exception;
}
