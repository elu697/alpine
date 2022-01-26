package model;

import org.json.JSONObject;
import java.util.HashMap;

public class ResponseData {
    public HashMap<String, String>  jsonData;

    public ResponseData() {
        jsonData = new HashMap<>();
    }

    public void set(String key, String value) {
        this.jsonData.put(key, value);
    }

    public JSONObject toJsonObj() {
        HashMap<String, String> encodeMap = new HashMap<>(jsonData);
        System.out.println(encodeMap);
        return new JSONObject(encodeMap);
    }
}
