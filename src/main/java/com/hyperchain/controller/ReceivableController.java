package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YanYufei on 2017/4/9.
 */
@SuppressWarnings({"JavaDoc", "SpringAutowiredFieldsWarningInspection"})
@Api(value = "Receivable", description = "应收款", position = 1)
@Controller
@RequestMapping("/v1/receivable")
public class ReceivableController {
    @Autowired
    ReceivableService receivableService;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    AccountEntityRepository accountEntityRepository;

    @LogInterceptor
    @ApiOperation(value = "签发申请", notes = "签发申请")
    @ResponseBody
    @RequestMapping(value = "sign",method = RequestMethod.POST)//路径
    public BaseResult<Object> signOutApply(
            //@ApiParam(value = "收款人私钥", required = true) @RequestParam String pyeeAddress,//收款人即签发人
            @ApiParam(value = "订单编号", required = true) @RequestParam String orderNo,
            @ApiParam(value = "付款人账号", required = true) @RequestParam String pyer,//承兑人 = 付款人
            @ApiParam(value = "收款人账号", required = true) @RequestParam String pyee,//签发人 = 收款人
            @ApiParam(value = "票面金额", required = true) @RequestParam long isseAmt,
            @ApiParam(value = "到期日", required = true) @RequestParam long dueDt,
            @ApiParam(value = "带息利率", required = true) @RequestParam String rate,
            @ApiParam(value = "合同编号", required = true) @RequestParam String contractNo,
            @ApiParam(value = "发票号", required = true) @RequestParam String invoiceNo,
            HttpServletRequest request
    ) throws Exception {

        long receivableGenerateTime = System.currentTimeMillis();
        String receivableNo = "120" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//应收款编号
        String serialNo = "121" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//签发申请流水号121
        List<String> list= new ArrayList<>();
        list.add(contractNo);
        list.add(invoiceNo);

//        ContractKey contractKey = new ContractKey(pyeeAddress);

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);


        Object[] params = new Object[12];
        params[0] = receivableNo;
        params[1] = orderNo;
        params[2] = pyee;
        params[3] = pyer;
        params[4] = pyee;
        params[5] = pyer;
        params[6] = isseAmt;
        params[7] = dueDt;
        params[8] = rate;
        params[9] = list;
        params[10] = serialNo;
        params[11] = receivableGenerateTime;

