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

    private String urlBase = "http://localhost:8080/Reparo/v1/account";


    @Test
    public void queryUserBM() throws UnsupportedEncodingException {
        String privateKey = TestData.BM001PrivateKey;
        System.out.println(privateKey);
        String url = urlBase + "?private_key=" + TestUtil.transferREST(privateKey) ;
        System.out.println(url);
        System.out.println(HttpHelper.get(url));
    }

    @Test
    public void queryUserOJ() throws UnsupportedEncodingException {
        String privateKey = TestData.OJ001PrivateKey;
        String url = urlBase + "?private_key=" + TestUtil.transferREST(privateKey) ;
        System.out.println(url);
        System.out.println(HttpHelper.get(url));
    }

}