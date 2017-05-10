package com.hyperchain.common.exception;

import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.base.BaseException;

/**
 * Created by ldy on 2017/5/9.
 */
public class QueryNotExistUserException extends BaseException {
    public QueryNotExistUserException() {
        super(Code.QUERY_USER_ERROR);
    }
}
