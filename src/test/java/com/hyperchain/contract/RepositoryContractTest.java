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

import static com.hyperchain.common.constant.BaseConstant.CONTRACT_NAME_REPOSITORY;

/**
 * Created by chenxiaoyang on 2017/4/26.
 */
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
    }
}
