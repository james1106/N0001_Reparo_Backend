package com.hyperchain.service;

import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.UserInvalidException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by YanYufei on 2017/4/9.
 */
public interface ReceivableService {
    BaseResult<Object> signOutApply(String orderNo, String pyer, String pyee,double isseAmt,long dueDt,String rate,String contractNo,String invoiceNo,HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException;
    BaseResult<Object> signOutReply(String receivableNo, String replyerAcctId, int response, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException;
    BaseResult<Object> discountApply(String receivableNo, String applicantAcctId, String replyerAcctId, double discountApplyAmount, double discountedRate, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException;
    BaseResult<Object> discountReply(String receivableNo, String replyerAcctId, int response, double discountInHandAmount, double discountRate, double isseAmt, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException;
//    BaseResult<Object> getReceivableAllInfo(ContractKey contractKey, Object[] contractParams);
//    BaseResult<Object> getRecordBySerialNo(ContractKey contractKey, Object[] contractParams);
//    BaseResult<Object> getReceivableHistorySerialNo(ContractKey contractKey, Object[] contractParams);
    BaseResult<Object> cash(String receivableNo, double cashedAmount, int response, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException;

    BaseResult<Object> receivableSimpleDetailList(int roleCode, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException;
    BaseResult<Object> getReceivableAllInfoWithSerial(String receivableNo, String operatorAcctId, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException;
//    BaseResult<Object> discountApplyBankList();
//    BaseResult<Object> getDiscountBankList(ContractKey contractKey, Object[] contractParams);

//    0x0000000000000000000000000000000000000000000000000000000000000000discountApplyBankList
//            0000000000000000000000000000000000000000000000000000000000000060
//            0000000000000000000000000000000000000000000000000000000000000080
//            0000000000000000000000000000000000000000000000000000000000000000
//            0000000000000000000000000000000000000000000000000000000000000000

}
