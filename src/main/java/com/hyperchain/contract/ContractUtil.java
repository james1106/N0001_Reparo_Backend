package com.hyperchain.contract;

import cn.hyperchain.common.log.LogUtil;
import cn.hyperchain.sdk.rpc.Transaction.Transaction;
import com.hyperchain.ESDKConnection;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.common.util.CommonUtil;
import com.hyperchain.controller.vo.BaseResult;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yangjiexie on 2017/3/15.
 */
public class ContractUtil {

    /**
     * 调用合约方法
     *
     * @param contractKey  合约钥匙（公钥、私钥、用户密码）
     * @param methodName   合约方法名
     * @param params       合约方法参数
     * @param resultMapKey 合于方法返回值参数名
     * @return 合约结果（结果代码、返回值）
     * @throws Exception
     */
    public static ContractResult invokeContract(ContractKey contractKey, String methodName, Object[] params, String[] resultMapKey) throws ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        List<Object> lists;
        try {
            // 利用（公钥，方法名，方法参数）发起交易
            Transaction transaction = ESDKUtil.getTxHash(contractKey.getPublicKey(), methodName, params);
            // 对交易进行签名
            transaction.sign(contractKey.getPrivateKey(), contractKey.getPassword());
            // 调用合约方法
            String contractRet = ESDKConnection.invokeContractMethod(transaction);
            // 获取合约方法结果
            lists = ESDKUtil.retDecode(methodName, contractRet);
            for(Object object:lists){
                LogUtil.info(object.toString());
            }
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("调用合约方法失败。\n*合约公私钥=" + contractKey + "\n*合约方法名=" + methodName + "\n*参数=" + Arrays.toString(params) + "\n*返回值名=" + Arrays.toString(resultMapKey));
            if (CommonUtil.isNotEmpty(e.getMessage())&&e.getMessage().contains("Given final block not properly padded")){
                throw new PasswordIllegalParam(Code.INVALID_PASSWORD);
            }else{
                throw new ContractInvokeFailException(Code.HYPERCHAIN_ERROR);
            }
        }
        // 如果返回结果为空，返回null
        if (CommonUtil.isEmpty(lists)) {
            LogUtil.error("调用合约方法返回为空。\n*合约公私钥=" + contractKey + "\n*合约方法名=" + methodName + "\n*参数=" + Arrays.toString(params) + "\n*返回值名=" + Arrays.toString(resultMapKey));
            throw new ValueNullException(Code.HYPERCHAIN_ERROR, "调用合约方法返回为空。");
        } else {
            // 将返回的整数转化为枚举类型Code
            int codeInt = Integer.parseInt(lists.get(0).toString());
//            Code code = Code.values()[codeInt];
            Code code = Code.fromInt(codeInt);
            // 如果返回结果只有结果代码Code,返回Code
            if (lists.size() == 1) {
                return new ContractResult(code);
            }
            // 否则，返回结果代码和结果值
            else {
                return new ContractResult(code, lists.subList(1, lists.size()), resultMapKey);
            }
        }
    }

    public static BaseResult<Object> convert2BaseResult(ContractResult contractResult) {
        BaseResult result = new BaseResult();
        int resultValueAmount = contractResult.getValue().size();
        switch (resultValueAmount) {
            case 0:
                result.returnWithoutValue(contractResult.getCode());
                break;
            case 1:
                result.returnWithValue(contractResult.getCode(), contractResult.getValue().get(0));
                break;
            default:
                result.returnWithValue(contractResult.getCode(), contractResult.getValueMap());
                break;
        }
        return result;
    }
}
