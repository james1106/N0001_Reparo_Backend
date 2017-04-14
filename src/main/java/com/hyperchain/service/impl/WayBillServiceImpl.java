package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.constant.WayBillStatus;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ldy on 2017/4/12.
 */
@Service
public class WayBillServiceImpl implements WayBillService {

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
    public BaseResult<Object> generateUnConfirmedWaybill(String orderNo, String logisticsEnterpriseName, String senderEnterpriseName, String receiverEnterpriseName, String productName, long productQuantity, long productValue, String senderRepoEnterpriseName, String senderRepoCertNo, String receiverRepoEnterpriseName, String receiverRepoBusinessNo, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
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
        String receivableContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_RECEIVABLE);

        ContractKey requestContractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String requestContractMethodName = "generateUnConfirmedWayBill";
        Object[] requestContractMethodParams = new Object[5];
        Long[] requestLongs = new Long[3];
        requestLongs[0] = new Date().getTime(); //requestTime
        requestLongs[1] = productValue; //productValue
        requestLongs[2] = productQuantity; //productQuantity
        String[] requestAddrs = new String[5];
        requestAddrs[0] = logisticsAddress; //logisticsAddress
        requestAddrs[1] = senderAddress; //senderAddress
        requestAddrs[2] = receiverAddress; //receiverAddress
        requestAddrs[3] = receiverRepoAddress; //receiverRepoAddress
        requestAddrs[4] = senderRepoAddress; //senderRepoAddress
        String[] requestStrs = new String[5];
        requestStrs[0] = orderNo; //orderNo
        requestStrs[1] = productName; //productName
        requestStrs[2] = senderRepoCertNo; //senderRepoCertNo
        requestStrs[3] = receiverRepoBusinessNo; //receiverRepoBusinessNo
        requestStrs[4] = requestStrs[0] + WayBillStatus.REQUESTING.getCode(); //statusTransId: orderNo + WayBillStatus
        requestContractMethodParams[0] = requestLongs;
        requestContractMethodParams[1] = requestAddrs;
        requestContractMethodParams[2] = requestStrs;
        requestContractMethodParams[3] = accountContractAddr;
        requestContractMethodParams[4] = receivableContractAddr;
        String[] requestResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult = ContractUtil.invokeContract(requestContractKey, requestContractMethodName, requestContractMethodParams, requestResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.info("调用合约generateUnConfirmedWayBill返回结果：" + requestContractResult.toString());

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
    public BaseResult<Object> generateConfirmedWaybill(String orderNo, HttpServletRequest request) throws PrivateKeyIllegalParam, ReadFileException, PropertiesLoadException, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);

        ContractKey confirmContractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String confirmContractMethodName = "generateWayBill";
        Object[] confirmContractMethodParams = new Object[5];
        confirmContractMethodParams[0] = orderNo; //orderNo
        confirmContractMethodParams[1] = orderNo + WayBillStatus.SENDING.getCode(); //statusTransId: orderNo + WayBillStatus
        confirmContractMethodParams[2] = generateWayBillNo(); //wayBillNo
        confirmContractMethodParams[3] = new Date().getTime(); //sendTime
        confirmContractMethodParams[4] = accountContractAddr; //accountContractAddr
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(confirmContractKey, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.info("调用合约generateWayBill返回结果：" + confirmContractResult.toString());

        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithoutValue(confirmContractResult.getCode());
        return baseResult;
    }

