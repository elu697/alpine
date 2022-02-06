package tensorflow;/*
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.framework.optimizers.*;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.op.Op;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.core.Variable;
import org.tensorflow.op.data.ShuffleDataset;
import org.tensorflow.op.math.*;
import org.tensorflow.types.TFloat32;

/**
 * In this example TensorFlow finds the weight and bias of the linear regression during 1 epoch,
 * training on observations one by one.
 * <p>
 * Also, the weight and bias are extracted and printed.
 */
public class SimpleModel {
    /**
     * Amount of data points.
     */
    private static final int N = 100;

    /**
     * This value is used to fill the Y placeholder in prediction.
     */
    public static final float LEARNING_RATE = 0.01f;
    public static final String WEIGHT_VARIABLE_NAME = "weight";
    public static final String BIAS_VARIABLE_NAME = "bias";

    public static final Random rand = new Random(10);

    public static ArrayList<Float> makeX() {
        ArrayList<Float> arrayList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            arrayList.add((float) (i+1));
        }
        return arrayList;
    }

    public static float prob(float x) {
        return x * 0.1f;
    }

    public static ArrayList<Float> makeY(ArrayList<Float> x) {
        ArrayList<Float> arrayList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            arrayList.add(prob(x.get(i)));
        }
        return arrayList;
    }

    public static float accuracy(ArrayList<Float> yValues,  ArrayList<Float> testValues) {
        int totalNum = yValues.size();
        int correctNum = 0;

        for (int i = 0; i < totalNum; i++) {
            float y = yValues.get(i);
            float test = testValues.get(i);

            if (Math.round(y) == Math.round(test)) {
                correctNum++;
            }
        }

        return (float) (correctNum/totalNum);
    }

    public static void main(String[] args) {

        // Prepare the data
        ArrayList<Float> xValues = makeX();
        ArrayList<Float> yValues = makeY(xValues);

        learn(xValues, yValues, "./test");
    }

    public static boolean Action(String savePath) {
        // Prepare the data
        ArrayList<Float> xValues = makeX();
        ArrayList<Float> yValues = makeY(xValues);
        try {
            learn(xValues, yValues, savePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void learn(ArrayList<Float> xValues, ArrayList<Float> yValues, String savePath) {
        String weightVariableName = WEIGHT_VARIABLE_NAME; String biasVariableName = BIAS_VARIABLE_NAME; float learningRate = LEARNING_RATE;
        try (Graph graph = new Graph()) {
            Ops tf = Ops.create(graph);

            // Define placeholders
            Placeholder<TFloat32> xData = tf.placeholder(TFloat32.class, Placeholder.shape(Shape.scalar()));
            Placeholder<TFloat32> yData = tf.placeholder(TFloat32.class, Placeholder.shape(Shape.scalar()));

            // Define variables
            Variable<TFloat32> weight = tf.withName(weightVariableName).variable(tf.constant(1f));
            Variable<TFloat32> bias = tf.withName(biasVariableName).variable(tf.constant(1f));

            // Define the model function weight*x + bias
            Mul<TFloat32> mul = tf.math.mul(xData, weight);
            Add<TFloat32> yPredicted = tf.math.add(mul, bias);

            // Define loss function MSE平均二乗誤差
            Pow<TFloat32> sum = tf.math.pow(tf.math.sub(yPredicted, yData), tf.constant(2f));
            Div<TFloat32> mse = tf.math.div(sum, tf.constant(2f * yValues.size()));

            // Back-propagate gradients to variables for training
            String optimizerName = "adam";
            String lcOptimizerName = optimizerName.toLowerCase();
            Optimizer optimizer = new Adamax(graph, 0.01f);
//            switch (lcOptimizerName) {
//                case "adadelta":
//                    optimizer = new AdaDelta(graph, 1f, 0.95f, 1e-8f);
//                    break;
//                case "adagradda":
//                    optimizer = new AdaGradDA(graph, 0.01f);
//                    break;
//                case "adagrad":
//                    optimizer = new AdaGrad(graph, 0.01f);
//                    break;
//                case "adam":
//                    optimizer = new Adam(graph, 0.001f, 0.9f, 0.999f, 1e-8f);
//                    break;
//                case "sgd":
//                    optimizer = new GradientDescent(graph, 0.01f);
//                    break;
//                case "momentum":
//                    optimizer = new Momentum(graph, 0.01f, 0.9f, false);
//                    break;
//                case "rmsprop":
//                    optimizer = new RMSProp(graph, 0.01f, 0.9f, 0.0f, 1e-10f, false);
//                    break;
//                default:
//                    throw new IllegalArgumentException("Unknown optimizer " + optimizerName);
//            }

            Op minimize = optimizer.minimize(mse);

            try (Session session = new Session(graph)) {
                for (int epoch = 0; epoch < 100; epoch++) {
                    Collections.shuffle(xValues);
                    yValues = makeY(xValues);
                    TFloat32 loss = TFloat32.scalarOf(0);
                    for (int i = 0; i < xValues.size(); i++) {
                        float x = xValues.get(i);
                        float y = yValues.get(i);
                        try (TFloat32 xTensor = TFloat32.scalarOf(x);
                             TFloat32 yTensor = TFloat32.scalarOf(y)) {

                            loss = (TFloat32) session.runner()
                                    .feed(xData.asOutput(), xTensor)
                                    .feed(yData.asOutput(), yTensor)
                                    .addTarget(minimize)
                                    .fetch(mse)
                                    .run().get(0);
//                            System.out.println("Training phase");
//                            System.out.println("x is " + x + " y is " + y);
                        }
                    }

                    if (epoch%10 == 0) {
                        Logger.getGlobal().log(Level.INFO,
                                "Iteration = " + epoch + ", training loss = " + loss.getFloat());
                        // Let's predict y for x = 10f
                        float x = epoch;
                        float predictedY = 0f;

                        try (TFloat32 xTensor = TFloat32.scalarOf(x);
                             TFloat32 yTensor = TFloat32.scalarOf(predictedY);
                             TFloat32 yPredictedTensor = (TFloat32) session.runner()
                                     .feed(xData.asOutput(), xTensor)
                                     .feed(yData.asOutput(), yTensor)
                                     .fetch(yPredicted)
                                     .run().get(0)) {

                            predictedY = yPredictedTensor.getFloat();

//                        System.out.println("Test phase value x: " + x);
                        System.out.println("Collect value: " + prob(x));
                        System.out.println("Predicted value: " + predictedY);
                        }
                    }
                }

                // Extract linear regression model weight and bias values
                List<?> tensorList = session.runner()
                        .fetch(weightVariableName)
                        .fetch(biasVariableName)
                        .run();

                try (TFloat32 weightValue = (TFloat32)tensorList.get(0);
                     TFloat32 biasValue = (TFloat32)tensorList.get(1)) {

//                    System.out.println("Weight is " + weightValue.getFloat());
//                    System.out.println("Bias is " + biasValue.getFloat());
                }
                session.save(savePath);
            }
        }
    }
}
