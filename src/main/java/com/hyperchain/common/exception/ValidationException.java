package com.hyperchain.common.exception;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.base.BaseException;
import com.hyperchain.controller.vo.BaseResult;
import org.apache.poi.ss.formula.functions.T;

/**
 * 校验异常
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:28
 */
public class ValidationException extends BaseException {
    public ValidationException(Code code) {
        super(code);
    }

    public ValidationException(Code code, String errorValue) {
        super(code, errorValue);
    }
}
