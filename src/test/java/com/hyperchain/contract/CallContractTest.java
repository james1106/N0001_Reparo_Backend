package com.hyperchain.contract;

import cn.hyperchain.sdk.rpc.returns.CompileReturn;
import com.hyperchain.ESDKConnection;
import com.hyperchain.ESDKUtil;
import com.hyperchain.exception.ESDKException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * by chenyufeng on 2017/3/31 .
 */
public class CallContractTest {

    public static void main(String[] args) {

    }

    @Test
    public void compileContract() throws IOException, ESDKException {
        //从文件中读取合约
        CompileReturn compileReturn = ESDKConnection.compileContract();
        Assert.assertNotNull(compileReturn);
        String abiStr = compileReturn.getAbi().get(0);
        String binStr = compileReturn.getBin().get(0);
        Assert.assertNotNull(abiStr);
        System.out.println("abi: " + abiStr);
        Assert.assertNotNull(binStr);
        System.out.println("bin: " + binStr);
    }

    @Test
    public void deployContract() throws Exception {
        List<String> keyInfos = ESDKUtil.newAccount("123");
        System.out.println(keyInfos);
        String contractAddress = ESDKConnection.deployContract(keyInfos.get(0), keyInfos.get(1), "123");
        Assert.assertNotNull(contractAddress);
        System.out.println("contractAddress: " + contractAddress);
    }
}