        // 调用合约查询账户，获取返回结果
        return receivableService.signOutApply(contractKey, params, receivableNo);
    }

    @LogInterceptor
    @ApiOperation(value = "签发回复——承兑", notes = "签发回复")
    @ResponseBody
    @RequestMapping(value = "accept",method = RequestMethod.POST)//路径
    public BaseResult<Object> signOutReply(
            //@ApiParam(value = "回复人私钥", required = true) @RequestParam String replyerAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            @ApiParam(value = "回复人账号", required = true) @RequestParam String replyerAcctId,
            @ApiParam(value = "回复意见", required = true) @RequestParam int response,
            HttpServletRequest request
    ) throws Exception {

        long signOutReplyTime = System.currentTimeMillis();
        String serialNo = "122" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//签发回复流水号122

//        ContractKey contractKey = new ContractKey(replyerAddress);
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[5];
        params[0] = receivableNo;
        params[1] = replyerAcctId;
        params[2] = response;
        params[3] = serialNo;
        params[4] = signOutReplyTime;

        // 调用合约查询账户，获取返回结果
        return receivableService.signOutReply(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "贴现申请", notes = "贴现申请")
    @ResponseBody
    @RequestMapping(value = "discountApply",method = RequestMethod.POST)//路径
    public BaseResult<Object> discountApply(
            //@ApiParam(value = "申请人私钥", required = true) @RequestParam String applicantAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,//应收款编号
            @ApiParam(value = "申请人账号", required = true) @RequestParam String applicantAcctId,//申请人账号
            @ApiParam(value = "回复人账号", required = true) @RequestParam String replyerAcctId,//回复人账号
            @ApiParam(value = "申请贴现金额", required = true) @RequestParam long discountApplyAmount,//申请贴现金额
            HttpServletRequest request
    ) throws Exception {

        long discountApplyTime = System.currentTimeMillis();
        String serialNo = "123" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//贴现申请流水号123

//        ContractKey contractKey = new ContractKey(applicantAddress);
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[6];
        params[0] = receivableNo;
        params[1] = applicantAcctId;
        params[2] = replyerAcctId;
        params[3] = serialNo;
        params[4] = discountApplyTime;
        params[5] = discountApplyAmount;

        // 调用合约查询账户，获取返回结果
        return receivableService.discountApply(contractKey, params, receivableNo);
    }

//    @LogInterceptor
//    @ApiOperation(value = "贴现回复", notes = "贴现回复")
//    @ResponseBody
//    @RequestMapping(value = "discountReply",method = RequestMethod.POST)//路径
//    public BaseResult<Object> discountReply(
//            @ApiParam(value = "回复人私钥", required = true) @RequestParam String ReplyerAddress,
//            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,//应收款编号
//            @ApiParam(value = "申请人账号", required = true) @RequestParam String applicantAcctId,//申请人账号
//            @ApiParam(value = "回复人账号", required = true) @RequestParam String replyerAcctId,//回复人账号
//            @ApiParam(value = "申请贴现金额", required = true) @RequestParam String discountApplyAmount//申请贴现金额
//    ) throws Exception {
//
//        long discountApplyTime = System.currentTimeMillis();
//        String serialNo = "123" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//贴现申请流水号123
//
//        ContractKey contractKey = new ContractKey(applicantAddress);
//
//        Object[] params = new Object[6];
//        params[0] = receivableNo;
//        params[1] = applicantAcctId;
//        params[2] = replyerAcctId;
//        params[3] = serialNo;
//        params[4] = discountApplyTime;
//        params[5] = discountApplyAmount;
//
//        // 调用合约查询账户，获取返回结果
//        return receivableService.discountApply(contractKey, params, receivableNo);
//    }

    @LogInterceptor
    @ApiOperation(value = "根据应收款编号查应收款详情", notes = "根据应收款编号查应收款详情")
    @ResponseBody
    @RequestMapping(value = "receivableInfo",method = RequestMethod.POST)//路径
    public BaseResult<Object> getReceivableAllInfo(
            //@ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            @ApiParam(value = "操作人账号", required = true) @RequestParam String operatorAcctId,
            HttpServletRequest request//http请求实体
    ) throws Exception {
//        ContractKey contractKey = new ContractKey(operatorAddress);

        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[2];
        params[0] = receivableNo;
        params[1] = operatorAcctId;


        // 调用合约查询账户，获取返回结果
        return receivableService.getReceivableAllInfo(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "用户根据流水号查交易记录详情", notes = "用户根据流水号查交易记录详情")
    @ResponseBody
    @RequestMapping(value = "recordInfo",method = RequestMethod.POST)//路径
    public BaseResult<Object> getRecordBySerialNo(
            //@ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "流水号", required = true) @RequestParam String serialNo,
            HttpServletRequest request
    ) throws Exception {

//        ContractKey contractKey = new ContractKey(operatorAddress);
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[1];
        params[0] = serialNo;

        // 调用合约查询账户，获取返回结果
        return receivableService.getRecordBySerialNo(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "应收款历史操作流水号", notes = "应收款历史操作流水号")
    @ResponseBody
    @RequestMapping(value = "historySerialNo",method = RequestMethod.POST)//路径
    public BaseResult<Object> getReceivableHistorySerialNo(
            //@ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "应收款编号", required = true) @RequestParam String receivableNo,
            HttpServletRequest request
    ) throws Exception {

//        ContractKey contractKey = new ContractKey(operatorAddress);
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        Object[] params = new Object[1];
        params[0] = receivableNo;

        // 调用合约查询账户，获取返回结果
        return receivableService.getReceivableHistorySerialNo(contractKey, params);
    }

    @LogInterceptor
    @ApiOperation(value = "买方／卖方应收款列表", notes = "买方／卖方应收款列表")
    @ResponseBody
    @RequestMapping(value = "receivableSimpleDeatilList",method = RequestMethod.POST)//路径
    public BaseResult<Object> receivableSimpleDeatilList(
            //@ApiParam(value = "操作人私钥", required = true) @RequestParam String operatorAddress,
            @ApiParam(value = "用户角色", required = true) @RequestParam int roleCode,//0是买家、1是卖家
            HttpServletRequest request
    ) throws Exception {

//        ContractKey contractKey = new ContractKey(operatorAddress);
        String address = TokenUtil.getAddressFromCookie(request);//用户address
        UserEntity userEntity = userEntityRepository.findByAddress(address);
        String privateKey = userEntity.getPrivateKey();
        String accountName = userEntity.getAccountName();
        ContractKey contractKey = new ContractKey(privateKey, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);

        String orderContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);//Order合约地址
        String accountContractAddress = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);//Account合约地址

        List<AccountEntity> accountEntityList = accountEntityRepository.findByAddress(address);
        AccountEntity accountEntity = accountEntityList.get(0);//取出来的结构体
        String acctId = accountEntity.getAcctId();

        Object[] params = new Object[4];
        params[0] = roleCode;
        params[1] = acctId;
        params[2] = orderContractAddress;
        params[3] = accountContractAddress;

        // 调用合约查询账户，获取返回结果
        return receivableService.receivableSimpleDeatilList(contractKey, params);
    }

}
