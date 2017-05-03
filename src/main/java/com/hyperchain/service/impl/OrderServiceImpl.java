package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.common.util.ReparoUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.*;
import com.hyperchain.controller.vo.OperationRecordVo;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.OrderService;
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

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public BaseResult<Object> createOrder(ContractKey contractKey, Object[] contractParams,String orderId) {
        String methodName = "createOrder";
        String[] resultMapKey = new String[]{};
        BaseResult result = new BaseResult();

        try {
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey, "OrderContract");
            Code code = contractResult.getCode();
            LogUtil.info("调用合约 : OrderContract 方法: createOrder()返回结果：" + contractResult.toString());
            if(code == Code.SUCCESS){
                result.returnWithValue(code, orderId);
            }
            else {
                result.returnWithoutValue(code);
            }

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
        String[] resultMapKey = new String[]{"address1[]", "bytesParams", "uintParams", "method", "txState"};


        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = null;
        Code code = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, "OrderContract");
            LogUtil.info("调用合约 : OrderContract 方法: queryOrderDetail 返回结果：" + contractResult.toString());
            code = contractResult.getCode();
            if(code != Code.SUCCESS){
                BaseResult result = new BaseResult();
                result.returnWithoutValue(code);
                return result;
            }
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
        List<String> addressList = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> partParams1 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> partParams2 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
        String payingMethodString = (String)contractResult.getValueMap().get(resultMapKey[3]);
        String txStateString = (String)contractResult.getValueMap().get(resultMapKey[4]);

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

        long productUnitPrice = Long.parseLong(partParams2.get(0));
        long productQuantity = Long.parseLong(partParams2.get(1));
        long productTotalPrice = Long.parseLong(partParams2.get(2));
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
        txDetailVo.setProductUnitPrice(ReparoUtil.convertCentToYuan(productUnitPrice));//changed
        txDetailVo.setProductQuantity(productQuantity);
        txDetailVo.setProductTotalPrice(ReparoUtil.convertCentToYuan(productTotalPrice));//changed
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
        receOverVo.setReceAmount(receAmount);
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

        orderDetailMap.put("txDetail", txDetailVo);
        orderDetailMap.put("receOver", receOverVo);
        orderDetailMap.put("wayBillOver", wayBillOverInfo);
        orderDetailMap.put("repoOver", repoOverVo);

        result.returnWithValue(code, orderDetailMap);
        return result;
    }

    @Override
    public BaseResult<Object> queryAllOrderOverViewInfoList(ContractKey contractKey, Object[] contractParams) {
        String contractMethodName = "queryAllOrderOverViewInfoList";
        String[] resultMapKey = new String[]{"partList1", "partList2", "partList3", "methodList", "stateList"};
        ContractResult contractResult = null;
        Code code = null;
        try {
            contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractParams, resultMapKey, "OrderContract");
            LogUtil.info("调用合约 : OrderContract 方法: queryAllOrderOverViewInfoList 返回结果：" + contractResult.toString());
            code = contractResult.getCode();
            if(code != Code.SUCCESS){
                BaseResult result = new BaseResult();
                result.returnWithoutValue(code);
                return result;
            }
        } catch (ContractInvokeFailException e) {
            e.printStackTrace();
        } catch (ValueNullException e) {
            e.printStackTrace();
        } catch (PasswordIllegalParam passwordIllegalParam) {
            passwordIllegalParam.printStackTrace();
        }

        List<String> partList1 = (List<String>) contractResult.getValueMap().get(resultMapKey[0]);
        List<String> partList2 = (List<String>) contractResult.getValueMap().get(resultMapKey[1]);
        List<String> partList3 = (List<String>) contractResult.getValueMap().get(resultMapKey[2]);
        List<String> methodList = (List<String>) contractResult.getValueMap().get(resultMapKey[3]);
        List<String> stateList = (List<String>) contractResult.getValueMap().get(resultMapKey[4]);
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
            String payerRepoAddress = partList2.get(i*4+2).equals("x0000000000000000000000000000000000000000")? "" : partList2.get(i*4+2).substring(1);
            String payeeRepoAddress = partList2.get(i*4+3).equals("x0000000000000000000000000000000000000000")? "" : partList2.get(i*4+3).substring(1);

            String payerCompanyName = userEntityRepository.findByAddress(payerAddress).getCompanyName();
            String payeeCompanyName = userEntityRepository.findByAddress(payeeAddress).getCompanyName();
            String payerRepoName = payerRepoAddress.equals("") ? "" : userEntityRepository.findByAddress(payerRepoAddress).getCompanyName();
            String payeeRepoName = payeeRepoAddress.equals("") ? "" : userEntityRepository.findByAddress(payeeRepoAddress).getCompanyName();

            orderOverVo.setPayerCompanyName(payerCompanyName);
            orderOverVo.setPayeeCompanyName(payeeCompanyName);
            orderOverVo.setPayerRepoName(payerRepoName);
            orderOverVo.setPayeeRepoName(payeeRepoName);

            orderOverVo.setProductQuantity(Long.parseLong(partList3.get(i*5)));
            orderOverVo.setProductUnitPrice(ReparoUtil.convertCentToYuan(Long.parseLong(partList3.get(i*5+1))));

            long orderConfirmTime = partList3.get(i*5+4).equals("")? 0 : Long.parseLong(partList3.get(i*5+4));
            orderOverVo.setProductTotalPrice(ReparoUtil.convertCentToYuan(Long.parseLong(partList3.get(i*5+2))));
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
            ContractResult contractResult = ContractUtil.invokeContract(contractKey, methodName, contractParams, resultMapKey, "OrderContract");
            LogUtil.info("调用合约 : OrderContract 方法: confirmOrder 返回结果：" + contractResult.toString());
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
