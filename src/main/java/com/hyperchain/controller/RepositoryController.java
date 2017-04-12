package com.hyperchain.controller;

import cn.hyperchain.common.log.LogInterceptor;
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
    @ApiOperation(value = "查询仓储流转历史", notes = "查询仓储流转历史")
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

}
