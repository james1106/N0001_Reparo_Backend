package com.hyperchain.common.exception;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.common.exception.base.BaseException;
import org.apache.poi.ss.formula.functions.T;

/**
 * Created by linbo on 16/12/16.
 */
public class ContractInvokeFailException extends BaseException{
    public ContractInvokeFailException(Code code) {
        super(code);
    }

    public ContractInvokeFailException(Code code, String errorValue) {
        super(code, errorValue);
    }
}
