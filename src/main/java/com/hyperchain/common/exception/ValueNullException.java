package com.hyperchain.common.exception;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.base.BaseException;
import com.hyperchain.controller.vo.BaseResult;
import org.apache.poi.ss.formula.functions.T;

/**
 * Created by linbo on 16/12/15.
 */
public class ValueNullException extends BaseException {
    public ValueNullException() {
        super(Code.HYPERCHAIN_ERROR, "调用合约方法返回为空");
    }
}
