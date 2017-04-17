package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.*;
import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.BankInfoEntity;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.ReceivableService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyperchain.common.constant.BaseConstant.CONTRACT_NAME_RECEIVABLE;

/**
 * Created by YanYufei on 2017/4/9.
 */
@Service
public class ReceivableServiceImpl implements ReceivableService{

    @Autowired
    AccountEntityRepository accountEntityRepository;
    @Autowired
    UserEntityRepository userEntityRepository;

    //签发申请
    @Override
    public BaseResult<Object> signOutApply(ContractKey contractKey, Object[] contractParams, String receivableNo) {//第二个参数是给合约的参数
        String methodName = "signOutApply";
        String[] resultMapKey = new String[]{};



        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-signOutApply返回结果：" + contractResult.toString());
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
        if(code == Code.INVALID_USER){//2
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PARAMETER_EMPTY){//3
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.SERIALNO_EXIST){//1032
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.ISSEAMT_ERROR){//1019
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.ACCTID_ADDRESS_ERROR){//1007
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.REPLYER_ACCOUNT_ERROR){//1004
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.REPLYER_ACCOUNT_INVALID){//1031
            result.returnWithoutValue(code);
            return result;
        }
        if(code == Code.RECEIVABLENO_EXITS){//1030
            result.returnWithoutValue(code);
            return result;
        }

        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的

    }

