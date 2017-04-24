package com.hyperchain.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ldy on 2017/4/24.
 */
public class ReparoUtilTest {
    @Test
    public void convertCentToYuan() throws Exception {
        System.out.println(ReparoUtil.convertCentToYuan(10000));
    }

    @Test
    public void convertYuanToCent() throws Exception {
        System.out.println(ReparoUtil.convertYuanToCent(100.00));
    }

}