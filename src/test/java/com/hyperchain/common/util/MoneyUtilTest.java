package com.hyperchain.common.util;

import org.junit.Test;

/**
 * Created by ldy on 2017/4/24.
 */
public class MoneyUtilTest {
    @Test
    public void convertCentToYuan() throws Exception {
        System.out.println(MoneyUtil.convertCentToYuan(10000));
    }

    @Test
    public void convertYuanToCent() throws Exception {
        System.out.println(MoneyUtil.convertYuanToCent(100.00));
    }

    @Test
    public void getPasswordForPrivateKey() {
        System.out.println(MoneyUtil.getPasswordForPrivateKey("企业名").length());
    }

}