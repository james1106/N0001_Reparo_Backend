package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.util.CommonUtil;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liangyue on 2017/4/7.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Order", description = "订单", position = 1)
@Controller
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    private AccountEntityRepository accountEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    OrderService orderService;


    @LogInterceptor
    @ApiOperation(value = "添加订单", notes = "添加订单")
    @ResponseBody
    @RequestMapping(value = "creation",method = RequestMethod.POST)
    public BaseResult<Object> createOrder(
            @ApiParam(value = "卖方公司名称", required = true) @RequestParam String payeeCompanyName,
            @ApiParam(value = "货品名称", required = true) @RequestParam String productName,
            @ApiParam(value = "货品单价", required = true) @RequestParam long productUnitPrice,
            @ApiParam(value = "货品数量", required = true) @RequestParam long productQuantity,
            @ApiParam(value = "货品总价", required = true) @RequestParam long productTotalPrice,
            @ApiParam(value = "付款人申请仓储公司", required = true) @RequestParam String payerRepo,
            @ApiParam(value = "付款人开户行", required = true) @RequestParam String payerBank,
            @ApiParam(value = "付款账户", required = true) @RequestParam String payerAccount,
            @ApiParam(value = "付款方式", required = true) @RequestParam int payingMethod
    ) throws Exception {
        BaseResult result = new BaseResult();
        Code code;

        //todo 之后address要从token中获取,如果查询不到数据，则返回无效用户
        String payerAddress = "c841cff583353b651b98fdd9ab72ec3fac98fac4";

        UserEntity payerUserEntity = userEntityRepository.findByAddress(payerAddress);
        if(CommonUtil.isEmpty(payerUserEntity)){
            code = Code.INVALID_USER;
            result.returnWithoutValue(code);
            return result;
        }
        //查询开户行号，如果用户无该银行账号,则返回用户无该银行账号
//        List<AccountEntity> accountList = accountEntityRepository.findByAddress(payerAddress);
//        boolean isAccountRight = false;
//        String payerBankClss = "";
//        for(AccountEntity accountEntity : accountList){
//            if(accountEntity.getAcctId() == payerAccount){
//                isAccountRight = true;
//                payerBankClss = accountEntity.getSvcrClass();
//                break;
//            }
//        }
//        if(!isAccountRight){
//            code = Code.BANKACCOUNT_NOT_EXIST;
//            result.returnWithoutValue(code);
//            return result;
//        }
        String payerBankClss = "";

        //从数据库中查询卖方公司对应的地址，如果查询不到数据，返回卖方公司名称未注册
        UserEntity payeeUserEntity = userEntityRepository.findByCompanyName(payeeCompanyName);
        if(CommonUtil.isEmpty(payeeUserEntity)){

            code = Code.COMPANY_NOT_BE_REGISTERED;
            result.returnWithoutValue(code);
            return  result;
        }
        String payerPrivateKey = payerUserEntity.getPrivateKey();
        String payeeAddress = payeeUserEntity.getAddress();

        long orderGenerateTime = System.currentTimeMillis();
        String orderNo = "100" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (new Random().nextInt(900)+100);
        String txSerialNo = orderNo + "00";
        String repoBusinessNo = "130" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);
        String acctContractAddress = ESDKUtil.getHyperchainInfo("AccountContract");

        List<String> orderParamlist= new ArrayList<>();
        orderParamlist.add(orderNo);
        orderParamlist.add(productName);
        orderParamlist.add(payerRepo);
        orderParamlist.add(repoBusinessNo);
        orderParamlist.add(payerBank);
        orderParamlist.add(payerBankClss);
        orderParamlist.add(payerAccount);
        orderParamlist.add(txSerialNo);

        ContractKey contractKey = new ContractKey(payerPrivateKey);
        Object[] orderParams = new Object[8];
        orderParams[0] = acctContractAddress;
        orderParams[1] = payeeAddress;
        orderParams[2] = productUnitPrice*100; //单价
        orderParams[3] = productQuantity; //
        orderParams[4] = productTotalPrice*100; //
        orderParams[5] = orderParamlist;
        orderParams[6] = payingMethod;
        orderParams[7] = orderGenerateTime;
        // 调用合约查询账户，获取返回结果


        //从数据库中查询卖方公司对应的地址，如果查询不到数据，返回仓储机构名称未注册
        UserEntity payerRepoEntity = userEntityRepository.findByCompanyName(payerRepo);
        if(CommonUtil.isEmpty(payeeUserEntity)){
            code = Code.COMPANY_NOT_BE_REGISTERED;
            result.returnWithoutValue(code);
            return  result;
        }
        String payerRepoAddress = payerRepoEntity.getAddress();
        Object[] repoParams = new Object[10];
        repoParams[0] = repoBusinessNo;
        repoParams[1] = repoBusinessNo + "0"; //仓储业务流转号
        repoParams[2] = orderNo;
        repoParams[3] = payerAddress; // 存货人
        repoParams[4] = payerRepoAddress; //保管人
        repoParams[5] = orderGenerateTime; //操作时间
        repoParams[6] = productName; //  仓储物名称
        repoParams[7] =   productQuantity;     //  仓储物数量
        repoParams[8] =    productUnitPrice;     //  货品单价(分)
        repoParams[9] =      productTotalPrice ;    //  货品合计金额(分)
        // 调用合约查询账户，获取返回结果

        BaseResult createOrderResult = orderService.createOrder(contractKey, orderParams, orderNo);
        BaseResult createRepoResult = repositoryService.incomeApply(contractKey, repoParams, repoBusinessNo);
        if(createOrderResult.getCode() != 0){
            return createOrderResult;
        }
        else if(createRepoResult.getCode() != 0){
            return createRepoResult;
        }

        else {
            BaseResult createResult = new BaseResult();
            Map<String, String> resultMap= new HashMap<>();
            resultMap.put("orderNo", (String)createOrderResult.getData());
            resultMap.put("repoBusinessNo", (String)createRepoResult.getData());
            createResult.returnWithValue(Code.SUCCESS, resultMap);
            return createResult;
        }
    }


    @LogInterceptor
    @ApiOperation(value = "确认订单", notes = "确认订单")
    @ResponseBody
    @RequestMapping(value = "confirmation",method = RequestMethod.POST)
    public BaseResult<Object> confirmOrder(
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo,
            @ApiParam(value = "仓储公司", required = true) @RequestParam String payeeRepo,
            @ApiParam(value = "仓单编号", required = true) @RequestParam String payeeRepoCertNo
    ) throws Exception {
        String payeeAddress1 = "3ebc9f7cfa20950b8b35524e491c56705c067400";
        String payeeAddress =  "d5911f79bb94b23b018e505add48c0748f510eac";
        UserEntity payeeUserEntity = userEntityRepository.findByAddress(payeeAddress);
        String payeePrivateKey = payeeUserEntity.getPrivateKey();

        ContractKey contractKey = new ContractKey(payeePrivateKey);
        long txConfirmTime = System.currentTimeMillis();
        String txSerialNo = orderNo + "01";
        String acctContractAddress = ESDKUtil.getHyperchainInfo("AccountContract");
        Object[] contractParams = new Object[6];
        contractParams[0] = acctContractAddress;
        contractParams[1] = orderNo;
        contractParams[2] = payeeRepo;
        contractParams[3] = payeeRepoCertNo;
        contractParams[4] = txSerialNo;
        contractParams[5] = txConfirmTime;
        // 调用合约确认订单，获取返回结果
        return orderService.confirmOrder(contractKey, contractParams);
    }


    @LogInterceptor
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    @ResponseBody
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public BaseResult<Object> queryOrderDetail(
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo
    ) throws Exception {
        //todo 之后address要从token中获取,如果查询不到数据，则返回无效用户
        String payerAddress = "c841cff583353b651b98fdd9ab72ec3fac98fac4";
        UserEntity payerUserEntity = userEntityRepository.findByAddress(payerAddress);

        String payerPrivateKey = payerUserEntity.getPrivateKey();
        String receAddress = ESDKUtil.getHyperchainInfo("ReceivableContract");
        String acctContractAddress = ESDKUtil.getHyperchainInfo("AccountContract");

        ContractKey contractKey = new ContractKey(payerPrivateKey);
        Object[] contractParams = new Object[3];
        contractParams[0] = acctContractAddress;
        contractParams[1] = receAddress;
        contractParams[2] = orderNo;

        return orderService.queryOrderDetail(contractKey, contractParams);
    }

    @LogInterceptor
    @ApiOperation(value = "用户查询订单编号列表", notes = "用户查询订单编号列表")
    @ResponseBody
    @RequestMapping(value = "order_list/{role}",method = RequestMethod.GET)
    public BaseResult<Object> queryAllOrderList(
            @ApiParam(value = "公司角色", required = true) @PathVariable(value = "role") int companyRole
    ) throws Exception {
        //todo 之后address要从token中获取,如果查询不到数据，则返回无效用户
        String payerAddress = "c841cff583353b651b98fdd9ab72ec3fac98fac4";
        UserEntity payeeUserEntity = userEntityRepository.findByAddress(payerAddress);
        String acctContractAddress = ESDKUtil.getHyperchainInfo("AccountContract");

        String payerPrivateKey = payeeUserEntity.getPrivateKey();
        ContractKey contractKey = new ContractKey(payerPrivateKey);
        Object[] contractParams = new Object[2];
        contractParams[0] = acctContractAddress;
        contractParams[1] = companyRole;
        // 调用合约确认订单，获取返回结果
        return orderService.queryAllOrderOverViewInfoList(contractKey, contractParams);
    }

}


