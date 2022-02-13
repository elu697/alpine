package model;

import java.util.Map;

public final class LearningInfo {
    private String uid;
    private String base64Data;
    private int progress;

    public LearningInfo(String uid, String base64Data, int progress) {
        this.uid = uid;
        this.base64Data = base64Data;
        this.progress = progress;
    }

    public LearningInfo(Map<String, Object> map) {
        this.uid = (String) map.get("uid");
        this.base64Data = (String) map.get("base64Data");
        this.progress = (int) map.get("progress");
    }

    public LearningInfo(){
        this.uid = "";
        this.base64Data = "";
        this.progress = 0;
    }

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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
