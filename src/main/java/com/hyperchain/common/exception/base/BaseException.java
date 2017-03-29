package com.hyperchain.common.exception.base;

import com.hyperchain.common.constant.BaseConstant.Code;
import com.hyperchain.controller.vo.BaseResult;
import org.apache.poi.ss.formula.functions.T;

/**
 * 异常基类
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:28
 */
public class BaseException extends Exception {

    protected Code code;

    protected BaseResult<T> baseResult;

    protected BaseException() {
        super();
    }

    protected BaseException(Code code) {
        super(code.getMsg());
        this.code = code;
        this.baseResult = new BaseResult(code);
    }

    protected BaseException(Code code, String errorValue) {
        super(code.getMsg() + "\n" + errorValue);
        this.code = code;
        this.baseResult = new BaseResult(code);
        this.baseResult.setMessage(code.getMsg() + " | " + errorValue);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public BaseResult<T> getBaseResult() {
        return baseResult;
    }

    public void setBaseResult(BaseResult<T> baseResult) {
        this.baseResult = baseResult;
    }
}
