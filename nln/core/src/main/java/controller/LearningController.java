package controller;

import datastore.Dataset;
import datastore.Model;
import model.LearningInfo;
import tensorflow.SimpleModel;

public class LearningController {
    public static LearningController shard = new LearningController();

    private LearningController() {}

    public LearningInfo simpleLearning(String name, Model model, Dataset dataset) {
        LearningInfo learningInfo = new LearningInfo();
        learningInfo.setUid(name);

//        Boolean isSuccess = VGG11OnFashionMNIST.action(model, dataset, 0);
        Boolean isSuccess = SimpleModel.Action(model.modelNamePath);
        if (isSuccess){
            learningInfo.setBase64Data(model.getZipModelBase64String());
            learningInfo.setProgress(100);
        } else {
            learningInfo.setProgress(0);
        }

        return learningInfo;
    }

    public static void main(String[] args) {
        LearningController learningController = new LearningController();
        learningController.simpleLearning("/model/A", Model.initModel(), Dataset.initMnist());
    }
}
