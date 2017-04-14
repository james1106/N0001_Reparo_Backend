package com.hyperchain.service;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;

/**
 * Created by chenxiaoyang on 2017/4/11.
 */
public interface RepositoryService {
    BaseResult<Object> incomeApply(ContractKey contractKey, Object[] contractParams,String repoBusiNo);

    BaseResult<Object> incomeApplyResponse(ContractKey contractKey, Object[] contractParams);

    BaseResult<Object> incomeConfirm(ContractKey contractKey, Object[] contractParams);

    BaseResult<Object> getRepoBusiInfo(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> getRepoBusiHistoryList(ContractKey contractKey, Object[] contractParams);



    BaseResult<Object> getRepoCertDetail(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> getRepoCertInfoList(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> getRepoBusiInfoList(ContractKey contractKey, Object[] contractParams);

}
