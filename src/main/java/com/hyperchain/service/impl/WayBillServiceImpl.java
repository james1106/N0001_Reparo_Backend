package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.constant.WayBillStatus;
import com.hyperchain.common.exception.*;
import com.hyperchain.common.util.MoneyUtil;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.controller.vo.OperationRecordVo;
import com.hyperchain.controller.vo.WayBillDetailListVo;
import com.hyperchain.controller.vo.WayBillDetailVo;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
import com.hyperchain.service.WayBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ldy on 2017/4/12.
 */
@Service
public class WayBillServiceImpl implements WayBillService {

    //合约函数名
    public static final String FUNCTION_GENERATE_UN_CONFIRMED_WAY_BILL = "generateUnConfirmedWayBill";
    public static final String FUNCTION_GENERATE_WAY_BILL = "generateWayBill";
    public static final String FUNCTION_UPDATE_WAY_BILL_STATUS_TO_RECEIVED = "updateWayBillStatusToReceived";
    public static final String FUNCTION_GET_ALL_WAYBILL_COUNT = "getAllWaybillCount";
    public static final String FUNCTION_GET_WAITING_WAYBILL_COUNT = "getWaitingWaybillCount";
    public static final String FUNCTION_GET_REQUESTING_WAYBILL_COUNT = "getRequestingWaybillCount";
    public static final String FUNCTION_GET_SENDING_WAYBILL_COUNT = "getSendingWaybillCount";
    public static final String FUNCTION_LIST_WAY_BILL_ORDER_NO = "listWayBillOrderNo";
    public static final String FUNCTION_GET_WAY_BILL = "getWayBill";
    //合约返回值key
    public static final String KEY_LONGS = "longs";
    public static final String KEY_COUNT = "count";
    public static final String KEY_ORDER_NO_LIST = "orderNoList";
    public static final String KEY_STRS = "strs";
    public static final String KEY_ADDRS = "addrs";
    public static final String KEY_LOGISTICS_INFO = "logisticsInfo";
    //订单状态编号后缀
    public static final String POSTFIX_TX_SERIAL_NO_FINISHED = "03";
    //运单编号前缀
    private static final String WAYBILL_NO_PREFIX = "110";

    @Autowired
    UserEntityRepository userEntityRepository;

