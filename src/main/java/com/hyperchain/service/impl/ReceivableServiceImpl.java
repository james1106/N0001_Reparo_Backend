package com.hyperchain.service.impl;

import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.ReceivableService;
import org.springframework.stereotype.Service;

/**
 * Created by YanYufei on 2017/4/9.
 */
@Service
public class ReceivableServiceImpl implements ReceivableService{
    @Override
    public BaseResult<Object> signOutApply(ContractKey contractKey, Object[] contractParams, String receivableNo) {//第二个参数是给合约的参数
        String methodName = "signOutApply";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();


        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey);
            Code code = contractResult.getCode();
            result.returnWithValue(code, receivableNo);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;//这个result是返回给前端的

    }

    @Override
    public BaseResult<Object> signOutReply(ContractKey contractKey, Object[] contractParams) {
        String methodName = "signOutReply";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();


        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey);
            Code code = contractResult.getCode();//第一个code
            result.returnWithoutValue(code);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;//这个result是返回给前端的

    }
}