    @Override
    public BaseResult<Object> updateWayBillStatusToReceived(String orderNo, HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);
        String repoContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_REPOSITORY);
        String orderContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);

        ContractKey updateToReceivedContractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String updateToReceivedMethodName = "updateWayBillStatusToReceived";
        Object[] updateToReceivedMethodParams = new Object[6];
        updateToReceivedMethodParams[0] = orderNo; //orderNo
        updateToReceivedMethodParams[1] = orderNo + WayBillStatus.RECEIVED.getCode(); //statusTransId: orderNo + WayBillStatus
        updateToReceivedMethodParams[2] = new Date().getTime(); //operateTime
        updateToReceivedMethodParams[3] = accountContractAddr; //accountContractAddr
        updateToReceivedMethodParams[4] = repoContractAddr; //repoContractAddr
        updateToReceivedMethodParams[5] = orderContractAddr; //orderContractAddr
        String[] updateToReceivedResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult updateToReceivedResult = ContractUtil.invokeContract(updateToReceivedContractKey, updateToReceivedMethodName, updateToReceivedMethodParams, updateToReceivedResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        System.out.println("调用合约updateWayBillStatusToReceived返回结果：" + updateToReceivedResult.toString());

        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithoutValue(updateToReceivedResult.getCode());
        return baseResult;
    }

    /**
     * 获取所有用户相关运单详情列表
     *
     * @param request
     * @return
     */
    @Override
    public BaseResult<Object> getAllRelatedWayBillDetail(HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);

        //获取所有用户相关运单的订单号列表
        ContractKey listOrderNoContractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String listOrderNoContractMethodName = "listWayBillOrderNo";
        Object[] listOrderNoContractMethodParams = new Object[1];
        listOrderNoContractMethodParams[0] = accountContractAddr; //accountContractAddr
        String[] listOrderNoResultMapKey = new String[]{"orderNoList"};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult listOrderNoContractResult = ContractUtil.invokeContract(listOrderNoContractKey, listOrderNoContractMethodName, listOrderNoContractMethodParams, listOrderNoResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        LogUtil.info("调用合约getWayBill返回结果：" + listOrderNoContractResult.toString());
        List<String> orderNoList = (List<String>)listOrderNoContractResult.getValue().get(0);

        //查询订单号列表失败
        if (listOrderNoContractResult.getCode() != Code.SUCCESS) {
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(listOrderNoContractResult.getCode());
            return baseResult;
        }

        //查询订单号列表成功
        //根据订单号获取运单详情
        List<WayBillDetailVo> wayBillDetailVoList = new ArrayList<>();
        for (int i = 0; i < orderNoList.size(); i++) {
            LogUtil.info("运单订单号" + i + " : " + orderNoList.get(i));

            ContractKey waybillContractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
            String waybillContractMethodName = "getWayBill";
            Object[] waybillContractMethodParams = new Object[2];
            waybillContractMethodParams[0] = orderNoList.get(i); //orderNo
            waybillContractMethodParams[1] = accountContractAddr; //accountContractAddr
            String[] waybillResultMapKey = new String[]{"longs", "strs", "addrs", "logisticsInfo", "wayBillStatus"};
            // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
            ContractResult waybillContractResult = ContractUtil.invokeContract(waybillContractKey, waybillContractMethodName, waybillContractMethodParams, waybillResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
            System.out.println("调用合约getWayBill返回结果：" + waybillContractResult.toString());
            WayBillDetailVo wayBillDetailVo = parseContractResultToWayBillDetailVo(waybillContractResult);
            wayBillDetailVoList.add(wayBillDetailVo);
        }
        WayBillDetailListVo wayBillDetailListVo = new WayBillDetailListVo(wayBillDetailVoList);

        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(Code.SUCCESS, wayBillDetailListVo);
        return baseResult;
    }

    @Override
    public BaseResult<Object> getWayBillDetailByOrderNo(String orderNo, HttpServletRequest request) throws ReadFileException, PropertiesLoadException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        //合约名称
        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);

        ContractKey waybillContractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String waybillContractMethodName = "getWayBill";
        Object[] waybillContractMethodParams = new Object[2];
        waybillContractMethodParams[0] = orderNo; //orderNo
        waybillContractMethodParams[1] = accountContractAddr; //accountContractAddr
        String[] waybillResultMapKey = new String[]{"longs", "strs", "addrs", "logisticsInfo", "wayBillStatus"};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult waybillContractResult = ContractUtil.invokeContract(waybillContractKey, waybillContractMethodName, waybillContractMethodParams, waybillResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        System.out.println("调用合约getWayBill返回结果：" + waybillContractResult.toString());
        WayBillDetailVo wayBillDetailVo = parseContractResultToWayBillDetailVo(waybillContractResult);
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(waybillContractResult.getCode(), wayBillDetailVo);
        return baseResult;
    }

    private String generateWayBillNo() {
        String waybillNo = "100" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (new Random().nextInt(900) + 100);
        return waybillNo;
    }

    private WayBillDetailVo parseContractResultToWayBillDetailVo(ContractResult waybillContractResult) {
        Map<String, Object> waybillResultValueMap = waybillContractResult.getValueMap();
        List<String> longs = (List<String>) waybillResultValueMap.get("longs");
        List<String> strs = (List<String>) waybillResultValueMap.get("strs");
        List<String> addrs = (List<String>) waybillResultValueMap.get("addrs");
        List<String> logisticsInfo = (List<String>) waybillResultValueMap.get("logisticsInfo");
        int wayBillStatus = Integer.parseInt((String) waybillResultValueMap.get("wayBillStatus"));

        WayBillDetailVo wayBillDetailVo = new WayBillDetailVo();

//            System.out.println("productQuantity: " + longs.get(0));
//            System.out.println("productValue: " + longs.get(1));
//            System.out.println("requestTime: " + longs.get(2));
//            System.out.println("receiveTime: " + longs.get(3));
//            System.out.println("sendTime: " + longs.get(4));
//            System.out.println("rejectTime: " + longs.get(5));
//            System.out.println("orderNo: " + strs.get(0));
//            System.out.println("wayBillNo: " + strs.get(1));
//            System.out.println("productName: " + strs.get(2));
//            System.out.println("senderRepoCertNo: " + strs.get(3));
//            System.out.println("receiverRepoBusinessNo: " + strs.get(4));
//            System.out.println("logisticsAddress: " + addrs.get(0)); //以"0x"开头
//            System.out.println("senderAddress: " + addrs.get(1)); //以"0x"开头
//            System.out.println("receiverAddress: " + addrs.get(2));//以"0x"开头
//            System.out.println("senderRepoAddress: " + addrs.get(3));//以"0x"开头
//            System.out.println("receiverRepoAddress: " + addrs.get(4));//以"0x"开头
//            System.out.println("logisticsInfo: " + logisticsInfo);
//            System.out.println("wayBillStatus: " + wayBillStatus);

        wayBillDetailVo.setProductQuantity((Long.parseLong(longs.get(0))));
        wayBillDetailVo.setProductValue(Long.parseLong(longs.get(1)));
        wayBillDetailVo.setOrderNo(strs.get(0));
        wayBillDetailVo.setWayBillNo(strs.get(1));
        wayBillDetailVo.setProductName(strs.get(2));
        wayBillDetailVo.setSenderRepoCertNo(strs.get(3));
        wayBillDetailVo.setReceiverRepoBusinessNo(strs.get(4));
        UserEntity logisticsUserEntity = userEntityRepository.findByAddress(addrs.get(0).substring(1, addrs.get(0).length()));
        wayBillDetailVo.setLogisticsEnterpriseName(logisticsUserEntity.getCompanyName());
        UserEntity senderUserEntity = userEntityRepository.findByAddress(addrs.get(1).substring(1, addrs.get(1).length()));
        wayBillDetailVo.setSenderEnterpriseName(senderUserEntity.getCompanyName());
        UserEntity receiverUserEntity = userEntityRepository.findByAddress(addrs.get(2).substring(1, addrs.get(2).length()));
        wayBillDetailVo.setReceiverEnterpriseName(receiverUserEntity.getCompanyName());
        UserEntity senderRepoUserEntity = userEntityRepository.findByAddress(addrs.get(3).substring(1, addrs.get(3).length()));
        wayBillDetailVo.setSenderRepoEnterpriseName(senderRepoUserEntity.getCompanyName());
        UserEntity receiverRepoUserEntity = userEntityRepository.findByAddress(addrs.get(4).substring(1, addrs.get(4).length()));
        wayBillDetailVo.setReceiverRepoEnterpriseName(receiverRepoUserEntity.getCompanyName());
        wayBillDetailVo.setWaybillStatusCode(wayBillStatus);
        wayBillDetailVo.setLogisticsInfo(logisticsInfo);
        if (wayBillStatus == WayBillStatus.REQUESTING.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[1];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[0].setState(WayBillStatus.REQUESTING.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        } else if (wayBillStatus == WayBillStatus.REJECTED.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[2];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[1] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[0].setState(WayBillStatus.REQUESTING.getCode());
            operationRecordVos[1].setOperateTime(Long.parseLong(longs.get(5))); //rejectTime
            operationRecordVos[1].setState(WayBillStatus.REJECTED.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        } else if (wayBillStatus == WayBillStatus.SENDING.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[2];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[1] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[0].setState(WayBillStatus.REQUESTING.getCode());
            operationRecordVos[1].setOperateTime(Long.parseLong(longs.get(4))); //sendTime
            operationRecordVos[1].setState(WayBillStatus.SENDING.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        } else if (wayBillStatus == WayBillStatus.RECEIVED.getCode()) {
            OperationRecordVo[] operationRecordVos = new OperationRecordVo[3];
            operationRecordVos[0] = new OperationRecordVo();
            operationRecordVos[1] = new OperationRecordVo();
            operationRecordVos[2] = new OperationRecordVo();
            operationRecordVos[0].setOperateTime(Long.parseLong(longs.get(2))); //requestTime
            operationRecordVos[0].setState(WayBillStatus.REQUESTING.getCode());
            operationRecordVos[1].setOperateTime(Long.parseLong(longs.get(4))); //sendTime
            operationRecordVos[1].setState(WayBillStatus.SENDING.getCode());
            operationRecordVos[2].setOperateTime(Long.parseLong(longs.get(3))); //receiveTime
            operationRecordVos[2].setState(WayBillStatus.RECEIVED.getCode());
            wayBillDetailVo.setOperationRecordVo(operationRecordVos);
        }
        return wayBillDetailVo;
    }
}
