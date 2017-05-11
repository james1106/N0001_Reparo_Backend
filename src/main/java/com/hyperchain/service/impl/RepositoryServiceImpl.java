package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.common.util.ReparoUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.controller.vo.*;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.hyperchain.common.constant.BaseConstant.*;
import static com.hyperchain.contract.ContractUtil.invokeContract;

/**
 * Created by chenxiaoyang on 2017/4/11.
 */
@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private UserEntityRepository userEntityRepository;


    @Override
    public BaseResult<Object> incomeApply(ContractKey contractKey, Object[] contractParams, String repoBusiNo)
            throws Exception {
        String methodName = "incomeApply";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: incomeApply 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();

            if (code == Code.SUCCESS) {
                result.returnWithValue(code, repoBusiNo);
            } else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }
        return result;
    }

    @Override
    public BaseResult<Object> incomeApplyResponse(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "incomeResponse";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult;
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: incomeResponse 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();

            if (code == Code.SUCCESS) {

                result.returnWithoutValue(code);
            } else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }
        return result;
    }

    @Override
    public BaseResult<Object> incomeConfirm(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "incomeConfirm";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult;
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: incomeConfirm 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();

            if (code == Code.SUCCESS) {
                //List<String> dataList = (List<String>) contractResult.getValue();
                //result.returnWithoutValue(code);
                String the_data = (String) contractResult.getValue().get(0);
                result.returnWithValue(code, the_data);
            } else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }

        return result;
    }

    @Override
    public BaseResult<Object> outcomeResponse(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "outcomeResponse";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult;
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: outcomeResponse 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();

            result.returnWithoutValue(code);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }

        return result;
    }

    @Override
    public BaseResult<Object> getRepobusiNoByrepoCert(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "getRepoBusinessByRepoCert";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult;
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: getRepoBusinessByRepoCert 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();
            String repoBusiNo = (String) contractResult.getValue().get(0);

            result.returnWithValue(code, repoBusiNo);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }
        return result;
    }

    @Override
    public BaseResult<Object> outcomeConfirm(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "outcomeConfirm";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult;
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: outcomeConfirm 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();


            result.returnWithoutValue(code);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }

        return result;
    }

    @Override
    public BaseResult<Object> getRepoBusiInfo(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "getRepoBusinessDetail";
        //String methodName = "getRepoBusinessDetail";
        String[] resultMapKey = new String[]{"repoCertNo"};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: getRepoBusinessDetail 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();

            if (code == Code.SUCCESS) {
                //result.returnWithValue(code);
                result.returnWithoutValue(code);
            } else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }
        return result;
    }

    @Override
    public BaseResult<Object> getRepoBusiHistoryList(ContractKey contractKey, Object[] contractParams) throws Exception {
        String contractMethodName = "getRepoBusiDtlAndHistoryList";
        String[] resultMapKey = new String[]{"historyList", "detailInfoList1", "detailInfoList2", "detailInfoList3"};//给返回值取了个名称

        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        BaseResult<Object> result = new BaseResult<>();
        try {
            contractResult = invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约getRepoBusiDtlAndHistoryList返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }


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
        for (int i = 0; i < length / 2; i++) {
            int state = historyList.get(i * 2).equals("") ? 0 : Integer.parseInt(historyList.get(i * 2));
            long time = historyList.get(i * 2 + 1).equals("") ? 0 : Long.parseLong(historyList.get(i * 2 + 1));
            OperationRecordVo opVo = new OperationRecordVo(state, time);
            opVoList.add(opVo);
        }
        RepoBusinessVo repoBusinessVo = new RepoBusinessVo();
        repoBusinessVo.setOperationRecordVoList(opVoList);
        repoBusinessVo.setRepoBusiNo(detailInfoList1.get(0));
        repoBusinessVo.setInWaybillNo(detailInfoList1.get(1));
        repoBusinessVo.setRepoCertNo(detailInfoList1.get(2));
        repoBusinessVo.setProductName(detailInfoList1.get(3));
        repoBusinessVo.setMeasureUnit(detailInfoList1.get(4));
        repoBusinessVo.setOutWaybillNo(detailInfoList1.get(5));

        int curRepoBusiStatus = detailInfoList2.get(0).equals("") ? 0 : Integer.parseInt(detailInfoList2.get(0));
        long productQuantity = detailInfoList2.get(1).equals("") ? 0 : Long.parseLong(detailInfoList2.get(1));
        long productTotalPrice = detailInfoList2.get(2).equals("") ? 0 : Long.parseLong(detailInfoList2.get(2));

        repoBusinessVo.setCurRepoBusiStatus(curRepoBusiStatus);
        repoBusinessVo.setProductQuantity(productQuantity);
        repoBusinessVo.setProductTotalPrice(ReparoUtil.convertCentToYuan(productTotalPrice));

        repoBusinessVo.setOpgTimeOfCurStatus(detailInfoList2.get(3).equals("") ? 0 : Long.parseLong(detailInfoList2.get(3)));
        //addressResultList.get(i).substring(1)

        String inLogisticsAddress = detailInfoList3.get(0).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList3.get(0).substring(1);
        String inLogiEnterpriseName = inLogisticsAddress.equals("") ? "" : userEntityRepository.findByAddress(inLogisticsAddress).getCompanyName();//入库物流公司名称

        String repoEnterpriseAddress = detailInfoList3.get(1).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList3.get(1).substring(1);
        String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();
        //repoBusinessVo.setRepoCertStatus(null);

        String sotreEnterpriseAddress = detailInfoList3.get(2).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList3.get(2).substring(1);
        String sotreEnterpriseName = sotreEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(sotreEnterpriseAddress).getCompanyName();

        String outLogisticsAddress = detailInfoList3.get(3).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList3.get(3).substring(1);
        String outLogiEnterpriseName = outLogisticsAddress.equals("") ? "" : userEntityRepository.findByAddress(outLogisticsAddress).getCompanyName();//出库物流公司名称

        repoBusinessVo.setInLogisticsEntepsName(inLogiEnterpriseName);
        repoBusinessVo.setRepoEnterpriceName(repoEnterpriseName);
        repoBusinessVo.setStoreEnterpriseName(sotreEnterpriseName);
        repoBusinessVo.setOutLogisticsEntepsName(outLogiEnterpriseName);

        //result.returnWithValue(code, repoBusuVoList);
        result.returnWithValue(code, repoBusinessVo);
        return result;

    }


    @Override
    public BaseResult<Object> getRepoCertDetail(ContractKey contractKey, Object[] contractParams) throws Exception {
        String contractMethodName = "getRepoCertDetail";
        String[] resultMapKey = new String[]{"bytes32List", "addressList", "uintList"};//给返回值取了个名称

        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约getRepoCertDetail返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }


        BaseResult<Object> result = new BaseResult<>();
