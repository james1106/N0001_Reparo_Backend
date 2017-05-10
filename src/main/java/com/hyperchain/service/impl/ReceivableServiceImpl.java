package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.common.util.ReparoUtil;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.*;
import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.BankInfoEntity;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
import com.hyperchain.service.ReceivableService;
import org.apache.poi.util.StringUtil;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hyperchain.common.constant.BaseConstant.*;

/**
 * Created by YanYufei on 2017/4/9.
 */
@Service
public class ReceivableServiceImpl implements ReceivableService {

    public static final String SIGN_OUT_APPLY = "signOutApply";
    public static final String SIGN_OUT_REPLY = "signOutReply";
    public static final String DISCOUNT_APPLY = "discountApply";
    public static final String DISCOUNT_REPLY = "discountReply";
    public static final String CASH = "cash";
    public static final String GET_RECEIVABLE_ALL_INFO_WITH_SERIAL = "getReceivableAllInfoWithSerial";
    public static final String RECEIVABLE_SIMPLE_DETAIL_LIST = "receivableSimpleDetailList";
    //    public static final String GET_DISCOUNT_BANK_LIST = "getDiscountBankList";
//    public static final String GET_RECEIVABLE_ALL_INFO = "getReceivableAllInfo";
//    public static final String GET_RECORD_BY_SERIAL_NO = "getRecordBySerialNo";
//    public static final String GET_RECEIVABLE_HISTORY_SERIAL_NO = "getReceivableHistorySerialNo";
    @Autowired
    AccountEntityRepository accountEntityRepository;
    @Autowired
    UserEntityRepository userEntityRepository;

