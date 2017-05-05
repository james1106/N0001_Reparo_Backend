package com.hyperchain.service;

import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;

/**
 * Created by YanYufei on 2017/4/9.
 */
public interface ReceivableService {
    BaseResult<Object> signOutApply(ContractKey contractKey, Object[] contractParams, String receivableNo);//第二个是合约的入参
    BaseResult<Object> signOutReply(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> discountApply(ContractKey contractKey, Object[] contractParams, String receivableNo);
    BaseResult<Object> discountReply(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> getReceivableAllInfo(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> getRecordBySerialNo(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> getReceivableHistorySerialNo(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> receivableSimpleDetailList(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> getReceivableAllInfoWithSerial(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> cash(ContractKey contractKey, Object[] contractParams, String receivableNo);
    BaseResult<Object> discountApplyBankList();
    BaseResult<Object> getDiscountBankList(ContractKey contractKey, Object[] contractParams);

//    0x0000000000000000000000000000000000000000000000000000000000000000discountApplyBankList
//            0000000000000000000000000000000000000000000000000000000000000060
//            0000000000000000000000000000000000000000000000000000000000000080
//            0000000000000000000000000000000000000000000000000000000000000000
//            0000000000000000000000000000000000000000000000000000000000000000

}
