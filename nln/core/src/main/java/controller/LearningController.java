package controller;

import datastore.Dataset;
import datastore.Model;
import model.LearningInfo;
import org.tensorflow.TensorFlow;
import tensorflow.SimpleModel;
import tensorflow.model.cnn.vgg.VGG11OnFashionMNIST;

public class LearningController {
    public static LearningController shard = new LearningController();

    private LearningController() {}

    public LearningInfo simpleLearning(String name, Model model) {
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
        InterestController interestController = new InterestController();
        interestController.request("/model/A", responseData -> {
            System.out.println(responseData.getPojo().getLearningInfo()
            );
        });
    }
}
