package com.ninelook.wecard.server.scene.message;

import com.ninelook.wecard.server.NMethodEnum;
import com.ninelook.wecard.server.NException;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息助手
 *
 * User: Ron
 */
public class MessageResult {

    /**
     * 错误码(0为正常, 大于0则为错误号)
     */
    protected int code = 0;

    /**
     * 方法名称, push信息后缀会加入.push
     */
    protected NMethodEnum method;

    /**
     * 返回数据
     */
    protected Map data = new HashMap();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public NMethodEnum getMethod() {
        return method;
    }

    public void setMethod(NMethodEnum method) {
        this.method = method;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    /**
     * 将当前结构体转换为JSON
     * @return
     */
    public String toJson() {
        Map resultInfo = new HashMap() {{
            put("code", 0);
            put("method", method.toString());
            put("data", data);
        }};


        ObjectMapper mapper = new ObjectMapper();

        String json = "";
        try {
            json = mapper.writeValueAsString(resultInfo);
        } catch (Exception e) {
            try {

                //解析失败则返回告警JSON
                resultInfo = new HashMap() {{
                    put("code", NException.ERROR_PARAM_VALID_FAIL);
                    put("method", method);
                }};

                json = mapper.writeValueAsString(resultInfo);

            } catch (Exception ex) {

            }
        }

        return json;
    }
}
