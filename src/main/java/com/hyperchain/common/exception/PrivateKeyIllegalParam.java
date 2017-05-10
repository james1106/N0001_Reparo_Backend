package com.hyperchain.common.exception;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.base.BaseException;

/**
 * Created by martin on 2017/3/18.
 */
public class PrivateKeyIllegalParam extends BaseException{
    public PrivateKeyIllegalParam() {
        super(Code.INVALID_PARAM_PRIVATE_KEY);
    }
}