    //签发回复
    @Override
    public BaseResult<Object> signOutReply(ContractKey contractKey, Object[] contractParams) {
        String methodName = "signOutReply";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();


        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-signOutReply返回结果：" + contractResult.toString());
            Code code = contractResult.getCode();
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

    //贴现申请
    @Override
    public BaseResult<Object> discountApply(ContractKey contractKey, Object[] contractParams, String receivableNo) {//第二个参数是给合约的参数
        String methodName = "discountApply";
        String[] resultMapKey = new String[]{};

        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-discountApply返回结果：" + contractResult.toString());
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
        if(code == Code.INVALID_USER){//2
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PARAMETER_EMPTY){//3
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.SERIALNO_EXIST){//1032
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.ACCTID_ADDRESS_ERROR){//1007
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.REPLYER_ACCOUNT_ERROR){//1004
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.REPLYER_ACCOUNT_INVALID){//1031
            result.returnWithoutValue(code);
            return result;
        }

        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的

    }

    @Override
    public BaseResult<Object> discountApplyBankList() {//第二个参数是给合约的参数
        Map<Integer, String> bankNameMap = new HashMap<>();
//        bankNameMap.put(new Long(3456), "zheshangyinhang");

        List<BankInfoEntity> bankInfoList = new ArrayList<>();
        bankInfoList.add(new BankInfoEntity(316, "浙商银行"));
        bankInfoList.add(new BankInfoEntity(102,"中国工商银行"));
        bankInfoList.add(new BankInfoEntity(103, "中国农业银行"));
        bankInfoList.add(new BankInfoEntity(104, "中国银行"));

        // 调用合约查询账户，获取返回结果
        BaseResult result = new BaseResult();
        result.returnWithValue(Code.SUCCESS, bankInfoList);
        return result;
    }

        //贴现回复
    @Override
    public BaseResult<Object> discountReply(ContractKey contractKey, Object[] contractParams, String newReceivableNo) {
        String methodName = "discountReply";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-discountReply返回结果：" + contractResult.toString());
//            Code code = contractResult.getCode();
//            result.returnWithoutValue(code);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        result.returnWithValue(code, newReceivableNo);
        return result;//这个result是返回给前端的

    }

    //兑付
    @Override
    public BaseResult<Object> cash(ContractKey contractKey, Object[] contractParams, String receivableNo) {
        String methodName = "cash";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-cash返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的

    }

    //单张应收款详情
    @Override
    public BaseResult<Object> getReceivableAllInfo(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "getReceivableAllInfo";
        String[] resultMapKey = new String[]{"receivable[]", "uint[]", "discounted", "note"};//给返回值取了个名称


        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-getReceivableAllInfo返回结果：" + contractResult.toString());
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
        if(code == Code.INVALID_USER){//2
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PARAMETER_EMPTY){//3
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.RECEIVABLE_NOT_EXITS){//1005
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PERMISSION_DENIED){//1
            result.returnWithoutValue(code);
            return result;
        }

        List<String> partParams0 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);//取的时候是已经去掉了第一个code的情况，所以是从0开始
        //List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        int discounted = Integer.parseInt(String.valueOf(contractResult.getValueMap().get(resultMapKey[2])));
        String note = (String) contractResult.getValueMap().get(resultMapKey[3]);

        String receivableNo = partParams0.get(0);
        String orderNo = partParams0.get(1);
        String signer = partParams0.get(2);
        String accptr = partParams0.get(3);
        String pyer = partParams0.get(4);
        String pyee = partParams0.get(5);
        String firstOwner = partParams0.get(6);
        String secondOwner = partParams0.get(7);
        String rate = partParams0.get(8);
        String contractNo = partParams0.get(9);
        String invoiceNo = partParams0.get(10);

//        String pyerEnterpriseName = partParams1.get(0);
//        String pyerAcctSvcrName = partParams1.get(1);
//        String pyeeEnterpriseName = partParams1.get(2);
//        String pyeeAcctSvcrName = partParams1.get(3);

        long isseAmt = Long.parseLong(partParams1.get(0));
        long cashedAmount = (partParams1.get(1).equals("")) ? 0L:Long.parseLong(partParams1.get(1));
        long isseDt = Long.parseLong(partParams1.get(2));
        long signInDt = (partParams1.get(3).equals("")) ? 0L:Long.parseLong(partParams1.get(3));
        long dueDt = Long.parseLong(partParams1.get(4));
        long discountInHandAmount = (partParams1.get(5).equals("")) ? 0L:Long.parseLong(partParams1.get(5));
        int status = (partParams1.get(6).equals("")) ? 0 : Integer.parseInt(partParams1.get(6));
        int lastStatus = (partParams1.get(7).equals("")) ? 0 : Integer.parseInt(partParams1.get(7));

        AccountEntity pyerAccountEntity = accountEntityRepository.findByAcctId(pyer);
        AccountEntity pyeeAccountEntity = accountEntityRepository.findByAcctId(pyee);
        String pyerAddress = pyerAccountEntity.getAddress();
        String pyeeAddress = pyeeAccountEntity.getAddress();
        String pyerAcctSvcrName = pyerAccountEntity.getAcctSvcrName();//付款人开户行
        String pyeeAcctSvcrName = pyeeAccountEntity.getAcctSvcrName();//收款人开户行
        UserEntity pyerUserEntity = userEntityRepository.findByAddress(pyerAddress);
        UserEntity pyeeUserEntity = userEntityRepository.findByAddress(pyeeAddress);
        String pyerLinkMan = pyerUserEntity.getPhone();//付款人联系方式
        String pyerEnterpriseName = pyerUserEntity.getCompanyName();//付款人企业名
        String pyeeLinkman = pyeeUserEntity.getPhone();//收款人联系方式
        String pyeeEnterpriseName = pyeeUserEntity.getCompanyName();//收款人企业名

        ReceivableDetailVo receivableDetailVo = new ReceivableDetailVo();

        receivableDetailVo.setReceivableNo(receivableNo);
        receivableDetailVo.setOrderNo(orderNo);
        receivableDetailVo.setSigner(signer);
        receivableDetailVo.setAccptr(accptr);
        receivableDetailVo.setPyer(pyer);
        receivableDetailVo.setPyee(pyee);
        receivableDetailVo.setFirstOwner(firstOwner);
        receivableDetailVo.setSecondOwner(secondOwner);
        receivableDetailVo.setStatus(status);
        receivableDetailVo.setLastStatus(lastStatus);
        receivableDetailVo.setRate(rate);
        receivableDetailVo.setContractNo(contractNo);
        receivableDetailVo.setInvoiceNo(invoiceNo);
        receivableDetailVo.setPyerEnterpriseName(pyerEnterpriseName);
        receivableDetailVo.setPyerAcctSvcrName(pyerAcctSvcrName);
        receivableDetailVo.setPyeeEnterpriseName(pyeeEnterpriseName);
        receivableDetailVo.setPyeeAcctSvcrName(pyeeAcctSvcrName);
        receivableDetailVo.setIsseAmt(isseAmt);
        receivableDetailVo.setCashedAmount(cashedAmount);
        receivableDetailVo.setIsseDt(isseDt);
        receivableDetailVo.setSignInDt(signInDt);
        receivableDetailVo.setDueDt(dueDt);
        receivableDetailVo.setDiscountInHandAmount(discountInHandAmount);
        receivableDetailVo.setDiscounted(discounted);
        receivableDetailVo.setNote(note);
        receivableDetailVo.setPyeeLinkMan(pyeeLinkman);
        receivableDetailVo.setPyerLinkMan(pyerLinkMan);


        result.returnWithValue(code, receivableDetailVo);
        return result;
    }

