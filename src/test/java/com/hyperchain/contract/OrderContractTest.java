package com.hyperchain.contract;

import cn.hyperchain.sdk.rpc.Transaction.Transaction;
import cn.hyperchain.sdk.rpc.returns.CompileReturn;
import com.alibaba.fastjson.JSON;
import com.hyperchain.ESDKConnection;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.exception.ESDKException;
import com.hyperchain.test.base.SpringBaseTest;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * by chenxiaoyang on 2017/4/5.
 */
public class OrderContractTest extends SpringBaseTest{

//    payerPublicKey:9f5650152700b609d8010ca0a7a717d58648aa52
//    payerPrivateKey:{"encrypted":"351197dfdadabe068072880a6c050c690118374fa98de53c1491abdf2ad16520","algo":"0x03","address":"9f5650152700b609d8010ca0a7a717d58648aa52","version":"2.0"}
//    payeePublicKey:9362e5b4b61c04ccdb99a8b14a5c7cb290b37fd7
//    payeePrivateKey:{"encrypted":"a9954bd3f73189eae0f8bd4dde4b94948b0dad382385e1b8a1e336df09f8239b","algo":"0x03","address":"9362e5b4b61c04ccdb99a8b14a5c7cb290b37fd7","version":"2.0"}




    //买方公私钥
    String payerPublicKey = "b792b2defcc31f77949049f1e8132211ac9cba27";
    String payerPrivateKey = "{\"address\":\"b792b2defcc31f77949049f1e8132211ac9cba27\",\"encrypted\":\"31ee2c6ce22903675672d98c125122c3b47d7b40359149bed7a34b32b2723ffa\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

    //卖方公私钥
    String payeePublicKey = "59a919333413f214131e1cb29a65b17ba42ffd86";
    String payeePrivateKey = "{\"address\":\"59a919333413f214131e1cb29a65b17ba42ffd86\",\"encrypted\":\"779bbf3e3779156f9b95928c1ac6ef9d1c3b2ffc72fc6f618e53cbee0fe1cc89\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

        @Test
    public void newAccount() throws Exception{

        List<String> keyInfos1 = ESDKUtil.newAccount();
        String publicKey1 = keyInfos1.get(0);
        String privateKey1 = keyInfos1.get(1);
        System.out.println("payerPublicKey:"+publicKey1);
        System.out.println("payerPrivateKey:"+privateKey1);


        List<String> keyInfos2 = ESDKUtil.newAccount();
        String publicKey2 = keyInfos2.get(0);
        String privateKey2 = keyInfos2.get(1);
        System.out.println("payeePublicKey:"+publicKey2);
        System.out.println("payeePrivateKey:"+privateKey2);
    }

    @Test
    public void addOrder() throws Exception {
        long unitPrice = 100;
        long prodNum = 100;
        long timeStamp = System.currentTimeMillis();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
        String orderId = "20170405205903117";
        String productName = "Apple";
        String payerRepo = "A仓储公司";
        String payerBank = "中国银行";
        String payerBankClss = "54321";
        String payerAccount = "55556777777";
        List<String> list= new ArrayList<>();
        list.add(orderId);
        list.add(productName);
        list.add(payerBank);
        list.add(payerBankClss);
        list.add(payerAccount);
        list.add(payerRepo);

        String funcName = "createOrder";
        Object[] params = new Object[7];
        params[0] = "59a919333413f214131e1cb29a65b17ba42ffd86";
        params[1] = unitPrice; //单价
        params[2] = prodNum; //
        params[3] = unitPrice * prodNum; //
        params[4] = list;
        params[5] = 0;
        params[6] = timeStamp;

        String methodName = "createOrder";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        ContractKey contractKey = new ContractKey(payerPrivateKey);


        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, params, resultMapKey, "order_reparo");
            Code code = contractResult.getCode();
            result.returnWithValue(code, orderId);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
//        return result;
//
//
//
//        Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params);
//        transaction.sign(payerPrivateKey, null);
//
//        String result = ESDKConnection.invokeContractMethod(transaction);
//        Assert.assertNotNull(result);
//        System.out.println("==================invoke result:================== " + result);
//        //返回值解码
//        List<Object> retDec;
        System.out.println("==================after decode result:==================" + JSON.toJSON(result));
    }

    @Test
    public void queryOrderDetailTest() throws Exception {
        String orderId = "20170405205903111";// new Date()为获取当前系统时间，也可使用当前时间戳
//
//        String funcName = "queryOrderDetail";
//        Object[] params = new Object[1];
//        params[0] = orderId;
//
//        Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params);
//        transaction.sign(payerPrivateKey, null);
//
//        String result = ESDKConnection.invokeContractMethod(transaction);
//        Assert.assertNotNull(result);
//        //System.out.println("==================invoke result:================== " + result);
//
//        //返回值解码
//        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
//        System.out.println("==================after decode result:==================" + retDecode);
//

        // 合约的公私钥
        ContractKey contractKey = new ContractKey(payerPrivateKey);
        String[] contractMethodReturns = new String[]{"address1", "address2", "string", "uint[]", "int", "int"};
        String contractMethodName = "queryOrderDetail";
        // 合约方法参数（公钥，角色代码，物流交换码）

        Object[] contractMethodParams = new Object[1];
        contractMethodParams[0] = orderId;

        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractMethodReturns, "order_reparo");
        BaseResult<Object> result = new BaseResult<>();
