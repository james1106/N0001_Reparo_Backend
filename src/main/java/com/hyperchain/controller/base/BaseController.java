package com.hyperchain.controller.base;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyperchain.controller.vo.BaseResult;

/**
 * 异常处理类
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:27
 */
public abstract class BaseController {

    /**
     * 参数格式错误-私钥
     *
     * @param e
     * @return
     */
    @ExceptionHandler(PrivateKeyIllegalParam.class)
    @ResponseBody
    public BaseResult<T> handleValidationException(PrivateKeyIllegalParam e) {
        LogUtil.error(e);
        return e.getBaseResult();
    }

    /**
     * 参数格式错误-密码
     *
     * @param e
     * @return
     */
    @ExceptionHandler(PasswordIllegalParam.class)
    @ResponseBody
    public BaseResult<T> handleValidationException(PasswordIllegalParam e) {
        LogUtil.error(e);
        return e.getBaseResult();
    }


    /**
     * 签名校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public BaseResult<T> handleValidationException(ValidationException e) {
        LogUtil.error(e);
        return e.getBaseResult();
    }

    /**
     * 合约调用失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ContractInvokeFailException.class)
    @ResponseBody
    public BaseResult<T> handleContractInvokeFailException(ContractInvokeFailException e) {
        LogUtil.error(e);
        return e.getBaseResult();
    }

    /**
     * 合约返回空值异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ValueNullException.class)
    @ResponseBody
    public BaseResult<T> handleValueNullException(ValueNullException e) {
        LogUtil.error(e);
        return e.getBaseResult();
    }

    /**
     * json转换异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    @ResponseBody
    public BaseResult<T> handleJSONConvertException(HttpMessageNotWritableException e) {    //JSON格式转换异常
        LogUtil.error(e);
        return new BaseResult(Code.JSON_TRANSFER_ERROR);
    }

    /**
     * 系统异常
     * 通常是未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult<T> handleUncaughtException(Exception e) {
        LogUtil.error(e);
        return new BaseResult(Code.SYSTEM_ERROR);
    }
}
