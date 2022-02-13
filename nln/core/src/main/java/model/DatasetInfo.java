package model;

import java.util.Map;

public final class DatasetInfo {
    private String uid;
    private String base64Data;

    public DatasetInfo(String uid, String base64Data) {
        this.uid = uid;
        this.base64Data = base64Data;
    }

    public DatasetInfo(Map<String, Object> map) {
        this.uid = (String) map.get("uid");
        this.base64Data = (String) map.get("base64Data");
    }

    public DatasetInfo() {
        this.uid = "";
        this.base64Data = "";
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBase64Data() {
        return this.base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }
}
