package com.hyperchain.common.exception;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.exception.base.BaseException;
import com.hyperchain.controller.vo.BaseResult;
import org.apache.poi.ss.formula.functions.T;

/**
 * Created by linbo on 16/12/15.
 */
public class ValueNullException extends BaseException {
    public ValueNullException(BaseConstant.Code code) {
        super(code);
    }

    public ValueNullException(BaseConstant.Code code, String errorValue) {
        super(code, errorValue);
    }
}
