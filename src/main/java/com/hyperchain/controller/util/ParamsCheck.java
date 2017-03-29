package com.hyperchain.controller.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.common.constant.BaseConstant.Role;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by martin on 2017/3/18.
 */
public class ParamsCheck {
    public static boolean checkPrivateKey(String privateKey) throws JSONException {
        JSONObject jsonObject = JSONObject.parseObject(privateKey);

        if (!jsonObject.containsKey("encrypted")) return false;
        String encrypted = jsonObject.getString("encrypted");
        String regString1 = "[a-f0-9]{80}";
        if (!Pattern.matches(regString1, encrypted))return false;

        if (!jsonObject.containsKey("algo")) return false;
        String algo = jsonObject.getString("algo");
        if (!algo.equals("0x02")) return false;

        if (!jsonObject.containsKey("address")) return false;
        String address = jsonObject.getString("address");
        String regString2 = "[a-f0-9]{40}";
        if (!Pattern.matches(regString2, address))return false;

        if (!jsonObject.containsKey("version")) return false;
        String version = jsonObject.getString("version");
        if(!version.equals("1.0"))return false;

        return true;
    }

    public static boolean checkPartyFunctionCode(String partyFunctionCode) {
        for (Role role : Role.values()) {
            if (partyFunctionCode.equals(role.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkStatus(String status) {
        return Arrays.asList(BaseConstant.Status).contains(status);
    }
}
