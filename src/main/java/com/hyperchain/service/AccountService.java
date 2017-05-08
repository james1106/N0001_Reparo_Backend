package com.hyperchain.service;

import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by ldy on 2017/4/5.
 */
public interface AccountService {

    BaseResult<Object> getSecurityCode(String phone) throws IOException;

    BaseResult<Object> register(
            String accountName,
            String password,
            String enterpriseName,
            String phone,
            int roleCode,
            String securityCode,
            long securityCodeId,
            String certType,
            String certNo,
            String acctIds,
            String svcrClass,
            String acctSvcr,
            String acctSvcrName,
            HttpServletRequest request,
            HttpServletResponse response)
            throws PasswordIllegalParam,
            GeneralSecurityException,
            PrivateKeyIllegalParam,
            ContractInvokeFailException,
            IOException, ValueNullException, ReadFileException, PropertiesLoadException;

    BaseResult<Object> login(String accountName, String password, HttpServletRequest request, HttpServletResponse response) throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException;

    BaseResult<Object> forgetPassword(String phone, String newPassword, String securityCode, String securityCodeId);

    BaseResult<Object> findAllEnterpriseNameByRoleCode(int roleCode);

    BaseResult<Object> setRateForFinancial(String rate, String address) throws ContractInvokeFailException, PasswordIllegalParam, PrivateKeyIllegalParam, ValueNullException;

    BaseResult<Object> getAllFinancialEnterpriseNameAndRate();
}
