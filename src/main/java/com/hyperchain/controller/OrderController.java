package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.util.CommonUtil;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hyperchain.common.constant.BaseConstant.*;

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
            @ApiParam(value = "付款方式", required = true) @RequestParam int payingMethod,
            HttpServletRequest request
    ) throws Exception {
        BaseResult result = new BaseResult();
        Code code;

        //todo 之后address要从token中获取,如果查询不到数据，则返回无效用户
//        String payerAddress = "c841cff583353b651b98fdd9ab72ec3fac98fac4";
        String payerAddress = TokenUtil.getAddressFromCookie(request);//用户address
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

        //从数据库中查询买方指定的仓储公司对应的地址，如果查询不到数据，返回仓储机构名称未注册
        UserEntity payerRepoEntity = userEntityRepository.findByCompanyName(payerRepo);
        if(CommonUtil.isEmpty(payerRepoEntity)){
            code = Code.COMPANY_NOT_BE_REGISTERED;
            result.returnWithoutValue(code);
            return  result;
        }
        String payerRepoAddress = payerRepoEntity.getAddress();
        String payerAccountName = payerRepoEntity.getAddress();
        String payerPrivateKey = payerUserEntity.getPrivateKey();
        String payeeAddress = payeeUserEntity.getAddress();

        long orderGenerateTime = System.currentTimeMillis();
        String orderNo = "100" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (new Random().nextInt(900)+100);
        String txSerialNo = orderNo + "00";
        String repoBusinessNo = "130" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);
        String acctContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ACCOUNT);

        List<String> orderParamlist= new ArrayList<>();
        orderParamlist.add(orderNo);
        orderParamlist.add(productName);
        orderParamlist.add(repoBusinessNo);
        orderParamlist.add(payerBank);
        orderParamlist.add(payerBankClss);
        orderParamlist.add(payerAccount);
        orderParamlist.add(txSerialNo);

        ContractKey contractKey = new ContractKey(payerPrivateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + payerAccountName);

        Object[] orderParams = new Object[9];
        orderParams[0] = acctContractAddress;
        orderParams[1] = payeeAddress;
        orderParams[2] = payerRepoAddress;
        orderParams[3] = productUnitPrice*100; //单价
        orderParams[4] = productQuantity; //
        orderParams[5] = productTotalPrice*100; //
        orderParams[6] = orderParamlist;
        orderParams[7] = payingMethod;
        orderParams[8] = orderGenerateTime;
        // 调用合约查询账户，获取返回结果

        Object[] repoParams = new Object[11];
        String orderContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ORDER);
        repoParams[0] = orderContractAddress;
        repoParams[1] = repoBusinessNo;
        repoParams[2] = repoBusinessNo + REPO_BUSI_WATING_INCOME_RESPONSE; //仓储业务流转号
        repoParams[3] = orderNo;
        repoParams[4] = payerAddress; // 存货人
        repoParams[5] = payerRepoAddress; //保管人
        repoParams[6] = orderGenerateTime; //操作时间
        repoParams[7] = productName; //  仓储物名称
        repoParams[8] = productQuantity;     //  仓储物数量
        repoParams[9] = productUnitPrice*100;     //  货品单价(分)
        repoParams[10] = productTotalPrice*100;    //  货品合计金额(分)
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
            @ApiParam(value = "仓储公司", required = true) @RequestParam String payeeRepoName,
            @ApiParam(value = "仓单编号", required = true) @RequestParam String payeeRepoCertNo,
            HttpServletRequest request
    ) throws Exception {
//        String payeeAddress =  "d5911f79bb94b23b018e505add48c0748f510eac";
        String payeeAddress = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity payeeUserEntity = userEntityRepository.findByAddress(payeeAddress);
        String payeePrivateKey = payeeUserEntity.getPrivateKey();
        String payeeAccountName = payeeUserEntity.getAccountName();

        //从数据库中查询卖方指定的仓储公司对应的地址，如果查询不到数据，返回仓储机构名称未注册
        UserEntity payeeRepoEntity = userEntityRepository.findByCompanyName(payeeRepoName);
        if(CommonUtil.isEmpty(payeeRepoEntity)){
            BaseResult result = new BaseResult();
            result.returnWithoutValue(Code.COMPANY_NOT_BE_REGISTERED);
            return  result;
        }
        String payeeRepoAddress = payeeRepoEntity.getAddress();
        ContractKey contractKey = new ContractKey(payeePrivateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + payeeAccountName);

        long txConfirmTime = System.currentTimeMillis();
        String txSerialNo = orderNo + "01";
        String acctContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ACCOUNT);
        Object[] contractParams = new Object[6];
        contractParams[0] = acctContractAddress;
        contractParams[1] = orderNo;
        contractParams[2] = payeeRepoAddress;
        contractParams[3] = payeeRepoCertNo;
        contractParams[4] = txSerialNo;
        contractParams[5] = txConfirmTime;
        // 调用合约确认订单，获取返回结果
        BaseResult confirmOrderResult = orderService.confirmOrder(contractKey, contractParams);

        Object[] params_1 = new Object[1];
        params_1[0] = payeeRepoCertNo;
        BaseResult repobusiResult = repositoryService.getRepobusiNoByrepoCert(contractKey, params_1);


        //String repoCertNo = "131" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);
        String orderContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ORDER);
        Object[] repoParams = new Object[5];
        repoParams[0] = orderContractAddress;
        repoParams[1] = payeeRepoCertNo;
        repoParams[2] = (String)repobusiResult.getData() + REPO_BUSI_INCOMED;
        repoParams[3] = (String)repobusiResult.getData() + REPO_BUSI_WATING_OUTCOME;//
        repoParams[4] = txConfirmTime;

        /*String orderContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ORDER);
        Object[] repoParams = new Object[3];
        repoParams[0] = orderContractAddress;
        repoParams[1] = payeeRepoCertNo;
        repoParams[2] = txConfirmTime;*/

        // 调用合约查询账户，获取返回结果
        BaseResult outcomeResult = repositoryService.outcomeResponse(contractKey, repoParams);

        if(confirmOrderResult.getCode() != 0){
            return confirmOrderResult;
        }
        else if(outcomeResult.getCode() != 0){
            return outcomeResult;
        }
        else{
            BaseResult result = new BaseResult(Code.SUCCESS);
            return result;
        }
    }


    @LogInterceptor
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    @ResponseBody
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public BaseResult<Object> queryOrderDetail(
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo,
            HttpServletRequest request
    ) throws Exception {
        //todo 之后address要从token中获取,如果查询不到数据，则返回无效用户
//        String payerAddress = "c841cff583353b651b98fdd9ab72ec3fac98fac4";
        String address = TokenUtil.getAddressFromCookie(request);//用户address

        UserEntity payerUserEntity = userEntityRepository.findByAddress(address);

        String payerPrivateKey = payerUserEntity.getPrivateKey();
        String payerAccountName = payerUserEntity.getAccountName();
        String receAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_RECEIVABLE);
        String acctContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ACCOUNT);
        String wbillContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_WAYBILL);
        ContractKey contractKey = new ContractKey(payerPrivateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + payerAccountName);

        Object[] contractParams = new Object[4];
        contractParams[0] = receAddress;
        contractParams[1] = wbillContractAddress;
        contractParams[2] = acctContractAddress;
        contractParams[3] = orderNo;


        return orderService.queryOrderDetail(contractKey, contractParams);
    }

    @LogInterceptor
    @ApiOperation(value = "用户查询订单编号列表", notes = "用户查询订单编号列表")
    @ResponseBody
    @RequestMapping(value = "order_list/{role}",method = RequestMethod.GET)
    public BaseResult<Object> queryAllOrderList(
            @ApiParam(value = "公司角色", required = true) @PathVariable(value = "role") int companyRole,
            HttpServletRequest request
    ) throws Exception {
        //todo 之后address要从token中获取,如果查询不到数据，则返回无效用户
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if(CommonUtil.isEmpty(userEntity)){
            BaseResult result = new BaseResult();
            result.returnWithoutValue(Code.COMPANY_NOT_BE_REGISTERED);
            return  result;
        }
        String acctContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ACCOUNT);

        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] contractParams = new Object[2];
        contractParams[0] = acctContractAddress;
        contractParams[1] = companyRole;
        // 调用合约确认订单，获取返回结果
        return orderService.queryAllOrderOverViewInfoList(contractKey, contractParams);
    }

}


