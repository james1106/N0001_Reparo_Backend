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

    /*@Test
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
    public void discountApply() throws Exception {

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

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("贴现申请返回:" + retDecode);
    }

    @Test
    public void discountResponse() throws Exception {

        String funcName = "discountResponse";
        Object[] params = new Object[5];
        params[0] = "10001";
        params[1] = 0;
        params[2] = "87654321";
        params[3] = "1480000000";
        params[4] = "回复";
        Transaction transaction = ESDKUtil.getTxHash(receiverPublicKey, funcName, params);
        transaction.sign(receiverPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("贴现回复返回:" + retDecode);
    }

    @Test
    public void queryPendingReceivables() throws Exception {

        String funcName = "queryPendingReceivables";
        Object[] params = new Object[0];
        Transaction transaction = ESDKUtil.getTxHash(receiverPublicKey, funcName, params);
        transaction.sign(receiverPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("查询待处理应收款返回:" + retDecode);
    }

    @Test
    public void queryAllReceivablMap() throws Exception {

        String funcName = "queryAllReceivablMap";
        Object[] params = new Object[0];
        Transaction transaction = ESDKUtil.getTxHash(applyerPublicKey, funcName, params);
        transaction.sign(applyerPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("申请人:" + retDecode);

        Transaction transaction1 = ESDKUtil.getTxHash(receiverPublicKey, funcName, params);
        transaction1.sign(receiverPrivateKey, null);

        String result1 = ESDKConnection.invokeContractMethod(transaction1);
        Assert.assertNotNull(result1);
        //返回值解码
        List<Object> retDecode1 = ESDKUtil.retDecode(funcName, result1);
        System.out.println("收款人:" + retDecode1);
    }

    @Test
    public void queryRecordDetailMap() throws Exception {

        String funcName = "queryRecordDetailMap";
        Object[] params = new Object[1];
        params[0] = "12345678";
        Transaction transaction = ESDKUtil.getTxHash(receiverPublicKey, funcName, params);
        transaction.sign(receiverPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("查询交易记录详情返回:" + retDecode);
    }

    @Test
    public void queryReceivableDetailMap() throws Exception {

        String funcName = "queryReceivableDetailMap";
        Object[] params = new Object[1];
        params[0] = "10001";
        Transaction transaction = ESDKUtil.getTxHash(receiverPublicKey, funcName, params);
        transaction.sign(receiverPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("查询应收款详情返回:" + retDecode);
    }*/
}
