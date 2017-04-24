package com.hyperchain.common.util;

import java.text.DecimalFormat;

/**
 * Created by ldy on 2017/4/24.
 */
public class ReparoUtil {

    /**
     * 分转换成元，并且格式化成始终保留两位小数
     * @param cent
     * @return
     */
    public static String convertCentToYuan(long cent) {
        DecimalFormat df=(DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("0.00");
        return df.format(cent / 100.00);
    }

    /**
     * 元转换成分
     * @param yuan
     * @return
     */
    public static long convertYuanToCent(double yuan) {
        return (long) yuan * 100;
    }
}
