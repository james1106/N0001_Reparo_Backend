package com.hyperchain.service.impl;

import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.service.AccountService;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by ldy on 2017/4/7.
 */
public class AccountServiceImplTest extends SpringBaseTest{

    @Autowired
    AccountService accountService;

    @Test
    public void getSecurityCode() throws Exception {
//        BaseResult<Object> result = accountService.getSecurityCode("1111");
//        System.out.println("生成验证码：" + result.toString());
//        Assert.assertEquals(result.getCode(), 0);
    }

    @Test
    public void registerAndLogin() throws Exception {

        //注册
        String randomString = TestUtil.getRandomString();
        BaseResult<Object> result = accountService.register("account" + randomString, //unique
                "123",
                "企业" + randomString, //unique
                "1881881" + randomString, //unique
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("注册返回结果：" + result.toString());
        Assert.assertEquals(result.getCode(), Code.SUCCESS.getCode());

        //重复注册
        BaseResult<Object> repeatedResult = accountService.register("account" + randomString, //unique（重复）
                "123",
                "企业11", //unique
                "188188111", //unique
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("用户名重复注册返回结果：" + repeatedResult.toString());
        Assert.assertEquals(repeatedResult.getCode(), Code.ACCOUNT_ALREADY_EXIST.getCode());

        BaseResult<Object> repeatedResult1 = accountService.register("account11", //unique
                "123",
                "企业" + randomString, //unique（重复）
                "1881881" , //unique
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("企业名称重复注册返回结果：" + repeatedResult1.toString());
        Assert.assertEquals(repeatedResult1.getCode(), Code.ACCOUNT_ALREADY_EXIST.getCode());

        BaseResult<Object> repeatedResult2 = accountService.register("account", //unique
                "123",
                "企业", //unique
                "1881881" + randomString, //unique（重复）
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111",
                "class",
                "acctSvcr",
                "acctSvcrName");
        System.out.println("电话号码重复注册返回结果：" + repeatedResult2.toString());
        Assert.assertEquals(repeatedResult2.getCode(), Code.ACCOUNT_ALREADY_EXIST.getCode());


        //正确登录
        String accountName = "account" + randomString;
        String pass = "123";
        BaseResult<Object> baseResult = accountService.login(accountName, pass, new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public String encodeUrl(String s) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public void setStatus(int i, String s) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        });
        System.out.println("登录结果：" + baseResult);
        Assert.assertEquals(baseResult.getCode(), Code.SUCCESS.getCode());

        //用户名错误
        String wrongAccountName = "account111";
        BaseResult<Object> baseResult1 = accountService.login(wrongAccountName, pass, new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public String encodeUrl(String s) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public void setStatus(int i, String s) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        });
        System.out.println("用户名错误登录结果：" + baseResult1);
        Assert.assertEquals(baseResult1.getCode(), Code.INVALID_USER.getCode());

        BaseConstant.ACCOUNT_LOCK_SECOND = -3; //设置用户锁定时间为3秒
        //密码错误3次锁定
        for(int i = 0 ; i < 4; i++) {
            String errPass = "123456";
            BaseResult<Object> baseResult2 = accountService.login(accountName, errPass, new HttpServletResponse() {
                @Override
                public void addCookie(Cookie cookie) {

                }

                @Override
                public boolean containsHeader(String s) {
                    return false;
                }

                @Override
                public String encodeURL(String s) {
                    return null;
                }

                @Override
                public String encodeRedirectURL(String s) {
                    return null;
                }

                @Override
                public String encodeUrl(String s) {
                    return null;
                }

                @Override
                public String encodeRedirectUrl(String s) {
                    return null;
                }

                @Override
                public void sendError(int i, String s) throws IOException {

                }

                @Override
                public void sendError(int i) throws IOException {

                }

                @Override
                public void sendRedirect(String s) throws IOException {

                }

                @Override
                public void setDateHeader(String s, long l) {

                }

                @Override
                public void addDateHeader(String s, long l) {

                }

                @Override
                public void setHeader(String s, String s1) {

                }

                @Override
                public void addHeader(String s, String s1) {

                }

                @Override
                public void setIntHeader(String s, int i) {

                }

                @Override
                public void addIntHeader(String s, int i) {

                }

                @Override
                public void setStatus(int i) {

                }

                @Override
                public void setStatus(int i, String s) {

                }

                @Override
                public int getStatus() {
                    return 0;
                }

                @Override
                public String getHeader(String s) {
                    return null;
                }

                @Override
                public Collection<String> getHeaders(String s) {
                    return null;
                }

                @Override
                public Collection<String> getHeaderNames() {
                    return null;
                }

                @Override
                public String getCharacterEncoding() {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public ServletOutputStream getOutputStream() throws IOException {
                    return null;
                }

                @Override
                public PrintWriter getWriter() throws IOException {
                    return null;
                }

                @Override
                public void setCharacterEncoding(String s) {

                }

                @Override
                public void setContentLength(int i) {

                }

                @Override
                public void setContentLengthLong(long l) {

                }

                @Override
                public void setContentType(String s) {

                }

                @Override
                public void setBufferSize(int i) {

                }

                @Override
                public int getBufferSize() {
                    return 0;
                }

                @Override
                public void flushBuffer() throws IOException {

                }

                @Override
                public void resetBuffer() {

                }

                @Override
                public boolean isCommitted() {
                    return false;
                }

                @Override
                public void reset() {

                }

                @Override
                public void setLocale(Locale locale) {

                }

                @Override
                public Locale getLocale() {
                    return null;
                }
            });
            System.out.println("密码错误登录结果：" + baseResult2);
            if (i == 3) {
                Assert.assertEquals(baseResult2.getCode(), Code.ACCOUNT_STATUS_LOCK.getCode());
            }else {
                Assert.assertEquals(baseResult2.getCode(), Code.ERROR_PASSWORD.getCode());
            }
        }
        //解锁后重新登录
        Thread.sleep(5000);//等待5秒 - 解锁
        BaseResult<Object> baseResult3 = accountService.login(accountName, pass, new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public String encodeUrl(String s) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public void setStatus(int i, String s) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        });
        System.out.println("解锁后登录结果：" + baseResult3);
        Assert.assertEquals(baseResult3.getCode(), Code.SUCCESS.getCode());
        BaseConstant.ACCOUNT_LOCK_SECOND = -3; //重置用户锁定时间为300秒（5分钟）

    }



}