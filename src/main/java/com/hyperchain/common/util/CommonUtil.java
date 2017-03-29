package com.hyperchain.common.util;

import cn.hyperchain.common.log.LogUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 日期,判空工具类
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:29
 */
public class CommonUtil {

    /**
     * 判断对象是否Empty(null或元素为0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object pObj) {
        if (pObj == null)
            return true;
        if ("".equals(pObj))
            return true;
        if (pObj instanceof String) {
            if (((String) pObj).length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素>0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Object pObj) {
        if (pObj == null)
            return false;
        if ("".equals(pObj))
            return false;
        if (pObj instanceof String) {
            if (((String) pObj).length() == 0) {
                return false;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return false;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数据格式化.
     *
     * @param pattern the pattern
     * @param
     * @return the string
     */
    public static String codeFormat(String pattern, Object value) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

    /**
     * 格式化时间.
     *
     * @param date the date
     * @return the string
     */
    public static String fomatDate(String date) {
        if (isNotEmpty(date)) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                    + date.substring(6, 8);
        }
        return null;
    }


    /**
     * 将double数值格式化成指定格式的字串
     *
     * @param value
     * @param format
     * @return
     */
    public static Double formatCurrency2Double(Double value, String format) {
        if (CommonUtil.isEmpty(format)) {
            format = "0.00";
        }
        DecimalFormat df = new DecimalFormat(format);
        String temp = df.format(value);
        ;
        return Double.valueOf(temp);
    }

    /**
     * 格式化时间.
     *
     * @param date the date
     * @return the string
     */
    public static String fomatLongDate(String date) {
        if (isNotEmpty(date)) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                    + date.substring(6, 8) + " " + date.substring(8, 10) + ":"
                    + date.substring(10, 12) + ":" + date.substring(12, 14);
        }
        return null;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTodayString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return f.format(c.getTime());
    }

    /**
     * 格式化时间.
     *
     * @param date the date
     * @return the string
     */
    public static String fomatDateTime2String(String date) {
        if (isNotEmpty(date)) {
            return date.replace("-", "").replace("T", "").replace(":", "")
                    .replace(" ", "");
        }
        return null;
    }

    /**
     * 将时间字符串格式化成一个日期(java.util.Date)
     *
     * @param dateStr   要格式化的日期字符串，如"2014-06-15 12:30:12"
     * @param formatStr 格式化模板，如"yyyy-MM-dd HH:mm:ss"
     * @return the string
     */
    public static Date formatDateString2Date(String dateStr, String formatStr) {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getNowTimeStr() {
        Long temp = new Date().getTime();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(temp);
        return sf.format(date);
    }

    /**
     * 将时间字符串格式化成一个日期(java.util.Date)
     *
     * @param "2014-06-15 12:30:12"
     * @param formatStr   格式化模板，如"yyyy-MM-dd HH:mm:ss"
     * @return the string
     */
    public static String formatDate2String(Date date, String formatStr) {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        String result = null;
        try {
            result = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将一个毫秒数时间转换为相应的时间格式
     *
     * @param longSecond
     * @return
     */
    public static String formateDateLongToString(long longSecond) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(longSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(gc.getTime());
    }

    public static String formateDateLongToString(long longSecond, String formatPatten) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(longSecond);
        if (CommonUtil.isEmpty(formatPatten)) {
            formatPatten = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatPatten);
        return format.format(gc.getTime());
    }

    /**
     * 格式化金额.
     *
     * @param value the value
     * @return the string
     */
    public static String formatCurrency2String(Long value) {
        if (value == null || "0".equals(String.valueOf(value))) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value / 100.00);
    }

    /**
     * 格式化金额.
     *
     * @param priceFormat the price format
     * @return the long
     */
    public static Long formatCurrency2Long(String priceFormat) {
        BigDecimal bg = new BigDecimal(priceFormat);
        Long price = bg.multiply(new BigDecimal(100)).longValue();
        return price;
    }

    /**
     * 获取当前时间.
     *
     * @param
     * @return
     * @throws
     */
    public static String getToDayStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间当作文件名称
     *
     * @return
     */
    public static String getToDayStrAsFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    public static Date getToDay() throws ParseException {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date date = sdf.parse(String.valueOf(System.currentTimeMillis()));
        return new Date();
    }

    /**
     * 获取下一天.
     *
     * @param currentDate the current date
     * @return the next date str
     * @throws ParseException the parse exception
     */
    public static String getNextDateStr(String currentDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        String nextDate = sdf.format(calendar.getTime());
        return nextDate;
    }

    public static String getNextNday(String currentDate, Integer n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, n);
            return sdf.format(calendar.getTime());

        } catch (Exception e) {
            return null;
        }


    }

    /**
     * 获取上一天.
     *
     * @param currentDate the current date
     * @return the next date str
     * @throws ParseException the parse exception
     */
    public static String getYesterdayStr(String currentDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        String nextDate = sdf.format(calendar.getTime());
        return nextDate;
    }

    /**
     * 根据日期获取星期
     *
     * @param strdate
     * @return
     */
    public static String getWeekDayByDate(String strdate) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六"};
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    public static String getProperty(String fileName, String key) {
        Properties pro = new Properties();
        if (!fileName.contains(".properties")) {
            fileName = fileName + ".properties";
        }
        InputStream in = null;
        try {
            in = CommonUtil.class.getClassLoader().getResourceAsStream(fileName);
            pro.load(in);
            if (CommonUtil.isNotEmpty(pro)) {
                String value = pro.getProperty(key);
                return value;
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            LogUtil.error("配置文件未找到：" + fileName);
            return null;
        } catch (IOException e) {
            LogUtil.error("配置文件加载失败：" + fileName);
            return null;
        } finally {
            if (CommonUtil.isNotEmpty(in)) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.error("FileInputStream关闭失败");
                }
            }
        }
    }

    public static void setProperty(String fileName, String key, String value, String comment) {
        LogUtil.info("写入私钥" + fileName + "。" + key + " = " + value);
        FileOutputStream oFile = null;
        Properties prop = new Properties();
        if (!fileName.contains(".properties")) {
            fileName = fileName + ".properties";
        }
        try {
            oFile = new FileOutputStream(fileName, true);
            prop.setProperty(key, value);
            prop.store(oFile, comment);
            oFile.close();
        } catch (FileNotFoundException e) {
            LogUtil.error("文件未找到：" + fileName);
        } catch (IOException e) {
            LogUtil.error("配置文件设置失败");
        }

    }


}