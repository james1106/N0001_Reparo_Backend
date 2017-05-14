package com.hyperchain.common.exception;

import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.base.BaseException;

/**
 * Created by linbo on 16/12/15.
 */
public class ValueNullException extends BaseException {
    public ValueNullException() {
        super(Code.HYPERCHAIN_ERROR, "调用合约方法返回为空");
    }
}
