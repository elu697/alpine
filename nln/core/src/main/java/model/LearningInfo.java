package model;

import java.util.Map;
import java.util.Objects;

public final class LearningInfo {
    private String uid;
    private String base64Data;

    public LearningInfo(String uid, String base64Data) {
        this.uid = uid;
        this.base64Data = base64Data;
    }

    public LearningInfo(Map<String, Object> map) {
        this.uid = (String) map.get("uid");
        this.base64Data = (String) map.get("base64Data");
    }

    public LearningInfo(){}

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