    //签发申请
    @Override
    public BaseResult<Object> signOutApply(String orderNo, String pyer, String pyee, double isseAmt, long dueDt, String rate, String contractNo, String invoiceNo, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException {//第二个参数是给合约的参数
        BaseResult<Object> result = new BaseResult<>();
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (userEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, ReparoUtil.getPasswordForPrivateKey(accountName));


        long receivableGenerateTime = System.currentTimeMillis();

        String receivableNo = ReparoUtil.generateBusinessNo(RECEIVABLE_NO_GENERATE);
        String serialNo = ReparoUtil.generateBusinessNo(SIGN_OUT_APPLY);
        List<String> list = new ArrayList<>();
        list.add(contractNo);
        list.add(invoiceNo);
        list.add(serialNo);

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//order合约地址

        long isseAmtFen = ReparoUtil.convertYuanToCent(isseAmt);
        Object[] params = new Object[12];
        params[0] = receivableNo;
        params[1] = orderNo;
        params[2] = pyee;
        params[3] = pyer;
        params[4] = pyee;
        params[5] = pyer;
        params[6] = isseAmtFen;
        params[7] = dueDt;
        params[8] = rate;
        params[9] = list;
        params[10] = receivableGenerateTime;
        params[11] = orderContractAddress;

        String[] resultMapKey = new String[]{};


        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, SIGN_OUT_APPLY, params, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.debug("调用合约ReceivableContract-signOutApply返回结果：" + contractResult.toString());
        } catch (
                ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }


        //将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        if (code != Code.SUCCESS) {
            result.returnWithoutValue(code);
            return result;
        }

        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的

    }

    //签发回复
    @Override
    public BaseResult<Object> signOutReply(String receivableNo, String replyerAcctId, int response, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException {
//        catch (Exception e) {
//            LogUtil.error("调用方法signOutReply异常");
//            e.printStackTrace();
//            result.returnWithoutValue(Code.UNKNOWN_ABNORMAL);
//        }
        BaseResult result = new BaseResult();
        long signOutReplyTime = System.currentTimeMillis();
        String serialNo = ReparoUtil.generateBusinessNo(SIGN_OUT_REPLY);

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (userEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, ReparoUtil.getPasswordForPrivateKey(accountName));

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        String wayBillContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_WAYBILL);//wayBill合约地址


        Object[] params = new Object[7];
        params[0] = receivableNo;
        params[1] = replyerAcctId;
        params[2] = response;
        params[3] = serialNo;
        params[4] = signOutReplyTime;
        params[5] = orderContractAddress;
        params[6] = wayBillContractAddress;


        String[] resultMapKey = new String[]{};

        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, SIGN_OUT_REPLY, params, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.debug("调用合约ReceivableContract-signOutReply返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }


//         将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        if (code != Code.SUCCESS) {
            result.returnWithoutValue(code);
            return result;
        }

        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的


    }

    //贴现申请
    @Override
    public BaseResult<Object> discountApply(String receivableNo, String applicantAcctId, String replyerAcctId, double discountApplyAmount, String discountedRate, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException {//第二个参数是给合约的参数
        BaseResult<Object> result = new BaseResult<>();

        long discountApplyTime = System.currentTimeMillis();
        String serialNo = ReparoUtil.generateBusinessNo(DISCOUNT_APPLY_SERIALNO);

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (userEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, ReparoUtil.getPasswordForPrivateKey(accountName));

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        long discountApplyAmountFen = ReparoUtil.convertYuanToCent(discountApplyAmount);
        Object[] params = new Object[8];
        params[0] = receivableNo;
        params[1] = applicantAcctId;
        params[2] = replyerAcctId;
        params[3] = serialNo;
        params[4] = discountApplyTime;
        params[5] = discountApplyAmountFen;
        params[6] = orderContractAddress;
        params[7] = discountedRate;


        String[] resultMapKey = new String[]{};

        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, DISCOUNT_APPLY, params, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.debug("调用合约ReceivableContract-discountApply返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }


//         将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        if (code != Code.SUCCESS) {
            result.returnWithoutValue(code);
            return result;
        }

        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的

    }

    //贴现回复
    @Override
    public BaseResult<Object> discountReply(String receivableNo, String replyerAcctId, int response, double discountInHandAmount, String discountRate, double isseAmt, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException {
        BaseResult<Object> result = new BaseResult<>();

        double doubleDiscountRate = Double.parseDouble(discountRate);
        if ((1 - doubleDiscountRate / 100) * isseAmt != discountInHandAmount) {//todo 利息保留三位（整型），金额计算一般都用整型
            result.returnWithoutValue(Code.DISCOUNTAMOUNT_NOT_MATCH);
            return result;
        }
        long discountReplyTime = System.currentTimeMillis();
        String serialNo = ReparoUtil.generateBusinessNo(DISCOUNT_REPLY_SERIALNO);

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (userEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, ReparoUtil.getPasswordForPrivateKey(accountName));

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        long discountInHandAmountFen = ReparoUtil.convertYuanToCent(discountInHandAmount);
        Object[] params = new Object[7];
        params[0] = receivableNo;
        params[1] = replyerAcctId;
        params[2] = response;
        params[3] = serialNo;
        params[4] = discountReplyTime;
        //params[5] = newReceivableNo;
        params[5] = discountInHandAmountFen;
        params[6] = orderContractAddress;


        String[] resultMapKey = new String[]{};
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, DISCOUNT_REPLY, params, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.debug("调用合约ReceivableContract-discountReply返回结果：" + contractResult.toString());
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
        if (code != Code.SUCCESS) {
            result.returnWithoutValue(code);
            return result;
        }
        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的

    }

    //兑付
    @Override
    public BaseResult<Object> cash(String receivableNo, double cashedAmount, int response, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException {
        BaseResult<Object> result = new BaseResult<>();

        long cashTime = System.currentTimeMillis();
        String serialNo = ReparoUtil.generateBusinessNo(CASH_SERIALNO);

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (userEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, ReparoUtil.getPasswordForPrivateKey(accountName));

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        long cashedAmountFen = ReparoUtil.convertYuanToCent(cashedAmount);
        Object[] params = new Object[6];
        params[0] = receivableNo;
        params[1] = cashedAmountFen;
        params[2] = cashTime;
        params[3] = serialNo;
        params[4] = response;
        params[5] = orderContractAddress;

        String[] resultMapKey = new String[]{};

        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, CASH, params, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.debug("调用合约ReceivableContract-cash返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        if (code != Code.SUCCESS) {
            result.returnWithoutValue(code);
            return result;
        }
        result.returnWithValue(code, receivableNo);
        return result;//这个result是返回给前端的

    }

    //单张应收款详情,包含流水信息
    @Override
    public BaseResult<Object> getReceivableAllInfoWithSerial(String receivableNo, String operatorAcctId, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException {
        BaseResult<Object> result = new BaseResult<>();
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (userEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, ReparoUtil.getPasswordForPrivateKey(accountName));

        List<String> list1 = new ArrayList<>();
        list1.add(receivableNo);
        list1.add(operatorAcctId);

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        String accountContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);//Account合约地址
        String wayBillContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_WAYBILL);//wayBill合约地址

        List<String> list2 = new ArrayList<>();
        list2.add(orderContractAddress);
        list2.add(wayBillContractAddress);
        list2.add(accountContractAddress);

        Object[] params = new Object[2];
        params[0] = list1;
        params[1] = list2;

        String[] resultMapKey = new String[]{"receivable[]", "uint[]", "discounted"};//给返回值取了个名称


        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, GET_RECEIVABLE_ALL_INFO_WITH_SERIAL, params, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-getReceivableAllInfoWithSerial返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }


//         将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        if (code != Code.SUCCESS) {//2
            result.returnWithoutValue(code);
            return result;
        }


        List<String> partParams0 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);//取的时候是已经去掉了第一个code的情况，所以是从0开始
        if (partParams0.size() == 0) {
            result.returnWithoutValue(Code.RETURN_VALUE_EMPTY);
            return result;
        }
        List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        if (partParams1.size() == 0) {
            result.returnWithoutValue(Code.RETURN_VALUE_EMPTY);
            return result;
        }
        int discounted = Integer.parseInt(String.valueOf(contractResult.getValueMap().get(resultMapKey[2])));

        String receivableNo1 = partParams0.get(0);
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
        String payerRepoCertNo = partParams0.get(11);
        String payeeRepoCertNo = partParams0.get(12);
        String payerRepoEnterpriseName = partParams0.get(13);
        String payeeRepoEnterpriseName = partParams0.get(14);
        String waybillNo = partParams0.get(15);
        String logisticsEnterpriseName = partParams0.get(16);
        String discountedRate = partParams0.get(17);

