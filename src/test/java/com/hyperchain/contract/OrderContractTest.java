package com.hyperchain.contract;

import cn.hyperchain.common.log.LogUtil;
import cn.hyperchain.sdk.rpc.Transaction.Transaction;
import cn.hyperchain.sdk.rpc.returns.CompileReturn;
import com.alibaba.fastjson.JSON;
import com.hyperchain.ESDKConnection;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.common.util.CommonUtil;
import com.hyperchain.common.util.ReparoUtil;
import com.hyperchain.controller.vo.*;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.exception.ESDKException;
import com.hyperchain.test.base.SpringBaseTest;
import jxl.common.BaseUnit;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * by chenxiaoyang on 2017/4/5.
 */
public class OrderContractTest extends SpringBaseTest{
//    payerPublicKey:c841cff583353b651b98fdd9ab72ec3fac98fac4
//    payerPrivateKey:{"address":"c841cff583353b651b98fdd9ab72ec3fac98fac4","encrypted":"07bb12934457f512c8e2ad82ed70ff88cca94a0f52dbb04af50ba56cf3f22d0b","version":"2.0","algo":"0x03"}
//    payeePublicKey:d5911f79bb94b23b018e505add48c0748f510eac
//    payeePrivateKey:{"address":"d5911f79bb94b23b018e505add48c0748f510eac","encrypted":"3a08804861fc9ca30b1f54ca81984fe96eb29501f106bcac3787d56ab366c0e5","version":"2.0","algo":"0x03"}

    //买方公私钥
    String payerPublicKey = "b792b2defcc31f77949049f1e8132211ac9cba27";
    String payerPrivateKey = "{\"address\":\"b792b2defcc31f77949049f1e8132211ac9cba27\",\"encrypted\":\"31ee2c6ce22903675672d98c125122c3b47d7b40359149bed7a34b32b2723ffa\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

    //卖方公私钥
    String payeePublicKey = "59a919333413f214131e1cb29a65b17ba42ffd86";
    String payeePrivateKey = "{\"address\":\"59a919333413f214131e1cb29a65b17ba42ffd86\",\"encrypted\":\"779bbf3e3779156f9b95928c1ac6ef9d1c3b2ffc72fc6f618e53cbee0fe1cc89\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

    @Autowired
    private UserEntityRepository userEntityRepository;


    @Test
    public void ThisIsTheMethodModelTry() throws Exception {

        //1.获取contractKey：ContractKey contractKey = new ContractKey(调用者的私钥, ReparoUtil.getPasswordForPrivateKey(调用者的账户名));
        //2.创建好需要的 (String)contractMethodName  (Object)contractMethodParams[]  (String)contractReturnsMapKey[]参数列表；
        //3.调用ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractMethodReturns, BaseConstant.合约方法名)
        //  方法，得到contractResult结果
        //4.判断所得到的结果与自己想要的结果是否符合 or 输出得到的结果供检查

        //--1.1获取用户名
        //----1.1.1首先根据用户地址获取用户实体
        UserEntity payerUserEntity = userEntityRepository.findByAddress("59a919333413f214131e1cb29a65b17ba42ffd86"); //参数为 买家地址
        //----1.1.2调用用户实体的getAccountName()方法获取用户账户名
        String payerAccountName = payerUserEntity.getAccountName();

        //1.2获取contractKey
        ContractKey contractKey = new ContractKey(payerPrivateKey, ReparoUtil.getPasswordForPrivateKey(payerAccountName));

        //2.1
        String contractMethodName = "updateOrderState";
        //2.2
        Object[] contractMethodParams = new Object[3];
        String orderNo = "100"+"20170405205903117"+"200";   //订单编号 21位
        String stateType = "";
        String newState = "";
        contractMethodParams[0] = orderNo;
        contractMethodParams[1] = stateType;
        contractMethodParams[2] = newState;
        //2.3若合约中的返回值有n个，则在String数组中填写n个名字，作为要查看的返回信息的key，对应合约中的各个返回值
        String[] contractReturnsMapKey = new String[]{};

        //3.调用
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKey, BaseConstant.CONTRACT_NAME_ORDER);

