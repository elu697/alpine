import org.tensorflow.TensorFlow;
import tensorflow.model.cnn.vgg.VGG11OnFashionMNIST;

public class LearningController {
    public static void main(String[] args) {
        System.out.println("Hello TensorFlow " + TensorFlow.version());
        VGG11OnFashionMNIST.action("mnist", 1000);
        VGG11OnFashionMNIST.action2("mnist", 1000);

    }
}