    //单张应收款详情,包含流水信息
    @Override
    public BaseResult<Object> getReceivableAllInfoWithSerial(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "getReceivableAllInfoWithSerial";
        String[] resultMapKey = new String[]{"receivable[]", "uint[]", "discounted", "note"};//给返回值取了个名称


        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-getReceivableAllInfoWithSerial返回结果：" + contractResult.toString());
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
        if(code == Code.INVALID_USER){//2
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PARAMETER_EMPTY){//3
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.RECEIVABLE_NOT_EXITS){//1005
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PERMISSION_DENIED){//1
            result.returnWithoutValue(code);
            return result;
        }

        List<String> partParams0 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);//取的时候是已经去掉了第一个code的情况，所以是从0开始
        //List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        int discounted = Integer.parseInt(String.valueOf(contractResult.getValueMap().get(resultMapKey[2])));
        String note = (String) contractResult.getValueMap().get(resultMapKey[3]);

        String receivableNo = partParams0.get(0);
        String orderNo = partParams0.get(1);
        String signer = partParams0.get(2);
        String accptr = partParams0.get(3);
        String pyer = partParams0.get(4);
        String pyee = partParams0.get(5);
        String firstOwner = partParams0.get(6);
        String secondOwner = partParams0.get(7);
        String rate = partParams0.get(8);
        String contractNo = partParams0.get(9);
        String invoiceNo = partParams0.get(10);

//        String pyerEnterpriseName = partParams1.get(0);
//        String pyerAcctSvcrName = partParams1.get(1);
//        String pyeeEnterpriseName = partParams1.get(2);
//        String pyeeAcctSvcrName = partParams1.get(3);

        List<ReceivableSimpleSerialVo> receivableSimpleSerialVoList = new ArrayList<>();
        List<ReceivableDetailVo> receivableDetailVoList = new ArrayList<>();
        int length = (partParams1.size() - 8) / 2;
        int lastLength = partParams1.size() - 8;

        long isseAmt = Long.parseLong(partParams1.get(lastLength));
        long cashedAmount = (partParams1.get(lastLength + 1).equals("")) ? 0L:Long.parseLong(partParams1.get(lastLength + 1));
        long isseDt = Long.parseLong(partParams1.get(lastLength + 2));
        long signInDt = (partParams1.get(lastLength + 3).equals("")) ? 0L:Long.parseLong(partParams1.get(lastLength + 3));
        long dueDt = Long.parseLong(partParams1.get(lastLength + 4));
        long discountInHandAmount = (partParams1.get(lastLength + 5).equals("")) ? 0L:Long.parseLong(partParams1.get(lastLength + 5));
        int status = (partParams1.get(lastLength + 6).equals("")) ? 0 : Integer.parseInt(partParams1.get(lastLength + 6));
        int lastStatus = (partParams1.get(lastLength + 7).equals("")) ? 0 : Integer.parseInt(partParams1.get(lastLength + 7));

        AccountEntity pyerAccountEntity = accountEntityRepository.findByAcctId(pyer);
        AccountEntity pyeeAccountEntity = accountEntityRepository.findByAcctId(pyee);
        String pyerAddress = pyerAccountEntity.getAddress();
        String pyeeAddress = pyeeAccountEntity.getAddress();
        String pyerAcctSvcrName = pyerAccountEntity.getAcctSvcrName();//付款人开户行
        String pyeeAcctSvcrName = pyeeAccountEntity.getAcctSvcrName();//收款人开户行
        UserEntity pyerUserEntity = userEntityRepository.findByAddress(pyerAddress);
        UserEntity pyeeUserEntity = userEntityRepository.findByAddress(pyeeAddress);
        String pyerLinkMan = pyerUserEntity.getPhone();//付款人联系方式
        String pyerEnterpriseName = pyerUserEntity.getCompanyName();//付款人企业名
        String pyeeLinkman = pyeeUserEntity.getPhone();//收款人联系方式
        String pyeeEnterpriseName = pyeeUserEntity.getCompanyName();//收款人企业名

