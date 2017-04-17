package com.hyperchain.contract;

import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.constant.WayBillStatus;
import com.hyperchain.common.exception.ContractInvokeFailException;
import com.hyperchain.common.exception.PasswordIllegalParam;
import com.hyperchain.common.exception.PrivateKeyIllegalParam;
import com.hyperchain.common.exception.ValueNullException;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.AccountEntityRepository;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
import com.hyperchain.service.AccountService;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.*;

/**
 * Created by ldy on 2017/4/11.
 */
public class WayBillContractTest extends SpringBaseTest {

    @Autowired
    AccountService accountService;

    @Autowired
    UserEntityRepository userEntityRepository;
    @Autowired
    AccountEntityRepository accountEntityRepository;

    private String senderAddress;
    private String receiverAddress;
    private String logisticsAddress;
    private String senderAccountJson;
    private String receiverAccountJson;
    private String logisticsAccountJson;
    private String senderAccountName;
    private String receiverAccountName;
    private String logisticsAccountName;
    private String senderRepoAddress;
    private String receiverRepoAddress;
    private String senderAcctId;
    private String receiverAcctId;


    @Before
    public void init() throws PasswordIllegalParam, GeneralSecurityException, PrivateKeyIllegalParam, ContractInvokeFailException, IOException, ValueNullException {
        //发货企业注册
        String randomString = TestUtil.getRandomString();
        BaseResult<Object> result = accountService.register("account" + randomString, //unique
                "123",
                "卖家" + randomString, //unique
                "1881881" + randomString, //unique
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111" + randomString,
                "class",
                "acctSvcr",
                "acctSvcrName",
                new HttpServletRequest() {
                    @Override
                    public String getAuthType() {
                        return null;
                    }

                    @Override
                    public Cookie[] getCookies() {
                        return new Cookie[0];
                    }

                    @Override
                    public long getDateHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getHeader(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaders(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public int getIntHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getMethod() {
                        return null;
                    }

                    @Override
                    public String getPathInfo() {
                        return null;
                    }

                    @Override
                    public String getPathTranslated() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public String getQueryString() {
                        return null;
                    }

                    @Override
                    public String getRemoteUser() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String s) {
                        return false;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public String getRequestedSessionId() {
                        return null;
                    }

                    @Override
                    public String getRequestURI() {
                        return null;
                    }

                    @Override
                    public StringBuffer getRequestURL() {
                        return null;
                    }

                    @Override
                    public String getServletPath() {
                        return null;
                    }

                    @Override
                    public HttpSession getSession(boolean b) {
                        return null;
                    }

                    @Override
                    public HttpSession getSession() {
                        return null;
                    }

                    @Override
                    public String changeSessionId() {
                        return null;
                    }

                    @Override
                    public boolean isRequestedSessionIdValid() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromCookie() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromURL() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromUrl() {
                        return false;
                    }

                    @Override
                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
                        return false;
                    }

                    @Override
                    public void login(String s, String s1) throws ServletException {

                    }

                    @Override
                    public void logout() throws ServletException {

                    }

                    @Override
                    public Collection<Part> getParts() throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Part getPart(String s) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Object getAttribute(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getAttributeNames() {
                        return null;
                    }

                    @Override
                    public String getCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

                    }

                    @Override
                    public int getContentLength() {
                        return 0;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return 0;
                    }

                    @Override
                    public String getContentType() {
                        return null;
                    }

                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public String getParameter(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public String[] getParameterValues(String s) {
                        return new String[0];
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return null;
                    }

                    @Override
                    public String getProtocol() {
                        return null;
                    }

                    @Override
                    public String getScheme() {
                        return null;
                    }

                    @Override
                    public String getServerName() {
                        return null;
                    }

                    @Override
                    public int getServerPort() {
                        return 0;
                    }

                    @Override
                    public BufferedReader getReader() throws IOException {
                        return null;
                    }

                    @Override
                    public String getRemoteAddr() {
                        return null;
                    }

                    @Override
                    public String getRemoteHost() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String s, Object o) {

                    }

                    @Override
                    public void removeAttribute(String s) {

                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public Enumeration<Locale> getLocales() {
                        return null;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String s) {
                        return null;
                    }

                    @Override
                    public String getRealPath(String s) {
                        return null;
                    }

                    @Override
                    public int getRemotePort() {
                        return 0;
                    }

                    @Override
                    public String getLocalName() {
                        return null;
                    }

                    @Override
                    public String getLocalAddr() {
                        return null;
                    }

                    @Override
                    public int getLocalPort() {
                        return 0;
                    }

                    @Override
                    public ServletContext getServletContext() {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync() throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public boolean isAsyncStarted() {
                        return false;
                    }

                    @Override
                    public boolean isAsyncSupported() {
                        return false;
                    }

                    @Override
                    public AsyncContext getAsyncContext() {
                        return null;
                    }

                    @Override
                    public DispatcherType getDispatcherType() {
                        return null;
                    }
                },
                new HttpServletResponse() {
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
                }
        );
        System.out.println("发货企业注册返回结果：" + result.toString());
        Assert.assertEquals(result.getCode(), Code.SUCCESS.getCode());
        UserEntity senderAccountEntity = userEntityRepository.findByAccountName("account" + randomString);
        senderAccountJson = senderAccountEntity.getPrivateKey();
        senderAccountName = senderAccountEntity.getAccountName();
        senderAddress = senderAccountEntity.getAddress();
        senderAcctId = "11111" + randomString;
        System.out.println("发货企业地址：" + senderAddress);
        System.out.println("发货企业账户：" + senderAccountJson);
        System.out.println("发货企业名称：" + senderAccountName);

        //收货企业注册
        String randomString1 = TestUtil.getRandomString();
        BaseResult<Object> result1 = accountService.register("account" + randomString1, //unique
                "123",
                "买家" + randomString1, //unique
                "1881881" + randomString1, //unique
                0,
                "859051",
                4,
                "certType",
                "1111",
                "11111" + randomString1,
                "class",
                "acctSvcr",
                "acctSvcrName",
                new HttpServletRequest() {
                    @Override
                    public String getAuthType() {
                        return null;
                    }

                    @Override
                    public Cookie[] getCookies() {
                        return new Cookie[0];
                    }

                    @Override
                    public long getDateHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getHeader(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaders(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public int getIntHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getMethod() {
                        return null;
                    }

                    @Override
                    public String getPathInfo() {
                        return null;
                    }

                    @Override
                    public String getPathTranslated() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public String getQueryString() {
                        return null;
                    }

                    @Override
                    public String getRemoteUser() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String s) {
                        return false;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public String getRequestedSessionId() {
                        return null;
                    }

                    @Override
                    public String getRequestURI() {
                        return null;
                    }

                    @Override
                    public StringBuffer getRequestURL() {
                        return null;
                    }

                    @Override
                    public String getServletPath() {
                        return null;
                    }

                    @Override
                    public HttpSession getSession(boolean b) {
                        return null;
                    }

                    @Override
                    public HttpSession getSession() {
                        return null;
                    }

                    @Override
                    public String changeSessionId() {
                        return null;
                    }

                    @Override
                    public boolean isRequestedSessionIdValid() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromCookie() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromURL() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromUrl() {
                        return false;
                    }

                    @Override
                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
                        return false;
                    }

                    @Override
                    public void login(String s, String s1) throws ServletException {

                    }

                    @Override
                    public void logout() throws ServletException {

                    }

                    @Override
                    public Collection<Part> getParts() throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Part getPart(String s) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Object getAttribute(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getAttributeNames() {
                        return null;
                    }

                    @Override
                    public String getCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

                    }

                    @Override
                    public int getContentLength() {
                        return 0;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return 0;
                    }

                    @Override
                    public String getContentType() {
                        return null;
                    }

                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public String getParameter(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public String[] getParameterValues(String s) {
                        return new String[0];
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return null;
                    }

                    @Override
                    public String getProtocol() {
                        return null;
                    }

                    @Override
                    public String getScheme() {
                        return null;
                    }

                    @Override
                    public String getServerName() {
                        return null;
                    }

                    @Override
                    public int getServerPort() {
                        return 0;
                    }

                    @Override
                    public BufferedReader getReader() throws IOException {
                        return null;
                    }

                    @Override
                    public String getRemoteAddr() {
                        return null;
                    }

                    @Override
                    public String getRemoteHost() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String s, Object o) {

                    }

                    @Override
                    public void removeAttribute(String s) {

                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public Enumeration<Locale> getLocales() {
                        return null;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String s) {
                        return null;
                    }

                    @Override
                    public String getRealPath(String s) {
                        return null;
                    }

                    @Override
                    public int getRemotePort() {
                        return 0;
                    }

                    @Override
                    public String getLocalName() {
                        return null;
                    }

                    @Override
                    public String getLocalAddr() {
                        return null;
                    }

                    @Override
                    public int getLocalPort() {
                        return 0;
                    }

                    @Override
                    public ServletContext getServletContext() {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync() throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public boolean isAsyncStarted() {
                        return false;
                    }

                    @Override
                    public boolean isAsyncSupported() {
                        return false;
                    }

                    @Override
                    public AsyncContext getAsyncContext() {
                        return null;
                    }

                    @Override
                    public DispatcherType getDispatcherType() {
                        return null;
                    }
                },
                new HttpServletResponse() {
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
                }
        );
        System.out.println("收货企业注册返回结果：" + result1.toString());
        Assert.assertEquals(result1.getCode(), Code.SUCCESS.getCode());
        UserEntity receiverAccountEntity = userEntityRepository.findByAccountName("account" + randomString1);
        receiverAccountJson = receiverAccountEntity.getPrivateKey();
        receiverAccountName = receiverAccountEntity.getAccountName();
        receiverAddress = receiverAccountEntity.getAddress();
        receiverAcctId = "11111" + randomString1;
        System.out.println("收货企业地址：" + receiverAddress);
        System.out.println("收货企业账户：" + receiverAccountJson);
        System.out.println("收货企业名称：" + receiverAccountName);

        //物流公司注册
        String randomString2 = TestUtil.getRandomString();
        BaseResult<Object> result2 = accountService.register("account" + randomString2, //unique
                "123",
                "物流" + randomString2, //unique
                "1881881" + randomString2, //unique
                1,
                "859051",
                4,
                "certType",
                "1111",
                "11111" + randomString2,
                "class",
                "acctSvcr",
                "acctSvcrName",
                new HttpServletRequest() {
                    @Override
                    public String getAuthType() {
                        return null;
                    }

                    @Override
                    public Cookie[] getCookies() {
                        return new Cookie[0];
                    }

                    @Override
                    public long getDateHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getHeader(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaders(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public int getIntHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getMethod() {
                        return null;
                    }

                    @Override
                    public String getPathInfo() {
                        return null;
                    }

                    @Override
                    public String getPathTranslated() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public String getQueryString() {
                        return null;
                    }

                    @Override
                    public String getRemoteUser() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String s) {
                        return false;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public String getRequestedSessionId() {
                        return null;
                    }

                    @Override
                    public String getRequestURI() {
                        return null;
                    }

                    @Override
                    public StringBuffer getRequestURL() {
                        return null;
                    }

                    @Override
                    public String getServletPath() {
                        return null;
                    }

                    @Override
                    public HttpSession getSession(boolean b) {
                        return null;
                    }

                    @Override
                    public HttpSession getSession() {
                        return null;
                    }

                    @Override
                    public String changeSessionId() {
                        return null;
                    }

                    @Override
                    public boolean isRequestedSessionIdValid() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromCookie() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromURL() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromUrl() {
                        return false;
                    }

                    @Override
                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
                        return false;
                    }

                    @Override
                    public void login(String s, String s1) throws ServletException {

                    }

                    @Override
                    public void logout() throws ServletException {

                    }

                    @Override
                    public Collection<Part> getParts() throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Part getPart(String s) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Object getAttribute(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getAttributeNames() {
                        return null;
                    }

                    @Override
                    public String getCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

                    }

                    @Override
                    public int getContentLength() {
                        return 0;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return 0;
                    }

                    @Override
                    public String getContentType() {
                        return null;
                    }

                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public String getParameter(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public String[] getParameterValues(String s) {
                        return new String[0];
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return null;
                    }

                    @Override
                    public String getProtocol() {
                        return null;
                    }

                    @Override
                    public String getScheme() {
                        return null;
                    }

                    @Override
                    public String getServerName() {
                        return null;
                    }

                    @Override
                    public int getServerPort() {
                        return 0;
                    }

                    @Override
                    public BufferedReader getReader() throws IOException {
                        return null;
                    }

                    @Override
                    public String getRemoteAddr() {
                        return null;
                    }

                    @Override
                    public String getRemoteHost() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String s, Object o) {

                    }

                    @Override
                    public void removeAttribute(String s) {

                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public Enumeration<Locale> getLocales() {
                        return null;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String s) {
                        return null;
                    }

                    @Override
                    public String getRealPath(String s) {
                        return null;
                    }

                    @Override
                    public int getRemotePort() {
                        return 0;
                    }

                    @Override
                    public String getLocalName() {
                        return null;
                    }

                    @Override
                    public String getLocalAddr() {
                        return null;
                    }

                    @Override
                    public int getLocalPort() {
                        return 0;
                    }

                    @Override
                    public ServletContext getServletContext() {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync() throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public boolean isAsyncStarted() {
                        return false;
                    }

                    @Override
                    public boolean isAsyncSupported() {
                        return false;
                    }

                    @Override
                    public AsyncContext getAsyncContext() {
                        return null;
                    }

                    @Override
                    public DispatcherType getDispatcherType() {
                        return null;
                    }
                },
                new HttpServletResponse() {
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
                }
        );
        System.out.println("物流公司注册返回结果：" + result2.toString());
        Assert.assertEquals(result2.getCode(), Code.SUCCESS.getCode());
        UserEntity logisticsAccountEntity = userEntityRepository.findByAccountName("account" + randomString2);
        logisticsAccountJson = logisticsAccountEntity.getPrivateKey();
        logisticsAccountName = logisticsAccountEntity.getAccountName();
        logisticsAddress = logisticsAccountEntity.getAddress();
        System.out.println("物流公司地址：" + logisticsAddress);
        System.out.println("物流公司名称：" + logisticsAccountName);
        System.out.println("物流公司账户：" + logisticsAccountJson);

        //发货仓储注册
        String randomString3 = TestUtil.getRandomString();
        BaseResult<Object> result3 = accountService.register("account" + randomString3, //unique
                "123",
                "发货仓储" + randomString3, //unique
                "1881881" + randomString3, //unique
                2,
                "859051",
                4,
                "certType",
                "1111",
                "11111" + randomString3,
                "class",
                "acctSvcr",
                "acctSvcrName",
                new HttpServletRequest() {
                    @Override
                    public String getAuthType() {
                        return null;
                    }

                    @Override
                    public Cookie[] getCookies() {
                        return new Cookie[0];
                    }

                    @Override
                    public long getDateHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getHeader(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaders(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public int getIntHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getMethod() {
                        return null;
                    }

                    @Override
                    public String getPathInfo() {
                        return null;
                    }

                    @Override
                    public String getPathTranslated() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public String getQueryString() {
                        return null;
                    }

                    @Override
                    public String getRemoteUser() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String s) {
                        return false;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public String getRequestedSessionId() {
                        return null;
                    }

                    @Override
                    public String getRequestURI() {
                        return null;
                    }

                    @Override
                    public StringBuffer getRequestURL() {
                        return null;
                    }

                    @Override
                    public String getServletPath() {
                        return null;
                    }

                    @Override
                    public HttpSession getSession(boolean b) {
                        return null;
                    }

                    @Override
                    public HttpSession getSession() {
                        return null;
                    }

                    @Override
                    public String changeSessionId() {
                        return null;
                    }

                    @Override
                    public boolean isRequestedSessionIdValid() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromCookie() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromURL() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromUrl() {
                        return false;
                    }

                    @Override
                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
                        return false;
                    }

                    @Override
                    public void login(String s, String s1) throws ServletException {

                    }

                    @Override
                    public void logout() throws ServletException {

                    }

                    @Override
                    public Collection<Part> getParts() throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Part getPart(String s) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Object getAttribute(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getAttributeNames() {
                        return null;
                    }

                    @Override
                    public String getCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

                    }

                    @Override
                    public int getContentLength() {
                        return 0;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return 0;
                    }

                    @Override
                    public String getContentType() {
                        return null;
                    }

                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public String getParameter(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public String[] getParameterValues(String s) {
                        return new String[0];
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return null;
                    }

                    @Override
                    public String getProtocol() {
                        return null;
                    }

                    @Override
                    public String getScheme() {
                        return null;
                    }

                    @Override
                    public String getServerName() {
                        return null;
                    }

                    @Override
                    public int getServerPort() {
                        return 0;
                    }

                    @Override
                    public BufferedReader getReader() throws IOException {
                        return null;
                    }

                    @Override
                    public String getRemoteAddr() {
                        return null;
                    }

                    @Override
                    public String getRemoteHost() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String s, Object o) {

                    }

                    @Override
                    public void removeAttribute(String s) {

                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public Enumeration<Locale> getLocales() {
                        return null;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String s) {
                        return null;
                    }

                    @Override
                    public String getRealPath(String s) {
                        return null;
                    }

                    @Override
                    public int getRemotePort() {
                        return 0;
                    }

                    @Override
                    public String getLocalName() {
                        return null;
                    }

                    @Override
                    public String getLocalAddr() {
                        return null;
                    }

                    @Override
                    public int getLocalPort() {
                        return 0;
                    }

                    @Override
                    public ServletContext getServletContext() {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync() throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public boolean isAsyncStarted() {
                        return false;
                    }

                    @Override
                    public boolean isAsyncSupported() {
                        return false;
                    }

                    @Override
                    public AsyncContext getAsyncContext() {
                        return null;
                    }

                    @Override
                    public DispatcherType getDispatcherType() {
                        return null;
                    }
                },
                new HttpServletResponse() {
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
                }
        );
        System.out.println("发货仓储注册返回结果：" + result3.toString());
        Assert.assertEquals(result3.getCode(), Code.SUCCESS.getCode());
        UserEntity senderRepoAccountEntity = userEntityRepository.findByAccountName("account" + randomString3);
        senderRepoAddress = senderRepoAccountEntity.getAddress();
        System.out.println("发货仓储地址：" + senderRepoAddress);

        //收货仓储注册
        String randomString4 = TestUtil.getRandomString();
        BaseResult<Object> result4 = accountService.register("account" + randomString4, //unique
                "123",
                "收货仓储" + randomString4, //unique
                "1881881" + randomString4, //unique
                2,
                "859051",
                4,
                "certType",
                "1111",
                "11111" + randomString4,
                "class",
                "acctSvcr",
                "acctSvcrName",
                new HttpServletRequest() {
                    @Override
                    public String getAuthType() {
                        return null;
                    }

                    @Override
                    public Cookie[] getCookies() {
                        return new Cookie[0];
                    }

                    @Override
                    public long getDateHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getHeader(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaders(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public int getIntHeader(String s) {
                        return 0;
                    }

                    @Override
                    public String getMethod() {
                        return null;
                    }

                    @Override
                    public String getPathInfo() {
                        return null;
                    }

                    @Override
                    public String getPathTranslated() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public String getQueryString() {
                        return null;
                    }

                    @Override
                    public String getRemoteUser() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String s) {
                        return false;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public String getRequestedSessionId() {
                        return null;
                    }

                    @Override
                    public String getRequestURI() {
                        return null;
                    }

                    @Override
                    public StringBuffer getRequestURL() {
                        return null;
                    }

                    @Override
                    public String getServletPath() {
                        return null;
                    }

                    @Override
                    public HttpSession getSession(boolean b) {
                        return null;
                    }

                    @Override
                    public HttpSession getSession() {
                        return null;
                    }

                    @Override
                    public String changeSessionId() {
                        return null;
                    }

                    @Override
                    public boolean isRequestedSessionIdValid() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromCookie() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromURL() {
                        return false;
                    }

                    @Override
                    public boolean isRequestedSessionIdFromUrl() {
                        return false;
                    }

                    @Override
                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
                        return false;
                    }

                    @Override
                    public void login(String s, String s1) throws ServletException {

                    }

                    @Override
                    public void logout() throws ServletException {

                    }

                    @Override
                    public Collection<Part> getParts() throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Part getPart(String s) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
                        return null;
                    }

                    @Override
                    public Object getAttribute(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getAttributeNames() {
                        return null;
                    }

                    @Override
                    public String getCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

                    }

                    @Override
                    public int getContentLength() {
                        return 0;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return 0;
                    }

                    @Override
                    public String getContentType() {
                        return null;
                    }

                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public String getParameter(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public String[] getParameterValues(String s) {
                        return new String[0];
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return null;
                    }

                    @Override
                    public String getProtocol() {
                        return null;
                    }

                    @Override
                    public String getScheme() {
                        return null;
                    }

                    @Override
                    public String getServerName() {
                        return null;
                    }

                    @Override
                    public int getServerPort() {
                        return 0;
                    }

                    @Override
                    public BufferedReader getReader() throws IOException {
                        return null;
                    }

                    @Override
                    public String getRemoteAddr() {
                        return null;
                    }

                    @Override
                    public String getRemoteHost() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String s, Object o) {

                    }

                    @Override
                    public void removeAttribute(String s) {

                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public Enumeration<Locale> getLocales() {
                        return null;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String s) {
                        return null;
                    }

                    @Override
                    public String getRealPath(String s) {
                        return null;
                    }

                    @Override
                    public int getRemotePort() {
                        return 0;
                    }

                    @Override
                    public String getLocalName() {
                        return null;
                    }

                    @Override
                    public String getLocalAddr() {
                        return null;
                    }

                    @Override
                    public int getLocalPort() {
                        return 0;
                    }

                    @Override
                    public ServletContext getServletContext() {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync() throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public boolean isAsyncStarted() {
                        return false;
                    }

                    @Override
                    public boolean isAsyncSupported() {
                        return false;
                    }

                    @Override
                    public AsyncContext getAsyncContext() {
                        return null;
                    }

                    @Override
                    public DispatcherType getDispatcherType() {
                        return null;
                    }
                },
                new HttpServletResponse() {
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
                }
        );
        System.out.println("收货仓储注册返回结果：" + result4.toString());
        Assert.assertEquals(result4.getCode(), Code.SUCCESS.getCode());
        UserEntity receiverRepoAccountEntity = userEntityRepository.findByAccountName("account" + randomString4);
        receiverRepoAddress = receiverRepoAccountEntity.getAddress();
        System.out.println("收货仓储地址：" + receiverRepoAddress);

    }

    @Test
    public void testWayBill() throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        String accountContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);
        String receivableContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_RECEIVABLE);
        String orderContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);
        String repoContractAddr = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_REPOSITORY);

        String random = TestUtil.getRandomString();

        //初始化运单状态（当应收账款状态为承兑已签收）
//        ContractKey initContractKey = new ContractKey(senderAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + senderAccountJson);
//        String initContractMethodName = "initWayBillStatus";
//        Object[] initContractMethodParams = new Object[4];
//        initContractMethodParams[0] = "123订单" + random; //orderNo
//        initContractMethodParams[1] = new Date().getTime(); //waitTime
//        initContractMethodParams[2] = senderAddress; //senderAddress
//        initContractMethodParams[3] = receiverAddress; //receiverAddress
//        String[] initResultMapKey = new String[]{};
//        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
//        ContractResult initContractResult = ContractUtil.invokeContract(initContractKey, initContractMethodName, initContractMethodParams, initResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
//        System.out.println("调用合约initWayBillStatus返回code：" + initContractResult.getCode());
//        System.out.println("调用合约initWayBillStatus返回结果：" + initContractResult.toString());
//        Assert.assertEquals(Code.SUCCESS, initContractResult.getCode());
        testReceivable("123订单" + random);

        //卖家企业发货申请，生成未完善运单
        ContractKey requestContractKey = new ContractKey(senderAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + senderAccountName);
        String requestContractMethodName = "generateUnConfirmedWayBill";
        Object[] requestContractMethodParams = new Object[5];
        Long[] requestLongs = new Long[3];
        requestLongs[0] = new Date().getTime(); //requestTime
        requestLongs[1] = new Long(100000); //productValue
        requestLongs[2] = new Long(1000); //productQuantity
        String[] requestAddrs = new String[5];
        requestAddrs[0] = logisticsAddress; //logisticsAddress
        requestAddrs[1] = senderAddress; //senderAddress
        requestAddrs[2] = receiverAddress; //receiverAddress
        requestAddrs[3] = receiverRepoAddress; //receiverRepoAddress
        requestAddrs[4] = senderRepoAddress; //senderRepoAddress
        String[] requestStrs = new String[5];
        requestStrs[0] = "123订单" + random; //orderNo
        requestStrs[1] = "productName"; //productName
        requestStrs[2] = "senderRepoCertNo"; //senderRepoCertNo
        requestStrs[3] = "receiverRepoBusinessNo"; //receiverRepoBusinessNo
        requestStrs[4] = requestStrs[0] + WayBillStatus.REQUESTING.getCode(); //statusTransId: orderNo + WayBillStatus
        requestContractMethodParams[0] = requestLongs;
        requestContractMethodParams[1] = requestAddrs;
        requestContractMethodParams[2] = requestStrs;
        requestContractMethodParams[3] = accountContractAddr;
        requestContractMethodParams[4] = receivableContractAddr;
        String[] requestResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult = ContractUtil.invokeContract(requestContractKey, requestContractMethodName, requestContractMethodParams, requestResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        System.out.println("调用合约generateUnConfirmedWayBill返回code：" + requestContractResult.getCode());
        System.out.println("调用合约generateUnConfirmedWayBill返回结果：" + requestContractResult.toString());
        Assert.assertEquals(Code.SUCCESS, requestContractResult.getCode());

        //物流确认发货，生成完整运单
        ContractKey confirmContractKey = new ContractKey(logisticsAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + logisticsAccountName);
        String confirmContractMethodName = "generateWayBill";
        Object[] confirmContractMethodParams = new Object[5];
        confirmContractMethodParams[0] = "123订单" + random; //orderNo
        confirmContractMethodParams[1] = "123订单" + random + WayBillStatus.SENDING.getCode(); //statusTransId: orderNo + WayBillStatus
        confirmContractMethodParams[2] = "123运单" + random; //wayBillNo
        confirmContractMethodParams[3] = new Date().getTime(); //sendTime
        confirmContractMethodParams[4] = accountContractAddr; //accountContractAddr
        String[] confirmResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult confirmContractResult = ContractUtil.invokeContract(confirmContractKey, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        System.out.println("调用合约generateWayBill返回code：" + confirmContractResult.getCode());
        System.out.println("调用合约generateWayBill返回结果：" + confirmContractResult.toString());
        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());

        //更新运单状态为已送达
        ContractKey updateToReceivedContractKey = new ContractKey(logisticsAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + logisticsAccountName);
        String updateToReceivedMethodName = "updateWayBillStatusToReceived";
        Object[] updateToReceivedMethodParams = new Object[6];
        updateToReceivedMethodParams[0] = "123订单" + random; //orderNo
        updateToReceivedMethodParams[1] = "123订单" + random + WayBillStatus.RECEIVED.getCode(); //statusTransId: orderNo + WayBillStatus
        updateToReceivedMethodParams[2] = new Date().getTime(); //operateTime
        updateToReceivedMethodParams[3] = accountContractAddr; //accountContractAddr
        updateToReceivedMethodParams[4] = repoContractAddr; //repoContractAddr
        updateToReceivedMethodParams[5] = orderContractAddr; //orderContractAddr
        String[] updateToReceivedResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult updateToReceivedResult = ContractUtil.invokeContract(updateToReceivedContractKey, updateToReceivedMethodName, updateToReceivedMethodParams, updateToReceivedResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        System.out.println("调用合约updateWayBillStatusToReceived返回code：" + updateToReceivedResult.getCode());
        System.out.println("调用合约updateWayBillStatusToReceived返回结果：" + updateToReceivedResult.toString());
        Assert.assertEquals(Code.SUCCESS, updateToReceivedResult.getCode());

        //获取所有用户相关运单的订单号列表
        ContractKey listOrderNoContractKey = new ContractKey(logisticsAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + logisticsAccountName);
        String listOrderNoContractMethodName = "listWayBillOrderNo";
        Object[] listOrderNoContractMethodParams = new Object[1];
        listOrderNoContractMethodParams[0] = accountContractAddr; //accountContractAddr
        String[] listOrderNoResultMapKey = new String[]{"orderNoList"};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult listOrderNoContractResult = ContractUtil.invokeContract(listOrderNoContractKey, listOrderNoContractMethodName, listOrderNoContractMethodParams, listOrderNoResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        System.out.println("调用合约listWayBillOrderNo返回code：" + listOrderNoContractResult.getCode());
        System.out.println("调用合约getWayBill返回结果：" + listOrderNoContractResult.toString());
        Assert.assertEquals(Code.SUCCESS, listOrderNoContractResult.getCode());
        List<String> orderNoList = (List<String>) listOrderNoContractResult.getValue().get(0); //注意：getValue()返回的是所有结果的List！如果只有一个结果，须取下标为0的结果！
        for (int i = 0; i < orderNoList.size(); i++) {
            System.out.println("运单订单号" + i + " : " + orderNoList.get(i));
        }

        //获取最后一个运单信息
        ContractKey waybillContractKey = new ContractKey(logisticsAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + logisticsAccountName);
        String waybillContractMethodName = "getWayBill";
        Object[] waybillContractMethodParams = new Object[2];
        waybillContractMethodParams[0] = orderNoList.get(orderNoList.size() - 1); //orderNo
        System.out.println("===========请求订单号：" + waybillContractMethodParams[0]);
//        waybillContractMethodParams[0] = "123订单" + random; //orderNo
        waybillContractMethodParams[1] = accountContractAddr; //accountContractAddr
        String[] waybillResultMapKey = new String[]{"longs", "strs", "addrs", "logisticsInfo"};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult waybillContractResult = ContractUtil.invokeContract(waybillContractKey, waybillContractMethodName, waybillContractMethodParams, waybillResultMapKey, BaseConstant.CONTRACT_NAME_WAYBILL);
        System.out.println("调用合约getWayBill返回code：" + waybillContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, waybillContractResult.getCode());
        Map<String, Object> waybillResultValueMap = waybillContractResult.getValueMap();
        System.out.println("调用合约getWayBill返回结果：" + waybillContractResult.toString());
        List<Long> longs = (List<Long>) waybillResultValueMap.get("longs");
        List<String> strs = (List<String>) waybillResultValueMap.get("strs");
        List<String> addrs = (List<String>) waybillResultValueMap.get("addrs");
        List<String> logisticsInfo = (List<String>) waybillResultValueMap.get("logisticsInfo");
        System.out.println("productQuantity: " + longs.get(0));
        System.out.println("productValue: " + longs.get(1));
        System.out.println("requestTime: " + longs.get(2));
        System.out.println("receiveTime: " + longs.get(3));
        System.out.println("sendTime: " + longs.get(4));
        System.out.println("rejectTime: " + longs.get(5));
        System.out.println("waitTime: " + longs.get(6));
        System.out.println("wayBillStatus: " + longs.get(7));
        System.out.println("orderNo: " + strs.get(0));
        System.out.println("wayBillNo: " + strs.get(1));
        System.out.println("productName: " + strs.get(2));
        System.out.println("senderRepoCertNo: " + strs.get(3));
        System.out.println("receiverRepoBusinessNo: " + strs.get(4));
        System.out.println("logisticsAddress: " + addrs.get(0));
        System.out.println("senderAddress: " + addrs.get(1));
        System.out.println("receiverAddress: " + addrs.get(2));
        System.out.println("senderRepoAddress: " + addrs.get(3));
        System.out.println("receiverRepoAddress: " + addrs.get(4));
        System.out.println("logisticsInfo: " + logisticsInfo);

    }

    public void testReceivable(String orderNumber) throws PrivateKeyIllegalParam, ContractInvokeFailException, ValueNullException, PasswordIllegalParam, ReadFileException, PropertiesLoadException, GeneralSecurityException, IOException {

        //账户信息存储到区块链
        ContractKey contractKey = new ContractKey(receiverAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + receiverAccountJson);
        String requestContractMethodName = "signOutApply";
        Object[] requestContractMethodParams = new Object[12];
        String receNo = "rece";
        String orderNo = orderNumber;
        String signer = senderAcctId;
        String acctptr = receiverAcctId;
        String pyee = senderAcctId;
        String pyer = receiverAcctId;
        long isseAmt = 100000;
        long dueDt = 20170413;
        String rate = "9.8";
        String[] conAndInv = new String[2];
        conAndInv[0] = "con";
        conAndInv[1] = "inv";
        String serialNo = "seNo";
        long time = 888888;

        requestContractMethodParams[0] = receNo;
        requestContractMethodParams[1] = orderNo;
        requestContractMethodParams[2] = signer;
        requestContractMethodParams[3] = acctptr;
        requestContractMethodParams[4] = pyee;
        requestContractMethodParams[5] = pyer;
        requestContractMethodParams[6] = isseAmt;
        requestContractMethodParams[7] = dueDt;
        requestContractMethodParams[8] = rate;
        requestContractMethodParams[9] = conAndInv;
        requestContractMethodParams[10] = serialNo;
        requestContractMethodParams[11] = time;

        String[] requestResultMapKey = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult = ContractUtil.invokeContract(contractKey, requestContractMethodName, requestContractMethodParams, requestResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约签发申请返回code：" + requestContractResult.getCode());
        Assert.assertEquals(Code.SUCCESS, requestContractResult.getCode());

        //签发回复
        //账户信息存储到区块链
        ContractKey contractKey2 = new ContractKey(senderAccountJson, BaseConstant.SALT_FOR_PRIVATE_KEY + senderAccountJson);

        String requestContractMethodName2 = "signOutReply";
        Object[] requestContractMethodParams2= new Object[7];
        String receivableNo2 = "rece";
        String replyerAcctId2 = receiverAcctId;
        int response2 = 0;
        String serialNo2 = "serialNo";
        int time2 = 20170416;
        String accountContractAddress2 = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);//Account合约地址
        String wayBillContractAddress2 = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_WAYBILL);//wayBill合约地址

        requestContractMethodParams2[0] = receivableNo2;
        requestContractMethodParams2[1] = replyerAcctId2;
        requestContractMethodParams2[2] = response2;
        requestContractMethodParams2[3] = serialNo2;
        requestContractMethodParams2[4] = time2;
        requestContractMethodParams2[5] = accountContractAddress2;
        requestContractMethodParams2[6] = wayBillContractAddress2;


        String[] requestResultMapKey2 = new String[]{};
        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
        ContractResult requestContractResult2 = ContractUtil.invokeContract(contractKey2, requestContractMethodName2, requestContractMethodParams2, requestResultMapKey2, BaseConstant.CONTRACT_NAME_RECEIVABLE);
        System.out.println("调用合约签发回复返回code：" + requestContractResult2.getCode().getCode());
        Assert.assertEquals(Code.SUCCESS, requestContractResult2.getCode());


//
//        //应收款列表
//        List<String> keyInfo1 = ESDKUtil.newAccount(BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD); //加密私钥
//        String accountJson1 = keyInfo1.get(1); //含address 私钥
//
//        //账户信息存储到区块链
//        ContractKey contractKey1 = new ContractKey(accountJson1, BaseConstant.DEFAULT_PRIVATE_KEY_PASSWORD);
//        String confirmContractMethodName = "receivableSimpleDeatilList";
//        Object[] confirmContractMethodParams = new Object[4];
//        confirmContractMethodParams[0] = 0; //orderNo
//        confirmContractMethodParams[1] = "pyer"; //statusTransId: orderNo + WayBillStatus
//        confirmContractMethodParams[2] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ORDER);
//        confirmContractMethodParams[3] = ESDKUtil.getHyperchainInfo(BaseConstant.CONTRACT_NAME_ACCOUNT);
//        String[] confirmResultMapKey = new String[]{};
//        // 利用（合约钥匙，合约方法名，合约方法参数，合约方法返回值名）获取调用合约结果
//        ContractResult confirmContractResult = ContractUtil.invokeContract(contractKey1, confirmContractMethodName, confirmContractMethodParams, confirmResultMapKey, BaseConstant.CONTRACT_NAME_RECEIVABLE);
//        System.out.println("调用合约generateWayBill返回code：" + confirmContractResult.getCode());
//        System.out.println("调用合约签发申请返回list：" + confirmContractResult);
//        Assert.assertEquals(Code.SUCCESS, confirmContractResult.getCode());
    }

}
