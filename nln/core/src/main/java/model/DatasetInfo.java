package model;

import datastore.Dataset;

import java.util.Map;

public class DatasetInfo {
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

    public DatasetInfo() {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }
}