        ReceivableDetailVo receivableDetailVo = new ReceivableDetailVo();

        receivableDetailVo.setReceivableNo(receivableNo);
        receivableDetailVo.setOrderNo(orderNo);
        receivableDetailVo.setSigner(signer);
        receivableDetailVo.setAccptr(accptr);
        receivableDetailVo.setPyer(pyer);
        receivableDetailVo.setPyee(pyee);
        receivableDetailVo.setFirstOwner(firstOwner);
        receivableDetailVo.setSecondOwner(secondOwner);
        receivableDetailVo.setStatus(status);
        receivableDetailVo.setLastStatus(lastStatus);
        receivableDetailVo.setRate(rate);
        receivableDetailVo.setContractNo(contractNo);
        receivableDetailVo.setInvoiceNo(invoiceNo);
        receivableDetailVo.setPyerEnterpriseName(pyerEnterpriseName);
        receivableDetailVo.setPyerAcctSvcrName(pyerAcctSvcrName);
        receivableDetailVo.setPyeeEnterpriseName(pyeeEnterpriseName);
        receivableDetailVo.setPyeeAcctSvcrName(pyeeAcctSvcrName);
        receivableDetailVo.setIsseAmt(isseAmt);
        receivableDetailVo.setCashedAmount(cashedAmount);
        receivableDetailVo.setIsseDt(isseDt);
        receivableDetailVo.setSignInDt(signInDt);
        receivableDetailVo.setDueDt(dueDt);
        receivableDetailVo.setDiscountInHandAmount(discountInHandAmount);
        receivableDetailVo.setDiscounted(discounted);
        receivableDetailVo.setNote(note);
        receivableDetailVo.setPyeeLinkMan(pyeeLinkman);
        receivableDetailVo.setPyerLinkMan(pyerLinkMan);

        receivableDetailVoList.add(receivableDetailVo);

        for(int i = 0; i < length; i++){
            ReceivableSimpleSerialVo receivableSimpleSerialVo = new ReceivableSimpleSerialVo();
            receivableSimpleSerialVo.setReceivableStatus(Integer.parseInt(partParams1.get(i*2)));
            receivableSimpleSerialVo.setTime(Long.parseLong(partParams1.get(i*2+1)));

            receivableSimpleSerialVoList.add(receivableSimpleSerialVo);
        }

        Map<String, List> map = new HashMap<>();
        map.put("detailVoList", receivableDetailVoList);
        map.put("serialVoList", receivableSimpleSerialVoList);