        //4.此处是选择打印出来返回信息
        System.out.println("调用OrderContract合约中的ThisIsTheMethodModelTry方法的返回code：" + contractResult.getCode());      //code为0表示成功
        System.out.println("调用OrderContract合约中的ThisIsTheMethodModelTry方法的返回content：" + contractResult.toString());  //把整个返回内容展现出来
    }

    //买家address： 0e1b81184266eaa1bbb19dabcefe78faeae11895
    //卖家address： f014bae4a69e0e4790214f52b6615a3a5e3d8c28
    //买家仓储address: 5cb0febe91a1c1a714cbf91a80bf51661f3b5ed6
    //卖家仓储address: eedddb31964b8896d795e1a0dbf1e854c3aab57f

    //单元测试--新增一个订单(默认为测试主体为买家)
    @Test
    public void addOrder() throws Exception {

        UserEntity payerUserEntity = userEntityRepository.findByAddress("0e1b81184266eaa1bbb19dabcefe78faeae11895"); //参数为 买家地址
        String payerPrivateKeyThis = payerUserEntity.getPrivateKey();
        String payerAccountName = payerUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(payerPrivateKeyThis, ReparoUtil.getPasswordForPrivateKey(payerAccountName));

        //2.1
        String contractMethodName = "createOrder";
        //2.2
        Object[] contractMethodParams = new Object[9];

        long unitPrice = 100;
        long prodNum = 100;
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
        String orderNo = "100"+"20170405205903117"+"200";   //订单编号 21位
        String txSerialNo = orderNo + "00";                 //交易序列号
        String productName = "Apple";
        String repoBusinessNo = "123";
        String payerBank = "中国银行";
        String payerBankClss = "54321";
        String payerAccount = "55556777777";
        List<String> list= new ArrayList<>();
        list.add(orderNo);
        list.add(productName);
        list.add(repoBusinessNo);
        list.add(payerBank);
        list.add(payerBankClss);
        list.add(payerAccount);
        list.add(txSerialNo);
        String accountAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);    //账户合约地址

        contractMethodParams[0] = accountAddr;                                  //账户合约地址
        contractMethodParams[1] = "f014bae4a69e0e4790214f52b6615a3a5e3d8c28";   //卖方address
        contractMethodParams[2] = "5cb0febe91a1c1a714cbf91a80bf51661f3b5ed6";   //买方仓储公司address
        contractMethodParams[3] = unitPrice;              //单价
        contractMethodParams[4] = prodNum;                //数量
        contractMethodParams[5] = unitPrice * prodNum;    //总价
        contractMethodParams[6] = list;                   //参数列表(内容见创建列表处)
        contractMethodParams[7] = 0;                      //付款方式(0代表应付账款方式，1代表现金)
        contractMethodParams[8] = timeStamp;              //生成订单时间


        //2.3若合约中的返回值有n个，则在String数组中填写n个名字，作为要查看的返回信息的key，对应合约中的各个返回值
        String[] contractReturnsMapKeys = new String[]{};


        //3.调用
        BaseResult result = new BaseResult();       //
        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKeys, BaseConstant.CONTRACT_NAME_ORDER);
            Code code = contractResult.getCode();
            result.returnWithValue(code, orderNo);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }

        System.out.println("==================after decode result:==================" + JSON.toJSON(result));
    }

    //单元测试--根据订单编号获取订单详情(默认为测试主体为买家)
    @Test
    public void queryOrderDetail() throws Exception {

        UserEntity payerUserEntity = userEntityRepository.findByAddress("0e1b81184266eaa1bbb19dabcefe78faeae11895"); //参数为 买家地址
        String payerPrivateKeyThis = payerUserEntity.getPrivateKey();
        String payerAccountName = payerUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(payerPrivateKeyThis, ReparoUtil.getPasswordForPrivateKey(payerAccountName));

        //2.1
        String contractMethodName = "queryOrderDetail";
        //2.2
        Object[] contractMethodParams = new Object[4];

        String receAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_RECEIVABLE);    //应收帐款合约地址
        String wbillAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_WAYBILL);      //运单合约地址
        String accountAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);    //账户合约地址
        String orderNo = "100"+"20170405205903117"+"200";   //订单编号

        contractMethodParams[0] = receAddr;
        contractMethodParams[1] = wbillAddr;
        contractMethodParams[2] = accountAddr;
        contractMethodParams[3] = orderNo;
        //2.3
        String[] contractReturnsMapKey = new String[]{"resultAddress", "resultBytes32", "resultUint", "resultMethod", "txState"};

        //3.调用
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKey, BaseConstant.CONTRACT_NAME_ORDER);

        //4.检查
        BaseResult<Object> result = new BaseResult<>();
        //将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code1 = Code.fromInt(resultCode);

        if(resultCode == 2001){
            result.returnWithoutValue(code1);
            //result.setCode(21);
            //result.setMessage("该订单编号不存在");
            System.out.println(JSON.toJSON(result));
        }
        else if(resultCode == 2002){
            result.setCode(22);
            result.setMessage("用户不可查询该订单详情");

            System.out.println(JSON.toJSON(result));
        }
        else {

            List<String> addressList = (List<String>)contractResult.getValueMap().get(contractReturnsMapKey[0]);
            List<String> partParams1 = (List<String>)contractResult.getValueMap().get(contractReturnsMapKey[1]);
            List<String> partParams2 = (List<String>)contractResult.getValueMap().get(contractReturnsMapKey[2]);
            String payingMethodString = (String)contractResult.getValueMap().get(contractReturnsMapKey[3]);
            String txStateString = (String)contractResult.getValueMap().get(contractReturnsMapKey[4]);

            int payingMethodInt = payingMethodString.equals("") ? 0 : Integer.parseInt(payingMethodString);
            int txStateInt = txStateString.equals("")? 0 : Integer.parseInt(txStateString);

            String payerAddress = addressList.get(0).substring(1);
            String payeeAddress = addressList.get(1).substring(1);
            String payerRepoAddress = addressList.get(2).equals("")? "" : addressList.get(2).substring(1);
            String payeeRepoAddress = addressList.get(3).equals("x0000000000000000000000000000000000000000")? "" : addressList.get(3).substring(1);

            String payerCompanyName = userEntityRepository.findByAddress(payerAddress).getCompanyName();
            String payeeCompanyName = userEntityRepository.findByAddress(payeeAddress).getCompanyName();
            String payerRepoName = payerRepoAddress.equals("") ? "" : userEntityRepository.findByAddress(payerRepoAddress).getCompanyName();
            String payeeRepoName = payeeRepoAddress.equals("") ? "" : userEntityRepository.findByAddress(payeeRepoAddress).getCompanyName();

            //获取交易信息
            String orderId = partParams1.get(0);
            String productName = partParams1.get(1);
            String payerBank = partParams1.get(2);
            String payerBankClss = partParams1.get(3);
            String payerAccount = partParams1.get(4);

            //以下为仓储详情
            String payerRepoBusinessNo = partParams1.get(5);//买家仓储流水号
            String payeeRepoBusinessNo = partParams1.get(6);//卖家仓储流水号
            String payerRepoCertNo = partParams1.get(7);    //买家仓单编号
            String payeeRepoCertNo = partParams1.get(8);    //卖家仓单编号

            long productUnitPrice = Long.parseLong(partParams2.get(0))/100;
            long productQuantity = Long.parseLong(partParams2.get(1));
            long productTotalPrice = Long.parseLong(partParams2.get(2))/100;
            long orderGenerateTime = Long.parseLong(partParams2.get(3));
            long orderComfirmTime = partParams2.get(4).equals("") ? 0:Long.parseLong(partParams2.get(4));
            int payerRepoBusiState = partParams2.get(5).equals("") ? 0: Integer.parseInt(partParams2.get(5));
            int payeeRepoBusiState = partParams2.get(6).equals("") ? 0: Integer.parseInt(partParams2.get(6));

            //物流信息详情
            String wayBillNo = partParams1.get(9);//物流单号
            String logisticCompany = addressList.get(4);//物流公司

            long wayBillGenerateTime = partParams2.get(7).equals("") ? 0 : Long.parseLong(partParams2.get(7));
            int wayBillLatestStatus = partParams2.get(8).equals("") ? 0 : Integer.parseInt(partParams2.get(8));
            long wayBillUpdateTime = partParams2.get(9).equals("") ? 0 : Long.parseLong(partParams2.get(9));

//        以下为应收账款概要信息

            String receNo = partParams1.get(10);
            String receivingSide = partParams1.get(11);
            String payingSide = partParams1.get(12);
            long coupon = partParams1.get(13).equals("") ? 0 : Long.parseLong(partParams1.get(13));

            long receGenerateTime = partParams2.get(10).equals("") ? 0 : Long.parseLong(partParams2.get(10));
            long receAmount = partParams2.get(11).equals("") ? 0 : Long.parseLong(partParams2.get(11));
            int receLatestStatus = partParams2.get(12).equals("") ? 0 : Integer.parseInt(partParams2.get(12));
            long receUpdateTime = partParams2.get(13).equals("") ? 0 : Long.parseLong(partParams2.get(13));
            long dueDate = partParams2.get(14).equals("") ? 0 : Long.parseLong(partParams2.get(14));

            TransactionDetailVo txDetailVo = new TransactionDetailVo();
            List<OperationRecordVo> txRecordList = new ArrayList<>();


            txRecordList.add(new OperationRecordVo(1, orderGenerateTime));
            if(txStateInt == 2){
                txRecordList.add(new OperationRecordVo(txStateInt, orderComfirmTime));
            }

            txDetailVo.setPayerCompanyName(payerCompanyName);
            txDetailVo.setPayeeCompanyName(payeeCompanyName);
            txDetailVo.setPayingMethod(payingMethodInt);
            txDetailVo.setProductUnitPrice(ReparoUtil.convertCentToYuan(productUnitPrice));
            txDetailVo.setProductQuantity(productQuantity);
            txDetailVo.setProductTotalPrice(ReparoUtil.convertCentToYuan(productTotalPrice));
            txDetailVo.setOrderId(orderId);
            txDetailVo.setOperationRecordVoList(txRecordList);
            txDetailVo.setProductName(productName);
            txDetailVo.setPayerBank(payerBank);
            txDetailVo.setPayerBankClss(payerBankClss);
            txDetailVo.setPayerAccount(payerAccount);
            txDetailVo.setPayeeRepo(payeeRepoName);
            txDetailVo.setPayerRepo(payerRepoName);
            txDetailVo.setPayeeRepoBusinessNo(payeeRepoBusinessNo);
            txDetailVo.setPayerRepoBusinessNo(payerRepoBusinessNo);
            txDetailVo.setPayeeRepoCertNo(payeeRepoCertNo);
            txDetailVo.setPayerRepoCertNo(payerRepoCertNo);

            ReceOverVo receOverVo = new ReceOverVo();
            receOverVo.setReceNo(receNo);
            receOverVo.setReceivingSide(receivingSide);
            receOverVo.setPayingSide(payingSide);
            receOverVo.setDueDate(dueDate);
            receOverVo.setReceGenerateTime(receGenerateTime);
//            receOverVo.setReceAmount(receAmount);
            receOverVo.setCoupon(coupon);
            receOverVo.setReceLatestStatus(receLatestStatus);
            receOverVo.setReceUpdateTime(receUpdateTime);

            WayBillOverInfo wayBillOverInfo = new WayBillOverInfo();
            wayBillOverInfo.setLogisticCompany(logisticCompany);
            wayBillOverInfo.setWayBillGenerateTime(wayBillGenerateTime);
            wayBillOverInfo.setWayBillLatestStatus(wayBillLatestStatus);
            wayBillOverInfo.setWayBillNo(wayBillNo);
            wayBillOverInfo.setWayBillUpdateTime(wayBillUpdateTime);

            RepoOverVo repoOverVo = new RepoOverVo();
            repoOverVo.setPayerRepoCertNo(payerRepoCertNo);
            repoOverVo.setPayeeRepoCertNo(payeeRepoCertNo);
            repoOverVo.setPayerRepoBusinessNo(payerRepoBusinessNo);
            repoOverVo.setPayeeRepoBusinessNo(payeeRepoBusinessNo);
            repoOverVo.setInApplyTime(orderGenerateTime);
            repoOverVo.setOutApplyTime(orderComfirmTime);
            repoOverVo.setPayeeRepoBusiState(payeeRepoBusiState);
            repoOverVo.setPayerRepoBusiState(payerRepoBusiState);


            Map<String, Object> orderDetailMap = new HashMap();
            orderDetailMap.put("txDetail", txDetailVo);
            orderDetailMap.put("receOver", receOverVo);
            orderDetailMap.put("wayBillOver", wayBillOverInfo);
            orderDetailMap.put("repoOver", repoOverVo);

            result.returnWithValue(contractResult.getCode(), orderDetailMap);

            System.out.println(JSON.toJSON(result));
        }

    }

    //单元测试--查询所有的订单概要信息(默认为测试主体为买家)
    @Test
    public void queryAllOrderOverViewInfoListTest1() throws Exception {
        UserEntity payerUserEntity = userEntityRepository.findByAddress("0e1b81184266eaa1bbb19dabcefe78faeae11895"); //参数为 买家地址
        String payerPrivateKeyThis = payerUserEntity.getPrivateKey();
        String payerAccountName = payerUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(payerPrivateKeyThis, ReparoUtil.getPasswordForPrivateKey(payerAccountName));

        //2.1
        String contractMethodName = "queryAllOrderOverViewInfoList";
        //2.2
        Object[] contractMethodParams = new Object[2];
        String acctContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);    //账户合约地址
        String companyRole = "0";   //0代表买家，1代表卖家
        contractMethodParams[0] = acctContractAddress;
        contractMethodParams[1] = companyRole;
        //2.3
        String[] contractReturnsMapKeys = new String[]{"partList1", "partList2", "partList3", "methodList", "stateList"};

        //3.
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKeys, BaseConstant.CONTRACT_NAME_ORDER);

        BaseResult<Object> result = new BaseResult<>();

        int resultCode = contractResult.getCode().getCode();
        Code code1 = Code.fromInt(resultCode);
        //打印查询结果的成功与否
        System.out.println(resultCode);

        List<String> partList1 = (List<String>)contractResult.getValueMap().get(contractReturnsMapKeys[0]);
        List<String> partList2 = (List<String>)contractResult.getValueMap().get(contractReturnsMapKeys[1]);
        List<String> partList3 = (List<String>)contractResult.getValueMap().get(contractReturnsMapKeys[2]);
        List<String> methodList = (List<String>)contractResult.getValueMap().get(contractReturnsMapKeys[3]);
        List<String> stateList = (List<String>)contractResult.getValueMap().get(contractReturnsMapKeys[4]);

        int length = methodList.size();
        List<OrderOverVo> orderOverVoList = new ArrayList<>();

        for(int i = 0; i < length; i++){
            OrderOverVo orderOverVo = new OrderOverVo();

            orderOverVo.setOrderNo(partList1.get(i*4));
            orderOverVo.setProductName(partList1.get(i*4+1));
            orderOverVo.setPayerBank(partList1.get(i*4+2));
            orderOverVo.setPayerBankAccount(partList1.get(i*4+3));

            String payerAddress = partList2.get(i*4).substring(1);
            String payeeAddress = partList2.get(i*4+1).substring(1);
            String payerRepoAddress = partList2.get(i*4+2).equals("x0000000000000000000000000000000000000000") ? "" : partList2.get(i*4+2).substring(1);
            String payeeRepoAddress = partList2.get(i*4+3).equals("x0000000000000000000000000000000000000000") ? "" : partList2.get(i*4+3).substring(1);
            String payerCompanyName = userEntityRepository.findByAddress(payerAddress).getCompanyName();
            String payeeCompanyName = userEntityRepository.findByAddress(payeeAddress).getCompanyName();
            String payerRepoName = payerRepoAddress.equals("") ? "" : userEntityRepository.findByAddress(payerRepoAddress).getCompanyName();
            String payeeRepoName = payeeRepoAddress.equals("") ? "" : userEntityRepository.findByAddress(payeeRepoAddress).getCompanyName();
            int iiii = 0;//别管这句话，任何用都没有

            orderOverVo.setPayerCompanyName(payerCompanyName);
            orderOverVo.setPayeeCompanyName(payeeCompanyName);
            orderOverVo.setPayerRepoName(payerRepoName);
            orderOverVo.setPayeeRepoName(payeeRepoName);

            orderOverVo.setProductQuantity(Long.parseLong(partList3.get(i*5)));
            orderOverVo.setProductUnitPrice(ReparoUtil.convertCentToYuan(Long.parseLong(partList3.get(i*5+1))/100));

            long orderConfirmTime = partList3.get(i*5+4).equals("")? 0 : Long.parseLong(partList3.get(i*5+4));
            orderOverVo.setProductTotalPrice(ReparoUtil.convertCentToYuan(Long.parseLong(partList3.get(i*5+2))/100));
            orderOverVo.setOrderGenerateTime(Long.parseLong(partList3.get(i*5+3)));
            orderOverVo.setOrderConfirmTime(orderConfirmTime);

            int txState = stateList.get(i*4).equals("")? 0 : Integer.parseInt(stateList.get(i*4));
            int repoState = stateList.get(i*4+1).equals("")? 0 : Integer.parseInt(stateList.get(i*4+1));
            int wayState = stateList.get(i*4+2).equals("")? 0 : Integer.parseInt(stateList.get(i*4+2));
            int receState = stateList.get(i*4+3).equals("")? 0 : Integer.parseInt(stateList.get(i*4+3));

            orderOverVo.setReceStatus(receState);
            orderOverVo.setRepoStatus(repoState);
            orderOverVo.setTransactionStatus(txState);
            orderOverVo.setWayBillStatus(wayState);
            int payingMethod = methodList.get(i).equals("")? 0 : Integer.parseInt(methodList.get(i));
            orderOverVo.setPayingMethod(payingMethod);
            orderOverVoList.add(orderOverVo);
        }


        result.returnWithValue(contractResult.getCode(),orderOverVoList);
        System.out.println(JSON.toJSON(result));

    }

    //单元测试--测试确认订单方法(默认为测试主体为卖家)
    @Test
    public void confirmOrderTest() throws Exception {
        UserEntity payeeUserEntity = userEntityRepository.findByAddress("f014bae4a69e0e4790214f52b6615a3a5e3d8c28"); //参数为 卖家地址
        String payeePrivateKeyThis = payeeUserEntity.getPrivateKey();
        String payeeAccountName = payeeUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(payeePrivateKeyThis, ReparoUtil.getPasswordForPrivateKey(payeeAccountName));

        //2.1
        String contractMethodName = "confirmOrder";
        //2.2
        Object[] contractMethodParams = new Object[6];

        UserEntity payeeRepoEntity = userEntityRepository.findByCompanyName("卖家仓储007");//参数为 仓储公司名字查询
        String acctContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);    //账户合约地址
        String orderNo = "100"+"20170405205903117"+"200";   //订单编号 21位
        String payeeRepoAddress = payeeRepoEntity.getAddress();
        String payeeRepoCertNo = ""; //(由swagger生成，暂无)
        String txSerialNo = orderNo + "01";
        long orderConfirmTime = System.currentTimeMillis();

        contractMethodParams[0] = acctContractAddress;
        contractMethodParams[1] = orderNo;
        contractMethodParams[2] = payeeRepoAddress;
        contractMethodParams[3] = payeeRepoCertNo;
        contractMethodParams[4] = txSerialNo;
        contractMethodParams[5] = orderConfirmTime;

        //2.3
        String[] contractReturnsMapKeys = new String[]{};

        //3.+4.
        BaseResult result = new BaseResult();
        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKeys, BaseConstant.CONTRACT_NAME_ORDER);
            Code code = contractResult.getCode();
            result.returnWithoutValue(code);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }

        System.out.println("==================after confirmOrder result:==================" + JSON.toJSON(result));
    }


    @Test
    public void randomTest(){
        for (int i = 0; i < 10; i++){
            System.out.println(new Random().nextInt(900)+100);
        }
    }


//    @Test
//    public void newAccount() throws Exception{
//
//        List<String> keyInfos1 = ESDKUtil.newAccount();
//        String publicKey1 = keyInfos1.get(0);
//        String privateKey1 = keyInfos1.get(1);
//        System.out.println("payerPublicKey:"+publicKey1);
//        System.out.println("payerPrivateKey:"+privateKey1);
//
//
//        List<String> keyInfos2 = ESDKUtil.newAccount();
//        String publicKey2 = keyInfos2.get(0);
//        String privateKey2 = keyInfos2.get(1);
//        System.out.println("payeePublicKey:"+publicKey2);
//        System.out.println("payeePrivateKey:"+privateKey2);
//    }


}
