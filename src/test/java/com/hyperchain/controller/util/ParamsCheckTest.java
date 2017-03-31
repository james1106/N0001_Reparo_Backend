package com.hyperchain.controller.util;

import com.hyperchain.common.util.ParamsCheck;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by martin on 2017/3/21.
 */
public class ParamsCheckTest {
    @Test
    public void checkPrivateKey() throws Exception {
        String privateKey = "{\"encrypted\":\"3756443e7128bd09dd0f3d647d39db5c634a317a6cdd2fbcd9837b5f35ef1e08c6f431ea0e0e22ac\",\"algo\":\"0x02\",\"address\":\"22aec712ba13be0fbba2238551456cd9d2df2cac\",\"version\":\"1.0\"}";
        Assert.assertTrue(ParamsCheck.checkPrivateKey(privateKey));
        String privateKeyError = "{\"encrypted\":\"3756443e7128bd09dd0f3d647d39db5c634a317a6cdd2fbcd9837b5f35ef1e08c6f431ea0e0e22ac\",\"algo\":\"0x02\",\"address\":\"22aec712ba13be0fbba2238551456cd9d2df2ccac\",\"version\":\"1.0\"}";
        Assert.assertFalse(ParamsCheck.checkPrivateKey(privateKeyError));
    }

    @Test
    public void checkPartyFunctionCode() throws Exception {
        String BMPartyFunctionCode = "BM";
        Assert.assertTrue(ParamsCheck.checkPartyFunctionCode(BMPartyFunctionCode));
    }

    @Test
    public void checkStatus() throws Exception {
        String status1 = "1";
        Assert.assertTrue(ParamsCheck.checkStatus(status1));
        String status2 = "2";
        Assert.assertTrue(ParamsCheck.checkStatus(status2));
        String status4 = "4";
        Assert.assertTrue(ParamsCheck.checkStatus(status4));
        String status8 = "8";
        Assert.assertTrue(ParamsCheck.checkStatus(status8));
        String statusPassive1 = "-1";
        Assert.assertTrue(ParamsCheck.checkStatus(statusPassive1));
        String statusPassive2 = "-2";
        Assert.assertTrue(ParamsCheck.checkStatus(statusPassive2));
        String statusError = "0";
        Assert.assertFalse(ParamsCheck.checkStatus(statusError));
    }

}