        result.returnWithValue(code, map);
        return result;
    }


    //查询单比流水信息
    @Override
    public BaseResult<Object> getRecordBySerialNo(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "getRecordBySerialNo";
        String[] resultMapKey = new String[]{"serialNo", "receivableNo", "applicantAcctId","replyerAcctId","responseType","time","operateType","dealAmt","receivableStatus"};//给返回值取了个名称


        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-getRecordBySerialNo返回结果：" + contractResult.toString());
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

        if(code == Code.PARAMETER_EMPTY){//3
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.RECEIVABLE_RECORD_NOT_EXITS){//1013
            result.returnWithoutValue(code);
            return result;
        }


        String  serialNo =  (String)contractResult.getValueMap().get(resultMapKey[0]);//取的时候是已经去掉了第一个code的情况，所以是从0开始
        String  receivableNo =  (String)contractResult.getValueMap().get(resultMapKey[1]);
        String applicantAcctId = (String)contractResult.getValueMap().get(resultMapKey[2]);
        String replyerAcctId = (String)contractResult.getValueMap().get(resultMapKey[3]);
        String  responseType =  (String)contractResult.getValueMap().get(resultMapKey[4]);
        long time = (String.valueOf(contractResult.getValueMap().get(resultMapKey[5])).equals("")) ? 0 : Long.parseLong(String.valueOf(contractResult.getValueMap().get(resultMapKey[5])));
        String operateType = (String)contractResult.getValueMap().get(resultMapKey[6]);
        long dealAmt = (String.valueOf(contractResult.getValueMap().get(resultMapKey[7])).equals("")) ? 0 : Long.parseLong(String.valueOf(contractResult.getValueMap().get(resultMapKey[7])));
        int receivableStatus = (String.valueOf(contractResult.getValueMap().get(resultMapKey[7])).equals("")) ? 0 : Integer.parseInt(String.valueOf(contractResult.getValueMap().get(resultMapKey[7])));

        ReceivableRecordDetailVo receivableRecordDetailVo = new ReceivableRecordDetailVo();

        receivableRecordDetailVo.setSerialNo(serialNo);
        receivableRecordDetailVo.setReceivableNo(receivableNo);
        receivableRecordDetailVo.setApplicantAcctId(applicantAcctId);
        receivableRecordDetailVo.setReplyerAcctId(replyerAcctId);
        receivableRecordDetailVo.setResponseType(responseType);
        receivableRecordDetailVo.setTime(time);
        receivableRecordDetailVo.setOperateType(operateType);
        receivableRecordDetailVo.setDealAmount(dealAmt);
        receivableRecordDetailVo.setReceivableStatus(receivableStatus);

        result.returnWithValue(code, receivableRecordDetailVo);
        return result;
    }

    //拿到单张应收款编号对应的历史流水编号
    @Override
    public BaseResult<Object> getReceivableHistorySerialNo(ContractKey contractKey, Object[] contractParams) {
//        System.out.println("=====+++++++ooooo");
        String contractMethodName = "getReceivableHistorySerialNo";
        //String[] resultMapKey = new String[]{"transferHistorySerialNo[]"};//给返回值取了个名称

        String[] resultMapKey = new String[]{"transferHistorySerialNo[]"};//给返回值取了个名称

        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-getReceivableHistorySerialNo返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }


        BaseResult<Object> result = new BaseResult<>();
//         将合约结果转化为接口返回数据
//        int resultCode = contractResult.getCode().getCode();
        Code code = contractResult.getCode();

        List<String> resutList = (ArrayList)contractResult.getValue().get(0);
        result.returnWithValue(code, resutList);

        return result;
        }


    //返回买卖方应收款列表
    @Override
    public BaseResult<Object> receivableSimpleDetailList(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "receivableSimpleDetailList";
        String[] resultMapKey = new String[]{"list1", "list2"};
        ContractResult contractResult = null;
        Code code = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, "ReceivableContract");
            LogUtil.info("调用合约ReceivableContract-receivableSimpleDetailList返回结果：" + contractResult.toString());
            code = contractResult.getCode();
            if(code != Code.SUCCESS){
                BaseResult result = new BaseResult();
                result.returnWithoutValue(code);
                return result;
            }
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }

        List<String> list1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> list2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
//        List<String> partList3 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
//        List<String> methodList = (List<String>) contractResult.getValueMap().get(resultMapKey[3]);
//        List<String> stateList = (List<String>) contractResult.getValueMap().get(resultMapKey[4]);
//        int length = methodList.size();
//        List<OrderOverVo> orderOverVoList = new ArrayList<>();
        List<ReceivableSimpleListVo> receivableSimpleList = new ArrayList<>();
        int length = list1.size() / 3;
        for(int i = 0; i < length; i++){
            ReceivableSimpleListVo receivableSimpleListVo = new ReceivableSimpleListVo();
            receivableSimpleListVo.setReceivableNo(list1.get(i*3));
            receivableSimpleListVo.setProductName(list1.get(i*3+1));
            receivableSimpleListVo.setEnterpriseName(list1.get(i*3+2));

//            String quantity =(list2.get(i*4) == null || list2.get(i*4).equals("") )? "0": list2.get(i*4);
            long quantity = (String.valueOf(list2.get(i*4)).equals("")) ? 0 : Long.parseLong(String.valueOf(list2.get(i*4)));

            receivableSimpleListVo.setProductQuantity(quantity);
            receivableSimpleListVo.setIsseAmt(Long.parseLong(list2.get(i*4+1)));
            receivableSimpleListVo.setDueDt(Long.parseLong(list2.get(i*4+2)));
            receivableSimpleListVo.setStatus(Integer.parseInt(list2.get(i*4+3)));
            receivableSimpleList.add(receivableSimpleListVo);
        }
        BaseResult<Object> result = new BaseResult<>();
        result.returnWithValue(code, receivableSimpleList);

        return result;
    }


}
