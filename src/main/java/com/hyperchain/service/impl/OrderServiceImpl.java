package com.hyperchain.service.impl;

import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.controller.vo.OrderDetailVo;
import com.hyperchain.service.OrderService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        long productUnitPrice = Long.parseLong(partParams2.get(0))/100;
        long productQuantity = Long.parseLong(partParams2.get(1));
        long productTotalPrice = Long.parseLong(partParams2.get(2))/100;
        String orderGenerateTime = partParams2.get(3);

        String orderId = partParams1.get(0);
        String productName = partParams1.get(1);
        String payerBank = partParams1.get(2);
        String payerBankClss = partParams1.get(3);
        String payerAccount = partParams1.get(4);
        String payerRepo = partParams1.get(5);
        String payeeRepo = partParams1.get(6);
        String repoCertNo = partParams1.get(7);
        String repoBusinessNo = partParams1.get(8);

        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setPayerAddress(payerAddress);
        orderDetailVo.setPayeeAddress(payeeAddress);
        orderDetailVo.setPayingMethod(payingMethod);
        orderDetailVo.setOrderState(orderState);
        orderDetailVo.setProductUnitPrice(productUnitPrice);
        orderDetailVo.setProductQuantity(productQuantity);
        orderDetailVo.setProductTotalPrice(productTotalPrice);
        orderDetailVo.setOrderGenerateTime(orderGenerateTime);
        orderDetailVo.setOrderId(orderId);
        orderDetailVo.setProductName(productName);
        orderDetailVo.setPayerBank(payerBank);
        orderDetailVo.setPayerBankClss(payerBankClss);
        orderDetailVo.setPayerAccount(payerAccount);
        orderDetailVo.setPayeeRepo(payeeRepo);
        orderDetailVo.setPayerRepo(payerRepo);
        orderDetailVo.setRepoBusinessNo(repoBusinessNo);
        orderDetailVo.setRepoCertNo(repoCertNo);

        result.returnWithValue(code, orderDetailVo);
        return result;
    }

    @Override
    public BaseResult<Object> queryOrderList(ContractKey contractKey, Object[] contractParams, int role) {
        if(role == 0){
            String contractMethodName = "queryAllOrderListForPayee";
            String[] resultMapKey = new String[]{"orderList"};
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
            List<String> orderList = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);


        }
        return null;
    }

}
