package com.hyperchain.contract;

import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.AccountStatus;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by ldy on 2017/4/9.
 */
public class AccountContractTest extends SpringBaseTest {

    @Test
    public void testAccount() throws GeneralSecurityException, IOException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //生成公私钥和用户地址
        List<String> keyInfo = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson = keyInfo.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String contractMethodName = "newAccount";
        Object[] contractMethodParams = new Object[10];
        String randomString = TestUtil.getRandomString();
        contractMethodParams[0] = "用户名" + randomString;
        contractMethodParams[1] = "企业名称" + randomString;
        contractMethodParams[2] = 0;
        contractMethodParams[3] = AccountStatus.VALID.getCode();
        contractMethodParams[4] = "certType";
        contractMethodParams[5] = "certNo";
        String[] acctIdList = new String[1];
        acctIdList[0] = "acctIds";
        contractMethodParams[6] = acctIdList;
        contractMethodParams[7] = "svcrClass";
        contractMethodParams[8] = "acctSvcr";
        contractMethodParams[9] = "acctSvcrName";
        String[] resultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, resultMapKey, BaseConstant.CONTRACT_NAME_ACCOUNT);
        System.out.println("调用合约newAccount返回code：" + contractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, contractResult.getCode());

        ContractKey contractKey1 = new ContractKey(accountJson, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String contractMethodName1 = "getAccount";
        Object[] contractMethodParams1 = new Object[0];
        String[] resultMapKey1 = new String[]{"accountName", "companyName", "roleCode", "accountStatus", "certType", "certNo", "acctId", "class", "acctSvcr", "acctSvcrName"};
        ContractResult contractResult1 = ContractUtil.invokeContract(contractKey1, contractMethodName1, contractMethodParams1, resultMapKey1, "ReparoAccount");
        System.out.println("调用合约getAccount返回：" + contractResult1.getValueMap());
        Assert.assertEquals(Code.SUCCESS, contractResult1.getCode());
    }

    @Test
    public void testRepeatedAccount() throws GeneralSecurityException, IOException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //生成公私钥和用户地址
        List<String> keyInfo = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson = keyInfo.get(1); //含address 私钥

        for (int i = 0; i < 2; i++) {
            //账户信息存储到区块链
            ContractKey contractKey = new ContractKey(accountJson, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
            String contractMethodName = "newAccount";
            Object[] contractMethodParams = new Object[10];
            String randomString = TestUtil.getRandomString();
            contractMethodParams[0] = "用户名" + randomString;
            contractMethodParams[1] = "企业名称" + randomString;
            contractMethodParams[2] = 0;
            contractMethodParams[3] = AccountStatus.VALID.getCode();
            contractMethodParams[4] = "certType";
            contractMethodParams[5] = "certNo";
            String[] acctIdList = new String[1];
            acctIdList[0] = "acctIds";
            contractMethodParams[6] = acctIdList;
            contractMethodParams[7] = "svcrClass";
            contractMethodParams[8] = "acctSvcr";
            contractMethodParams[9] = "acctSvcrName";
            String[] resultMapKey = new String[]{};
            // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, resultMapKey, BaseConstant.CONTRACT_NAME_ACCOUNT);
            System.out.println("调用合约newAccount返回code：" + contractResult.getCode());
            if (i == 0) {
                Assert.assertEquals(Code.SUCCESS, contractResult.getCode());
            } else {
                Assert.assertEquals(Code.ACCOUNT_ALREADY_EXIST, contractResult.getCode());
            }
        }
    }
}