//         将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);


        List<String> detailInfoList1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> detailInfoList2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> uintList = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);

        RepoCertVo repoCertVo = new RepoCertVo();
        repoCertVo.setRepoCertNo(detailInfoList1.get(1));
        repoCertVo.setRepoBusinessNo(detailInfoList1.get(2));
        repoCertVo.setProductName(detailInfoList1.get(3));
        repoCertVo.setMeasureUnit(detailInfoList1.get(4));
        repoCertVo.setProductLocation(detailInfoList1.get(6));

        repoCertVo.setRepoEnterpriseAddress(detailInfoList2.get(0));
        repoCertVo.setStorerAddress(detailInfoList2.get(1));
        repoCertVo.setHolderAddress(detailInfoList2.get(2));

        String repoEnterpriseAddress = detailInfoList2.get(0).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList2.get(0).substring(1);
        String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();

        String sotreEnterpriseAddress = detailInfoList2.get(1).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList2.get(1).substring(1);
        String sotreEnterpriseName = sotreEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(sotreEnterpriseAddress).getCompanyName();

        String holderEnterpriseAddress = detailInfoList2.get(2).equals("x0000000000000000000000000000000000000000") ? "" : detailInfoList2.get(2).substring(1);
        String holderEnterpriseName = holderEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(holderEnterpriseAddress).getCompanyName();

        repoCertVo.setHolderName(holderEnterpriseName);
        repoCertVo.setRepoEnterpriseName(repoEnterpriseName);
        repoCertVo.setStorerName(sotreEnterpriseName);

        repoCertVo.setProductQuantity(uintList.get(0).equals("") ? 0 : Long.parseLong(uintList.get(0)));
        repoCertVo.setProductTotalPrice(ReparoUtil.convertCentToYuan(uintList.get(1).equals("") ? 0 : Long.parseLong(uintList.get(1))));
        repoCertVo.setRepoCreateDate(uintList.get(2).equals("") ? 0 : Long.parseLong(uintList.get(2)));
        //repoCertVo.setRepoCertStatus(uintList.get(3).equals("") ? 0 : Integer.parseInt(uintList.get(3)));
        List<OperationRecordVo> recordVos = new ArrayList<>();

        int length = (uintList.size() - 3) / 2;
        for (int i = 3; i < length + 3; i++) {
            OperationRecordVo recordVo = new OperationRecordVo();
            int status = uintList.get(i).equals("") ? 0 : Integer.parseInt(uintList.get(i));
            recordVo.setState(status);
            repoCertVo.setRepoCertStatus(status);
            recordVo.setOperateTime(uintList.get(i + length).equals("") ? 0 : Long.parseLong(uintList.get(i + length)));
            recordVos.add(recordVo);
        }
        repoCertVo.setRecordVos(recordVos);
        result.returnWithValue(code, repoCertVo);
        return result;
    }

    @Override
    public BaseResult<Object> getRepoCertInfoList(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "getRepoCertInfoList";
        String[] resultMapKey = new String[]{"bytesResultList", "uintResultList", "addressResultList"};
        BaseResult result = new BaseResult();

        ContractResult contractResult = null;
        Code code = null;
        try {
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            code = contractResult.getCode();
            LogUtil.debug("调用合约getRepoCertInfoList返回结果：" + contractResult.toString());
            if (code != Code.SUCCESS) {
                result.returnWithoutValue(code);
            }
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }

        List<String> bytesResultList = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> uintResultList = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> addressResultList = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);

        int length = addressResultList.size();
        List<RepoCertList> repoCertVos = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            String repoacertNo = bytesResultList.get(i * 2);
            String productName = bytesResultList.get(i * 2 + 1);
            String repoEnterpriseAddress = addressResultList.get(i).equals("x0000000000000000000000000000000000000000") ? "" : addressResultList.get(i).substring(1);
            int productQuantitiy = uintResultList.get(i * 2).equals("") ? 0 : Integer.parseInt(uintResultList.get(i * 2));
            int repoCertStatus = uintResultList.get(i * 2 + 1).equals("") ? 0 : Integer.parseInt(uintResultList.get(i * 2 + 1));
            String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();

            RepoCertList repoCertVo = new RepoCertList();
            repoCertVo.setRepoCertNo(repoacertNo);
            repoCertVo.setProductName(productName);
            repoCertVo.setProductQuantity(productQuantitiy);
            repoCertVo.setRepoEnterpriseName(repoEnterpriseName);
            repoCertVo.setRepoCertStatus(repoCertStatus);
            repoCertVos.add(repoCertVo);
        }

        Collections.sort(repoCertVos, new Comparator<RepoCertList>() {
            @Override
            public int compare(RepoCertList b1, RepoCertList b2) {//按仓单号降序排列
                return b2.getRepoCertNo().compareTo(b1.getRepoCertNo());
            }

        });
        result.returnWithValue(code, repoCertVos);
        return result;
    }

    @Override
    public BaseResult<Object> getRepoBusiInfoList(ContractKey contractKey, Object[] contractParams, int role) throws Exception {
        String methodName = "getRepoBusiList";
        String[] resultMapKey = new String[]{"repoBusiDetail1", "repoBusiDetail2", "repoBusiDetail3"};
        BaseResult result = new BaseResult();

        ContractResult contractResult = null;
        Code code = null;
        try {
            contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            code = contractResult.getCode();
            if (code != Code.SUCCESS) {
                result.returnWithoutValue(code);
            }
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }
        LogUtil.debug("调用合约getRepoBusiList返回结果：" + contractResult.toString());
        List<RepoBusinessVo> repoBusinessVos = new ArrayList<>();
        try {

            List<String> repoBusiDetail1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
            List<String> repoBusiDetail2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
            List<String> repoBusiDetail3 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);

            int length = repoBusiDetail3.size() / 2;


            for (int i = 0; i < length; i++) {
                boolean flag = false;

                int repoBusiStatus = repoBusiDetail2.get(i * 4).equals("") ? 0 : Integer.parseInt(repoBusiDetail2.get(i * 4));
                if (repoBusiStatus == 0) {
                    continue;
                }
                //入库管理
                if (role == 1 && (repoBusiStatus == Integer.parseInt(REPO_BUSI_WATING_INCOME_RESPONSE)
                        || repoBusiStatus == Integer.parseInt(REPO_BUSI_WATING_INCOME)
                        || repoBusiStatus == Integer.parseInt(REPO_BUSI_INCOMED))) {
                    flag = true;
                }
                //出库管理
                if (role == 2 && (repoBusiStatus == Integer.parseInt(REPO_BUSI_WATING_OUTCOME)
                        || repoBusiStatus == Integer.parseInt(REPO_BUSI_OUTCOMED))) {
                    flag = true;
                }
                //仓储机构
                if (role == 3) {
                    flag = true;
                }
                if (flag) {
                    RepoBusinessVo repoBusiVo = new RepoBusinessVo();
                    repoBusiVo.setRepoBusiNo(repoBusiDetail1.get(i * 5));
                    repoBusiVo.setOrderNo(repoBusiDetail1.get(i * 5 + 1));
                    repoBusiVo.setRepoCertNo(repoBusiDetail1.get(i * 5 + 2));
                    repoBusiVo.setProductName(repoBusiDetail1.get(i * 5 + 3));
                    repoBusiVo.setMeasureUnit(repoBusiDetail1.get(i * 5 + 4));

                    repoBusiVo.setCurRepoBusiStatus(repoBusiStatus);
                    repoBusiVo.setRepoCertStatus(repoBusiDetail2.get(i * 4 + 1).equals("") ? 0 : Integer.parseInt(repoBusiDetail2.get(i * 4 + 1)));
                    repoBusiVo.setProductQuantity(repoBusiDetail2.get(i * 4 + 2).equals("") ? 0 : Long.parseLong(repoBusiDetail2.get(i * 4 + 2)));
                    repoBusiVo.setOpgTimeOfCurStatus(repoBusiDetail2.get(i * 4 + 3).equals("") ? 0 : Long.parseLong((repoBusiDetail2.get(i * 4 + 3))));

                    String repoEnterpriseAddress = repoBusiDetail3.get(i * 2).equals("x0000000000000000000000000000000000000000") ? "" : repoBusiDetail3.get(i * 2).substring(1);
                    String repoEnterpriseName = repoEnterpriseAddress.equals("") ? "" : userEntityRepository.findByAddress(repoEnterpriseAddress).getCompanyName();
                    repoBusiVo.setRepoEnterpriceName(repoEnterpriseName);

                    String holderAddress = repoBusiDetail3.get(i * 2 + 1).equals("x0000000000000000000000000000000000000000") ? "" : repoBusiDetail3.get(i * 2 + 1).substring(1);
                    String holderEnterpriseName = holderAddress.equals("") ? "" : userEntityRepository.findByAddress(holderAddress).getCompanyName();
                    repoBusiVo.setHolderEnterpriseName(holderEnterpriseName);
                    repoBusinessVos.add(repoBusiVo);
                }

            }
            Collections.sort(repoBusinessVos, new Comparator<RepoBusinessVo>() {
                @Override
                public int compare(RepoBusinessVo b1, RepoBusinessVo b2) {//按仓储业务编号降序排列
                    return b2.getRepoBusiNo().compareTo(b1.getRepoBusiNo());
                }

            });

        } catch (Exception e) {
            LogUtil.error("RepositoryServiceImpl.getRepoBusiInfoList方法异常");
            e.printStackTrace();
        }
        result.returnWithValue(code, repoBusinessVos);
        return result;
    }

    @Override
    public BaseResult<Object> createRepoCertForRepoeEnterprise(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "createRepoCertForRepoEnter";
        LogUtil.debug("调用合约 : RepositoryContract 方法: createRepoCertForRepoEnter 入参：" + contractParams.toString());
        String[] resultMapKey = new String[]{""};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: createRepoCertForRepoEnter 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();
            if (code == Code.SUCCESS) {

                result.returnWithoutValue(code);
            } else {
                result.returnWithoutValue(code);
            }
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }
        return result;
    }

    @Override
    public BaseResult<Object> updateRepoCertinfo(ContractKey contractKey, Object[] contractParams) throws Exception {
        String methodName = "updateLogisInfo";
        LogUtil.debug("调用合约 : RepositoryContract 方法: updateLogisInfo 入参：" + contractParams.toString());
        String[] resultMapKey = new String[]{""};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.debug("调用合约 : RepositoryContract 方法: updateLogisInfo 返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();
            if (code == Code.SUCCESS) {
                result.returnWithoutValue(code);
            } else {
                result.returnWithoutValue(code);
            }

        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
            throw new ContractInvokeFailException();
        } catch (ValueNullException e) {
            e.printStackTrace();
            throw new ValueNullException();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
            throw new PasswordIllegalParam();
        }
        return result;
    }


}