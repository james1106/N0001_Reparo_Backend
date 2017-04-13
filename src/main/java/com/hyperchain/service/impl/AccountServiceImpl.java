package com.hyperchain.service.impl;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.AccountStatus;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.contract.ContractKey;
import com.hyperchain.contract.ContractResult;
import com.hyperchain.contract.ContractUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.controller.vo.UserVo;
import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.SecurityCodeEntity;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.SecurityCodeEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ldy on 2017/4/5.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private SecurityCodeEntityRepository securityCodeEntityRepository;

    @Autowired
    private AccountEntityRepository accountEntityRepository;

    @Override
    public BaseResult<Object> getSecurityCode(String phone) {
        //TODO 完成逻辑代码，待测试（暂时不做）
        //手机号是否存在
        if (isPhoneExist(phone)) {
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(Code.PHONE_ALREADY_EXIST);
            LogUtil.info("手机号已存在：" + phone);
            return baseResult;
        }

        //生成验证码并存入数据库
        String securityCode = generateSecurityCode(100000, 999999);
        LogUtil.info("生成验证码：" + securityCode);
        long securityCodeId = saveSecurityCode(phone, securityCode);
        LogUtil.info("验证码id：" + securityCodeId);

        //请求第三方发送验证码，并返回验证码id
        try {
            sendSecurityCode(phone, securityCode);
            BaseResult<Object> baseResult = new BaseResult<Object>();
            baseResult.returnWithValue(Code.SUCCESS, securityCodeId);
            LogUtil.info("生成验证码返回：" + baseResult.toString());
            return baseResult;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error("请求第三方发送验证码失败");
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(Code.SYSTEM_ERROR);
            return baseResult;
//            throw new IOException("请求第三方发送验证码失败");
        }
    }

    @Override
    public BaseResult<Object> register(String accountName,
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
            throws PasswordIllegalParam, GeneralSecurityException, PrivateKeyIllegalParam, ContractInvokeFailException, IOException, ValueNullException {
        //判断用户名 或 手机号 或 企业名称是否存在
        if (isAccountExist(accountName) || isPhoneExist(phone) || isCompanyExist(enterpriseName)) {
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(Code.ACCOUNT_ALREADY_EXIST);
            LogUtil.info("账户已存在");
            return baseResult;
        }

        //TODO 验证码校验逻辑代码完成，待测试（暂时不做）
//        Code validateResultCode = validateSecurityCode(securityCodeId, securityCode);
//        if (validateResultCode != Code.SUCCESS) {
//            BaseResult<Object> baseResult = new BaseResult<>();
//            baseResult.returnWithoutValue(validateResultCode);
//            return baseResult;
//        }

        //生成账号
        String[] acctIdList = new String[1];
        acctIdList[0] = acctIds;
        ContractResult contractResult = saveAccount(accountName, password, enterpriseName, phone, roleCode, certType, certNo, acctIdList, svcrClass, acctSvcr, acctSvcrName);
        BaseResult<Object> baseResult = login(accountName, password, request, response);
        return baseResult;
    }

    @Override
    public BaseResult<Object> login(String accountName, String password, HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = userEntityRepository.findByAccountName(accountName);

        //用户不存在
        if (null == userEntity) {
            LogUtil.info("用户不存在");
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(Code.INVALID_USER);
            return baseResult;
        }

        AccountEntity accountEntity = accountEntityRepository.findByAddress(userEntity.getAddress()).get(0); //TODO 暂时每个address只有一个账号

        //用户已被锁定
        if (AccountStatus.LOCK.getCode() == userEntity.getUserStatus()) {
            LogUtil.info("用户在锁定状态");
            Date currentDate = new Date();
//            Date lockDate = DateUtils.addMinutes(currentDate, -5); // 锁定时间为为5分钟
            Date lockDate = DateUtils.addSeconds(currentDate, BaseConstant.ACCOUNT_LOCK_SECOND); // 锁定时间
            if (lockDate.getTime() <= userEntity.getLockTime()) { //在锁定时间内
                LogUtil.info("在锁定时间内");
                BaseResult<Object> baseResult = new BaseResult<>();
                baseResult.returnWithoutValue(Code.ACCOUNT_STATUS_LOCK);
                return baseResult;
            } else { //锁定时间过期
                //解锁
                LogUtil.info("锁定时间过期，解锁");
                userEntity.setUserStatus(AccountStatus.VALID.getCode());
                userEntityRepository.save(userEntity);
                //正常检查密码登录
                Code resultCode = checkPassword(userEntity, accountName, password);
                if (resultCode != Code.SUCCESS) { //密码不正确
                    BaseResult<Object> baseResult = new BaseResult<>();
                    baseResult.returnWithoutValue(resultCode);
                    return baseResult;
                } else { //密码正确：生成token
                    String address = userEntity.getAddress();
                    String token = TokenUtil.generateToken(address, userEntity.getRoleCode());
                    LogUtil.info("生成token：" + token);
                    //把token存到cookie中
                    Cookie cookie = new Cookie("token", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    //返回成功状态和用户信息
                    UserVo userVo = new UserVo(userEntity, accountEntity);
                    BaseResult<Object> baseResult = new BaseResult<>();
                    baseResult.returnWithValue(resultCode, userVo);
                    return baseResult;
                }
            }
        }

        //正常检查密码登录
        Code resultCode = checkPassword(userEntity, accountName, password);
        if (resultCode != Code.SUCCESS) { //密码不正确
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithoutValue(resultCode);
            return baseResult;
        } else { //密码正确：生成token
            //把token存到cookie中
            setTokenToCookie(userEntity.getAddress(), userEntity.getRoleCode(), request, response);
            //返回成功状态和用户信息
            UserVo userVo = new UserVo(userEntity, accountEntity);
            BaseResult<Object> baseResult = new BaseResult<>();
            baseResult.returnWithValue(resultCode, userVo);
            return baseResult;
        }
    }

    @Override
    public BaseResult<Object> forgetPassword(String phone, String newPassword, String securityCode, String securityCodeId) {
        // TODO 找回密码、修改密码、修改用户信息（暂时不做）
        return null;
    }

    @Override
    public BaseResult<Object> findAllEnterpriseNameByRoleCode(int roleCode) {
        List<UserEntity> userEntityList = userEntityRepository.findByRoleCode(roleCode);
        List<String> nameList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            nameList.add(userEntity.getCompanyName());
        }
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.returnWithValue(Code.SUCCESS, nameList);
        return baseResult;
    }

    /**
     * 判断用户是否存在
     *
     * @param phone 手机号
     * @return
     */
    private boolean isPhoneExist(String phone) {
        UserEntity userEntity = userEntityRepository.findByPhone(phone);
        if (userEntity == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断用户是否存在
     *
     * @param accountName 用户名
     * @return
     */
    private boolean isAccountExist(String accountName) {
        UserEntity userEntity = userEntityRepository.findByAccountName(accountName);
        if (userEntity == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isCompanyExist(String companyName) {
        UserEntity userEntity = userEntityRepository.findByCompanyName(companyName);
        if (userEntity == null) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 生成随机验证码
     *
     * @return
     */
    private String generateSecurityCode(int min, int max) {
        int randNum = min + (int) (Math.random() * ((max - min) + 1));
        return randNum + "";
    }

    /**
     * 验证码存储到数据库
     *
     * @param phone
     * @param securityCode
     * @return 验证码id
     */
    private long saveSecurityCode(String phone, String securityCode) {
        SecurityCodeEntity securityCodeEntity = new SecurityCodeEntity();
        securityCodeEntity.setSecurityCode(securityCode);
        securityCodeEntity.setPhone(phone);
        securityCodeEntity.setErrorCodeCount(0);
        securityCodeEntity.setCreateTime(new Date().getTime());
        SecurityCodeEntity securityCodeEntityResult = securityCodeEntityRepository.save(securityCodeEntity);
        return securityCodeEntityResult.getId();
    }

    /**
     * 第三方发送验证码
     *
     * @param phone
     * @param securityCode
     * @throws IOException
     */
    private void sendSecurityCode(String phone, String securityCode) throws IOException {
        //TODO 第三方发送验证码
    }

    /**
     * 验证码校验
     *
     * @param securityCodeId
     * @param securityCode
     * @return
     */
    private Code validateSecurityCode(long securityCodeId, String securityCode) {
        SecurityCodeEntity securityCodeEntity = securityCodeEntityRepository.findById(securityCodeId);

        //验证码不存在
        if (securityCodeEntity == null) {
            LogUtil.info("验证码不存在");
            return Code.NON_EXIST_SECURITY_CODE;
        }

        //验证码存在但过期
        Date currentDate = new Date();
        Date validDate = DateUtils.addMinutes(currentDate, -5); // 有效期为5分钟
        if (validDate.getTime() > securityCodeEntity.getCreateTime()) {
            LogUtil.info("验证码已过期");
            LogUtil.info("需要重新发送验证码");
            securityCodeEntityRepository.delete(securityCodeEntity);
            return Code.INVALID_SECURITY_CODE;
        }

        //验证码存在但不一致
        if (!securityCodeEntity.getSecurityCode().equals(securityCode)) {
            LogUtil.info("验证码不一致");
            if (securityCodeEntity.getErrorCodeCount() >= 3) {
                LogUtil.info("需要重新发送验证码");
                securityCodeEntityRepository.delete(securityCodeEntity);
                return Code.INVALID_SECURITY_CODE;
            } else {
                LogUtil.info("还可以重新输入验证码：" + (2 - securityCodeEntity.getErrorCodeCount()) + "次");
                Code code = Code.WRONG_SECURITY_CODE;
                code.setMsg(code.getMsg() + (2 - securityCodeEntity.getErrorCodeCount()) + "次");
                securityCodeEntity.setErrorCodeCount(securityCodeEntity.getErrorCodeCount() + 1);
                securityCodeEntityRepository.save(securityCodeEntity);
            }
            return Code.WRONG_SECURITY_CODE;
        }

        //验证码在有效期内且一致
        securityCodeEntityRepository.delete(securityCodeEntity);
        return Code.SUCCESS;
    }

    /**
     * 存储账户信息到数据库和区块链
     */
    private ContractResult saveAccount(String accountName,
                                       String password,
                                       String companyName,
                                       String phone,
                                       int roleCode,
                                       String certType,
                                       String certNo,
                                       String[] acctIds,
                                       String svcrClass,
                                       String acctSvcr,
                                       String acctSvcrName)
            throws GeneralSecurityException, IOException, PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam {
        //生成公私钥和用户地址
        List<String> keyInfo = ESDKUtil.newAccount(BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String address = keyInfo.get(0);
        String accountJson = keyInfo.get(1);
        LogUtil.debug("accountJson：" + accountJson);
        LogUtil.debug("address：" + address);

        //账户信息存储到数据库
        UserEntity userEntity = new UserEntity();
        userEntity.setAccountName(accountName);
        userEntity.setPassword(DigestUtils.md5Hex(password + BaseConstant.SALT_FOR_PASSWORD + accountName));
        userEntity.setCompanyName(companyName);
        userEntity.setPhone(phone);
        userEntity.setRoleCode(roleCode);
        userEntity.setErrorPasswordCount(0);
        userEntity.setLockTime(new Long(0));
        userEntity.setUserStatus(AccountStatus.VALID.getCode());
        userEntity.setAddress(address);
        userEntity.setPrivateKey(accountJson);
        UserEntity savedUserEntity = userEntityRepository.save(userEntity);
        LogUtil.info("存入数据库user表：" + savedUserEntity.toString());
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAddress(address);
        accountEntity.setCertType(certType);
        accountEntity.setCertNo(certNo);
        accountEntity.setAcctId(acctIds[0]);
        accountEntity.setSvcrClass(svcrClass);
        accountEntity.setAcctSvcr(acctSvcr);
        accountEntity.setAcctSvcrName(acctSvcrName);
        AccountEntity savedAccountEntity = accountEntityRepository.save(accountEntity);
        LogUtil.info("存入数据库account表：" + savedAccountEntity.toString());

        //账户信息存储到区块链
        ContractKey contractKey = new ContractKey(accountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + accountName);
        String contractMethodName = "newAccount";
        Object[] contractMethodParams = new Object[10];
        contractMethodParams[0] = accountName;
        contractMethodParams[1] = companyName;
        contractMethodParams[2] = roleCode;
        contractMethodParams[3] = AccountStatus.VALID.getCode();
        contractMethodParams[4] = certType;
        contractMethodParams[5] = certNo;
        contractMethodParams[6] = acctIds;
        contractMethodParams[7] = svcrClass;
        contractMethodParams[8] = acctSvcr;
        contractMethodParams[9] = acctSvcrName;
        String[] resultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, resultMapKey, BaseConstant.CONTRACT_NAME_ACCOUNT);
        LogUtil.info("调用合约newAccount返回code：" + contractResult.getCode());
        return contractResult;
    }

    private void setTokenToCookie(String address, int roleCode, HttpServletRequest request, HttpServletResponse response) {
        boolean isSet = false; //toke是否已经设置

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = TokenUtil.generateToken(address, roleCode);
                    LogUtil.info("生成token：" + token);
                    cookie.setValue(token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    isSet = true;
                }
            }
        }
        if (isSet == false) {
            String token = TokenUtil.generateToken(address, roleCode);
            LogUtil.info("生成token：" + token);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    /**
     * 判断密码是否正确
     *
     * @param userEntity  userEntity
     * @param accountName 用户名
     * @param password    明文密码
     * @return
     */
    private Code checkPassword(UserEntity userEntity, String accountName, String password) {
        //密码不正确
        if (!DigestUtils.md5Hex(password + BaseConstant.SALT_FOR_PASSWORD + accountName).equals(userEntity.getPassword())) {
            LogUtil.info("用户密码不正确");
            if (userEntity.getErrorPasswordCount() >= 3) { //连续输错密码三次
                userEntity.setErrorPasswordCount(0);
                userEntity.setLockTime(new Date().getTime());
                userEntity.setUserStatus(AccountStatus.LOCK.getCode());
                LogUtil.info("连续输错密码三次，账户被锁定");
                userEntityRepository.save(userEntity);
                return Code.ACCOUNT_STATUS_LOCK;
            } else {
                LogUtil.info("密码错误次数 + 1");
                userEntity.setErrorPasswordCount(userEntity.getErrorPasswordCount() + 1);
                userEntityRepository.save(userEntity);
                return Code.ERROR_PASSWORD;
            }
        }

        //密码正确
        userEntity.setErrorPasswordCount(0);
        userEntityRepository.save(userEntity);
        return Code.SUCCESS;

    }

}