    /**
     * 卖家企业发货申请，生成未完善运单
     *
     * @param orderNo
     * @param logisticsEnterpriseName
     * @param senderEnterpriseName
     * @param receiverEnterpriseName
     * @param productName
     * @param productQuantity
     * @param productValue
     * @param senderRepoEnterpriseName
     * @param senderRepoCertNo
     * @param receiverRepoEnterpriseName
     * @param receiverRepoBusinessNo
     * @param request
     * @return
     * @throws PrivateKeyIllegalParam
     * @throws ReadFileException
     * @throws PropertiesLoadException
     * @throws ContractInvokeFailException
     * @throws ValueNullException
     * @throws PasswordIllegalParam
     */
    @Override
    public BaseResult<Object> generateUnConfirmedWaybill(String orderNo, String logisticsEnterpriseName, String senderEnterpriseName, String receiverEnterpriseName, String productName, long productQuantity, double productValue, String senderRepoEnterpriseName, String senderRepoCertNo, String receiverRepoEnterpriseName, String receiverRepoBusinessNo, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();

        UserEntity logisticsUserEntity = userEntityRepository.findByCompanyName(logisticsEnterpriseName);  //物流公司信息
        UserEntity receiverUserEntity = userEntityRepository.findByCompanyName(receiverEnterpriseName); //买方企业信息
        UserEntity senderUserEntity = userEntityRepository.findByCompanyName(senderEnterpriseName); //卖方企业信息
        UserEntity senderRepoUserEntity = userEntityRepository.findByCompanyName(senderRepoEnterpriseName); //发货仓储信息
        UserEntity receiverRepoUserEntity = userEntityRepository.findByCompanyName(receiverRepoEnterpriseName); //收货仓储信息
        if (null == logisticsUserEntity || null == receiverUserEntity || null == senderUserEntity || null == senderRepoUserEntity || null == receiverRepoUserEntity) {
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(Code.WAY_BILL_CONTENT_INVALID);
            return baseResult;
        }
        String logisticsAddress = logisticsUserEntity.getAddress();
        String receiverAddress = receiverUserEntity.getAddress();
        String senderAddress = senderUserEntity.getAddress();
        String senderRepoAddress = senderRepoUserEntity.getAddress();
        String receiverRepoAddress = receiverRepoUserEntity.getAddress();

        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);
        String repoContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_REPOSITORY);

        ContractKey requestContractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String requestContractMethodName = FUNCTION_GENERATE_UN_CONFIRMED_WAY_BILL;
        Object[] requestContractMethodParams = new Object[5];
        Long[] requestLongs = new Long[3];
        requestLongs[0] = new Date().getTime(); //requestTime
        requestLongs[1] = MoneyUtil.convertYuanToCent(productValue); //productValue
        requestLongs[2] = productQuantity; //productQuantity
        String[] requestAddrs = new String[5];
        requestAddrs[0] = logisticsAddress; //logisticsAddress
        requestAddrs[1] = senderAddress; //senderAddress
        requestAddrs[2] = receiverAddress; //receiverAddress
        requestAddrs[3] = receiverRepoAddress; //receiverRepoAddress
        requestAddrs[4] = senderRepoAddress; //senderRepoAddress
        String[] requestStrs = new String[6];
        requestStrs[0] = orderNo; //orderNo
        requestStrs[1] = productName; //productName
        requestStrs[2] = senderRepoCertNo; //senderRepoCertNo
        requestStrs[3] = receiverRepoBusinessNo; //receiverRepoBusinessNo
        requestStrs[4] = requestStrs[0] + WayBillStatus.REQUESTING.getCode(); //statusTransId: orderNo + WayBillStatus
        requestStrs[5] = MoneyUtil.generateBusinessNo(WAYBILL_NO_PREFIX); //waybillNo
        requestContractMethodParams[0] = requestLongs;
        requestContractMethodParams[1] = requestAddrs;
        requestContractMethodParams[2] = requestStrs;
        requestContractMethodParams[3] = accountContractAddr;
        requestContractMethodParams[4] = repoContractAddr;
        String[] requestResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult = ContractUtil.invokeContract(requestContractKey, requestContractMethodName, requestContractMethodParams, requestResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约generateUnConfirmedWayBill返回结果：" + requestContractResult.toString());

        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithoutValue(requestContractResult.getCode());
        return baseResult;
    }

    /**
     * 物流确认发货，生成完整运单
     *
     * @param orderNo
     * @param request
     * @return
     */
    @Override
    public BaseResult<Object> generateConfirmedWaybill(String orderNo, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);

        ContractKey confirmContractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String confirmContractMethodName = FUNCTION_GENERATE_WAY_BILL;
        Object[] confirmContractMethodParams = new Object[4];
        confirmContractMethodParams[0] = orderNo; //orderNo
        confirmContractMethodParams[1] = orderNo + WayBillStatus.SENDING.getCode(); //statusTransId: orderNo + WayBillStatus
        confirmContractMethodParams[2] = new Date().getTime(); //sendTime
        confirmContractMethodParams[3] = accountContractAddr; //accountContractAddr
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(confirmContractKey, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约generateWayBill返回结果：" + confirmContractResult.toString());

        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithoutValue(confirmContractResult.getCode());
        return baseResult;
    }

    @Override
    public BaseResult<Object> updateWayBillStatusToReceived(String orderNo, HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);
        String repoContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_REPOSITORY);
        String orderContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);

        ContractKey updateToReceivedContractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String updateToReceivedMethodName = FUNCTION_UPDATE_WAY_BILL_STATUS_TO_RECEIVED;
        Object[] updateToReceivedMethodParams = new Object[7];
        updateToReceivedMethodParams[0] = orderNo; //orderNo
        updateToReceivedMethodParams[1] = orderNo + WayBillStatus.RECEIVED.getCode(); //statusTransId: orderNo + WayBillStatus
        updateToReceivedMethodParams[2] = new Date().getTime(); //operateTime
        updateToReceivedMethodParams[3] = accountContractAddr; //accountContractAddr
        updateToReceivedMethodParams[4] = repoContractAddr; //repoContractAddr
        updateToReceivedMethodParams[5] = orderContractAddr; //orderContractAddr
        updateToReceivedMethodParams[6] = orderNo + POSTFIX_TX_SERIAL_NO_FINISHED; //txSerialNo
        String[] updateToReceivedResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult updateToReceivedResult = ContractUtil.invokeContract(updateToReceivedContractKey, updateToReceivedMethodName, updateToReceivedMethodParams, updateToReceivedResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约updateWayBillStatusToReceived返回结果：" + updateToReceivedResult.toString());

        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithoutValue(updateToReceivedResult.getCode());
        return baseResult;
    }

    @Override
    public BaseResult<Object> getAllWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();

        ContractKey contractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String methodName = FUNCTION_GET_ALL_WAYBILL_COUNT;
        Object[] methodParams = new Object[0];
        String[] resultMapKey = new String[]{KEY_COUNT};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, methodParams, resultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约getAllWaybillCount返回结果：" + contractResult.toString());

        int count = Integer.parseInt((String)contractResult.getValue().get(0));
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(contractResult.getCode(), count);
        return baseResult;
    }

    @Override
    public BaseResult<Object> getWaitingWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();

        ContractKey contractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String methodName = FUNCTION_GET_WAITING_WAYBILL_COUNT;
        Object[] methodParams = new Object[0];
        String[] resultMapKey = new String[]{"count"};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, methodParams, resultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约getWaitingWaybillCount返回结果：" + contractResult.toString());

        int count = Integer.parseInt((String)contractResult.getValue().get(0));
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(contractResult.getCode(), count);
        return baseResult;
    }

    @Override
    public BaseResult<Object> getRequestingWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();

        ContractKey contractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String methodName = FUNCTION_GET_REQUESTING_WAYBILL_COUNT;
        Object[] methodParams = new Object[0];
        String[] resultMapKey = new String[]{KEY_COUNT};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, methodParams, resultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约getRequestingWaybillCount返回结果：" + contractResult.toString());

        int count = Integer.parseInt((String)contractResult.getValue().get(0));
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(contractResult.getCode(), count);
        return baseResult;
    }

    @Override
    public BaseResult<Object> getSendingWayBillCount(String address) throws UserInvalidException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();

        ContractKey contractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String methodName = FUNCTION_GET_SENDING_WAYBILL_COUNT;
        Object[] methodParams = new Object[0];
        String[] resultMapKey = new String[]{KEY_COUNT};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, methodParams, resultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约getSendingWaybillCount返回结果：" + contractResult.toString());

        int count = Integer.parseInt((String)contractResult.getValue().get(0));
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(contractResult.getCode(), count);
        return baseResult;
    }

    /**
     * 获取所有用户相关运单详情列表
     *
     * @param request
     * @return
     */
    @Override
    public BaseResult<Object> getAllRelatedWayBillDetail(HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException, QueryNotExistUserException {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);

        //获取所有用户相关运单的订单号列表
        ContractKey listOrderNoContractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String listOrderNoContractMethodName = FUNCTION_LIST_WAY_BILL_ORDER_NO;
        Object[] listOrderNoContractMethodParams = new Object[1];
        listOrderNoContractMethodParams[0] = accountContractAddr; //accountContractAddr
        String[] listOrderNoResultMapKey = new String[]{KEY_ORDER_NO_LIST};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult listOrderNoContractResult = ContractUtil.invokeContract(listOrderNoContractKey, listOrderNoContractMethodName, listOrderNoContractMethodParams, listOrderNoResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约listWayBillOrderNo返回结果：" + listOrderNoContractResult.toString());
        List<String> orderNoList = (List<String>) listOrderNoContractResult.getValue().get(0);

        //查询订单号列表失败
        if (listOrderNoContractResult.getCode() != Code.SUCCESS) {
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(listOrderNoContractResult.getCode());
            return baseResult;
        }

        //查询订单号列表成功
        //根据订单号获取运单详情
        List<WayBillDetailVo> wayBillDetailVoList = new ArrayList<>();
        //按时间最新排序
        for (int i = orderNoList.size() - 1; i >= 0; i--) {
            LogUtil.debug("运单订单号" + i + " : " + orderNoList.get(i));

            ContractKey waybillContractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
            String waybillContractMethodName = FUNCTION_GET_WAY_BILL;
            Object[] waybillContractMethodParams = new Object[2];
            waybillContractMethodParams[0] = orderNoList.get(i); //orderNo
            waybillContractMethodParams[1] = accountContractAddr; //accountContractAddr
            String[] waybillResultMapKey = new String[]{KEY_LONGS, KEY_STRS, KEY_ADDRS, KEY_LOGISTICS_INFO};
            // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
            ContractResult waybillContractResult = ContractUtil.invokeContract(waybillContractKey, waybillContractMethodName, waybillContractMethodParams, waybillResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
            LogUtil.debug("调用合约getWayBill返回结果：" + waybillContractResult.toString());
            WayBillDetailVo wayBillDetailVo = parseContractResultToWayBillDetailVo(waybillContractResult);
            wayBillDetailVoList.add(wayBillDetailVo);
        }
        WayBillDetailListVo wayBillDetailListVo = new WayBillDetailListVo(wayBillDetailVoList);

        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(Code.SUCCESS, wayBillDetailListVo);
        return baseResult;
    }

    @Override
    public BaseResult<Object> getWayBillDetailByOrderNo(String orderNo, HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, UserInvalidException, QueryNotExistUserException {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if (null == userEntity) {
            throw new UserInvalidException();
        }
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);

        ContractKey waybillContractKey = new ContractKey(accountJson, MoneyUtil.getPasswordForPrivateKey(accountName));
        String waybillContractMethodName = FUNCTION_GET_WAY_BILL;
        Object[] waybillContractMethodParams = new Object[2];
        waybillContractMethodParams[0] = orderNo; //orderNo
        waybillContractMethodParams[1] = accountContractAddr; //accountContractAddr
        String[] waybillResultMapKey = new String[]{KEY_LONGS, KEY_STRS, KEY_ADDRS, KEY_LOGISTICS_INFO};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult waybillContractResult = ContractUtil.invokeContract(waybillContractKey, waybillContractMethodName, waybillContractMethodParams, waybillResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.debug("调用合约getWayBill返回结果：" + waybillContractResult.toString());
        WayBillDetailVo wayBillDetailVo = parseContractResultToWayBillDetailVo(waybillContractResult);
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(waybillContractResult.getCode(), wayBillDetailVo);
        return baseResult;
    }

    private WayBillDetailVo parseContractResultToWayBillDetailVo(ContractResult waybillContractResult) throws UserInvalidException, QueryNotExistUserException {

        if (waybillContractResult.getCode() != Code.SUCCESS) {
            return null;
        }

        Map<String, Object> waybillResultValueMap = waybillContractResult.getValueMap();
        List<String> longs = (List<String>) waybillResultValueMap.get(KEY_LONGS);
        List<String> strs = (List<String>) waybillResultValueMap.get(KEY_STRS);
        List<String> addrs = (List<String>) waybillResultValueMap.get(KEY_ADDRS);
        List<String> logisticsInfo = (List<String>) waybillResultValueMap.get(KEY_LOGISTICS_INFO);

        WayBillDetailVo wayBillDetailVo = new WayBillDetailVo();


        if (Integer.parseInt(longs.get(7)) != WayBillStatus.WAITING.getCode()) { //只有发货待确认(卖家填完发货申请单)之后才有的数据
            UserEntity logisticsUserEntity = userEntityRepository.findByAddress(addrs.get(0).substring(1, addrs.get(0).length()));
            wayBillDetailVo.setLogisticsEnterpriseName(logisticsUserEntity.getCompanyName());
            wayBillDetailVo.setWayBillNo(strs.get(1));
        }
        if (Integer.parseInt(longs.get(7)) != WayBillStatus.WAITING.getCode() &&
                Integer.parseInt(longs.get(7)) != WayBillStatus.REQUESTING.getCode() &&
                Integer.parseInt(longs.get(7)) != WayBillStatus.REJECTED.getCode()) { //只有已发货（物流确认生成运单）之后才有的数据
            wayBillDetailVo.setLogisticsInfo(logisticsInfo);
        }
        //所有状态都有的数据(即从待发货状态就已经有的数据)
        wayBillDetailVo.setOrderNo(strs.get(0));
        wayBillDetailVo.setProductQuantity((Long.parseLong(longs.get(0))));
        wayBillDetailVo.setProductValue(MoneyUtil.convertCentToYuan(Long.parseLong(longs.get(1))));
        wayBillDetailVo.setProductName(strs.get(2));
        wayBillDetailVo.setSenderRepoCertNo(strs.get(3));
        wayBillDetailVo.setReceiverRepoBusinessNo(strs.get(4));
        UserEntity senderUserEntity = userEntityRepository.findByAddress(addrs.get(1).substring(1, addrs.get(1).length())); //返回address类型的数据是"x"开头（其实应该是"0x"，ESDK中把字符串开头的"0"都去掉了），需要把"x"去掉
        UserEntity receiverUserEntity = userEntityRepository.findByAddress(addrs.get(2).substring(1, addrs.get(2).length()));
        UserEntity senderRepoUserEntity = userEntityRepository.findByAddress(addrs.get(3).substring(1, addrs.get(3).length()));
        UserEntity receiverRepoUserEntity = userEntityRepository.findByAddress(addrs.get(4).substring(1, addrs.get(4).length()));
        if (null == senderRepoUserEntity || null == receiverRepoUserEntity || null == senderUserEntity || null == receiverUserEntity) {
            throw new QueryNotExistUserException();
        }
        wayBillDetailVo.setSenderEnterpriseName(senderUserEntity.getCompanyName());
        wayBillDetailVo.setReceiverEnterpriseName(receiverUserEntity.getCompanyName());
        wayBillDetailVo.setSenderRepoEnterpriseName(senderRepoUserEntity.getCompanyName());
        wayBillDetailVo.setReceiverRepoEnterpriseName(receiverRepoUserEntity.getCompanyName());
        //历史状态
        int wayBillStatus = Integer.parseInt(longs.get(7));
        wayBillDetailVo.setWaybillStatusCode(wayBillStatus);
        if (wayBillStatus == WayBillStatus.WAITING.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[1];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(6))); //waitTime
            operationRecordVos[0].setState(WayBillStatus.WAITING.getCode());
        } else if (wayBillStatus == WayBillStatus.REQUESTING.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[2];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[1] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(6))); //waitTime
            operationRecordVos[0].setState(WayBillStatus.WAITING.getCode());
            operationRecordVos[1].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[1].setState(WayBillStatus.REQUESTING.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        } else if (wayBillStatus == WayBillStatus.REJECTED.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[3];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[1] = new OperationRecordVo();
            operationRecordVos[2] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(6))); //waitTime
            operationRecordVos[0].setState(WayBillStatus.WAITING.getCode());
            operationRecordVos[1].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[1].setState(WayBillStatus.REQUESTING.getCode());
            operationRecordVos[2].setOperateTime(Long.parseLong(longs.get(5))); //rejectTime
            operationRecordVos[2].setState(WayBillStatus.REJECTED.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        } else if (wayBillStatus == WayBillStatus.SENDING.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[3];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[1] = new OperationRecordVo();
            operationRecordVos[2] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(6))); //waitTime
            operationRecordVos[0].setState(WayBillStatus.WAITING.getCode());
            operationRecordVos[1].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[1].setState(WayBillStatus.REQUESTING.getCode());
            operationRecordVos[2].setOperateTime(Long.parseLong(longs.get(4))); //sendTime
            operationRecordVos[2].setState(WayBillStatus.SENDING.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        } else if (wayBillStatus == WayBillStatus.RECEIVED.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[4];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[1] = new OperationRecordVo();
            operationRecordVos[2] = new OperationRecordVo();
            operationRecordVos[3] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(6))); //waitTime
            operationRecordVos[0].setState(WayBillStatus.WAITING.getCode());
            operationRecordVos[1].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[1].setState(WayBillStatus.REQUESTING.getCode());
            operationRecordVos[2].setOperateTime(Long.parseLong(longs.get(4))); //sendTime
            operationRecordVos[2].setState(WayBillStatus.SENDING.getCode());
            operationRecordVos[3].setOperateTime(Long.parseLong(longs.get(3))); //receiveTime
            operationRecordVos[3].setState(WayBillStatus.RECEIVED.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        }
        return wayBillDetailVo;
    }
}
