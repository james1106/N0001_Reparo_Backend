package com.hyperchain.service.impl;

import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.controller.vo.OrderOverVo;
import com.hyperchain.controller.vo.ReceOverVo;
import com.hyperchain.controller.vo.TransactionDetailVo;
import com.hyperchain.service.OrderService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangyue on 2017/4/9.
 */
@Service
public class OrderServiceImpl implements OrderService{

    @Override
    public BaseResult<Object> createOrder(ContractKey contractKey, Object[] contractParams,String orderId) {
        String methodName = "createOrder";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();


        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey);
            Code code = contractResult.getCode();
            result.returnWithValue(code, orderId);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;

    }

    @Override
    public BaseResult<Object> queryOrderDetail(ContractKey contractKey, Object[] contractParams) {

        String contractMethodName = "queryOrderDetail";
        String[] resultMapKey = new String[]{"address1", "address2", "string", "uint[]", "int", "int"};


        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }


        BaseResult<Object> result = new BaseResult<>();
        Map<String, Object> orderDetailMap = new HashMap();
//         将合约结果转化为接口返回数据
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        if(code == Code.ORDER_NOT_EXIST){
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.QEURY_ORDER_PERMISSION_DENIED){
            result.returnWithoutValue(code);
            return result;
        }
        String  payerAddress =  (String)contractResult.getValueMap().get(resultMapKey[0]);
        String  payeeAddress =  (String)contractResult.getValueMap().get(resultMapKey[1]);
        List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
        List<String> partParams2 = (List<String>) contractResult.getValueMap().get(resultMapKey[3]);
        String payingMethod = (String)contractResult.getValueMap().get(resultMapKey[4]);
        String orderState = (String)contractResult.getValueMap().get(resultMapKey[5]);

        String orderId = partParams1.get(0);
        String productName = partParams1.get(1);
        String payerBank = partParams1.get(2);
        String payerBankClss = partParams1.get(3);
        String payerAccount = partParams1.get(4);
        String payerRepo = partParams1.get(5);
        String payeeRepo = partParams1.get(6);
        String repoCertNo = partParams1.get(7);
        String repoBusinessNo = partParams1.get(8);

        long productUnitPrice = Long.parseLong(partParams2.get(0))/100;
        long productQuantity = Long.parseLong(partParams2.get(1));
        long productTotalPrice = Long.parseLong(partParams2.get(2))/100;
        String orderGenerateTime = partParams2.get(3);

        //以下为应收账款概要信息


        String receNo = partParams1.get(9);
        String receivingSide = partParams1.get(10);
        String payingSide = partParams1.get(11);
        String dueDate = partParams1.get(12);
        long receGenerateTime = Long.parseLong(partParams2.get(4));
        long receAmount = Long.parseLong(partParams2.get(5));
        long coupon = Long.parseLong(partParams2.get(6));
        int receLatestStatus = Integer.parseInt(partParams2.get(7));
        long receUpdateTime = Long.parseLong(partParams2.get(8));


        TransactionDetailVo txDetailVo = new TransactionDetailVo();
        ReceOverVo receOverVo = new ReceOverVo();

        txDetailVo.setPayerAddress(payerAddress);
        txDetailVo.setPayeeAddress(payeeAddress);
        txDetailVo.setPayingMethod(payingMethod);
        txDetailVo.setOrderState(orderState);
        txDetailVo.setProductUnitPrice(productUnitPrice);
        txDetailVo.setProductQuantity(productQuantity);
        txDetailVo.setProductTotalPrice(productTotalPrice);
        txDetailVo.setOrderGenerateTime(Long.parseLong(orderGenerateTime));
        txDetailVo.setOrderId(orderId);
        txDetailVo.setProductName(productName);
        txDetailVo.setPayerBank(payerBank);
        txDetailVo.setPayerBankClss(payerBankClss);
        txDetailVo.setPayerAccount(payerAccount);
        txDetailVo.setPayeeRepo(payeeRepo);
        txDetailVo.setPayerRepo(payerRepo);
        txDetailVo.setRepoBusinessNo(repoBusinessNo);
        txDetailVo.setRepoCertNo(repoCertNo);


        receOverVo.setReceNo(receNo);
        receOverVo.setReceivingSide(receivingSide);
        receOverVo.setPayingSide(payingSide);
        receOverVo.setDueDate(dueDate);
        receOverVo.setReceGenerateTime(receGenerateTime);
        receOverVo.setReceAmount(receAmount);
        receOverVo.setCoupon(coupon);
        receOverVo.setReceLatestStatus(receLatestStatus);
        receOverVo.setReceUpdateTime(receUpdateTime);


        orderDetailMap.put("txDetail", txDetailVo);
        orderDetailMap.put("receOver", receOverVo);

        result.returnWithValue(code, orderDetailMap);
        return result;
    }

    @Override
    public BaseResult<Object> queryAllOrderOverViewInfoList(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "queryAllOrderOverViewInfoList";
        String[] resultMapKey = new String[]{"partList1", "partList2", "partList3", "methodList"};
        ContractResult contractResult = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        int resultCode = contractResult.getCode().getCode();
        Code code = Code.fromInt(resultCode);
        List<String> partList1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> partList2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> partList3 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
        List<String> methodList = (List<String>) contractResult.getValueMap().get(resultMapKey[3]);
        int length = methodList.size();
        List<OrderOverVo> orderOverVoList = new ArrayList<>();

        for(int i = 0; i < length; i++){
            OrderOverVo orderOverVo = new OrderOverVo();
            orderOverVo.setOrderNo(partList1.get(i*5));
            orderOverVo.setProductName(partList1.get(i*5+1));
            orderOverVo.setPayerRepo(partList1.get(i*5+2));
            orderOverVo.setPayerBank(partList1.get(i*5+3));
            orderOverVo.setPayerBankAccount(partList1.get(i*5+4));

            orderOverVo.setPayerAddress(partList2.get(i*2));
            orderOverVo.setPayeeAddress(partList2.get(i*2+1));

            orderOverVo.setProductQuantity(Long.parseLong(partList3.get(i*5)));
            orderOverVo.setProductUnitPrice(Long.parseLong(partList3.get(i*5+1)));

            orderOverVo.setProductTotalPrice(Long.parseLong(partList3.get(i*5+2)));
            orderOverVo.setOrderGenerateTime(Long.parseLong(partList3.get(i*5+3)));
            orderOverVo.setOrderConfirmTime(Long.parseLong(partList3.get(i*5+4)));

            orderOverVo.setPayingMethod(methodList.get(i));
            orderOverVoList.add(orderOverVo);
        }
        BaseResult<Object> result = new BaseResult<>();
        result.returnWithValue(code, orderOverVoList);

        return result;
    }

    @Override
    public BaseResult<Object> confirmOrder(ContractKey contractKey, Object[] contractParams) {
        String methodName = "confirmOrder";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();
        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey);
            Code code = contractResult.getCode();
            result.returnWithoutValue(code);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;

    }

}
