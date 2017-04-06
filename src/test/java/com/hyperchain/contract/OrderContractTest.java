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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * by chenxiaoyang on 2017/4/5.
 */
public class OrderContractTest extends SpringBaseTest{

    //买方公私钥
    String payerPublicKey = "742ef389942f5c670337e35133dec96ed959fe7e";
    String payerPrivateKey = "{\"address\":\"742ef389942f5c670337e35133dec96ed959fe7e\",\"encrypted\":\"ddeb9fb8471f7cd23d519e8bc2ad7436b36cc1ad0d48c89804a8d0dee7534051\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

    //卖方公私钥
    String payeePublicKey = "92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";
    String payeePrivateKey = "{\"address\":\"92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e\",\"encrypted\":\"d19d6986f5b57e902768be3ebf7e1521fbc20b3fb19108848c4304c5fdb8216f\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

    /*List<String> keyInfos = ESDKUtil.newAccount();
        String publicKey = keyInfos.get(0);
        String privateKey = keyInfos.get(1);
        System.out.println("publicKey:"+publicKey);
        System.out.println("privateKey:"+privateKey);*/

    @Test
    public void addOrder() throws Exception {
        long unitPrice=100;
        long prodNum = 100;
        long timeStamp =  System.currentTimeMillis();

        java.util.Random random = new java.util.Random();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
        String orderId = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

        orderId = "20170405205903840";
        String funcName = "addOrder";
        Object[] params = new Object[7];
        params[0] = orderId; //+"92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";
        params[1] = "92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";
        params[2] = "product";
        params[3] = unitPrice; //单价
        params[4] = prodNum; //
        params[5] = unitPrice * prodNum; //
        params[6] = orderId; //

        Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params);
        transaction.sign(payerPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        //System.out.println("==================invoke result:================== " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("==================after decode result:==================" + retDecode);

        //System.out.println("===========addOrder 入参数 timeStamp:"+timeStamp +"===========");
        //System.out.println("===========addOrder 入参数 date:"+date + random.nextInt() +"===========");

        /*Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params);
        transaction.sign(payerPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        System.out.println("invoke result: " + result);*/

        /*String contractMethodName = "addOrder";
        String[] contractMethodReturns = new String[]{"result"};
        // 合约的公私钥
        ContractKey contractKey = new ContractKey(payerPrivateKey);
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, params, contractMethodReturns);*/
    }

    @Test
    public void queryOrderDetail() throws Exception {
        String orderId = "20170405205903840";// new Date()为获取当前系统时间，也可使用当前时间戳

        String funcName = "queryOrderDetail";
        Object[] params = new Object[1];
        params[0] = orderId; //+"92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";


        Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params);
        transaction.sign(payerPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        //System.out.println("==================invoke result:================== " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("==================after decode result:==================" + retDecode);

    }

    @Test //由买方进行确认时会返回22-订单仅允许卖方进行确认
    public void orderConfirm() throws Exception {
        String orderId = "20170405205903840";// new Date()为获取当前系统时间，也可使用当前时间戳

        String funcName = "orderConfirm";
        Object[] params = new Object[1];
        params[0] = orderId; //+"92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";


        Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params);
        transaction.sign(payerPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        //System.out.println("==================invoke result:================== " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("==================after decode result:==================" + retDecode);
        Assert.assertEquals("22", retDecode.get(0));
      }

    @Test //由卖方进行确认时会返回0-成功
    public void orderConfirm_2() throws Exception {
        String orderId = "20170405205903840";// new Date()为获取当前系统时间，也可使用当前时间戳

        String funcName = "orderConfirm";
        Object[] params = new Object[1];
        params[0] = orderId; //+"92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";


        Transaction transaction = ESDKUtil.getTxHash(payeePublicKey, funcName, params);
        transaction.sign(payeePrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        //System.out.println("==================invoke result:================== " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
        System.out.println("==================after decode result:==================" + retDecode);
        Assert.assertEquals("0", retDecode.get(0));
    }
}
