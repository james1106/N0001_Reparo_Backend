package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.repository.UserEntityRepository;
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
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_INCOMED;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_WATING_INCOME;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_WATING_INCOME_RESPONSE;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_WATING_OUTCOME;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_OUTCOMED;
/**
 * Created by chenxiaoyang on 2017/4/11.
 */
@Service
public class RepositoryServiceImpl implements RepositoryService{

    @Autowired
    private UserEntityRepository userEntityRepository;



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
    public BaseResult<Object> outcomeResponse(ContractKey contractKey, Object[] contractParams) {
        String methodName = "outcomeResponse";
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
    public BaseResult<Object> outcomeConfirm(ContractKey contractKey, Object[] contractParams) {
        String methodName = "outcomeConfirm";
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
        BaseResult<Object> result = new BaseResult<>();
        try {
            contractResult = invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        LogUtil.info("调用合约getRepoBusiDtlAndHistoryList返回结果：" + contractResult.toString());
        try{

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


            repoBusinessVo.setOpgTimeOfCurStatus(detailInfoList2.get(3).equals("")? 0 : Long.parseLong(detailInfoList2.get(3)));
            //addressResultList.get(i).substring(1)

            String logisticsAddress = detailInfoList3.get(0).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList3.get(0).substring(1);
            String logiEnterpriseName = logisticsAddress.equals("") ? "" : userEntityRepository.findByAddress(logisticsAddress).getCompanyName();//物流公司名称

            String repoEnterpriseAddress = detailInfoList3.get(1).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList3.get(1).substring(1);
            String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();


            repoBusinessVo.setLogisticsEntepsName(logiEnterpriseName);
            repoBusinessVo.setRepoEnterpriceName(repoEnterpriseName);

            //result.returnWithValue(code, repoBusuVoList);
            result.returnWithValue(code, repoBusinessVo);
            return result;
        } catch (Exception e){
            LogUtil.error("执行RepositoryServiceImpl.getRepoBusiHistoryList方法出异常");
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public BaseResult<Object> getRepoCertDetail(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "getRepoCertDetail";
        String[] resultMapKey = new String[]{"bytes32List", "addressList", "uintList"};//给返回值取了个名称

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
        List<String> uintList = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);


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
//        int productQuantity = (contractResult.getValue().get(2)).equals("")? 0 : Integer.parseInt((String)contractResult.getValue().get(2));
//        int productTotalPrice = (contractResult.getValue().get(3)).equals("")? 0 : Integer.parseInt((String)contractResult.getValue().get(3));
//        long repoCreateDate = (contractResult.getValue().get(3)).equals("")? 0 : Long.parseLong((String)contractResult.getValue().get(3));
        //String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();

        repoCertVo.setProductQuantity(uintList.get(0).equals("") ? 0 : Long.parseLong(uintList.get(0)));
        repoCertVo.setProductTotalPrice(uintList.get(1).equals("") ? 0 : Long.parseLong(uintList.get(1)));
        repoCertVo.setRepoCreateDate(uintList.get(2).equals("") ? 0 : Long.parseLong(uintList.get(2)));
        List<OperationRecordVo> recordVos = new ArrayList<>();

        int length = (uintList.size()-3) / 2;
        for(int i = 3; i < length+3; i++){
            OperationRecordVo recordVo = new OperationRecordVo();
            recordVo.setState(uintList.get(i).equals("")? 0 : Integer.parseInt(uintList.get(i)));
            recordVo.setOperateTime(uintList.get(i + length).equals("")? 0 : Long.parseLong(uintList.get(i + length)));
            recordVos.add(recordVo);
        }
        repoCertVo.setRecordVos(recordVos);
        result.returnWithValue(code, repoCertVo);
        return result;
    }

    @Override
    public BaseResult<Object> getRepoCertInfoList(ContractKey contractKey, Object[] contractParams) {
        String methodName = "getRepoCertInfoList";
        String[] resultMapKey = new String[]{"bytesResultList", "uintResultList", "addressResultList"};
        BaseResult result = new BaseResult();

        ContractResult contractResult = null;
        Code code = null;
        try {
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            code = contractResult.getCode();
            if(code != Code.SUCCESS){
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }

        List<String> bytesResultList = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> uintResultList = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> addressResultList = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);

        int length = addressResultList.size();
        List<RepoCertVo> repoCertVos = new ArrayList<>();
        for(int i = 0; i < length; i++){
            String repoacertNo = bytesResultList.get(i*2);
            String productName = bytesResultList.get(i*2+1);
            String repoEnterpriseAddress = addressResultList.get(i).equals("x0000000000000000000000000000000000000000") ? "" : addressResultList.get(i).substring(1);
            int productQuantitiy = uintResultList.get(i*2).equals("")? 0 : Integer.parseInt(uintResultList.get(i*2));
            int repoCertStatus = uintResultList.get(i*2+1).equals("")? 0 : Integer.parseInt(uintResultList.get(i*2+1));
            String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();
            RepoCertVo repoCertVo = new RepoCertVo();
            repoCertVo.setRepoCertNo(repoacertNo);
            repoCertVo.setProductName(productName);
            repoCertVo.setProductQuantity(productQuantitiy);
            repoCertVo.setRepoEnterpriseName(repoEnterpriseName);
            repoCertVo.setRepoCertStatus(repoCertStatus);
            repoCertVos.add(repoCertVo);
        }
        result.returnWithValue(code, repoCertVos);
        return result;
    }

    @Override
    public BaseResult<Object> getRepoBusiInfoList(ContractKey contractKey, Object[] contractParams, int role) {
        String methodName = "getRepoBusiList";
        String[] resultMapKey = new String[]{"repoBusiDetail1", "repoBusiDetail2", "repoBusiDetail3"};
        BaseResult result = new BaseResult();

        ContractResult contractResult = null;
        Code code = null;
        try {
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            code = contractResult.getCode();
            if(code != Code.SUCCESS){
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        LogUtil.info("调用合约getRepoBusiList返回结果：" + contractResult.toString());
        List<RepoBusinessVo> repoBusinessVos = new ArrayList<>();
        try{

            List<String> repoBusiDetail1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
            List<String> repoBusiDetail2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
            List<String> repoBusiDetail3 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);

            int length = repoBusiDetail3.size();


            for(int i = 0; i < length; i++){
                boolean flag=false;
            /*String repoBusinessNo = bytesResultList.get(i*3);
            String productName = bytesResultList.get(i*3+1);
            String repoEnterpriseAddress = bytesResultList.get(i*3+2).equals("x0000000000000000000000000000000000000000") ? "" : bytesResultList.get(i*3+2).substring(1);
            int productQuantitiy = uintResultList.get(i*2).equals("")? 0 : Integer.parseInt(uintResultList.get(i*2));
            int repoBusiStatus = uintResultList.get(i*2+1).equals("")? 0 : Integer.parseInt(uintResultList.get(i*2+1));
            String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();
            //RepoCertVo repoCertVo = new RepoCertVo(repoBusinessNo, productName, productQuantitiy, repoBusiStatus, repoEnterpriseName);
            RepoCertVo repoCertVo = new RepoCertVo();
            repoCertVo.setRepoBusinessNo(repoBusinessNo);
            repoCertVo.setProductName(productName);
            repoCertVo.setProductQuantity(productQuantitiy);
            repoCertVo.setRepoEnterpriseName(repoEnterpriseName);*/
                int repoBusiStatus = repoBusiDetail2.get(i*4).equals("")? 0 : Integer.parseInt(repoBusiDetail2.get(i*4));
                if (repoBusiStatus == 0){
                    continue;
                }
                //入库管理
                if(role == 1 && (repoBusiStatus == Integer.parseInt(REPO_BUSI_WATING_INCOME_RESPONSE)
                        || repoBusiStatus == Integer.parseInt(REPO_BUSI_WATING_INCOME)
                        || repoBusiStatus ==Integer.parseInt(REPO_BUSI_INCOMED))){
                    flag = true;
                }
                //出库管理
                if(role == 2 && (repoBusiStatus == Integer.parseInt(REPO_BUSI_WATING_OUTCOME)
                        || repoBusiStatus == Integer.parseInt(REPO_BUSI_OUTCOMED))){
                    flag = true;
                }
                //仓储机构
                if(role == 3 ){
                    flag = true;
                }
                if(flag){
                    RepoBusinessVo repoBusiVo  = new RepoBusinessVo();
                    repoBusiVo.setRepoBusiNo(repoBusiDetail1.get(i*5));
                    repoBusiVo.setOrderNo(repoBusiDetail1.get(i*5 +1));
                    repoBusiVo.setRepoCertNo(repoBusiDetail1.get(i*5 +2));
                    repoBusiVo.setProductName(repoBusiDetail1.get(i*5 +3));
                    repoBusiVo.setMeasureUnit(repoBusiDetail1.get(i*5 +4));

                    repoBusiVo.setCurRepoBusiStatus(repoBusiStatus);
                    repoBusiVo.setRepoCertStatus(repoBusiDetail2.get(i*4 +1).equals("")? 0 : Integer.parseInt(repoBusiDetail2.get(i*4+1)));
                    repoBusiVo.setProductQuantity(repoBusiDetail2.get(i*4+2).equals("")? 0 : Long.parseLong(repoBusiDetail2.get(i*4+2)));
                    repoBusiVo.setOpgTimeOfCurStatus(repoBusiDetail2.get(i*4+3).equals("")? 0 : Long.parseLong((repoBusiDetail2.get(i*4+3))));

                    String repoEnterpriseAddress = repoBusiDetail3.get(i).equals("x0000000000000000000000000000000000000000") ? "" : repoBusiDetail3.get(i).substring(1);
                    String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();
                    repoBusiVo.setRepoEnterpriceName(repoEnterpriseName);

                    repoBusinessVos.add(repoBusiVo);
                }

            }
        }
        catch (Exception e){
            LogUtil.error("RepositoryServiceImpl.getRepoBusiInfoList方法异常");
            e.printStackTrace();
        }
        result.returnWithValue(code, repoBusinessVos);
        return result;
    }


}