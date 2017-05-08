package com.hyperchain.common.filter;

import cn.hyperchain.common.log.LogUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hyperchain.common.util.CommonUtil;
import com.hyperchain.common.util.DesUtils;
import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.controller.AccountController;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ldy on 2017/4/9.
 */
@Component
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements javax.servlet.Filter {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext()); //在filter中使能spring bean自动注入
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //不拦截的页面
        if (isNoFilterUri(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    LogUtil.info("用户raw token：" + token);
                    String tokenInfo = DesUtils.decryptToken(token);

                    //token为空
                    if (CommonUtil.isEmpty(token)) {
                        LogUtil.info("token为空");
                        redirectToLogin(httpServletRequest, httpServletResponse);
                        return;
                    }

                    LogUtil.info("用户token：" + tokenInfo);
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(tokenInfo);
                        Long timestamp = Long.parseLong(jsonObject.getString("timestamp")); //时间戳
                        String address = jsonObject.getString("address"); //用户地址
                        int roleCode = Integer.parseInt(jsonObject.getString("roleCode")); //角色code
                        LogUtil.info("用户地址：" + address);
                        LogUtil.info("角色code：" + roleCode);
                        LogUtil.info("时间戳：" + timestamp);

                        //TODO 判断token是否过期
                        //TODO 判断角色权限
                        //判断用户是否存在
                        UserEntity userEntity = userEntityRepository.findByAddress(address);
                        if (null == userEntity) {
                            LogUtil.info("token用户未注册");
                            redirectToLogin(httpServletRequest, httpServletResponse);
                            return;
                        } else {
                            LogUtil.info("token验证通过");
                            //重设cookie中的token时间戳
                            String newToken = TokenUtil.generateToken(address, roleCode);
                            LogUtil.info("新的token：" + newToken);
                            cookie.setValue(newToken);
                            cookie.setPath("/");
                            httpServletResponse.addCookie(cookie);
                            filterChain.doFilter(servletRequest, servletResponse);
                            return;
                        }
                    } catch (JSONException e) {
                        LogUtil.info("token json解析失败");
                        redirectToLogin(httpServletRequest, httpServletResponse);
                        return;
                    }

                }
            }
        }

        //cookie中无token
        LogUtil.info("cookie中无token, 重定向到登录页面");
        redirectToLogin(httpServletRequest, httpServletResponse);
        return;

    }

    //重定向到登录页面
    private void redirectToLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String host = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort();
        httpServletResponse.sendRedirect(host + "/reparo/module/login.html");
    }

    //不需要过滤的url
    private boolean isNoFilterUri(HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI().toString();
        if (uri.indexOf("static") > 0 || uri.indexOf("login") > 0 || uri.indexOf("commons") > 0) { //登录等页面
            return true;
        }
        if (uri.indexOf("docs") > 0) { //swagger
            return true;
        }
        switch (uri) { //后台接口
            case "/reparo/v1/account/user":
                return true;
            case "/reparo/v1/account/login":
                return true;
            default:
                return false;
        }
    }

    @Override
    public void destroy() {

    }
}
