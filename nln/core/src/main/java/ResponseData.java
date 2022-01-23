import net.named_data.jndn.Data;
import net.named_data.jndn.util.Blob;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class ResponseData {
    public HashMap<String, String>  jsonData;

    ResponseData() {
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
