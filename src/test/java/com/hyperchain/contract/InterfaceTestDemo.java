package com.hyperchain.contract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyperchain.http.HttpHelper;
import com.hyperchain.test.TestData;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringWebBaseTest;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * by chenyufeng on 2017/3/31 .
 */
public class InterfaceTestDemo extends SpringWebBaseTest {

    private String urlBase = "http://localhost:8080/reparo/v1/account";

    @Test
    public void addUserTest() throws UnsupportedEncodingException {
        String route = "/addUser";
        String id = "1";
        String nickName = "robert";
        String password = "123456";
        String phoneNum = "1888888";
        String url = urlBase + route + "?id=" + TestUtil.transferREST(id) + "&nickName=" +
                TestUtil.transferREST(nickName) +
                "&password=" + TestUtil.transferREST(password) +
                "&phoneNum=" + TestUtil.transferREST(phoneNum);
        System.out.println(url);

        String jsonString = HttpHelper.get(url);
        System.out.println(jsonString);
        JSONObject object = JSON.parseObject(jsonString);
        Assert.assertEquals("0", object.getString("code"));
    }

    @Test
    public void queryUserTest() throws UnsupportedEncodingException {
        String route = "/queryUser";
        String id = "1";
        String url = urlBase + route + "?id=" + TestUtil.transferREST(id);
        System.out.println(url);

        String jsonString = HttpHelper.get(url);
        System.out.println(jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Assert.assertEquals("0", jsonObject.getString("code"));
        Assert.assertNotNull(jsonObject.getJSONObject("data").getString("password"));
        Assert.assertNotNull(jsonObject.getJSONObject("data").getString("nickname"));
        Assert.assertNotNull(jsonObject.getJSONObject("data").getString("phoneNum"));
    }
}
