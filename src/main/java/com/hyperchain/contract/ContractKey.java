package com.hyperchain.contract;

import cn.hyperchain.common.log.LogUtil;
import cn.hyperchain.sdk.crypto.DESKeyJSON;
import com.hyperchain.common.constant.BaseConstant.Code;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;

/**
 * Created by martin on 2017/3/17.
 */
public class ContractKey {
    String privateKey;
    String password = "123";
    String publicKey;

    public ContractKey(String privateKey, String password) throws PrivateKeyIllegalParam {
        this.privateKey = privateKey;
        this.password = password;
        try {
            this.publicKey = new DESKeyJSON(privateKey).getAddress();
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("从私钥获取公钥失败。\n*privateKey=" + privateKey);
            throw new PrivateKeyIllegalParam(Code.INVALID_PARAM_PRIVATE_KEY, "private-key=" + privateKey);
        }
    }

    public ContractKey(String privateKey) throws PrivateKeyIllegalParam {
        this.privateKey = privateKey;
        try {
            this.publicKey = new DESKeyJSON(privateKey).getAddress();
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("从私钥获取公钥失败。\n*privateKey=" + privateKey);
            throw new PrivateKeyIllegalParam(Code.INVALID_PARAM_PRIVATE_KEY, "private-key=" + privateKey);
        }
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "ContractKey{" +
                "privateKey='" + privateKey + '\'' +
                ", password='" + password + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
