//package com.hyperchain.service.impl;
//
//import com.hyperchain.common.constant.Code;
//import com.hyperchain.common.exception.ContractInvokeFailException;
//import com.hyperchain.common.exception.PasswordIllegalParam;
//import com.hyperchain.common.exception.PrivateKeyIllegalParam;
//import com.hyperchain.common.exception.ValueNullException;
//import com.hyperchain.controller.vo.BaseResult;
//import com.hyperchain.dal.entity.UserEntity;
//import com.hyperchain.dal.repository.UserEntityRepository;
//import com.hyperchain.exception.PropertiesLoadException;
//import com.hyperchain.exception.ReadFileException;
//import com.hyperchain.service.AccountService;
//import com.hyperchain.service.WayBillService;
//import com.hyperchain.test.TestUtil;
//import com.hyperchain.test.base.SpringBaseTest;
//import org.junit.Assert;
//import org.junit.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.*;
//import javax.servlet.http.*;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.security.GeneralSecurityException;
//import java.security.Principal;
//import java.util.Collection;
//import java.util.Enumeration;
//import java.util.Locale;
//import java.util.Map;
//
//import static org.junit.Assert.*;
//
///**
// * Created by ldy on 2017/4/13.
// */
//public class WayBillServiceImplTest extends SpringBaseTest{
//
//    @Autowired
//    UserEntityRepository userEntityRepository;
//    @Autowired
//    AccountService accountService;
//    @Autowired
//    WayBillService wayBillService;
//
//    private String senderAddress;
//    private String receiverAddress;
//    private String logisticsAddress;
//    private String senderAccountJson;
//    private String receiverAccountJson;
//    private String logisticsAccountJson;
//    private String senderAccountName;
//    private String receiverAccountName;
//    private String logisticsAccountName;
//    private String senderRepoAddress;
//    private String receiverRepoAddress;
//    private String senderEnterpriseName;
//    private String receiverEnterpriseName;
//    private String logisticsEnterpriseName;
//    private String senderRepoEnterpriseName;
//    private String receiverRepoEnterpriseName;
//
//    private HttpServletRequest request;
//
//    @Before
//    public void init() throws PasswordIllegalParam, GeneralSecurityException, PrivateKeyIllegalParam, ContractInvokeFailException, IOException, ValueNullException {
//
//        request = new HttpServletRequest() {
//            @Override
//            public String getAuthType() {
//                return null;
//            }
//
//            @Override
//            public Cookie[] getCookies() {
//                return new Cookie[0];
//            }
//
//            @Override
//            public long getDateHeader(String s) {
//                return 0;
//            }
//
//            @Override
//            public String getHeader(String s) {
//                return null;
//            }
//
//            @Override
//            public Enumeration<String> getHeaders(String s) {
//                return null;
//            }
//
//            @Override
//            public Enumeration<String> getHeaderNames() {
//                return null;
//            }
//
//            @Override
//            public int getIntHeader(String s) {
//                return 0;
//            }
//
//            @Override
//            public String getMethod() {
//                return null;
//            }
//
//            @Override
//            public String getPathInfo() {
//                return null;
//            }
//
//            @Override
//            public String getPathTranslated() {
//                return null;
//            }
//
//            @Override
//            public String getContextPath() {
//                return null;
//            }
//
//            @Override
//            public String getQueryString() {
//                return null;
//            }
//
//            @Override
//            public String getRemoteUser() {
//                return null;
//            }
//
//            @Override
//            public boolean isUserInRole(String s) {
//                return false;
//            }
//
//            @Override
//            public Principal getUserPrincipal() {
//                return null;
//            }
//
//            @Override
//            public String getRequestedSessionId() {
//                return null;
//            }
//
//            @Override
//            public String getRequestURI() {
//                return null;
//            }
//
//            @Override
//            public StringBuffer getRequestURL() {
//                return null;
//            }
//
//            @Override
//            public String getServletPath() {
//                return null;
//            }
//
//            @Override
//            public HttpSession getSession(boolean b) {
//                return null;
//            }
//
//            @Override
//            public HttpSession getSession() {
//                return null;
//            }
//
//            @Override
//            public String changeSessionId() {
//                return null;
//            }
//
//            @Override
//            public boolean isRequestedSessionIdValid() {
//                return false;
//            }
//
//            @Override
//            public boolean isRequestedSessionIdFromCookie() {
//                return false;
//            }
//
//            @Override
//            public boolean isRequestedSessionIdFromURL() {
//                return false;
//            }
//
//            @Override
//            public boolean isRequestedSessionIdFromUrl() {
//                return false;
//            }
//
//            @Override
//            public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
//                return false;
//            }
//
//            @Override
//            public void login(String s, String s1) throws ServletException {
//
//            }
//
//            @Override
//            public void logout() throws ServletException {
//
//            }
//
//            @Override
//            public Collection<Part> getParts() throws IOException, ServletException {
//                return null;
//            }
//
//            @Override
//            public Part getPart(String s) throws IOException, ServletException {
//                return null;
//            }
//
//            @Override
//            public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
//                return null;
//            }
//
//            @Override
//            public Object getAttribute(String s) {
//                return null;
//            }
//
//            @Override
//            public Enumeration<String> getAttributeNames() {
//                return null;
//            }
//
//            @Override
//            public String getCharacterEncoding() {
//                return null;
//            }
//
//            @Override
//            public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
//
//            }
//
//            @Override
//            public int getContentLength() {
//                return 0;
//            }
//
//            @Override
//            public long getContentLengthLong() {
//                return 0;
//            }
//
//            @Override
//            public String getContentType() {
//                return null;
//            }
//
//            @Override
//            public ServletInputStream getInputStream() throws IOException {
//                return null;
//            }
//
//            @Override
//            public String getParameter(String s) {
//                return null;
//            }
//
//            @Override
//            public Enumeration<String> getParameterNames() {
//                return null;
//            }
//
//            @Override
//            public String[] getParameterValues(String s) {
//                return new String[0];
//            }
//
//            @Override
//            public Map<String, String[]> getParameterMap() {
//                return null;
//            }
//
//            @Override
//            public String getProtocol() {
//                return null;
//            }
//
//            @Override
//            public String getScheme() {
//                return null;
//            }
//
//            @Override
//            public String getServerName() {
//                return null;
//            }
//
//            @Override
//            public int getServerPort() {
//                return 0;
//            }
//
//            @Override
//            public BufferedReader getReader() throws IOException {
//                return null;
//            }
//
//            @Override
//            public String getRemoteAddr() {
//                return null;
//            }
//
//            @Override
//            public String getRemoteHost() {
//                return null;
//            }
//
//            @Override
//            public void setAttribute(String s, Object o) {
//
//            }
//
//            @Override
//            public void removeAttribute(String s) {
//
//            }
//
//            @Override
//            public Locale getLocale() {
//                return null;
//            }
//
//            @Override
//            public Enumeration<Locale> getLocales() {
//                return null;
//            }
//
//            @Override
//            public boolean isSecure() {
//                return false;
//            }
//
//            @Override
//            public RequestDispatcher getRequestDispatcher(String s) {
//                return null;
//            }
//
//            @Override
//            public String getRealPath(String s) {
//                return null;
//            }
//
//            @Override
//            public int getRemotePort() {
//                return 0;
//            }
//
//            @Override
//            public String getLocalName() {
//                return null;
//            }
//
//            @Override
//            public String getLocalAddr() {
//                return null;
//            }
//
//            @Override
//            public int getLocalPort() {
//                return 0;
//            }
//
//            @Override
//            public ServletContext getServletContext() {
//                return null;
//            }
//
//            @Override
//            public AsyncContext startAsync() throws IllegalStateException {
//                return null;
//            }
//
//            @Override
//            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
//                return null;
//            }
//
//            @Override
//            public boolean isAsyncStarted() {
//                return false;
//            }
//
//            @Override
//            public boolean isAsyncSupported() {
//                return false;
//            }
//
//            @Override
//            public AsyncContext getAsyncContext() {
//                return null;
//            }
//
//            @Override
//            public DispatcherType getDispatcherType() {
//                return null;
//            }
//        }
//
//        //发货企业注册
//        String randomString = TestUtil.getRandomString();
//        BaseResult<Object> result = accountService.register("account" + randomString, //unique
//                "123",
//                "企业" + randomString, //unique
//                "1881881" + randomString, //unique
//                0,
//                "859051",
//                4,
//                "certType",
//                "1111",
//                "11111",
//                "class",
//                "acctSvcr",
//                "acctSvcrName",
//                new HttpServletRequest() {
//                    @Override
//                    public String getAuthType() {
//                        return null;
//                    }
//
//                    @Override
//                    public Cookie[] getCookies() {
//                        return new Cookie[0];
//                    }
//
//                    @Override
//                    public long getDateHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getIntHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getMethod() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathInfo() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathTranslated() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContextPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getQueryString() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteUser() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isUserInRole(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public Principal getUserPrincipal() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestedSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestURI() {
//                        return null;
//                    }
//
//                    @Override
//                    public StringBuffer getRequestURL() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServletPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession(boolean b) {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession() {
//                        return null;
//                    }
//
//                    @Override
//                    public String changeSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdValid() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromCookie() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromURL() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromUrl() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
//                        return false;
//                    }
//
//                    @Override
//                    public void login(String s, String s1) throws ServletException {
//
//                    }
//
//                    @Override
//                    public void logout() throws ServletException {
//
//                    }
//
//                    @Override
//                    public Collection<Part> getParts() throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Part getPart(String s) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getAttribute(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getAttributeNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
//
//                    }
//
//                    @Override
//                    public int getContentLength() {
//                        return 0;
//                    }
//
//                    @Override
//                    public long getContentLengthLong() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletInputStream getInputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getParameter(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getParameterNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String[] getParameterValues(String s) {
//                        return new String[0];
//                    }
//
//                    @Override
//                    public Map<String, String[]> getParameterMap() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getProtocol() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getScheme() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServerName() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getServerPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public BufferedReader getReader() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteHost() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setAttribute(String s, Object o) {
//
//                    }
//
//                    @Override
//                    public void removeAttribute(String s) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<Locale> getLocales() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isSecure() {
//                        return false;
//                    }
//
//                    @Override
//                    public RequestDispatcher getRequestDispatcher(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRealPath(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public int getRemotePort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getLocalName() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getLocalAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getLocalPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public ServletContext getServletContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync() throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isAsyncStarted() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isAsyncSupported() {
//                        return false;
//                    }
//
//                    @Override
//                    public AsyncContext getAsyncContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public DispatcherType getDispatcherType() {
//                        return null;
//                    }
//                },
//                new HttpServletResponse() {
//                    @Override
//                    public void addCookie(Cookie cookie) {
//
//                    }
//
//                    @Override
//                    public boolean containsHeader(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public String encodeURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public void sendError(int i, String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendError(int i) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendRedirect(String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void setDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void addDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void setHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void addHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void setIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void addIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i, String s) {
//
//                    }
//
//                    @Override
//                    public int getStatus() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletOutputStream getOutputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public PrintWriter getWriter() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) {
//
//                    }
//
//                    @Override
//                    public void setContentLength(int i) {
//
//                    }
//
//                    @Override
//                    public void setContentLengthLong(long l) {
//
//                    }
//
//                    @Override
//                    public void setContentType(String s) {
//
//                    }
//
//                    @Override
//                    public void setBufferSize(int i) {
//
//                    }
//
//                    @Override
//                    public int getBufferSize() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void flushBuffer() throws IOException {
//
//                    }
//
//                    @Override
//                    public void resetBuffer() {
//
//                    }
//
//                    @Override
//                    public boolean isCommitted() {
//                        return false;
//                    }
//
//                    @Override
//                    public void reset() {
//
//                    }
//
//                    @Override
//                    public void setLocale(Locale locale) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//                }
//        );
//        System.out.println("发货企业注册返回结果：" + result.toString());
//        Assert.assertEquals(result.getCode(), Code.SUCCESS.getCode());
//        UserEntity senderAccountEntity = userEntityRepository.findByAccountName("account" + randomString);
//        senderAccountJson = senderAccountEntity.getPrivateKey();
//        senderAccountName = senderAccountEntity.getAccountName();
//        senderAddress = senderAccountEntity.getAddress();
//        senderEnterpriseName = senderAccountEntity.getCompanyName();
//        System.out.println("发货企业地址：" + senderAddress);
//        System.out.println("发货企业账户：" + senderAccountJson);
//        System.out.println("发货企业名称：" + senderAccountName);
//
//        //收货企业注册
//        String randomString1 = TestUtil.getRandomString();
//        BaseResult<Object> result1 = accountService.register("account" + randomString1, //unique
//                "123",
//                "企业" + randomString1, //unique
//                "1881881" + randomString1, //unique
//                0,
//                "859051",
//                4,
//                "certType",
//                "1111",
//                "11111",
//                "class",
//                "acctSvcr",
//                "acctSvcrName",
//                new HttpServletRequest() {
//                    @Override
//                    public String getAuthType() {
//                        return null;
//                    }
//
//                    @Override
//                    public Cookie[] getCookies() {
//                        return new Cookie[0];
//                    }
//
//                    @Override
//                    public long getDateHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getIntHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getMethod() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathInfo() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathTranslated() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContextPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getQueryString() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteUser() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isUserInRole(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public Principal getUserPrincipal() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestedSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestURI() {
//                        return null;
//                    }
//
//                    @Override
//                    public StringBuffer getRequestURL() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServletPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession(boolean b) {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession() {
//                        return null;
//                    }
//
//                    @Override
//                    public String changeSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdValid() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromCookie() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromURL() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromUrl() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
//                        return false;
//                    }
//
//                    @Override
//                    public void login(String s, String s1) throws ServletException {
//
//                    }
//
//                    @Override
//                    public void logout() throws ServletException {
//
//                    }
//
//                    @Override
//                    public Collection<Part> getParts() throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Part getPart(String s) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getAttribute(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getAttributeNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
//
//                    }
//
//                    @Override
//                    public int getContentLength() {
//                        return 0;
//                    }
//
//                    @Override
//                    public long getContentLengthLong() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletInputStream getInputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getParameter(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getParameterNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String[] getParameterValues(String s) {
//                        return new String[0];
//                    }
//
//                    @Override
//                    public Map<String, String[]> getParameterMap() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getProtocol() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getScheme() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServerName() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getServerPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public BufferedReader getReader() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteHost() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setAttribute(String s, Object o) {
//
//                    }
//
//                    @Override
//                    public void removeAttribute(String s) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<Locale> getLocales() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isSecure() {
//                        return false;
//                    }
//
//                    @Override
//                    public RequestDispatcher getRequestDispatcher(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRealPath(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public int getRemotePort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getLocalName() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getLocalAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getLocalPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public ServletContext getServletContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync() throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isAsyncStarted() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isAsyncSupported() {
//                        return false;
//                    }
//
//                    @Override
//                    public AsyncContext getAsyncContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public DispatcherType getDispatcherType() {
//                        return null;
//                    }
//                },
//                new HttpServletResponse() {
//                    @Override
//                    public void addCookie(Cookie cookie) {
//
//                    }
//
//                    @Override
//                    public boolean containsHeader(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public String encodeURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public void sendError(int i, String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendError(int i) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendRedirect(String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void setDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void addDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void setHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void addHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void setIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void addIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i, String s) {
//
//                    }
//
//                    @Override
//                    public int getStatus() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletOutputStream getOutputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public PrintWriter getWriter() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) {
//
//                    }
//
//                    @Override
//                    public void setContentLength(int i) {
//
//                    }
//
//                    @Override
//                    public void setContentLengthLong(long l) {
//
//                    }
//
//                    @Override
//                    public void setContentType(String s) {
//
//                    }
//
//                    @Override
//                    public void setBufferSize(int i) {
//
//                    }
//
//                    @Override
//                    public int getBufferSize() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void flushBuffer() throws IOException {
//
//                    }
//
//                    @Override
//                    public void resetBuffer() {
//
//                    }
//
//                    @Override
//                    public boolean isCommitted() {
//                        return false;
//                    }
//
//                    @Override
//                    public void reset() {
//
//                    }
//
//                    @Override
//                    public void setLocale(Locale locale) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//                }
//        );
//        System.out.println("收货企业注册返回结果：" + result1.toString());
//        Assert.assertEquals(result1.getCode(), Code.SUCCESS.getCode());
//        UserEntity receiverAccountEntity = userEntityRepository.findByAccountName("account" + randomString1);
//        receiverAccountJson = receiverAccountEntity.getPrivateKey();
//        receiverAccountName = receiverAccountEntity.getAccountName();
//        receiverAddress = receiverAccountEntity.getAddress();
//        receiverEnterpriseName = receiverAccountEntity.getCompanyName();
//        System.out.println("收货企业地址：" + receiverAddress);
//        System.out.println("收货企业账户：" + receiverAccountJson);
//        System.out.println("收货企业名称：" + receiverAccountName);
//
//        //物流公司注册
//        String randomString2 = TestUtil.getRandomString();
//        BaseResult<Object> result2 = accountService.register("account" + randomString2, //unique
//                "123",
//                "企业" + randomString2, //unique
//                "1881881" + randomString2, //unique
//                1,
//                "859051",
//                4,
//                "certType",
//                "1111",
//                "11111",
//                "class",
//                "acctSvcr",
//                "acctSvcrName",
//                new HttpServletRequest() {
//                    @Override
//                    public String getAuthType() {
//                        return null;
//                    }
//
//                    @Override
//                    public Cookie[] getCookies() {
//                        return new Cookie[0];
//                    }
//
//                    @Override
//                    public long getDateHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getIntHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getMethod() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathInfo() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathTranslated() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContextPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getQueryString() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteUser() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isUserInRole(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public Principal getUserPrincipal() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestedSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestURI() {
//                        return null;
//                    }
//
//                    @Override
//                    public StringBuffer getRequestURL() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServletPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession(boolean b) {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession() {
//                        return null;
//                    }
//
//                    @Override
//                    public String changeSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdValid() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromCookie() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromURL() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromUrl() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
//                        return false;
//                    }
//
//                    @Override
//                    public void login(String s, String s1) throws ServletException {
//
//                    }
//
//                    @Override
//                    public void logout() throws ServletException {
//
//                    }
//
//                    @Override
//                    public Collection<Part> getParts() throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Part getPart(String s) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getAttribute(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getAttributeNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
//
//                    }
//
//                    @Override
//                    public int getContentLength() {
//                        return 0;
//                    }
//
//                    @Override
//                    public long getContentLengthLong() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletInputStream getInputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getParameter(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getParameterNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String[] getParameterValues(String s) {
//                        return new String[0];
//                    }
//
//                    @Override
//                    public Map<String, String[]> getParameterMap() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getProtocol() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getScheme() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServerName() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getServerPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public BufferedReader getReader() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteHost() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setAttribute(String s, Object o) {
//
//                    }
//
//                    @Override
//                    public void removeAttribute(String s) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<Locale> getLocales() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isSecure() {
//                        return false;
//                    }
//
//                    @Override
//                    public RequestDispatcher getRequestDispatcher(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRealPath(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public int getRemotePort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getLocalName() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getLocalAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getLocalPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public ServletContext getServletContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync() throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isAsyncStarted() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isAsyncSupported() {
//                        return false;
//                    }
//
//                    @Override
//                    public AsyncContext getAsyncContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public DispatcherType getDispatcherType() {
//                        return null;
//                    }
//                },
//                new HttpServletResponse() {
//                    @Override
//                    public void addCookie(Cookie cookie) {
//
//                    }
//
//                    @Override
//                    public boolean containsHeader(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public String encodeURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public void sendError(int i, String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendError(int i) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendRedirect(String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void setDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void addDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void setHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void addHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void setIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void addIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i, String s) {
//
//                    }
//
//                    @Override
//                    public int getStatus() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletOutputStream getOutputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public PrintWriter getWriter() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) {
//
//                    }
//
//                    @Override
//                    public void setContentLength(int i) {
//
//                    }
//
//                    @Override
//                    public void setContentLengthLong(long l) {
//
//                    }
//
//                    @Override
//                    public void setContentType(String s) {
//
//                    }
//
//                    @Override
//                    public void setBufferSize(int i) {
//
//                    }
//
//                    @Override
//                    public int getBufferSize() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void flushBuffer() throws IOException {
//
//                    }
//
//                    @Override
//                    public void resetBuffer() {
//
//                    }
//
//                    @Override
//                    public boolean isCommitted() {
//                        return false;
//                    }
//
//                    @Override
//                    public void reset() {
//
//                    }
//
//                    @Override
//                    public void setLocale(Locale locale) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//                }
//        );
//        System.out.println("物流公司注册返回结果：" + result2.toString());
//        Assert.assertEquals(result2.getCode(), Code.SUCCESS.getCode());
//        UserEntity logisticsAccountEntity = userEntityRepository.findByAccountName("account" + randomString2);
//        logisticsAccountJson = logisticsAccountEntity.getPrivateKey();
//        logisticsAccountName = logisticsAccountEntity.getAccountName();
//        logisticsAddress = logisticsAccountEntity.getAddress();
//        logisticsEnterpriseName = logisticsAccountEntity.getCompanyName();
//        System.out.println("物流公司地址：" + logisticsAddress);
//        System.out.println("物流公司名称：" + logisticsAccountName);
//        System.out.println("物流公司账户：" + logisticsAccountJson);
//
//        //发货仓储注册
//        String randomString3 = TestUtil.getRandomString();
//        BaseResult<Object> result3 = accountService.register("account" + randomString3, //unique
//                "123",
//                "企业" + randomString3, //unique
//                "1881881" + randomString3, //unique
//                2,
//                "859051",
//                4,
//                "certType",
//                "1111",
//                "11111",
//                "class",
//                "acctSvcr",
//                "acctSvcrName",
//                new HttpServletRequest() {
//                    @Override
//                    public String getAuthType() {
//                        return null;
//                    }
//
//                    @Override
//                    public Cookie[] getCookies() {
//                        return new Cookie[0];
//                    }
//
//                    @Override
//                    public long getDateHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getIntHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getMethod() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathInfo() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathTranslated() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContextPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getQueryString() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteUser() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isUserInRole(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public Principal getUserPrincipal() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestedSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestURI() {
//                        return null;
//                    }
//
//                    @Override
//                    public StringBuffer getRequestURL() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServletPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession(boolean b) {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession() {
//                        return null;
//                    }
//
//                    @Override
//                    public String changeSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdValid() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromCookie() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromURL() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromUrl() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
//                        return false;
//                    }
//
//                    @Override
//                    public void login(String s, String s1) throws ServletException {
//
//                    }
//
//                    @Override
//                    public void logout() throws ServletException {
//
//                    }
//
//                    @Override
//                    public Collection<Part> getParts() throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Part getPart(String s) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getAttribute(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getAttributeNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
//
//                    }
//
//                    @Override
//                    public int getContentLength() {
//                        return 0;
//                    }
//
//                    @Override
//                    public long getContentLengthLong() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletInputStream getInputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getParameter(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getParameterNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String[] getParameterValues(String s) {
//                        return new String[0];
//                    }
//
//                    @Override
//                    public Map<String, String[]> getParameterMap() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getProtocol() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getScheme() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServerName() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getServerPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public BufferedReader getReader() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteHost() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setAttribute(String s, Object o) {
//
//                    }
//
//                    @Override
//                    public void removeAttribute(String s) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<Locale> getLocales() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isSecure() {
//                        return false;
//                    }
//
//                    @Override
//                    public RequestDispatcher getRequestDispatcher(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRealPath(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public int getRemotePort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getLocalName() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getLocalAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getLocalPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public ServletContext getServletContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync() throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isAsyncStarted() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isAsyncSupported() {
//                        return false;
//                    }
//
//                    @Override
//                    public AsyncContext getAsyncContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public DispatcherType getDispatcherType() {
//                        return null;
//                    }
//                },
//                new HttpServletResponse() {
//                    @Override
//                    public void addCookie(Cookie cookie) {
//
//                    }
//
//                    @Override
//                    public boolean containsHeader(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public String encodeURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public void sendError(int i, String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendError(int i) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendRedirect(String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void setDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void addDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void setHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void addHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void setIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void addIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i, String s) {
//
//                    }
//
//                    @Override
//                    public int getStatus() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletOutputStream getOutputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public PrintWriter getWriter() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) {
//
//                    }
//
//                    @Override
//                    public void setContentLength(int i) {
//
//                    }
//
//                    @Override
//                    public void setContentLengthLong(long l) {
//
//                    }
//
//                    @Override
//                    public void setContentType(String s) {
//
//                    }
//
//                    @Override
//                    public void setBufferSize(int i) {
//
//                    }
//
//                    @Override
//                    public int getBufferSize() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void flushBuffer() throws IOException {
//
//                    }
//
//                    @Override
//                    public void resetBuffer() {
//
//                    }
//
//                    @Override
//                    public boolean isCommitted() {
//                        return false;
//                    }
//
//                    @Override
//                    public void reset() {
//
//                    }
//
//                    @Override
//                    public void setLocale(Locale locale) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//                }
//        );
//        System.out.println("发货仓储注册返回结果：" + result3.toString());
//        Assert.assertEquals(result3.getCode(), Code.SUCCESS.getCode());
//        UserEntity senderRepoAccountEntity = userEntityRepository.findByAccountName("account" + randomString3);
//        senderRepoAddress = senderRepoAccountEntity.getAddress();
//        senderRepoEnterpriseName = senderRepoAccountEntity.getCompanyName();
//        System.out.println("发货仓储地址：" + senderRepoAddress);
//
//        //收货仓储注册
//        String randomString4 = TestUtil.getRandomString();
//        BaseResult<Object> result4 = accountService.register("account" + randomString4, //unique
//                "123",
//                "企业" + randomString4, //unique
//                "1881881" + randomString4, //unique
//                2,
//                "859051",
//                4,
//                "certType",
//                "1111",
//                "11111",
//                "class",
//                "acctSvcr",
//                "acctSvcrName",
//                new HttpServletRequest() {
//                    @Override
//                    public String getAuthType() {
//                        return null;
//                    }
//
//                    @Override
//                    public Cookie[] getCookies() {
//                        return new Cookie[0];
//                    }
//
//                    @Override
//                    public long getDateHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getIntHeader(String s) {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getMethod() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathInfo() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPathTranslated() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContextPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getQueryString() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteUser() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isUserInRole(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public Principal getUserPrincipal() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestedSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRequestURI() {
//                        return null;
//                    }
//
//                    @Override
//                    public StringBuffer getRequestURL() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServletPath() {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession(boolean b) {
//                        return null;
//                    }
//
//                    @Override
//                    public HttpSession getSession() {
//                        return null;
//                    }
//
//                    @Override
//                    public String changeSessionId() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdValid() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromCookie() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromURL() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRequestedSessionIdFromUrl() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
//                        return false;
//                    }
//
//                    @Override
//                    public void login(String s, String s1) throws ServletException {
//
//                    }
//
//                    @Override
//                    public void logout() throws ServletException {
//
//                    }
//
//                    @Override
//                    public Collection<Part> getParts() throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Part getPart(String s) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getAttribute(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getAttributeNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
//
//                    }
//
//                    @Override
//                    public int getContentLength() {
//                        return 0;
//                    }
//
//                    @Override
//                    public long getContentLengthLong() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletInputStream getInputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getParameter(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<String> getParameterNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String[] getParameterValues(String s) {
//                        return new String[0];
//                    }
//
//                    @Override
//                    public Map<String, String[]> getParameterMap() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getProtocol() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getScheme() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getServerName() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getServerPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public BufferedReader getReader() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRemoteHost() {
//                        return null;
//                    }
//
//                    @Override
//                    public void setAttribute(String s, Object o) {
//
//                    }
//
//                    @Override
//                    public void removeAttribute(String s) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//
//                    @Override
//                    public Enumeration<Locale> getLocales() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isSecure() {
//                        return false;
//                    }
//
//                    @Override
//                    public RequestDispatcher getRequestDispatcher(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String getRealPath(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public int getRemotePort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getLocalName() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getLocalAddr() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getLocalPort() {
//                        return 0;
//                    }
//
//                    @Override
//                    public ServletContext getServletContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync() throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isAsyncStarted() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isAsyncSupported() {
//                        return false;
//                    }
//
//                    @Override
//                    public AsyncContext getAsyncContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public DispatcherType getDispatcherType() {
//                        return null;
//                    }
//                },
//                new HttpServletResponse() {
//                    @Override
//                    public void addCookie(Cookie cookie) {
//
//                    }
//
//                    @Override
//                    public boolean containsHeader(String s) {
//                        return false;
//                    }
//
//                    @Override
//                    public String encodeURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectURL(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public String encodeRedirectUrl(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public void sendError(int i, String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendError(int i) throws IOException {
//
//                    }
//
//                    @Override
//                    public void sendRedirect(String s) throws IOException {
//
//                    }
//
//                    @Override
//                    public void setDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void addDateHeader(String s, long l) {
//
//                    }
//
//                    @Override
//                    public void setHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void addHeader(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void setIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void addIntHeader(String s, int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i) {
//
//                    }
//
//                    @Override
//                    public void setStatus(int i, String s) {
//
//                    }
//
//                    @Override
//                    public int getStatus() {
//                        return 0;
//                    }
//
//                    @Override
//                    public String getHeader(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaders(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public Collection<String> getHeaderNames() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getCharacterEncoding() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getContentType() {
//                        return null;
//                    }
//
//                    @Override
//                    public ServletOutputStream getOutputStream() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public PrintWriter getWriter() throws IOException {
//                        return null;
//                    }
//
//                    @Override
//                    public void setCharacterEncoding(String s) {
//
//                    }
//
//                    @Override
//                    public void setContentLength(int i) {
//
//                    }
//
//                    @Override
//                    public void setContentLengthLong(long l) {
//
//                    }
//
//                    @Override
//                    public void setContentType(String s) {
//
//                    }
//
//                    @Override
//                    public void setBufferSize(int i) {
//
//                    }
//
//                    @Override
//                    public int getBufferSize() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void flushBuffer() throws IOException {
//
//                    }
//
//                    @Override
//                    public void resetBuffer() {
//
//                    }
//
//                    @Override
//                    public boolean isCommitted() {
//                        return false;
//                    }
//
//                    @Override
//                    public void reset() {
//
//                    }
//
//                    @Override
//                    public void setLocale(Locale locale) {
//
//                    }
//
//                    @Override
//                    public Locale getLocale() {
//                        return null;
//                    }
//                }
//        );
//        System.out.println("收货仓储注册返回结果：" + result4.toString());
//        Assert.assertEquals(result4.getCode(), Code.SUCCESS.getCode());
//        UserEntity receiverRepoAccountEntity = userEntityRepository.findByAccountName("account" + randomString4);
//        receiverRepoAddress = receiverRepoAccountEntity.getAddress();
//        receiverRepoEnterpriseName = receiverRepoAccountEntity.getCompanyName();
//        System.out.println("收货仓储地址：" + receiverRepoAddress);
//
//    }
//
//    @Autowired
//    public void testWayBillServiceImpl() throws PasswordIllegalParam, ReadFileException, PrivateKeyIllegalParam, ContractInvokeFailException, PropertiesLoadException, ValueNullException {
//        //生成未确认运单
//        String random = TestUtil.getRandomString();
//        String orderNo = "订单" + random;
//        String productName = "productName";
//        long productQuantity = 100;
//        long productValue = 1000000;
//        String senderRepoCertNo = "11111";
//        String receiverRepoBusinessNo = "2222";
//        BaseResult<Object> genUnConfirmedResult = wayBillService.generateUnConfirmedWaybill(orderNo, logisticsEnterpriseName, senderEnterpriseName, receiverEnterpriseName,
//                productName, productQuantity, productValue, senderRepoEnterpriseName, senderRepoCertNo, receiverRepoEnterpriseName, receiverRepoBusinessNo, request);
//        System.out.println("生成未确认运单结果：" + genUnConfirmedResult.toString());
//        Assert.assertEquals(genUnConfirmedResult.getCode(), Code.SUCCESS);
//
//        //生成确认运单
//        BaseResult<Object> genConfirmedResult = wayBillService.generateConfirmedWaybill(orderNo, request);
//        System.out.println("生成已确认运单结果：" + genConfirmedResult.toString());
//        Assert.assertEquals(genConfirmedResult.getCode(), Code.SUCCESS);
//
//        //更新运单状态为已送达
//        BaseResult<Object> updateToReceivedResult = wayBillService.updateWayBillStatusToReceived(orderNo, request);
//        System.out.println("更新运单状态为已送达结果：" + updateToReceivedResult.toString());
//        Assert.assertEquals(updateToReceivedResult.getCode(), Code.SUCCESS);
//
//        //获取与自己相关的所有运单
//        BaseResult<Object> updateToReceivedResult = wayBillService.getAllRelatedWayBillDetail(request);
//        System.out.println("更新运单状态为已送达结果：" + updateToReceivedResult.toString());
//        Assert.assertEquals(updateToReceivedResult.getCode(), Code.SUCCESS);
//
//    }
//
//
//}