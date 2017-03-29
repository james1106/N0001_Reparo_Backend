package com.hyperchain.controller.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by martin on 2017/3/27.
 */
public class BytesVSJsonTest {
    @Test
    public void json2Bytes() throws Exception {
        String json="{\"SequenceCode\":\"123456789\",\"InsuranceBillCode\":\"12345678\",\"PartyInformation\":[{\"PartyFunctionCode\":\"BM\",\"LogisticsExchangeCode\":\"BM001\",\"PartyName\":\"bmname\",\"PersonalIdentityDocument\":\"\"},{\"PartyFunctionCode\":\"OJ\",\"LogisticsExchangeCode\":\"OJ001\",\"PartyName\":\"ojname\",\"PersonalIdentityDocument\":\"\"}]}";
        System.out.println(BytesVSJson.json2Bytes(json));
    }

    @Test
    public void isJson(){
        String json="{\"SequenceCode\":\"123456789\",\"InsuranceBillCode\":\"12345678\",\"PartyInformation\":[{\"PartyFunctionCode\":\"BM\",\"LogisticsExchangeCode\":\"BM001\",\"PartyName\":\"bmname\",\"PersonalIdentityDocument\":\"\"},{\"PartyFunctionCode\":\"OJ\",\"LogisticsExchangeCode\":\"OJ001\",\"PartyName\":\"ojname\",\"PersonalIdentityDocument\":\"\"}]}";
        String testJsonOject="{\"key\":\""+BytesVSJson.json2Bytes(json)+"\"}";
        JSONObject jsonObject=JSONObject.parseObject(testJsonOject);
        System.out.println(jsonObject.get("key"));
    }

}