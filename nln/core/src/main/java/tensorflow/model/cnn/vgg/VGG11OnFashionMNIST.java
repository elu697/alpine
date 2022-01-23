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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.tensorflow.op.data.ShuffleDataset;
import tensorflow.model.datasets.mnist.MnistDataset;

import javax.jws.Oneway;

/**
 * Trains and evaluates VGG'11 model on FashionMNIST dataset.
 */
public class VGG11OnFashionMNIST {
    // Hyper-parameters
    public static final int EPOCHS = 5;
    public static final int BATCH_SIZE = 512;

    // Fashion MNIST dataset paths
//    public static final String TRAINING_IMAGES_ARCHIVE = "fashionmnist/train-images-idx3-ubyte.gz";
//    public static final String TRAINING_LABELS_ARCHIVE = "fashionmnist/train-labels-idx1-ubyte.gz";
//    public static final String TEST_IMAGES_ARCHIVE = "fashionmnist/t10k-images-idx3-ubyte.gz";
//    public static final String TEST_LABELS_ARCHIVE = "fashionmnist/t10k-labels-idx1-ubyte.gz";
    private static final String TRAINING_IMAGES_ARCHIVE = "mnist/train-images-idx3-ubyte.gz";
    private static final String TRAINING_LABELS_ARCHIVE = "mnist/train-labels-idx1-ubyte.gz";
    private static final String TEST_IMAGES_ARCHIVE = "mnist/t10k-images-idx3-ubyte.gz";
    private static final String TEST_LABELS_ARCHIVE = "mnist/t10k-labels-idx1-ubyte.gz";
    private static final String MODEL_NAME = "modelA";

    private static final Logger logger = Logger.getLogger(VGG11OnFashionMNIST.class.getName());

    public static void action(String modelName, Integer foldNum,
                              String trainDataPath, String trainLabelPath,
                              String testDataPath, String testLabelPath) {
        logger.info("Data loading.");
        MnistDataset dataset = MnistDataset.create(foldNum, trainDataPath, trainLabelPath, testDataPath, testLabelPath);
        String modelStore = "./ModelStore";

        String modelDir = modelStore + "/" + modelName + "_dir"; // ModelStore/modelA_dir

        String modelPath = modelDir + "/" + modelName; // ModelStore/modelA_dir/modelA.index
        String modelZipFile = modelDir + "/" + modelName + ".zip"; // ModelStore/modelA_dir/modelA.zip

        try (VGGModel vggModel = unzipFile(modelZipFile, modelStore) ? new VGGModel(modelPath) : new VGGModel()) {
            logger.info("Model training.");
            vggModel.train(dataset, EPOCHS, BATCH_SIZE);

            logger.info("Model evaluation.");
            vggModel.test(dataset, BATCH_SIZE);
            //            vggModel.average();
            if (vggModel.saveParam(modelPath)) zipFiles(modelDir, modelZipFile);
        }
    }

    public static void main(String[] args) {
        action("modelA", 0, TRAINING_IMAGES_ARCHIVE, TRAINING_LABELS_ARCHIVE, TEST_IMAGES_ARCHIVE, TEST_LABELS_ARCHIVE);
    }

    public static Boolean zipFiles(String srcDir, String targetZip) {
        try (ZipFile zipFile = new ZipFile(targetZip)) {
            zipFile.addFolder(new File(srcDir));
            zipFile.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean unzipFile(String srcZip, String targetDir) {

        File source = new File(srcZip);
        try (ZipFile zipFile = new ZipFile(source)){
            zipFile.extractAll(targetDir);
            zipFile.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                Files.delete(Paths.get(srcZip));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
