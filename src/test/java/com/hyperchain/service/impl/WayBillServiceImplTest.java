package com.hyperchain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hyperchain.common.constant.Code;
import com.hyperchain.common.exception.*;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.exception.PropertiesLoadException;
import com.hyperchain.exception.ReadFileException;
import com.hyperchain.service.AccountService;
import com.hyperchain.service.WayBillService;
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by ldy on 2017/4/13.
 */
public class WayBillServiceImplTest extends SpringBaseTest{

    @Autowired
    UserEntityRepository userEntityRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    WayBillService wayBillService;

    @Test
    public void getCount() throws IOException, ValueNullException, GeneralSecurityException, PasswordIllegalParam, ReadFileException, UserInvalidException, ContractInvokeFailException, PropertiesLoadException, PrivateKeyIllegalParam {
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
        UserEntity userEntity = userEntityRepository.findByAccountName("account" + randomString);
        String address = userEntity.getAddress();

        BaseResult<Object> result1 = wayBillService.getAllWayBillCount(address);
        System.out.println(result1.toString());
        Assert.assertEquals(result1.getMessage(), 0);
        BaseResult<Object> result2= wayBillService.getWaitingWayBillCount(address);
        System.out.println(result2.toString());
        Assert.assertEquals(result1.getMessage(), 0);
        BaseResult<Object> result3 = wayBillService.getRequestingWayBillCount(address);
        System.out.println(result3.toString());
        Assert.assertEquals(result1.getMessage(), 0);
        BaseResult<Object> result4 = wayBillService.getSendingWayBillCount(address);
        System.out.println(result4.toString());
        Assert.assertEquals(result1.getMessage(), 0);
    }

}