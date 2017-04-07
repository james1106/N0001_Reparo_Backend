package com.hyperchain.common.exception;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.base.BaseException;

/**
 * Created by martin on 2017/3/18.
 */
public class PasswordIllegalParam extends BaseException{
    public PasswordIllegalParam(Code code) {
        super(code);
    }
    public PasswordIllegalParam(Code code, String errorValue) {
        super(code, errorValue);
    }
}
