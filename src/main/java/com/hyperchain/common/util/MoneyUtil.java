package com.hyperchain.common.util;

import com.hyperchain.common.constant.BaseConstant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.cxf.common.util.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by ldy on 2017/4/24.
 */
public class MoneyUtil {

    /**
     * 分转换成元，并且格式化成始终保留两位小数
     *
     * @param cent
     * @return
     */
    public static String convertCentToYuan(long cent) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("0.00");
        return df.format(cent / 100.00);
    }

    /**
     * 元转换成分
     *
     * @param yuan
     * @return
     */
    public static long convertYuanToCent(double yuan) {
        return (long) (yuan * 100);
    }

    /**
     * 生成加密私钥的密码
     *
     * @param accountName 用户名
     * @return
     */
    public static String getPasswordForPrivateKey(String accountName) {

        byte[] hashBytes = DigestUtils.md5(BaseConstant.SALT_FOR_PRIVATE_KEY + accountName); //16个字节，128位
        String hash = new String(hashBytes);
        return hash.substring(0, 8); //des密钥为64位，8个字节
    }

    /**
     * 生成业务编号
     *
     * @param prefix 业务编号前缀
     * @return
     */
    public static String generateBusinessNo(String prefix) {
        String businessNo = prefix + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (new Random().nextInt(900) + 100);
        return businessNo;
    }

    /**
     * 将利率放大1000倍
     *
     * @param rate 未放大1000倍的金融机构贴现利率
     * @return
     */
    public static long enlargeRateThousandfold(double rate) {
        return (long)(rate * 1000);
    }

    /**
     * 将利率缩小1000倍
     *
     * @param rateThousandfold 放大1000倍的金融机构贴现利率
     * @return
     */
    public static String minishRateThousandfold(long rateThousandfold) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("0.000");
        return df.format(rateThousandfold / 1000.000);
    }

}
