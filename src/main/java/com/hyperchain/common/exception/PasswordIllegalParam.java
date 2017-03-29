package com.hyperchain.common.exception;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.exception.base.BaseException;

/**
 * Created by martin on 2017/3/18.
 */
public class PasswordIllegalParam extends BaseException{
    public PasswordIllegalParam(BaseConstant.Code code) {
        super(code);
    }
    public PasswordIllegalParam(BaseConstant.Code code, String errorValue) {
        super(code, errorValue);
    }
}
