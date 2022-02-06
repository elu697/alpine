package model;

import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.*;

public final class ResponseData {
    public static final class POJO {
        private String name;
        private ArrayList<DatasetInfo> datasetInfo;
        private ArrayList<LearningInfo> learningInfo;

        public POJO(String name, ArrayList<DatasetInfo> datasetInfo, ArrayList<LearningInfo> learningInfo) {
            this.name = name;
            this.datasetInfo = datasetInfo;
            this.learningInfo = learningInfo;
        }

        public POJO(Map<String, Object> map) {
            this.name = (String) map.get("name");
            this.datasetInfo = new ArrayList<>();
            ArrayList<Map<String, Object>> map1 = (ArrayList<Map<String, Object>>) map.getOrDefault("datasetInfo", new ArrayList<>());
            map1.forEach(o -> {
                this.datasetInfo.add(new DatasetInfo(o));
            });

            this.learningInfo = new ArrayList<>();
            ArrayList<Map<String, Object>> map2 = (ArrayList<Map<String, Object>>) map.getOrDefault("learningInfo", new ArrayList<>());
            map2.forEach(o -> {
                this.learningInfo.add(new LearningInfo(o));
            });
        }

        public POJO() {
            this.name = "";
            this.datasetInfo = new ArrayList<>();
            this.learningInfo = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<DatasetInfo> getDatasetInfo() {
            return datasetInfo;
        }

        public void setDatasetInfo(ArrayList<DatasetInfo> datasetInfo) {
            this.datasetInfo = datasetInfo;
        }

        public void addDatasetInfo(DatasetInfo datasetInfo) {
            this.datasetInfo.add(datasetInfo);
        }

        public ArrayList<LearningInfo> getLearningInfo() {
            return learningInfo;
        }

        public void setLearningInfo(ArrayList<LearningInfo> learningInfo) {
            this.learningInfo = learningInfo;
        }

        public void addLearningInfo(LearningInfo learningInfo) {
            this.learningInfo.add(learningInfo);
        }
    }

    private POJO pojo;

    public ResponseData(POJO pojo) {
        this.pojo = pojo;
    }

    public ResponseData(String json) {
        this.pojo = new POJO(new JSONObject(json).toMap());
    }

    public ResponseData(String name, ArrayList<ResponseData> responseDataArrayList) {
        POJO listPojo = new POJO();
        listPojo.setName(name);
        responseDataArrayList.forEach(responseData -> {
            responseData.pojo.getLearningInfo().forEach(listPojo::addLearningInfo);
            responseData.pojo.getDatasetInfo().forEach(listPojo::addDatasetInfo);
        });
        this.pojo = listPojo;
    }

    public static ResponseData returnEmpty() {
        return new ResponseData(new POJO());
    }

    public JSONObject toJsonObj() {
        return new JSONObject(pojo);
    }

    public POJO getPojo() {
        return pojo;
    }

    public static void main(String[] args) {
        LearningInfo learningInfo = new LearningInfo("A", "B", 0);
        DatasetInfo datasetInfo = new DatasetInfo("C", "D");
        POJO pojo = new POJO();
        pojo.setName("Name");
        pojo.addLearningInfo(learningInfo);
        pojo.addDatasetInfo(datasetInfo);

        ResponseData responseData = new ResponseData(pojo);
        System.out.println(responseData.toJsonObj());

        ResponseData responseData1 = new ResponseData(responseData.toJsonObj().toString());
        POJO pojo1 = responseData1.getPojo();
        System.out.println(responseData1.toJsonObj().toString());
        System.out.println(pojo1.getName());
        System.out.println(pojo1.getDatasetInfo().toString());
        System.out.println(pojo1.getLearningInfo().toString());
        System.out.println(((LearningInfo) pojo1.getLearningInfo().get(0)).getBase64Data());
    }
}
