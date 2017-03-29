package com.hyperchain.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by martin on 2017/3/24.
 */
public class TestUtil {
    public static String transferREST(String privateKey) throws UnsupportedEncodingException {
        privateKey = URLEncoder.encode(privateKey, "utf-8");
        return privateKey;
    }

    @Test
    public void testRetDecode() {
        String result = "[{\"type\":\"java.math.BigInteger\",\"value\":\"0\",\"mayvalue\":\"0\"},{\"type\":\"array\",\"value\":[{\"type\":\"bytes\",\"value\":\"3132333435363738390000000000000000000000000000000000000000000000\",\"mayvalue\":\"123456789\"}],\"mayvalue\":\"array\"}]";
        JSONArray jsonArray = JSON.parseArray(result);
        ArrayList list = new ArrayList();

        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject methodbody = (JSONObject) jsonArray.get(i);
            String type = methodbody.getString("type");
            if (!type.equals("array")) {
                list.add(methodbody.getString("mayvalue"));
            } else {
                JSONArray arrayBody = JSON.parseArray(methodbody.getString("value"));
                ArrayList listArray = new ArrayList();

                for (int index = 0; index < arrayBody.size(); ++index) {
                    JSONObject value = (JSONObject) arrayBody.get(index);
                    listArray.add(value.getString("mayvalue").replaceAll("^(0+)", ""));
                }

                list.add(listArray);
            }
        }

        System.out.println(list);
    }

}
