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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

        //从数据库中查询卖方公司对应的地址，如果查询不到数据，返回卖方公司名称未注册
        UserEntity payeeUserEntity = userEntityRepository.findByCompanyName(payeeCompanyName);
        if(CommonUtil.isEmpty(payeeUserEntity)){

            code = Code.COMPANY_NOT_BE_REGISTERED;
            result.returnWithoutValue(code);
            return  result;
        }
        String payerPrivateKey = payerUserEntity.getPrivateKey();
        String payeeAddress = payeeUserEntity.getAddress();


        //从数据库中查询卖方公司对应的地址，如果查询不到数据，返回仓储机构名称未注册
        UserEntity payerRepoEntity = userEntityRepository.findByCompanyName(payerRepo);
        if(CommonUtil.isEmpty(payeeUserEntity)){

            code = Code.COMPANY_NOT_BE_REGISTERED;
            result.returnWithoutValue(code);
            return  result;
        }
        try{

        }
        catch (Exception e){
            e.printStackTrace();
        }
        String payerRepoPrivateKey = payerUserEntity.getPrivateKey();
        String payerRepoAddress = payerRepoEntity.getAddress();

        long orderGenerateTime = System.currentTimeMillis();
        String orderId = "100" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (new Random().nextInt(900)+100);
        String txSerialNo = orderId + "00";
        String repoBusinessNo = "130" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ (new Random().nextInt(900)+100);

        //String privatekey = "{\"address\":\"c841cff583353b651b98fdd9ab72ec3fac98fac4\",\"encrypted\":\"07bb12934457f512c8e2ad82ed70ff88cca94a0f52dbb04af50ba56cf3f22d0b\",\"version\":\"2.0\",\"algo\":\"0x03\"}";
        ContractKey contractKey = new ContractKey(payerRepoPrivateKey);
        Object[] params = new Object[10];
        params[0] = repoBusinessNo;
        params[1] = repoBusinessNo + "0"; //仓储业务流转号
        params[2] = orderId;
        params[3] = payerAddress; // 存活人
        params[4] = payerRepoAddress; //保管人
        params[5] = orderGenerateTime; //操作时间
        params[6] = productName; //  仓储物名称
        params[7] =   productQuantity;     //  仓储物数量
        params[8] =    productUnitPrice;     //  货品单价(分)
        params[9] =      productTotalPrice ;    //  货品合计金额(分)
        // 调用合约查询账户，获取返回结果
        return repositoryService.incomeApply(contractKey, params, repoBusinessNo);
    }

    @LogInterceptor
    @ApiOperation(value = "回复入库申请", notes = "仓储机构回复是否同意企业的入库申请")
    @ResponseBody
    @RequestMapping(value = "incomeApplyResponse",method = RequestMethod.POST)
    public BaseResult<Object> incomeApplyResponse(
            @ApiParam(value = "仓储业务编号", required = true) @RequestParam String repoBusinessNo
     ) throws Exception {
        //String methodName = "getRepoBusinessDetail";
        BaseResult result = new BaseResult();
        Code code;
        long operateTime = System.currentTimeMillis();

        String privatekey = "{\"address\":\"c841cff583353b651b98fdd9ab72ec3fac98fac4\",\"encrypted\":\"07bb12934457f512c8e2ad82ed70ff88cca94a0f52dbb04af50ba56cf3f22d0b\",\"version\":\"2.0\",\"algo\":\"0x03\"}";
        ContractKey contractKey = new ContractKey(privatekey);

        Object[] params = new Object[4];
        params[0] = repoBusinessNo;
        params[1] = repoBusinessNo + "0"; //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        params[2] = repoBusinessNo + "1"; //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
        params[3] = operateTime; //操作时间

        // 调用合约查询账户，获取返回结果
        return repositoryService.incomeApplyResponse(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "查询仓储详情+仓储流转历史", notes = "查询仓储详情+仓储流转历史")
    @ResponseBody
    @RequestMapping(value = "getRepoBusiHistoryList",method = RequestMethod.POST)
    public BaseResult<Object> getRepoBusiHistoryList(
            @ApiParam(value = "仓储业务编号", required = true) @RequestParam String repoBusinessNo
    ) throws Exception {


        String privatekey = "{\"address\":\"c841cff583353b651b98fdd9ab72ec3fac98fac4\",\"encrypted\":\"07bb12934457f512c8e2ad82ed70ff88cca94a0f52dbb04af50ba56cf3f22d0b\",\"version\":\"2.0\",\"algo\":\"0x03\"}";
        ContractKey contractKey = new ContractKey(privatekey);

        Object[] params = new Object[1];
        params[0] = repoBusinessNo;


        // 调用合约查询账户，获取返回结果
        return repositoryService.getRepoBusiHistoryList(contractKey, params);
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
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[0];
        // 调用合约查询账户，获取返回结果
        return repositoryService.getRepoCertInfoList(contractKey, params);
    }

}
