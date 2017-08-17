package com.ninelook.wecard.common.util;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import sun.misc.BASE64Decoder;

import java.util.Map;

/**
 * @Author Ron
 */
public class Codec {

    static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对象转换为json
     *
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        String json = "";
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            json = "";
        }
        return json;
    }

    /**
     * json转换为map
     *
     * @param json
     * @return
     */
    public static Map jsonToMap(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }


    // 加密
    public static String base64Encode(String str) {
        String s = "";
        try {
            byte[] b = str.getBytes("UTF-8");
            s = Base64.encodeBase64String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    // 解密
    public static String base64Decode(String encodeStr) {
        String s = "";
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(encodeStr);
            s = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }
}
