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
 * by chenyufeng on 2017/3/31 .
 */
public class ContractTestDemo extends SpringBaseTest{
    String Reparo = "Reparo";
    String contract1 = "contract1";
    String testContract = "testContract";

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

    /*@Test
    public void deployContract() throws Exception {
        List<String> keyInfos = ESDKUtil.newAccount("123");
        System.out.println(keyInfos);
        String contractAddress = ESDKConnection.deployContract(keyInfos.get(0), keyInfos.get(1), "123");
        Assert.assertNotNull(contractAddress);
        System.out.println("contractAddress: " + contractAddress);
    }*/

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
        Transaction transaction = ESDKUtil.getTxHash(publicKey, funcName, params,Reparo);
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
        Transaction transaction = ESDKUtil.getTxHash(publicKey, funcName, params,Reparo);
        transaction.sign(privateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        System.out.println("invoke result: " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result,Reparo);
        System.out.println("after decode result:" + retDecode);
    }

    @Test
    public void invokeUserController() throws Exception {

        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        // 合约方法参数
        Object[] contractParams = new Object[4];
        contractParams[0] = 1;
        contractParams[1] = "Jack";
        contractParams[2] = "123456";
        contractParams[3] = "110";

        String[] resultParams = new String[1];

        ContractResult contractResult = ContractUtil.invokeContract(contractKey, "addUser", contractParams, resultParams,Reparo);
        System.out.println("contractResult:" + contractResult);
    }

    @Test
    public void callContractByName() throws Exception {

        String contractAddress = ESDKUtil.getHyperchainInfo("contract1");
        Assert.assertNotNull(contractAddress);
        System.out.println("==address of contract1:"+contractAddress);

        String contractAddress2 = ESDKUtil.getHyperchainInfo("testContract");
        Assert.assertNotNull(contractAddress2);
        System.out.println("==address of testContract:"+contractAddress2);
    }

    @Test
    public void setMap() throws Exception {
        String testContract = "testContract";
        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        // 合约方法参数
        Object[] contractParams = new Object[2];
        contractParams[0] = "1";
        contractParams[1] = "111";


        String[] resultParams = new String[1];

        ContractResult contractResult = ContractUtil.invokeContract(contractKey, "setMap", contractParams, resultParams,testContract);
        System.out.println("contractResult:" + contractResult);
        //合约的方法名，参数以及顺序都需要与编写合约的同学约定
   /*     String funcName = "setMap";
        String contractName = "testContract";
        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);
        Object[] params = new Object[2];
        params[0] = 1;
        params[1] = 1111;
       // params[2] = 1;
//获取transaction对象后就可以进行签名了
        Transaction transaction = ESDKUtil.getTxHash(publicKey, funcName, params,contractName);
        transaction.sign(privateKey, null);
//准备工作做好后调用合约方法会返回一个hash，你百分之九十需要使用ESDK的解码方法对它进行解码，但这里我假设你知道
        String result = ESDKConnection.invokeContractMethod(transaction);
        System.out.println("invoke result: " + result);*/

    }

    @Test
    public void callOthenContract() throws Exception {
        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        String testContractAddress = ESDKUtil.getHyperchainInfo("testContract");
        Assert.assertNotNull(testContractAddress);
        //System.out.println("==address of testContract:"+contractAddress2);

        // 合约方法参数
        Object[] contractParams = new Object[2];

        contractParams[0] = testContractAddress;
        contractParams[1] = 1;


        String[] resultParams = new String[1];

        ContractResult contractResult = ContractUtil.invokeContract(contractKey, "callOthersContract2", contractParams, resultParams,"contract1");
        System.out.println("contractResult:" + contractResult);



    }

    @Test
    public void setv() throws Exception {
        String testContract = "testContract";
        List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(privateKey);

        // 合约方法参数
        Object[] contractParams = new Object[1];
        contractParams[0] = 123456;
        //contractParams[1] = "111";


        String[] resultParams = new String[1];

        ContractResult contractResult = ContractUtil.invokeContract(contractKey, "setV", contractParams, resultParams,testContract);
        System.out.println("contractResult:" + contractResult);

    }


}
