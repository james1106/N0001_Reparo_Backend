package com.hyperchain.controller.base;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.*;
import com.hyperchain.common.exception.base.BaseException;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyperchain.controller.vo.BaseResult;

/**
 * 异常处理类
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:27
 */
public abstract class BaseController {

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public BaseResult<T> handleBaseException(BaseException e) {
        LogUtil.error(e);
        return e.getBaseResult();
    }

    /**
     * 系统异常
     * 通常是未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult<T> handleUncaughtException(Exception e) {
        LogUtil.error(e);
        return new BaseResult(Code.SYSTEM_ERROR);
    }
}