//         将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code1 = Code.fromInt(resultCode);
        System.out.println(resultCode);
        if(resultCode == 2001){
            result.returnWithoutValue(code1);

//            result.setCode(21);
//            result.setMessage("该订单编号不存在");
            System.out.println(JSON.toJSON(result));
        }

        else if(resultCode == 2002){
            result.setCode(22);
            result.setMessage("用户不可查询该订单详情");

            System.out.println(JSON.toJSON(result));
        }
        else {

            String  payerAccount =  (String)contractResult.getValueMap().get(contractMethodReturns[0]);
            String  payeeAccount =  (String)contractResult.getValueMap().get(contractMethodReturns[1]);
            List<String> partParams1 = (List<String>) contractResult.getValueMap().get(contractMethodReturns[2]);
            List<String> partParams2 = (List<String>) contractResult.getValueMap().get(contractMethodReturns[3]);
            String payingMethod = (String) contractResult.getValueMap().get(contractMethodReturns[4]);
            String orderState = (String)contractResult.getValueMap().get(contractMethodReturns[5]);

            String productPrice = partParams2.get(0);
            String productNum = partParams2.get(1);
            String totalPrice = partParams2.get(2);
            String timeStamp = partParams2.get(3);

            String orderId2 = partParams1.get(0);
            String productName = partParams1.get(1);
            String payerBank = partParams1.get(2);
            String payerBankClss = partParams1.get(3);
            String payerBankAccount = partParams1.get(4);

            Map<String, Object> resultMap = new HashedMap();

            resultMap.put("payerAccount", payerAccount);
            resultMap.put("payeeAccount", payeeAccount);
            resultMap.put("orderId", orderId2);
            resultMap.put("productName", productName);
            resultMap.put("payerBank", payerBank);
            resultMap.put("payerBankClss", payerBankClss);
            resultMap.put("payerBankAccount", payerBankAccount);
            resultMap.put("productPrice", Long.parseLong(productPrice)/100);
            resultMap.put("productNum", productNum);
            resultMap.put("totalPrice", Long.parseLong(totalPrice)/100);
            resultMap.put("timeStamp", timeStamp);
            resultMap.put("payingMethod", payingMethod);
            resultMap.put("orderState", orderState);

            result.setData(resultMap);
            result.setCode(contractResult.getCode().getCode());
            System.out.println(JSON.toJSON(result));
        }

    }

    @Test
    public void queryAllOrderListForPayerTest() throws Exception {
        String funcName = "queryAllOrderListForPayer";
        Object[] params = new Object[0];

        Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params, "order_reparo");
        transaction.sign(payerPrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        //System.out.println("==================invoke result:================== " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result, "order_reparo");
        System.out.println("==================after decode result:==================" + retDecode);

    }


    @Test
    public void queryAllOrderListForPayeeTest() throws Exception {
        String orderId = "20170405205903840";// new Date()为获取当前系统时间，也可使用当前时间戳

        String funcName = "queryAllOrderListForPayee";
        Object[] params = new Object[0];
        //params[0] = orderId;

        Transaction transaction = ESDKUtil.getTxHash(payeePublicKey, funcName, params, "order_reparo");
        transaction.sign(payeePrivateKey, null);

        String result = ESDKConnection.invokeContractMethod(transaction);
        Assert.assertNotNull(result);
        //System.out.println("==================invoke result:================== " + result);

        //返回值解码
        List<Object> retDecode = ESDKUtil.retDecode(funcName, result, "order_reparo");
        System.out.println("==================after decode result:==================" + retDecode);

    }



//
//    @Test //由买方进行确认时会返回22-订单仅允许卖方进行确认
//    public void orderConfirm() throws Exception {
//        String orderId = "20170405205903840";// new Date()为获取当前系统时间，也可使用当前时间戳
//
//        String funcName = "orderConfirm";
//        Object[] params = new Object[1];
//        params[0] = orderId; //+"92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";
//
//
//        Transaction transaction = ESDKUtil.getTxHash(payerPublicKey, funcName, params);
//        transaction.sign(payerPrivateKey, null);
//
//        String result = ESDKConnection.invokeContractMethod(transaction);
//        Assert.assertNotNull(result);
//        //System.out.println("==================invoke result:================== " + result);
//
//        //返回值解码
//        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
//        System.out.println("==================after decode result:==================" + retDecode);
//        Assert.assertEquals("22", retDecode.get(0));
//      }
//
//    @Test //由卖方进行确认时会返回0-成功
//    public void orderConfirm_2() throws Exception {
//        String orderId = "20170405205903840";// new Date()为获取当前系统时间，也可使用当前时间戳
//
//        String funcName = "orderConfirm";
//        Object[] params = new Object[1];
//        params[0] = orderId; //+"92a87ad2c26d80705cf1f0d7c0c1f6ecb140459e";
//
//
//        Transaction transaction = ESDKUtil.getTxHash(payeePublicKey, funcName, params);
//        transaction.sign(payeePrivateKey, null);
//
//        String result = ESDKConnection.invokeContractMethod(transaction);
//        Assert.assertNotNull(result);
//        //System.out.println("==================invoke result:================== " + result);
//
//        //返回值解码
//        List<Object> retDecode = ESDKUtil.retDecode(funcName, result);
//        System.out.println("==================after decode result:==================" + retDecode);
//        Assert.assertEquals("0", retDecode.get(0));
//    }
    @Test
    public void randomTest(){
            for (int i = 0; i < 10; i++){
                System.out.println(new Random().nextInt(900)+100);
            }
    }
}
