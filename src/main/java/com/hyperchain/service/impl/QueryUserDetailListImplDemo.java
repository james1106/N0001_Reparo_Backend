//package com.hyperchain.service.impl;
//
//import com.hyperchain.contract.ContractKey;
//import com.hyperchain.contract.ContractResult;
//import com.hyperchain.contract.ContractUtil;
//import com.hyperchain.controller.vo.BaseResult;
//import com.hyperchain.controller.vo.UserDemo;
//import com.hyperchain.service.QueryUserDetailListDemo;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * by chenyufeng on 2017/4/1 .
// */
//@Service("QueryUserDetailListService")
//public class QueryUserDetailListImplDemo implements QueryUserDetailListDemo {
//    @Override
//    public BaseResult<Object> invokeContract(ContractKey contractKey, Object[] contractMethodParams) throws Exception {
//        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
//        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractMethodReturns);
//
//        List<String> ids = (List<String>) contractResult.getValueMap().get(contractMethodReturns[0]);
//        List<String> nicknames = (List<String>) contractResult.getValueMap().get(contractMethodReturns[1]);
//        List<String> passwords = (List<String>) contractResult.getValueMap().get(contractMethodReturns[2]);
//        List<String> phoneNum = (List<String>) contractResult.getValueMap().get(contractMethodReturns[3]);
//
//        //组装合约返回的每个对象的详情
//        List<UserDemo> userObj = new ArrayList<>();
//        for (int i = 0; i < ids.size(); i++) {
//            UserDemo user = new UserDemo(ids.get(i), nicknames.get(i), passwords.get(i), phoneNum.get(i));
//            userObj.add(user);
//        }
//        //重新构造返回的BaseResult
//        BaseResult<Object> baseResult = new BaseResult<>();
//        baseResult.setCode(contractResult.getCode().getCode());
//        baseResult.setMessage(contractResult.getCode().getMsg());
//        baseResult.setData(userObj);
//
//        // 将合约结果转化为接口返回数据
//        return baseResult;
//    }
//}
