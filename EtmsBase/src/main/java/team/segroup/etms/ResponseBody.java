package team.segroup.etms;

import java.util.HashMap;

public class ResponseBody extends HashMap<String, Object> {
    public static ResponseBody create() {
        return new ResponseBody();
    }

    public static ResponseBody create(String key, Object value) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.put(key, value);
        return responseBody;
    }

    public ResponseBody add(String key, Object value){
        put(key, value);
        return this;
    }
}
