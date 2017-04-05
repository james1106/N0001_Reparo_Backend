package com.hyperchain.contract;

import cn.hyperchain.sdk.rpc.Transaction.Transaction;
import cn.hyperchain.sdk.rpc.returns.CompileReturn;
import com.hyperchain.ESDKConnection;
import com.hyperchain.ESDKUtil;
import com.hyperchain.exception.ESDKException;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * by chenyufeng on 2017/4/5 .
 */
public class chenyufeng_reparo_Test{

    private static String applyerPublicKey = "77ca88f54040deab13dd86ae56b54b1392640fe5";
    private static String applyerPrivateKey = "{\"address\":\"77ca88f54040deab13dd86ae56b54b1392640fe5\",\"encrypted\":\"eab905b355cf943e5253a73fea0d84e9313e0755766054d1e81da041fcacae33\",\"version\":\"2.0\",\"algo\":\"0x03\"}";
    private static String receiverPublicKey = "a4c3137e463f0830b2691e1642d7bbb59e5d5850";
    private static String receiverPrivateKey = "{\"address\":\"a4c3137e463f0830b2691e1642d7bbb59e5d5850\",\"encrypted\":\"1acfea0fdba9d1efa1e289856c3e426f9bfebcdb9d5b9f014478fdb7fbcb644f\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

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
    public void invokeDiscountApply() throws Exception {

        List<String> applyerKeyInfos = ESDKUtil.newAccount();
        applyerPublicKey = applyerKeyInfos.get(0);
        applyerPrivateKey = applyerKeyInfos.get(1);

        List<String> receiverKeyInfos = ESDKUtil.newAccount();
        receiverPublicKey = receiverKeyInfos.get(0);
        receiverPrivateKey = receiverKeyInfos.get(1);

        String funcName = "discountApply";
        Object[] params = new Object[5];
        params[0] = receiverPublicKey;
        params[1] = "10001";
        params[2] = "12345678";
        params[3] = "1480000000";
        params[4] = "申请";
        Transaction transaction = ESDKUtil.getTxHash(applyerPublicKey, funcName, params);
        transaction.sign(applyerPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        System.out.println("invoke result: " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("after decode result:" + retDecode);
    }

    @Test
    public void invokeDiscountResponse() throws Exception {

        String funcName = "discountResponse";
        Object[] params = new Object[6];
        params[0] = "10001";
        params[1] = 1;
        params[2] = 1;
        params[3] = "12345678";
        params[4] = "1480000000";
        params[5] = "回复";
        Transaction transaction = ESDKUtil.getTxHash(receiverPublicKey, funcName, params);
        transaction.sign(receiverPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        System.out.println("invoke result: " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("after decode result:" + retDecode);
    }
}