//        String pyerEnterpriseName = partParams1.get(0);
//        String pyerAcctSvcrName = partParams1.get(1);
//        String pyeeEnterpriseName = partParams1.get(2);
//        String pyeeAcctSvcrName = partParams1.get(3);

        List<ReceivableSimpleSerialVo> receivableSimpleSerialVoList = new ArrayList<>();
        List<ReceivableDetailVo> receivableDetailVoList = new ArrayList<>();
        int length = (partParams1.size() - 8) / 2;
        int lastLength = partParams1.size() - 8;
        long isseAmt;
        long cashedAmount;
        long isseDt;
        long signInDt;
        long dueDt;
        long discountInHandAmount;
        int status;
        int lastStatus;
        if (StringUtils.isBlank(partParams1.get(lastLength))) {
            isseAmt = 0;
        } else {
            isseAmt = Long.parseLong(partParams1.get(lastLength));
        }

        if (StringUtils.isBlank(partParams1.get(lastLength + 1))) {
            cashedAmount = 0;
        } else {
            cashedAmount = Long.parseLong(partParams1.get(lastLength + 1));
        }

        if (StringUtils.isBlank(partParams1.get(lastLength + 2))) {
            isseDt = 0;
        } else {
            isseDt = Long.parseLong(partParams1.get(lastLength + 2));
        }

        if (StringUtils.isBlank(partParams1.get(lastLength + 3))) {
            signInDt = 0;
        } else {
            signInDt = Long.parseLong(partParams1.get(lastLength + 3));
        }

        if (StringUtils.isBlank(partParams1.get(lastLength + 4))) {
            dueDt = 0;
        } else {
            dueDt = Long.parseLong(partParams1.get(lastLength + 4));
        }

        if (StringUtils.isBlank(partParams1.get(lastLength + 5))) {
            discountInHandAmount = 0;
        } else {
            discountInHandAmount = Long.parseLong(partParams1.get(lastLength + 5));
        }

        if (StringUtils.isBlank(partParams1.get(lastLength + 6))) {
            status = 0;
        } else {
            status = Integer.parseInt(partParams1.get(lastLength + 6));
        }

        if (StringUtils.isBlank(partParams1.get(lastLength + 7))) {
            lastStatus = 0;
        } else {
            lastStatus = Integer.parseInt(partParams1.get(lastLength + 7));
        }
