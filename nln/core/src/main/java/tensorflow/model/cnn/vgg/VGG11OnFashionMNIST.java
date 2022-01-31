/*
 *  Copyright 2020 The TensorFlow Authors. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  =======================================================================
 */
package tensorflow.model.cnn.vgg;
import java.util.logging.Logger;
import tensorflow.model.datasets.mnist.MnistDataset;
import datastore.*;

/**
 * Trains and evaluates VGG'11 model on FashionMNIST dataset.
 */
public class VGG11OnFashionMNIST<data> {
    // Hyper-parameters
    public static final int EPOCHS = 1;
    public static final int BATCH_SIZE = 2048;

    // Fashion MNIST dataset paths
    private static final String TRAINING_IMAGES_ARCHIVE = "mnist/train-images-idx3-ubyte.gz";
    private static final String TRAINING_LABELS_ARCHIVE = "mnist/train-labels-idx1-ubyte.gz";
    private static final String TEST_IMAGES_ARCHIVE = "mnist/t10k-images-idx3-ubyte.gz";
    private static final String TEST_LABELS_ARCHIVE = "mnist/t10k-labels-idx1-ubyte.gz";
    private static final String TRAINING_IMAGES_ARCHIVE2 = "fashionmnist/train-images-idx3-ubyte.gz";
    private static final String TRAINING_LABELS_ARCHIVE2 = "fashionmnist/train-labels-idx1-ubyte.gz";
    private static final String TEST_IMAGES_ARCHIVE2 = "fashionmnist/t10k-images-idx3-ubyte.gz";
    private static final String TEST_LABELS_ARCHIVE2 = "fashionmnist/t10k-labels-idx1-ubyte.gz";
    private static final String MODEL_NAME = "modelA";

    private static final Logger logger = Logger.getLogger(VGG11OnFashionMNIST.class.getName());

    public static Boolean action(Model model, Dataset dataset, Integer foldNum) {
        logger.info("Data loading: " + dataset.datasetsPath);
        MnistDataset datasets = MnistDataset.create(foldNum,
                                    dataset.trainingImagesArchivePath,
                                    dataset.trainingLabelsArchivePath,
                                    dataset.testImagesArchivePath,
                                    dataset.testLabelsArchivePath);

        try (VGGModel vggModel = new VGGModel(model.modelNamePath)) {
            logger.info("Datastore.Model training: " + model.modelNamePath);
            vggModel.train(datasets, EPOCHS, BATCH_SIZE);
            logger.info("Datastore.Model evaluation.");
            vggModel.test(datasets, BATCH_SIZE);
            vggModel.saveParam();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        Dataset dataset = Dataset.initMnist();
        Model model = Model.initModel();

        action(model, dataset, 0);
    }
}
