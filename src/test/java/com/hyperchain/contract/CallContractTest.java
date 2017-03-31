package com.hyperchain.contract;

import cn.hyperchain.sdk.rpc.Transaction.Transaction;
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

    @Test
    public void invokeAddUser() throws Exception {

        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        String funcName = "addUser";
        Object[] params = new Object[4];
        params[0] = 2;
        params[1] = "chenyufeng2";
        params[2] = "123456";
        params[3] = "110";
        Transaction transaction = ESDKUtil.getTxHash(publicKey, funcName, params);
        transaction.sign(privateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        System.out.println("invoke result: " + result);
    }

    @Test
    public void invokeQueryUser() throws Exception {

        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        String funcName = "queryUser";
        Object[] params = new Object[1];
        params[0] = 2;
        Transaction transaction = ESDKUtil.getTxHash(publicKey, funcName, params);
        transaction.sign(privateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        System.out.println("invoke result: " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("after decode result:" + retDecode);
    }
}
