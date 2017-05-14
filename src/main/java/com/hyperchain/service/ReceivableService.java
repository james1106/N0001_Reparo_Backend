package com.hyperchain.service;

import com.hyperchain.common.exception.*;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by YanYufei on 2017/4/9.
 */
public interface ReceivableService {
    BaseResult<Object> signOutApply(String orderNo, String pyer, String pyee,double isseAmt,long dueDt,String rate,String contractNo,String invoiceNo,HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;
    BaseResult<Object> signOutReply(String receivableNo, String replyerAcctId, int response, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;
    BaseResult<Object> discountApply(String receivableNo, String applicantAcctId, String replyerAcctId, double discountApplyAmount, double discountedRate, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;
    BaseResult<Object> discountReply(String receivableNo, String replyerAcctId, int response, double discountInHandAmount, double discountRate, double isseAmt, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;

    BaseResult<Object> cash(String receivableNo, double cashedAmount, int response, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;

    BaseResult<Object> receivableSimpleDetailList(int roleCode, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;
    BaseResult<Object> getReceivableAllInfoWithSerial(String receivableNo, String operatorAcctId, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, UserInvalidException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam;


}
