package com.hyperchain.service;

import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by ldy on 2017/4/5.
 */
public interface AccountService {

    BaseResult<Object> getSecurityCode(String phone) throws IOException;

    BaseResult<Object> register(String account, String password, String companyName, String phone, int roleCode, String securityCode, long securityCodeId) throws PasswordIllegalParam, GeneralSecurityException, PrivateKeyIllegalParam, ContractInvokeFailException, IOException, ValueNullException;

    BaseResult<Object> login(String account, String password);

    BaseResult<Object> forgetPassword(String phone, String newPassword, String securityCode, String securityCodeId);
}