//        long isseAmt = (partParams1.get(lastLength).equals("")) ? 0L : Long.parseLong(partParams1.get(lastLength));
//        long cashedAmount = (partParams1.get(lastLength + 1).equals("")) ? 0L : Long.parseLong(partParams1.get(lastLength + 1));
//        long isseDt = (partParams1.get(lastLength + 2).equals("")) ? 0L : Long.parseLong(partParams1.get(lastLength + 2));
//        long signInDt = (partParams1.get(lastLength + 3).equals("")) ? 0L : Long.parseLong(partParams1.get(lastLength + 3));
//        long dueDt = (partParams1.get(lastLength + 4).equals("")) ? 0L : Long.parseLong(partParams1.get(lastLength + 4));
//        long discountInHandAmount = (partParams1.get(lastLength + 5).equals("")) ? 0L : Long.parseLong(partParams1.get(lastLength + 5));
//        int status = (partParams1.get(lastLength + 6).equals("")) ? 0 : Integer.parseInt(partParams1.get(lastLength + 6));
//        int lastStatus = (partParams1.get(lastLength + 7).equals("")) ? 0 : Integer.parseInt(partParams1.get(lastLength + 7));

        String isseAmtYuan = ReparoUtil.convertCentToYuan(isseAmt);
        String cashedAmountYuan = ReparoUtil.convertCentToYuan(cashedAmount);
        String discountInHandAmountYuan = ReparoUtil.convertCentToYuan(discountInHandAmount);

        AccountEntity pyerAccountEntity = accountEntityRepository.findByAcctId(pyer);
        if (pyerAccountEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        AccountEntity pyeeAccountEntity = accountEntityRepository.findByAcctId(pyee);//todo
        if (pyeeAccountEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String pyerAddress = pyerAccountEntity.getAddress();
        String pyeeAddress = pyeeAccountEntity.getAddress();
        String pyerAcctSvcrName = pyerAccountEntity.getAcctSvcrName();//付款人开户行
        String pyeeAcctSvcrName = pyeeAccountEntity.getAcctSvcrName();//收款人开户行
        UserEntity pyerUserEntity = userEntityRepository.findByAddress(pyerAddress);
        UserEntity pyeeUserEntity = userEntityRepository.findByAddress(pyeeAddress);
        String pyerLinkPhone = pyerUserEntity.getPhone();//付款人联系方式
        String pyerEnterpriseName = pyerUserEntity.getCompanyName();//付款人企业名
        String pyeeLinkPhone = pyeeUserEntity.getPhone();//收款人联系方式
        String pyeeEnterpriseName = pyeeUserEntity.getCompanyName();//收款人企业名

        ReceivableDetailVo receivableDetailVo = new ReceivableDetailVo();

        receivableDetailVo.setReceivableNo(receivableNo1);
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
        receivableDetailVo.setPayeeRepoCertNo(payeeRepoCertNo);
        receivableDetailVo.setPayerRepoCertNo(payerRepoCertNo);
        receivableDetailVo.setPayeeRepoEnterpriseName(payeeRepoEnterpriseName);
        receivableDetailVo.setPayerRepoEnterpriseName(payerRepoEnterpriseName);
        receivableDetailVo.setWaybillNo(waybillNo);
        receivableDetailVo.setLogisticsEnterpriseName(logisticsEnterpriseName);
        receivableDetailVo.setDiscountedRate(discountedRate);

        receivableDetailVo.setPyerEnterpriseName(pyerEnterpriseName);
        receivableDetailVo.setPyerAcctSvcrName(pyerAcctSvcrName);
        receivableDetailVo.setPyeeEnterpriseName(pyeeEnterpriseName);
        receivableDetailVo.setPyeeAcctSvcrName(pyeeAcctSvcrName);
        receivableDetailVo.setIsseAmt(isseAmtYuan);
        receivableDetailVo.setCashedAmount(cashedAmountYuan);
        receivableDetailVo.setIsseDt(isseDt);
        receivableDetailVo.setSignInDt(signInDt);
        receivableDetailVo.setDueDt(dueDt);
        receivableDetailVo.setDiscountInHandAmount(discountInHandAmountYuan);
        receivableDetailVo.setDiscounted(discounted);
        //receivableDetailVo.setNote(note);
        receivableDetailVo.setPyeeLinkPhone(pyeeLinkPhone);
        receivableDetailVo.setPyerLinkPhone(pyerLinkPhone);

        receivableDetailVoList.add(receivableDetailVo);

        for (int i = 0; i < length; i++) {
            ReceivableSimpleSerialVo receivableSimpleSerialVo = new ReceivableSimpleSerialVo();
            if (StringUtils.isBlank(partParams1.get(i * 2))) {
                receivableSimpleSerialVo.setReceivableStatus(0);
            } else {
                receivableSimpleSerialVo.setReceivableStatus(Integer.parseInt(partParams1.get(i * 2)));
            }
            if (StringUtils.isBlank(partParams1.get(i * 2 + 1))) {
                receivableSimpleSerialVo.setTime(0);
            } else {
                receivableSimpleSerialVo.setTime(Long.parseLong(partParams1.get(i * 2 + 1)));
            }

            receivableSimpleSerialVoList.add(receivableSimpleSerialVo);
        }

        Map<String, List> map = new HashMap<>();
        map.put("detailVoList", receivableDetailVoList);
        map.put("serialVoList", receivableSimpleSerialVoList);

        result.returnWithValue(code, map);
        return result;
    }

    //返回买卖方应收款列表
    @Override
    public BaseResult<Object> receivableSimpleDetailList(int roleCode, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException {
        BaseResult<Object> result = new BaseResult<>();

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (userEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, ReparoUtil.getPasswordForPrivateKey(accountName));

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        String accountContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);//Account合约地址

        List<AccountEntity> accountEntityList = accountEntityRepository.findByAddress(address);
        AccountEntity accountEntity = accountEntityList.get(0);//取出来的结构体
        if (accountEntity == null) {
            result.returnWithoutValue(Code.QUERY_USER_ERROR);
            return result;
        }
        String acctId = accountEntity.getAcctId();

        Object[] params = new Object[4];
        params[0] = roleCode;
        params[1] = acctId;
        params[2] = orderContractAddress;
        params[3] = accountContractAddress;


        String[] resultMapKey = new String[]{"list1", "list2"};
        ContractResult contractResult = null;
        Code code = null;

        try {
            contractResult = ContractUtil.invokeContract(contractKey, RECEIVABLE_SIMPLE_DETAIL_LIST, params, resultMapKey, "ReceivableContract");
            LogUtil.info("调用合约ReceivableContract-receivableSimpleDetailList返回结果：" + contractResult.toString());
            code = contractResult.getCode();
            if (code != Code.SUCCESS) {
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
        if (list1.size() == 0) {
            result.returnWithoutValue(Code.RETURN_VALUE_EMPTY);
            return result;
        }
        List<String> list2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        if (list2.size() == 0) {
            result.returnWithoutValue(Code.RETURN_VALUE_EMPTY);
            return result;
        }

        List<ReceivableSimpleListVo> receivableSimpleList = new ArrayList<>();
        int length = list1.size() / 5;

/*        for(int i = 0; i < length; i++){
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
*/

        AccountEntity firstOwnerAccountEntity;
        AccountEntity accptrAccountEntity;
        String firstOwnerAddress;
        String accptrAddress;

        UserEntity firstOwnerUserEntity;
        UserEntity accptrUserEntity;
        String firstOwnerEnterpriseName;
        String accptrEnterpriseName;

        for (int i = length - 1; i >= 0; i--) {
            ReceivableSimpleListVo receivableSimpleListVo = new ReceivableSimpleListVo();
            receivableSimpleListVo.setReceivableNo(list1.get(i * 5));
            receivableSimpleListVo.setProductName(list1.get(i * 5 + 1));
            receivableSimpleListVo.setEnterpriseName(list1.get(i * 5 + 2));

            firstOwnerAccountEntity = accountEntityRepository.findByAcctId(list1.get(i * 5 + 3));
            if (firstOwnerAccountEntity == null) {
                result.returnWithoutValue(Code.QUERY_USER_ERROR);
                return result;
            }
            accptrAccountEntity = accountEntityRepository.findByAcctId(list1.get(i * 5 + 4));
            if (accptrAccountEntity == null) {
                result.returnWithoutValue(Code.QUERY_USER_ERROR);
                return result;
            }
            firstOwnerAddress = firstOwnerAccountEntity.getAddress();
            accptrAddress = accptrAccountEntity.getAddress();
            firstOwnerUserEntity = userEntityRepository.findByAddress(firstOwnerAddress);
            if (firstOwnerUserEntity == null) {
                result.returnWithoutValue(Code.QUERY_USER_ERROR);
                return result;
            }
            accptrUserEntity = userEntityRepository.findByAddress(accptrAddress);
            if (accptrUserEntity == null) {
                result.returnWithoutValue(Code.QUERY_USER_ERROR);
                return result;
            }
            firstOwnerEnterpriseName = firstOwnerUserEntity.getCompanyName();//持有人企业名
            accptrEnterpriseName = accptrUserEntity.getCompanyName();//承兑人企业名

            receivableSimpleListVo.setFirstOwnerName(firstOwnerEnterpriseName);
            receivableSimpleListVo.setAccptrName(accptrEnterpriseName);

            long quantity;
            if (StringUtils.isBlank(list2.get(i * 4))) {
                quantity = 0;
            } else {
                quantity = Long.parseLong(list2.get(i * 4));
            }
            receivableSimpleListVo.setProductQuantity(quantity);

            String isseAmtYuan;
            if (StringUtils.isBlank(list2.get(i * 4 + 1))) {
                isseAmtYuan = ReparoUtil.convertCentToYuan(0);
            } else {
                isseAmtYuan = ReparoUtil.convertCentToYuan(Long.parseLong(list2.get(i * 4 + 1)));
            }
            receivableSimpleListVo.setIsseAmt(isseAmtYuan);//票面金额

            long dueDt;
            if (StringUtils.isBlank(list2.get(i * 4 + 2))) {
                dueDt = 0;
            } else {
                dueDt = Long.parseLong(list2.get(i * 4 + 2));
            }
            receivableSimpleListVo.setDueDt(dueDt);

            int status;
            if (StringUtils.isBlank(list2.get(i * 4 + 3))) {
                status = 0;
            } else {
                status = Integer.parseInt(list2.get(i * 4 + 3));
            }
            receivableSimpleListVo.setStatus(status);

            long discountApplyTime;
            if (StringUtils.isBlank(list2.get(i * 4 + 4))) {
                discountApplyTime = 0;
            } else {
                discountApplyTime = Long.parseLong(list2.get(i * 4 + 4));
            }
            receivableSimpleListVo.setDiscountApplyTime(discountApplyTime);

            receivableSimpleList.add(receivableSimpleListVo);
        }

        result.returnWithValue(code, receivableSimpleList);

        return result;
    }


/*
    @Override
    public BaseResult<Object> discountApplyBankList() {//第二个参数是给合约的参数
        Map<Integer, String> bankNameMap = new HashMap<>();
//        bankNameMap.put(new Long(3456), "zheshangyinhang");

        List<BankInfoEntity> bankInfoList = new ArrayList<>();
        bankInfoList.add(new BankInfoEntity(316, "浙商银行"));
        bankInfoList.add(new BankInfoEntity(102, "中国工商银行"));
        bankInfoList.add(new BankInfoEntity(103, "中国农业银行"));
        bankInfoList.add(new BankInfoEntity(104, "中国银行"));

        // 调用合约查询账户，获取返回结果
        BaseResult result = new BaseResult();
        result.returnWithValue(Code.SUCCESS, bankInfoList);
        return result;
    }

    //新银行列表
    @Override
    public BaseResult<Object> getDiscountBankList(ContractKey contractKey, Object[] contractParams) {//第二个参数是给合约的参数
        String contractMethodName = GET_DISCOUNT_BANK_LIST;
        String[] resultMapKey = new String[]{"bytes32BankSvcr[]", "bytes32BankName[]", "bytes32BankDiscountRate[]"};//给返回值取了个名称


        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, CONTRACT_NAME_RECEIVABLE);
            LogUtil.info("调用合约ReceivableContract-getDiscountBankList返回结果：" + contractResult.toString());
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }

        BaseResult<Object> result = new BaseResult<>();
        Code code = contractResult.getCode();
        if (code == Code.DISOCOUNT_BANK_NOT_EXITS) {
            result.returnWithoutValue(code);
            return result;
        }

        List<String> partParams0 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> partParams2 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);

//        String bankSvcr1 = partParams0.get(0);
//        String bankSvcr2 = partParams0.get(1);
//        String bankSvcr3 = partParams0.get(2);
//        String bankSvcr4 = partParams0.get(3);
//
//        String bankName1 = partParams1.get(0);
//        String bankName2 = partParams1.get(1);
//        String bankName3 = partParams1.get(2);
//        String bankName4 = partParams1.get(3);

        List<ReceivableBankListVo> receivableBankListVoList = new ArrayList<>();
        for (int i = 0; i < partParams0.size(); i++) {
            ReceivableBankListVo receivableBankListVo = new ReceivableBankListVo();
            receivableBankListVo.setBankSvcr(Integer.parseInt(partParams0.get(i)));
            receivableBankListVo.setBankName(partParams1.get(i));
            receivableBankListVo.setBankDicountRate(partParams2.get(i));
            receivableBankListVoList.add(receivableBankListVo);
        }

        result.returnWithValue(code, receivableBankListVoList);
        return result;
    }
*/


    /*
        //单张应收款详情
        @Override
        public BaseResult<Object> getReceivableAllInfo(ContractKey contractKey, Object[] contractParams) {
            String contractMethodName = GET_RECEIVABLE_ALL_INFO;
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
            if (code == Code.INVALID_USER) {//2
                result.returnWithoutValue(code);
                return result;
            }

            if (code == Code.PARAMETER_EMPTY) {//3
                result.returnWithoutValue(code);
                return result;
            }

            if (code == Code.RECEIVABLE_NOT_EXITS) {//1005
                result.returnWithoutValue(code);
                return result;
            }

            if (code == Code.PERMISSION_DENIED) {//1
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
            long cashedAmount = (partParams1.get(1).equals("")) ? 0L : Long.parseLong(partParams1.get(1));
            long isseDt = Long.parseLong(partParams1.get(2));
            long signInDt = (partParams1.get(3).equals("")) ? 0L : Long.parseLong(partParams1.get(3));
            long dueDt = Long.parseLong(partParams1.get(4));
            long discountInHandAmount = (partParams1.get(5).equals("")) ? 0L : Long.parseLong(partParams1.get(5));
            int status = (partParams1.get(6).equals("")) ? 0 : Integer.parseInt(partParams1.get(6));
            int lastStatus = (partParams1.get(7).equals("")) ? 0 : Integer.parseInt(partParams1.get(7));

            String isseAmtYuan = ReparoUtil.convertCentToYuan(isseAmt);
            String cashedAmountYuan = ReparoUtil.convertCentToYuan(cashedAmount);
            String discountInHandAmountYuan = ReparoUtil.convertCentToYuan(discountInHandAmount);

            AccountEntity pyerAccountEntity = accountEntityRepository.findByAcctId(pyer);
            AccountEntity pyeeAccountEntity = accountEntityRepository.findByAcctId(pyee);
            String pyerAddress = pyerAccountEntity.getAddress();
            String pyeeAddress = pyeeAccountEntity.getAddress();
            String pyerAcctSvcrName = pyerAccountEntity.getAcctSvcrName();//付款人开户行
            String pyeeAcctSvcrName = pyeeAccountEntity.getAcctSvcrName();//收款人开户行
            UserEntity pyerUserEntity = userEntityRepository.findByAddress(pyerAddress);
            UserEntity pyeeUserEntity = userEntityRepository.findByAddress(pyeeAddress);
            String pyerLinkPhone = pyerUserEntity.getPhone();//付款人联系方式
            String pyerEnterpriseName = pyerUserEntity.getCompanyName();//付款人企业名
            String pyeeLinkPhone = pyeeUserEntity.getPhone();//收款人联系方式
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
            receivableDetailVo.setIsseAmt(isseAmtYuan);
            receivableDetailVo.setCashedAmount(cashedAmountYuan);
            receivableDetailVo.setIsseDt(isseDt);
            receivableDetailVo.setSignInDt(signInDt);
            receivableDetailVo.setDueDt(dueDt);
            receivableDetailVo.setDiscountInHandAmount(discountInHandAmountYuan);
            receivableDetailVo.setDiscounted(discounted);
            receivableDetailVo.setNote(note);
            receivableDetailVo.setPyeeLinkPhone(pyeeLinkPhone);
            receivableDetailVo.setPyerLinkPhone(pyerLinkPhone);


            result.returnWithValue(code, receivableDetailVo);
            return result;
        }
    */

    /*
        //查询单比流水信息
        @Override
        public BaseResult<Object> getRecordBySerialNo(ContractKey contractKey, Object[] contractParams) {
            String contractMethodName = GET_RECORD_BY_SERIAL_NO;
            String[] resultMapKey = new String[]{"serialNo", "receivableNo", "applicantAcctId", "replyerAcctId", "responseType", "time", "operateType", "dealAmt", "receivableStatus"};//给返回值取了个名称


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

            if (code == Code.PARAMETER_EMPTY) {//3
                result.returnWithoutValue(code);
                return result;
            }

            if (code == Code.RECEIVABLE_RECORD_NOT_EXITS) {//1013
                result.returnWithoutValue(code);
                return result;
            }


            String serialNo = (String) contractResult.getValueMap().get(resultMapKey[0]);//取的时候是已经去掉了第一个code的情况，所以是从0开始
            String receivableNo = (String) contractResult.getValueMap().get(resultMapKey[1]);
            String applicantAcctId = (String) contractResult.getValueMap().get(resultMapKey[2]);
            String replyerAcctId = (String) contractResult.getValueMap().get(resultMapKey[3]);
            String responseType = (String) contractResult.getValueMap().get(resultMapKey[4]);
            long time = (String.valueOf(contractResult.getValueMap().get(resultMapKey[5])).equals("")) ? 0 : Long.parseLong(String.valueOf(contractResult.getValueMap().get(resultMapKey[5])));
            String operateType = (String) contractResult.getValueMap().get(resultMapKey[6]);
            long dealAmt = (String.valueOf(contractResult.getValueMap().get(resultMapKey[7])).equals("")) ? 0 : Long.parseLong(String.valueOf(contractResult.getValueMap().get(resultMapKey[7])));
            int receivableStatus = (String.valueOf(contractResult.getValueMap().get(resultMapKey[7])).equals("")) ? 0 : Integer.parseInt(String.valueOf(contractResult.getValueMap().get(resultMapKey[7])));

            String dealAmtYuan = ReparoUtil.convertCentToYuan(dealAmt);

            ReceivableRecordDetailVo receivableRecordDetailVo = new ReceivableRecordDetailVo();

            receivableRecordDetailVo.setSerialNo(serialNo);
            receivableRecordDetailVo.setReceivableNo(receivableNo);
            receivableRecordDetailVo.setApplicantAcctId(applicantAcctId);
            receivableRecordDetailVo.setReplyerAcctId(replyerAcctId);
            receivableRecordDetailVo.setResponseType(responseType);
            receivableRecordDetailVo.setTime(time);
            receivableRecordDetailVo.setOperateType(operateType);
            receivableRecordDetailVo.setDealAmount(dealAmtYuan);
            receivableRecordDetailVo.setReceivableStatus(receivableStatus);

            result.returnWithValue(code, receivableRecordDetailVo);
            return result;
        }

        //拿到单张应收款编号对应的历史流水编号
        @Override
        public BaseResult<Object> getReceivableHistorySerialNo(ContractKey contractKey, Object[] contractParams) {
    //        System.out.println("=====+++++++ooooo");
            String contractMethodName = GET_RECEIVABLE_HISTORY_SERIAL_NO;
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

            List<String> resutList = (ArrayList) contractResult.getValue().get(0);
            result.returnWithValue(code, resutList);

            return result;
        }

    */


}
