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
import com.hyperchain.service.ReceivableService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by YanYufei on 2017/4/9.
 */
@Service
public class ReceivableServiceImpl implements ReceivableService{
    @Override
    public BaseResult<Object> signOutApply(ContractKey contractKey, Object[] contractParams, String receivableNo) {//第二个参数是给合约的参数
        String methodName = "signOutApply";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();


        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey);
            Code code = contractResult.getCode();//拿到合约返回的第一个code
            result.returnWithValue(code, receivableNo);
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }
        return result;//这个result是返回给前端的

    }

    @Override
    public BaseResult<Object> signOutReply(ContractKey contractKey, Object[] contractParams) {
        String methodName = "signOutReply";
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
        return result;//这个result是返回给前端的

    }

    @Override
    public BaseResult<Object> getReceivableAllInfo(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "getReceivableAllInfo";
        String[] resultMapKey = new String[]{"receivable[]", "name[]", "uint[]"};//给返回值取了个名称


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
        if(code == Code.INVALID_USER){
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PARAMETER_EMPTY){
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.RECEIVABLE_NOT_EXITS){
            result.returnWithoutValue(code);
            return result;
        }

        if(code == Code.PERMISSION_DENIED){
            result.returnWithoutValue(code);
            return result;
        }


        String  payerAddress =  (String)contractResult.getValueMap().get(resultMapKey[0]);//取的时候是已经去掉了第一个code的情况
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
}
