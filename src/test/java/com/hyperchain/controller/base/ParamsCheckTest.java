package com.hyperchain.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by martin on 2017/3/18.
 */
public class ParamsCheckTest {
    @Test
    public void checkPrivateKey() throws Exception {
        String privateKey="{\"encrypted\":\"3756443e7128bd09dd0f3d647d39db5c634a317a6cdd2fbcd9837b5f35ef1e08c6f431ea0e0e22ac\",\"algo\":\"0x02\",\"address\":\"22aec712ba13be0fbba2238551456cd9d2df2cac\",\"version\":\"1.0\"}";
        JSONObject jsonObject= JSONObject.parseObject(privateKey);
        System.out.println(jsonObject.getString("encrypted").length());
        System.out.println(jsonObject.getString("address").length());
    }

}