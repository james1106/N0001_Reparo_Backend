package com.hyperchain.contract;

import com.hyperchain.common.constant.AccountStatus;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.constant.WayBillStatus;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.AccountService;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Map;

/**
 * Created by ldy on 2017/4/11.
 */
public class WayBillContractTest extends SpringBaseTest {

    @Autowired
    AccountService accountService;

    @Autowired
    UserEntityRepository userEntityRepository;

    private String senderAddress;
    private String receiverAddress;
    private String logisticsAddress;
    private String senderAccountJson;
    private String receiverAccountJson;
    private String logisticsAccountJson;
    private String senderAccountName;
    private String receiverAccountName;
    private String logisticsAccountName;
    private String senderRepoAddress;
    private String receiverRepoAddress;


    @Before
    public void init() throws PasswordIllegalParam, GeneralSecurityException, PrivateKeyIllegalParam, ContractInvokeFailException, IOException, ValueNullException {
        //发货企业注册
        String randomString = TestUtil.getRandomString();
        BaseResult<Object> result = accountService.register("account" + randomString, //unique
                "123",
                "企业" + randomString, //unique
                "1881881" + randomString, //unique
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("发货企业注册返回结果：" + result.toString());
        Assert.assertEquals(result.getCode(), Code.SUCCESS.getCode());
        UserEntity senderAccountEntity = userEntityRepository.findByAccountName("account" + randomString);
        senderAccountJson = senderAccountEntity.getPrivateKey();
        senderAccountName = senderAccountEntity.getAccountName();
        senderAddress = senderAccountEntity.getAddress();
        System.out.println("发货企业地址：" + senderAddress);
        System.out.println("发货企业账户：" + senderAccountJson);
        System.out.println("发货企业名称：" + senderAccountName);

        //收货企业注册
        String randomString1 = TestUtil.getRandomString();
        BaseResult<Object> result1 = accountService.register("account" + randomString1, //unique
                "123",
                "企业" + randomString1, //unique
                "1881881" + randomString1, //unique
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("收货企业注册返回结果：" + result1.toString());
        Assert.assertEquals(result1.getCode(), Code.SUCCESS.getCode());
        UserEntity receiverAccountEntity = userEntityRepository.findByAccountName("account" + randomString1);
        receiverAccountJson = receiverAccountEntity.getPrivateKey();
        receiverAccountName = receiverAccountEntity.getAccountName();
        receiverAddress = receiverAccountEntity.getAddress();
        System.out.println("收货企业地址：" + receiverAddress);
        System.out.println("收货企业账户：" + receiverAccountJson);
        System.out.println("收货企业名称：" + receiverAccountName);

        //物流公司注册
        String randomString2 = TestUtil.getRandomString();
        BaseResult<Object> result2 = accountService.register("account" + randomString2, //unique
                "123",
                "企业" + randomString2, //unique
                "1881881" + randomString2, //unique
                1,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("物流公司注册返回结果：" + result2.toString());
        Assert.assertEquals(result2.getCode(), Code.SUCCESS.getCode());
        UserEntity logisticsAccountEntity = userEntityRepository.findByAccountName("account" + randomString2);
        logisticsAccountJson = logisticsAccountEntity.getPrivateKey();
        logisticsAccountName = logisticsAccountEntity.getAccountName();
        logisticsAddress = logisticsAccountEntity.getAddress();
        System.out.println("物流公司地址：" + logisticsAddress);
        System.out.println("物流公司名称：" + logisticsAccountName);
        System.out.println("物流公司账户：" + logisticsAccountJson);

        //发货仓储注册
        String randomString3 = TestUtil.getRandomString();
        BaseResult<Object> result3 = accountService.register("account" + randomString3, //unique
                "123",
                "企业" + randomString3, //unique
                "1881881" + randomString3, //unique
                2,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("发货仓储注册返回结果：" + result3.toString());
        Assert.assertEquals(result3.getCode(), Code.SUCCESS.getCode());
        UserEntity senderRepoAccountEntity = userEntityRepository.findByAccountName("account" + randomString3);
        senderRepoAddress = senderRepoAccountEntity.getAddress();
        System.out.println("发货仓储地址：" + senderRepoAddress);

        //收货仓储注册
        String randomString4 = TestUtil.getRandomString();
        BaseResult<Object> result4 = accountService.register("account" + randomString4, //unique
                "123",
                "企业" + randomString4, //unique
                "1881881" + randomString4, //unique
                2,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("收货仓储注册返回结果：" + result4.toString());
        Assert.assertEquals(result4.getCode(), Code.SUCCESS.getCode());
        UserEntity receiverRepoAccountEntity = userEntityRepository.findByAccountName("account" + randomString4);
        receiverRepoAddress = receiverRepoAccountEntity.getAddress();
        System.out.println("收货仓储地址：" + receiverRepoAddress);

    }

    @Test
    public void testWayBill() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {

        //卖家企业发货申请，生成未完善运单
        ContractKey requestContractKey = new ContractKey(senderAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + senderAccountName);
        String requestContractMethodName = "generateUnConfirmedWayBill";
        Object[] requestContractMethodParams = new Object[3];
        String random = TestUtil.getRandomString();
        Long[] requestLongs = new Long[3];
        requestLongs[0] = new Date().getTime(); //requestTime
        requestLongs[1] = new Long(100000); //productValue
        requestLongs[2] = new Long(1000); //productQuantity
        String[] requestSddrs = new String[5];
        requestSddrs[0] = logisticsAddress; //logisticsAddress
        requestSddrs[1] = senderAddress; //senderAddress
        requestSddrs[2] = receiverAddress; //receiverAddress
        requestSddrs[3] = receiverRepoAddress; //receiverRepoAddress
        requestSddrs[4] = senderRepoAddress; //senderRepoAddress
        String[] requestStrs = new String[5];
        requestStrs[0] = "123订单" + random; //orderNo
        requestStrs[1] = "productName"; //productName
        requestStrs[2] = "senderRepoCertNo"; //senderRepoCertNo
        requestStrs[3] = "receiverRepoBusinessNo"; //receiverRepoBusinessNo
        requestStrs[4] = requestStrs[0] + WayBillStatus.REQUESTING.getCode(); //statusTransId: orderNo + WayBillStatus
        requestContractMethodParams[1] = requestLongs;
        requestContractMethodParams[2] = requestSddrs;
        requestContractMethodParams[3] = requestStrs;
        String[] requestResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult = ContractUtil.invokeContract(requestContractKey, requestContractMethodName, requestContractMethodParams, requestResultMapKey);
        System.out.println("调用合约generateUnConfirmedWayBill返回code：" + requestContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, requestContractResult.getCode());

        //物流确认发货，生成完整运单
        ContractKey confirmContractKey = new ContractKey(logisticsAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + logisticsAccountName);
        String confirmContractMethodName = "generateWayBill";
        Object[] confirmContractMethodParams = new Object[4];
        confirmContractMethodParams[0] = "123订单" + random; //orderNo
        confirmContractMethodParams[1] = "123订单" + random + WayBillStatus.SENDING.getCode(); //statusTransId: orderNo + WayBillStatus
        confirmContractMethodParams[2] = "123运单" + random; //wayBillNo
        confirmContractMethodParams[3] = new Date().getTime(); //sendTime
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(confirmContractKey, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey);
        System.out.println("调用合约generateWayBill返回code：" + confirmContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());

        //获取所有用户相关运单的订单号列表
        ContractKey listOrderNoContractKey = new ContractKey(logisticsAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + logisticsAccountName);
        String listOrderNoContractMethodName = "listWayBillOrderNo";
        Object[] listOrderNoContractMethodParams = new Object[4];
        listOrderNoContractMethodParams[0] = "123订单" + random; //orderNo
        listOrderNoContractMethodParams[1] = "123订单" + random + WayBillStatus.SENDING.getCode(); //statusTransId: orderNo + WayBillStatus
        listOrderNoContractMethodParams[2] = "123运单" + random; //wayBillNo
        listOrderNoContractMethodParams[3] = new Date().getTime(); //sendTime
        String[] listOrderNoResultMapKey = new String[]{"orderNoList"};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult listOrderNoContractResult = ContractUtil.invokeContract(listOrderNoContractKey, listOrderNoContractMethodName, listOrderNoContractMethodParams, listOrderNoResultMapKey);
        System.out.println("调用合约listWayBillOrderNo返回code：" + listOrderNoContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, listOrderNoContractResult.getCode());
        Map<String, Object> listOrderNoResultValueMap = listOrderNoContractResult.getValueMap();
        String[] orderNoList = (String[])listOrderNoResultValueMap.get("orderNoList");
        for (int i= 0; i < orderNoList.length ; i++) {
            System.out.println("运单订单号" + i + " : " + orderNoList[i]);
        }

        //获取最后一个运单信息
//        ContractKey waybillContractKey = new ContractKey(logisticsAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + logisticsAccountName);
//        String waybillContractMethodName = "getWayBill";
//        Object[] waybillContractMethodParams = new Object[1];
//        waybillContractMethodParams[0] = "123订单" + random; //orderNo
//        String[] waybillResultMapKey = new String[]{"longs", "strs", "addrs", "logisticsInfo", "wayBillStatus"};
//        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
//        ContractResult waybillContractResult = ContractUtil.invokeContract(listOrderNoContractKey, listOrderNoContractMethodName, listOrderNoContractMethodParams, listOrderNoResultMapKey);
//        System.out.println("调用合约listWayBillOrderNo返回code：" + listOrderNoContractResult.getCode());
//        Assert.assertEquals(Code.SUCCESS, listOrderNoContractResult.getCode());
//        Map<String, Object> waybillResultValueMap = listOrderNoContractResult.getValueMap();
//        String[] orderNoList = (String[])valueMap.get("orderNoList");
    }

}
