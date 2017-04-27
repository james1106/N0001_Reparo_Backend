/*
package com.hyperchain.contract;
import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.AccountStatus;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.hyperchain.common.constant.BaseConstant.CONTRACT_NAME_REPOSITORY;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_INCOMED;
import static com.hyperchain.contract.ContractUtil.invokeContract;

*/
/**
 * Created by chenxiaoyang on 2017/4/26.
 *//*

public class RepositoryContractTest extends SpringBaseTest {
    @Test
    public void createRepoCertForRepoEnter() throws GeneralSecurityException, IOException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam{
        //生成公私钥和用户地址
        List<String> keyInfo = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
        String accountJson = keyInfo.get(1); //含address 私钥

        //账户信息存储到区块链
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
        String contractMethodName = "createRepoCertForRepoEnter";
        String contractName = CONTRACT_NAME_REPOSITORY;
        long    operateOperateTime = System.currentTimeMillis();  //  操作时间(时间戳)

        Object[] params = new Object[9];
        params[0] = "1302017111111000";//repoBusinessNo;
        params[1] = "1312017111111000";//repoCertNo;
        params[2] = "1302017111111000" +REPO_BUSI_INCOMED;//businessTransNo;
        params[3] = "";//"storerAddress";
        params[4] = "";//"repoEnterpriseAddress";
        params[5] = "";//"measureUnit";
        params[6] = "productName";
        params[7] = "productLocation";

        long[] productIntInfo = new long[4];

        productIntInfo[0] = 100;
        productIntInfo[1] = 2 * 100;//金额以分为单位
        productIntInfo[2] = 200 * 100;
        productIntInfo[3] = operateOperateTime;
        params[8] = productIntInfo;
        //repositoryService.createRepoCertForRepoeEnterprise(contractKey, params);
        String methodName = "createRepoCertForRepoEnter";
        LogUtil.info("调用合约 : RepositoryContract 方法: createRepoCertForRepoEnter 入参：" + params.toString());
        String[] resultMapKey = new String[]{""};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = invokeContract(contractKey, methodName, params, resultMapKey, CONTRACT_NAME_REPOSITORY);
            LogUtil.info("调用合约 : RepositoryContract 方法: createRepoCertForRepoEnter 返回结果：" + contractResult.toString());
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
    }
}
*/
