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
    public BaseResult<Object> incomeConfirm(ContractKey contractKey, Object[] contractParams) {
        String methodName = "incomeConfirm";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult;
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            Code code = contractResult.getCode();
            if(code == Code.SUCCESS){
                //List<String> dataList = (List<String>) contractResult.getValue();
                //result.returnWithoutValue(code);
                String the_data = (String) contractResult.getValue().get(0);
                result.returnWithValue(code,the_data);
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
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public BaseResult<Object> getRepoBusiInfo(ContractKey contractKey, Object[] contractParams) {
        String methodName = "getRepoBusinessDetail";
        //String methodName = "getRepoBusinessDetail";
        String[] resultMapKey = new String[]{"repoCertNo"};
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
        String contractMethodName = "getRepoBusiDtlAndHistoryList";
        String[] resultMapKey = new String[]{"historyList", "detailInfoList1","detailInfoList2","detailInfoList3"};//给返回值取了个名称

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

        List<String> historyList = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> detailInfoList1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> detailInfoList2 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
        List<String> detailInfoList3 = (List<String>) contractResult.getValueMap().get(resultMapKey[3]);

       // List<String> partList3 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
        int length = historyList.size();
        List<OperationRecordVo> opVoList = new ArrayList<>();
        for(int i = 0; i < length / 2; i++){
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


            //RepoBusinessVo repoBusinessVo  = new RepoBusinessVo();

            /*repoBusinessVo.setBusinessTransNo(partList1.get(i*2));
            repoBusinessVo.setOperateOperateTime(partList1.get(i*2 + 1));
            repoBusinessVo.setRepoBusiStatus(partList2.get(i));*/

            //OperationRecordVo opVo = new OperationRecordVo();

            //repoBusuVoList.add(opVo);
            //operationRecordVoList
            OperationRecordVo opVo = new OperationRecordVo(Integer.parseInt(historyList.get(i * 2)), Long.parseLong(historyList.get(i * 2+ 1)));
            opVoList.add(opVo);
        }
        RepoBusinessVo repoBusinessVo  = new RepoBusinessVo();
        repoBusinessVo.setOperationRecordVoList(opVoList);
        repoBusinessVo.setRepoBusiNo(detailInfoList1.get(0));
        repoBusinessVo.setWaybillNo(detailInfoList1.get(1));
        repoBusinessVo.setRepoCertNo(detailInfoList1.get(2));
        repoBusinessVo.setProductName(detailInfoList1.get(3));
        repoBusinessVo.setMeasureUnit(detailInfoList1.get(4));

        int curRepoBusiStatus =  detailInfoList2.get(0).equals("")? 0 :Integer.parseInt(detailInfoList2.get(0));
        long productQuantity  = detailInfoList2.get(1).equals("")?0:Long.parseLong(detailInfoList2.get(1));
        long productTotalPrice = detailInfoList2.get(2).equals("")?0: Long.parseLong(detailInfoList2.get(2));

        repoBusinessVo.setCurRepoBusiStatus(curRepoBusiStatus);
        repoBusinessVo.setProductQuantity(productQuantity);
        repoBusinessVo.setProductTotalPrice(productTotalPrice);

        /*repoBusinessVo.setCurRepoBusiStatus(Integer.parseInt(detailInfoList2.get(0)));
        repoBusinessVo.setProductQuantity(Long.parseLong(detailInfoList2.get(1)));
        repoBusinessVo.setProductTotalPrice(Long.parseLong(detailInfoList2.get(2)));*/

        repoBusinessVo.setOpgTimeOfCurStatus(detailInfoList2.get(3));

        repoBusinessVo.setLogisticsEntepsName(detailInfoList3.get(0));
        repoBusinessVo.setRepoEnterpriceName(detailInfoList3.get(1));

        //result.returnWithValue(code, repoBusuVoList);
         result.returnWithValue(code, repoBusinessVo);
         return result;
    }


    @Override
    public BaseResult<Object> getRepoCertDetail(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "getRepoCertDetail";
        String[] resultMapKey = new String[]{"detailInfoList1","detailInfoList2"};//给返回值取了个名称

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


        List<String> detailInfoList1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> detailInfoList2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);


        /*待补充流转历史
        int length = historyList.size();
        List<OperationRecordVo> opVoList = new ArrayList<>();
        for(int i = 0; i < length / 2; i++){
            OperationRecordVo opVo = new OperationRecordVo(Integer.parseInt(historyList.get(i * 2)), Long.parseLong(historyList.get(i * 2+ 1)));
            opVoList.add(opVo);
        }*/
        RepoCertVo repoCertVo  = new RepoCertVo();
        repoCertVo.setRepoCertNo(detailInfoList1.get(1));
        repoCertVo.setRepoBusinessNo(detailInfoList1.get(2));
        repoCertVo.setProductName(detailInfoList1.get(3));
        repoCertVo.setMeasureUnit(detailInfoList1.get(4));
        repoCertVo.setProductLocation(detailInfoList1.get(6));

        repoCertVo.setRepoEnterpriseAddress(detailInfoList2.get(0));
        repoCertVo.setStorerAddress(detailInfoList2.get(1));
        repoCertVo.setHolderAddress(detailInfoList2.get(2));

        //.equals("")? 0
        /*repoCertVo.setProductQuantity((String) contractResult.getValue().get(2));
        repoCertVo.setProductTotalPrice((String) contractResult.getValue().get(3));
        repoCertVo.setRepoCreateDate((String) contractResult.getValue().get(4));*/
        repoCertVo.setProductQuantity((String) contractResult.getValue().get(2));
        repoCertVo.setProductTotalPrice((String) contractResult.getValue().get(3));
        repoCertVo.setRepoCreateDate((String) contractResult.getValue().get(4));

        result.returnWithValue(code, repoCertVo);
        return result;
    }

}
