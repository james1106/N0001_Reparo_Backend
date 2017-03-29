package com.hyperchain.controller.vo;

import java.util.HashMap;
import java.util.Map;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.hyperchain.common.constant.BaseConstant.Code;

/**
 * 接口返回结果类
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:27
 */
@SuppressWarnings("unchecked")
public class BaseResult<T> {

    @ApiModelProperty(value = "code - 返回代码：0表示成功，否则表示失败",dataType = "int")
    private int code ;

    @ApiModelProperty(value = "message - 消息：如果code为0显示成功，否则显示错误信息",dataType = "string")
    private String message ;

    @ApiModelProperty(value = "data - 数据：返回结果集",dataType = "JSON")
    private T data = (T) new Object();

    @ApiModelProperty(value = "对object签名")
    private String signature;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResult() {
        this.data = (T) new Object();
    }

    public BaseResult(String errorMsg) {
        this();
        this.code=200;
        this.message=errorMsg;

    }

    public BaseResult(Code code){
        this();
        this.code=code.getCode();
        this.message=code.getMsg();
    }


    /**
     * 返回结果代码code和消息msg，不需要返回值
     * @param code 结果类型
     */
    public void returnWithoutValue(Code code) {
        this.code = code.getCode();
        this.message = code.getMsg();
    }

    /**
     * 返回结果代码code和消息msg，并返回值
     * @param code 结果类型
     * @param object 返回值
     */
    public void returnWithValue(Code code, T object) {
        returnWithoutValue(code);
        this.data = object;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

