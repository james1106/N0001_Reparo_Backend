package com.hyperchain.service;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;

/**
 * Created by chenxiaoyang on 2017/4/11.
 */
public interface RepositoryService {
    BaseResult<Object> incomeApply(ContractKey contractKey, Object[] contractParams,String repoBusiNo)throws Exception;

    BaseResult<Object> incomeApplyResponse(ContractKey contractKey, Object[] contractParams)throws Exception;

    BaseResult<Object> incomeConfirm(ContractKey contractKey, Object[] contractParams)throws Exception;
    BaseResult<Object> outcomeResponse(ContractKey contractKey, Object[] contractParams)throws Exception;
    BaseResult<Object> outcomeConfirm(ContractKey contractKey, Object[] contractParams)throws Exception;

    BaseResult<Object> getRepoBusiInfo(ContractKey contractKey, Object[] contractParams)throws Exception ;
    BaseResult<Object> getRepoBusiHistoryList(ContractKey contractKey, Object[] contractParams)throws Exception;


    BaseResult<Object> getRepobusiNoByrepoCert(ContractKey contractKey, Object[] contractParams)throws Exception ;
    BaseResult<Object> getRepoCertDetail(ContractKey contractKey, Object[] contractParams)throws Exception ;
    BaseResult<Object> getRepoCertInfoList(ContractKey contractKey, Object[] contractParams)throws Exception ;
    BaseResult<Object> getRepoBusiInfoList(ContractKey contractKey, Object[] contractParams,int role)throws Exception ;
    BaseResult<Object> createRepoCertForRepoeEnterprise(ContractKey contractKey, Object[] contractParams)throws Exception ;
    BaseResult<Object> updateRepoCertinfo(ContractKey contractKey, Object[] contractParams)throws Exception ;
    }
