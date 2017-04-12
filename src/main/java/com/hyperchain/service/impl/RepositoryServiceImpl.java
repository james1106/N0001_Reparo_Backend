package com.hyperchain.service.impl;

import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.RepositoryService;
import com.hyperchain.controller.vo.*;
import com.hyperchain.dal.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.hyperchain.common.constant.BaseConstant.CONTRACT_NAME_REPOSITORY;
import static com.hyperchain.contract.ContractUtil.*;

/**
 * Created by chenxiaoyang on 2017/4/11.
 */
@Service
public class RepositoryServiceImpl implements RepositoryService{
    @Override
    public BaseResult<Object> incomeApply(ContractKey contractKey, Object[] contractParams,String repoBusiNo) {
        String methodName = "incomeApply";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            Code code = contractResult.getCode();
            if(code == Code.SUCCESS){
                result.returnWithValue(code, repoBusiNo);
            }
            else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;
    }

    @Override
    public BaseResult<Object> incomeApplyResponse(ContractKey contractKey, Object[] contractParams) {
        String methodName = "incomeResponse";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult;
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            Code code = contractResult.getCode();
            if(code == Code.SUCCESS){
                //result.returnWithValue(code);
                result.returnWithoutValue(code);
            }
            else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;
    }

    @Override
    public BaseResult<Object> getRepoBusiInfo(ContractKey contractKey, Object[] contractParams) {
        String methodName = "getRepoBusinessDetail";
        //String methodName = "getRepoBusinessDetail";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            Code code = contractResult.getCode();
            if(code == Code.SUCCESS){
                //result.returnWithValue(code);
                result.returnWithoutValue(code);
            }
            else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;
    }

    @Override
    public BaseResult<Object> getRepoBusiHistoryList(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "getRepoBusiHistoryList";
        String[] resultMapKey = new String[]{"partList1", "partList2"};//给返回值取了个名称

        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }


        BaseResult<Object> result = new BaseResult<>();
//         将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);

        List<String> partList1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> partList2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
       // List<String> partList3 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
        int length = partList2.size();
        List<RepoBusinessVo> repoBusuVoList = new ArrayList<>();
        for(int i = 0; i < length; i++){
            /*ReceivableRecordDetailVo receivableVo = new ReceivableRecordDetailVo();
            receivableVo.setSerialNo(partList1.get(i*5+1));
            receivableVo.setReceivableNo(partList1.get(i*5));
            receivableVo.setApplicantAcctId(partList1.get(i*5+2));
            receivableVo.setReplyerAcctId(partList1.get(i*5+3));
            receivableVo.setOperateType(partList1.get(i*5+4));

            receivableVo.setTime(Long.parseLong(partList2.get(i*2)));
            receivableVo.setDealAmount(Long.parseLong(partList3.get(i*5)));
            receivableVo.setResponseType(partList3.get(i));*/


            // receivableVo.setReceivableStatus(Long.parseLong(partList3.get(i*5+1)));


            RepoBusinessVo repoBusinessVo  = new RepoBusinessVo();

            repoBusinessVo.setBusinessTransNo(partList1.get(i*2));
            repoBusinessVo.setOperateOperateTime(partList1.get(i*2 + 1));
            repoBusinessVo.setRepoBusiStatus(partList2.get(i));

            repoBusuVoList.add(repoBusinessVo);
        }

        result.returnWithValue(code, repoBusuVoList);

        return result;
    }

}
