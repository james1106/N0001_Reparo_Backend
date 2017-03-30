package com.hyperchain.controller.util;

/**
 * Created by martin on 2017/3/27.
 */
public class BytesVSJson {
    public static String json2Bytes(String json) {
        String bytes = json.replace("\"", "\\\"");
        return bytes;
    }
}
