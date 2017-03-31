package com.hyperchain.controller;

import com.hyperchain.http.HttpHelper;
import com.hyperchain.test.TestData;
import com.hyperchain.test.TestUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by martin on 2017/3/16.
 */
public class UserControllerTest {

    private String urlBase = "http://localhost:8080/reparo/v1/account";

    @Test
    public void addUserTest() throws UnsupportedEncodingException {
        String route = "/addUser";
        String privateKey = TestData.BM001PrivateKey;
        String id = "1";
        String nickName = "robert";
        String password = "123456";
        String phoneNum = "1888888";
        String url = urlBase + route + "?id=" + TestUtil.transferREST(id) + "&nickName=" +
                TestUtil.transferREST(nickName) +
                "&password=" + TestUtil.transferREST(password) +
                "&phoneNum=" + TestUtil.transferREST(phoneNum);
        System.out.println(url);
        System.out.println(HttpHelper.get(url));
    }

    @Test
    public void queryUserTest() throws UnsupportedEncodingException {
        String privateKey = TestData.OJ001PrivateKey;
        String url = urlBase + "?private_key=" + TestUtil.transferREST(privateKey);
        System.out.println(url);
        System.out.println(HttpHelper.get(url));
    }

}