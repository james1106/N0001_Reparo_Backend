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
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.RepositoryService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_INCOMED;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_WATING_INCOME;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_WATING_INCOME_RESPONSE;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_WATING_OUTCOME;
import static com.hyperchain.common.constant.BaseConstant.REPO_BUSI_OUTCOMED;

/**
 * Created by chenxiaoyang on 2017/4/11.
 */

@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Repository", description = "仓储管理", position = 1)
@Controller
@RequestMapping("/v1/repository")
public class RepositoryController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    RepositoryService repositoryService;



    @LogInterceptor
    @ApiOperation(value = "入库申请", notes = "生成仓储业务信息")
    @ResponseBody
    @RequestMapping(value = "creation",method = RequestMethod.POST)
    public BaseResult<Object> incomeApply(
            @ApiParam(value = "卖方公司名称", required = true) @RequestParam String payeeCompanyName,
            @ApiParam(value = "货品名称", required = true) @RequestParam String productName,
            @ApiParam(value = "货品单价", required = true) @RequestParam long productUnitPrice,
            @ApiParam(value = "货品数量", required = true) @RequestParam long productQuantity,
            @ApiParam(value = "货品总价", required = true) @RequestParam long productTotalPrice,
            @ApiParam(value = "付款人申请仓储公司", required = true) @RequestParam String payerRepo,
            @ApiParam(value = "付款人开户行", required = true) @RequestParam String payerBank,
            @ApiParam(value = "开户行别", required = true) @RequestParam String payerBankClss,
            @ApiParam(value = "付款账户", required = true) @RequestParam String payerAccount,
            @ApiParam(value = "付款方式", required = true) @RequestParam int payingMethod,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        /*
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey requestContractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        */
        BaseResult result = new BaseResult();
        Code code;
        //用户信息
        String payerAddress = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity payerUserEntity = userEntityRepository.findByAddress(payerAddress);
        if(CommonUtil.isEmpty(payerUserEntity)){
            code = Code.INVALID_USER;
            result.returnWithoutValue(code);
            return result;
        }
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

        long orderGenerateTime = System.currentTimeMillis();
        String orderId = "100" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (new Random().nextInt(900)+100);
        String txSerialNo = orderId + "00";
        String repoBusinessNo = "130" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);

        ContractKey contractKey = new ContractKey(payerPrivateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + payerAccountName);

        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");
        Object[] params = new Object[11];
        params[0] = orderContractAddress;
        params[1] = repoBusinessNo;
        //params[1] = repoBusinessNo + "0"; //仓储业务流转号
        params[2] = repoBusinessNo + REPO_BUSI_WATING_INCOME_RESPONSE;
        params[3] = orderId;
        params[4] = payerAddress; // 存活人
        params[5] = payerRepoAddress; //保管人
        params[6] = orderGenerateTime; //操作时间
        params[7] = productName; //  仓储物名称
        params[8] =   productQuantity;     //  仓储物数量
        params[9] =    productUnitPrice;     //  货品单价(分)
        params[10] =      productTotalPrice ;    //  货品合计金额(分)
        // 调用合约查询账户，获取返回结果
        return repositoryService.incomeApply(contractKey, params, repoBusinessNo);
    }

    @LogInterceptor
    @ApiOperation(value = "回复入库申请", notes = "仓储机构回复是否同意企业的入库申请")
    @ResponseBody
    @RequestMapping(value = "incomeApplyResponse",method = RequestMethod.POST)
    public BaseResult<Object> incomeApplyResponse(
            @ApiParam(value = "仓储业务编号", required = true) @RequestParam String repoBusinessNo,
            HttpServletRequest request
     ) throws Exception {

        BaseResult result = new BaseResult();
        Code code;
        long operateTime = System.currentTimeMillis();

        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");
        Object[] params = new Object[5];
        params[0] = orderContractAddress;
        params[1] = repoBusinessNo;
        params[2] = repoBusinessNo + REPO_BUSI_WATING_INCOME_RESPONSE; //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号+REPO_BUSI_WATING_INCOME_RESPONSE
        params[3] = repoBusinessNo + REPO_BUSI_WATING_INCOME; //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + REPO_BUSI_WATING_INCOME
        params[4] = operateTime; //操作时间

        // 调用合约查询账户，获取返回结果
        return repositoryService.incomeApplyResponse(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "入库确认", notes = "仓储机构进行入库确认")
    @ResponseBody
    @RequestMapping(value = "incomeConfirm",method = RequestMethod.PUT)
    public BaseResult<Object> incomeConfirm(
            @ApiParam(value = "仓储业务编号", required = true) @RequestParam String repoBusinessNo,
            HttpServletRequest request
    ) throws Exception {

        BaseResult result = new BaseResult();
        Code code;
        long operateTime = System.currentTimeMillis();

        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        String repoCertNo = "131" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);
        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");
        Object[] params = new Object[6];
        params[0] = orderContractAddress;
        params[1] = repoBusinessNo;
        params[2] = repoCertNo;
        params[3] = repoBusinessNo + REPO_BUSI_WATING_INCOME; //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        params[4] = repoBusinessNo + REPO_BUSI_INCOMED; //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + REPO_BUSI_INCOMED
        params[5] = operateTime; //操作时间

        // 调用合约查询账户，获取返回结果
        return repositoryService.incomeConfirm(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "仓储机构确认是否同意出库", notes = "仓储机构确认是否同意出库")
    @ResponseBody
    @RequestMapping(value = "outcomeResponse",method = RequestMethod.PUT)
    public BaseResult<Object> outcomeResponse(
            @ApiParam(value = "仓单编号", required = true) @RequestParam String repoCertNo,
            HttpServletRequest request
    ) throws Exception {

        BaseResult result = new BaseResult();
        Code code;
        long operateTime = System.currentTimeMillis();

        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        //String repoCertNo = "131" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);
        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");
        Object[] params = new Object[3];
        params[0] = orderContractAddress;
        params[1] = repoCertNo;
        params[2] = operateTime;
        /*
        params[3] = repoBusinessNo + REPO_BUSI_WATING_INCOME; //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        params[4] = repoBusinessNo + REPO_BUSI_INCOMED; //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + REPO_BUSI_INCOMED
        params[5] = operateTime; //操作时间
        */
        // 调用合约查询账户，获取返回结果
        return repositoryService.outcomeResponse(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "仓储机构-确认出库", notes = "仓储机构确认出库，置状态为已出库")
    @ResponseBody
    @RequestMapping(value = "outcomeConfirm",method = RequestMethod.PUT)
    public BaseResult<Object> outcomeConfirm(
            @ApiParam(value = "仓储业务编号", required = true) @RequestParam String repoBusinessNo,
            HttpServletRequest request
    ) throws Exception {

        BaseResult result = new BaseResult();
        Code code;
        long operateTime = System.currentTimeMillis();

        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        //String repoCertNo = "131" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);
        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");
        Object[] params = new Object[4];
        params[0] = orderContractAddress;
        params[1] = repoBusinessNo;
        params[2] = repoBusinessNo + REPO_BUSI_WATING_OUTCOME;
        params[3] = repoBusinessNo + REPO_BUSI_OUTCOMED;
        /*
        params[3] = repoBusinessNo + REPO_BUSI_WATING_INCOME; //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        params[4] = repoBusinessNo + REPO_BUSI_INCOMED; //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + REPO_BUSI_INCOMED
        params[5] = operateTime; //操作时间
        */
        // 调用合约查询账户，获取返回结果
        return repositoryService.outcomeConfirm(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "查询仓储详情+仓储流转历史", notes = "查询仓储详情+仓储流转历史")
    @ResponseBody
    @RequestMapping(value = "getRepoBusiHistoryList",method = RequestMethod.GET)
    public BaseResult<Object> getRepoBusiHistoryList(
            @ApiParam(value = "仓储业务编号", required = true) @RequestParam String repoBusinessNo,
            HttpServletRequest request
    ) throws Exception {


        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        Object[] params = new Object[1];
        params[0] = repoBusinessNo;


        // 调用合约查询账户，获取返回结果
        return repositoryService.getRepoBusiHistoryList(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "查询仓单详情", notes = "查询仓单详情")
    @ResponseBody
    @RequestMapping(value = "getRepoCert",method = RequestMethod.GET)
    public BaseResult<Object> getRepoCert(
            @ApiParam(value = "仓单编号", required = true) @RequestParam String repoCertNo,
            HttpServletRequest request
    ) throws Exception {
        //用户信息
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String accountJson = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        Object[] params = new Object[1];
        params[0] = repoCertNo;


        // 调用合约查询账户，获取返回结果
        return repositoryService.getRepoCertDetail(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "查询仓单信息列表", notes = "查询仓单信息列表")
    @ResponseBody
    @RequestMapping(value = "getRepoCertInfoList",method = RequestMethod.GET)
    public BaseResult<Object> getRepoCertInfoList(HttpServletRequest request) throws Exception {

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if(CommonUtil.isEmpty(userEntity)){
            BaseResult result = new BaseResult();
            result.returnWithoutValue(Code.COMPANY_NOT_BE_REGISTERED);
            return  result;
        }
//        String acctContractAddress = ESDKUtil.getHyperchainInfo("AccountContract");
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        String userAddress = userEntity.getAddress();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[1];
        params[0] = userAddress;
        // 调用合约查询账户，获取返回结果
        return repositoryService.getRepoCertInfoList(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "查询仓储列表", notes = "查询仓储列表,1-入库管理(仓储状态为1-入库待响应,2-待入库,3-已入库)，2-出库管理（5-待出库,6-已出库），3-仓储机构（不区分状态）")
    @ResponseBody
    @RequestMapping(value = "getRepoBusiList",method = RequestMethod.GET)
    public BaseResult<Object> getRepoBusiList(
            @ApiParam(value = "用户角色", required = true) @RequestParam int role,
            HttpServletRequest request) throws Exception {

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        if(CommonUtil.isEmpty(userEntity)){
            BaseResult result = new BaseResult();
            result.returnWithoutValue(Code.COMPANY_NOT_BE_REGISTERED);
            return  result;
        }
//        String acctContractAddress = ESDKUtil.getHyperchainInfo("AccountContract");
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[1];
        params[0] = address;

        //params[1] = role;
        // 调用合约查询账户，获取返回结果
        return repositoryService.getRepoBusiInfoList(contractKey, params,role);
    }

}
