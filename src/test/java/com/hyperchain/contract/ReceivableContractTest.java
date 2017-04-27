package com.hyperchain.contract;

import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.AccountStatus;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.constant.WayBillStatus;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by YanYufei on 2017/4/13.
 */
public class ReceivableContractTest extends SpringBaseTest{

    //签发申请
    @Test
    public void testReceivableSignOutApply() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {
        List<String> keyInfo = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson = keyInfo.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);

        String requestContractMethodName = "signOutApply";
        Object[] requestContractMethodParams = new Object[12];
        String receNo = "rece";
        String orderNo = "orderNo";
        String signer = "pyee";
        String acctptr = "pyer";
        String pyee = "pyee";
        String pyer = "pyer";
        long isseAmt = 100000;
        long dueDt = 20170413;
        String rate = "9.8%";
        String[] conAndInvAndSerialNo = new String[3];
        conAndInvAndSerialNo[0] = "con";
        conAndInvAndSerialNo[1] = "inv";
        conAndInvAndSerialNo[2] = "serialNoSignOutApply";
        long time = 888888;

        requestContractMethodParams[0] = receNo;
        requestContractMethodParams[1] = orderNo;
        requestContractMethodParams[2] = signer;
        requestContractMethodParams[3] = acctptr;
        requestContractMethodParams[4] = pyee;
        requestContractMethodParams[5] = pyer;
        requestContractMethodParams[6] = isseAmt;
        requestContractMethodParams[7] = dueDt;
        requestContractMethodParams[8] = rate;
        requestContractMethodParams[9] = conAndInvAndSerialNo;
        requestContractMethodParams[10] = time;
        requestContractMethodParams[11] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);


        String[] requestResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult = ContractUtil.invokeContract(contractKey, requestContractMethodName, requestContractMethodParams, requestResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约签发申请返回code：" + requestContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, requestContractResult.getCode());
    }

    //签发回复
    @Test
    public void testReceivableSignOutReply() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        //签发回复
        List<String> keyInfo2 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson2 = keyInfo2.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey2 = new ContractKey(accountJson2, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);

        String requestContractMethodName2 = "signOutReply";
        Object[] requestContractMethodParams2 = new Object[7];
        String receivableNo2 = "rece";
        String replyerAcctId2 = "pyer";
        int response2 = 0;
        String serialNo2 = "serialNoSignOutReply";
        int time2 = 20170427;
        String orderContractAddress2 = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        String wayBillContractAddress2 = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_WAYBILL);//wayBill合约地址

        requestContractMethodParams2[0] = receivableNo2;
        requestContractMethodParams2[1] = replyerAcctId2;
        requestContractMethodParams2[2] = response2;
        requestContractMethodParams2[3] = serialNo2;
        requestContractMethodParams2[4] = time2;
        requestContractMethodParams2[5] = orderContractAddress2;
        requestContractMethodParams2[6] = wayBillContractAddress2;


        String[] requestResultMapKey2 = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult2 = ContractUtil.invokeContract(contractKey2, requestContractMethodName2, requestContractMethodParams2, requestResultMapKey2, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约签发回复返回code：" + requestContractResult2.getCode());
        Assert.assertEquals(Code.SUCCESS, requestContractResult2.getCode());
    }

    //贴现申请
    @Test
    public void testReceivableDiscountApply() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        //贴现申请
        List<String> keyInfo1 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson1 = keyInfo1.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey1 = new ContractKey(accountJson1, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String confirmContractMethodName = "discountApply";
        Object[] confirmContractMethodParams = new Object[7];
        confirmContractMethodParams[0] = "rece"; //receNo
        confirmContractMethodParams[1] = "pyee"; //applicantAcctId
        confirmContractMethodParams[2] = "discountReplyer";
        confirmContractMethodParams[3] = "serialNoDiscountApply";
        confirmContractMethodParams[4] = 201604;
        confirmContractMethodParams[5] = 10;
        confirmContractMethodParams[6] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(contractKey1, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约discountApply返回code：" + confirmContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());
    }

    //贴现回复
    @Test
    public void testReceivableDiscountReply() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        //贴现回复
        List<String> keyInfo1 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson1 = keyInfo1.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey1 = new ContractKey(accountJson1, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String confirmContractMethodName = "discountReply";
        Object[] confirmContractMethodParams = new Object[8];
        confirmContractMethodParams[0] = "rece"; //receNo
        confirmContractMethodParams[1] = "discountReplyer"; //applicantAcctId
        confirmContractMethodParams[2] = 0;
        confirmContractMethodParams[3] = "serialNoDiscountReply";
        confirmContractMethodParams[4] = 2016046;
        confirmContractMethodParams[5] = "NewReceDiscountReply";
        confirmContractMethodParams[6] = 6;
        confirmContractMethodParams[7] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(contractKey1, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约discountReply返回code：" + confirmContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());
    }

    //兑付
    @Test
    public void testReceivableCash() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        //兑付
        List<String> keyInfo1 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson1 = keyInfo1.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey1 = new ContractKey(accountJson1, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String confirmContractMethodName = "cash";
        Object[] confirmContractMethodParams = new Object[6];
        confirmContractMethodParams[0] = "rece"; //receNo
        confirmContractMethodParams[1] = 88; //applicantAcctId
        confirmContractMethodParams[2] = 2011;
        confirmContractMethodParams[3] = "serialNoCash";
        confirmContractMethodParams[4] = 0;
        confirmContractMethodParams[5] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(contractKey1, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约cash返回code：" + confirmContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());
    }

    //获取买卖方列表
    @Test
    public void testReceivableSimpleDetailList() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        //获取买卖方列表
        List<String> keyInfo1 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson1 = keyInfo1.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey1 = new ContractKey(accountJson1, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String confirmContractMethodName = "receivableSimpleDetailList";
        Object[] confirmContractMethodParams = new Object[4];
        confirmContractMethodParams[0] = 0; //orderNo
        confirmContractMethodParams[1] = "pyer"; //statusTransId: orderNo + WayBillStatus
        confirmContractMethodParams[2] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);
        confirmContractMethodParams[3] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(contractKey1, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约generateWayBill返回code：" + confirmContractResult.getCode());
        System.out.println("调用合约签发申请返回list：" + confirmContractResult);
        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());
    }

    @Test
    public void testGetReceivableAllInfoWithSerial() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        //获取买卖方列表
        List<String> keyInfo1 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson1 = keyInfo1.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey1 = new ContractKey(accountJson1, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String confirmContractMethodName = "getReceivableAllInfoWithSerial";
        Object[] confirmContractMethodParams = new Object[2];
        confirmContractMethodParams[0] = "rece"; //receNo
        confirmContractMethodParams[1] = "pyee"; //statusTransId: orderNo + WayBillStatus
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(contractKey1, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约getReceivableAllInfoWithSerial返回code：" + confirmContractResult.getCode());
        System.out.println("带有应收款流水信息的应收款更具体详情：" + confirmContractResult);
        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());
    }
}
