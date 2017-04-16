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

    @Test
    public void testReceivable() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {
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
        String rate = "9.8";
        String[] conAndInv = new String[2];
        conAndInv[0] = "con";
        conAndInv[1] = "inv";
        String serialNo = "seNo";
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
        requestContractMethodParams[9] = conAndInv;
        requestContractMethodParams[10] = serialNo;
        requestContractMethodParams[11] = time;

        String[] requestResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult = ContractUtil.invokeContract(contractKey, requestContractMethodName, requestContractMethodParams, requestResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约签发申请返回code：" + requestContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, requestContractResult.getCode());

        //签发回复
        List<String> keyInfo2 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson2 = keyInfo2.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey2 = new ContractKey(accountJson2, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);

        String requestContractMethodName2 = "signOutReply";
        Object[] requestContractMethodParams2= new Object[7];
        String receivableNo2 = "rece";
        String replyerAcctId2 = "replyerAcctId";
        int response2 = 0;
        String serialNo2 = "serialNo";
        int time2 = 20170416;
        String accountContractAddress2 = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);//Account合约地址
        String wayBillContractAddress2 = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_WAYBILL);//wayBill合约地址

        requestContractMethodParams2[0] = receivableNo2;
        requestContractMethodParams2[1] = replyerAcctId2;
        requestContractMethodParams2[2] = response2;
        requestContractMethodParams2[3] = serialNo2;
        requestContractMethodParams2[4] = time2;
        requestContractMethodParams2[5] = accountContractAddress2;
        requestContractMethodParams2[6] = wayBillContractAddress2;


        String[] requestResultMapKey2 = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult2 = ContractUtil.invokeContract(contractKey2, requestContractMethodName2, requestContractMethodParams2, requestResultMapKey2, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约签发回复返回code：" + requestContractResult2.getCode());
        Assert.assertEquals(Code.SUCCESS, requestContractResult2.getCode());


//
//        //应收款列表
//        List<String> keyInfo1 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
//        String accountJson1 = keyInfo1.get(1); //含address 私钥
//
//        //账户信息存储到区块链
//        ContractKey contractKey1 = new ContractKey(accountJson1, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
//        String confirmContractMethodName = "receivableSimpleDeatilList";
//        Object[] confirmContractMethodParams = new Object[4];
//        confirmContractMethodParams[0] = 0; //orderNo
//        confirmContractMethodParams[1] = "pyer"; //statusTransId: orderNo + WayBillStatus
//        confirmContractMethodParams[2] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);
//        confirmContractMethodParams[3] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);
//        String[] confirmResultMapKey = new String[]{};
//        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
//        ContractResult confirmContractResult = ContractUtil.invokeContract(contractKey1, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
//        System.out.println("调用合约generateWayBill返回code：" + confirmContractResult.getCode());
//        System.out.println("调用合约签发申请返回list：" + confirmContractResult);
//        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());
    }
}
