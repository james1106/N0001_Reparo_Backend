package com.hyperchain.common.exception;

import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.base.BaseException;

/**
 * Created by ldy on 2017/5/9.
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException() {
        super(Code.INVALID_USER);
    }
}
