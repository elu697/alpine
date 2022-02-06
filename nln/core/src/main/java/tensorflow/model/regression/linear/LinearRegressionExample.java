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
package tensorflow.model.regression.linear;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.framework.optimizers.GradientDescent;
import org.tensorflow.framework.optimizers.Momentum;
import org.tensorflow.framework.optimizers.Optimizer;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.op.Op;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.core.Variable;
import org.tensorflow.op.math.Add;
import org.tensorflow.op.math.Div;
import org.tensorflow.op.math.Mul;
import org.tensorflow.op.math.Pow;
import org.tensorflow.types.TFloat32;
import tensorflow.SimpleModel;

/**
 * In this example TensorFlow finds the weight and bias of the linear regression during 1 epoch,
 * training on observations one by one.
 * <p>
 * Also, the weight and bias are extracted and printed.
 */
public class LinearRegressionExample {
    /**
     * Amount of data points.
     */
    private static final int N = 1000;

    /**
     * This value is used to fill the Y placeholder in prediction.
     */
    public static final float LEARNING_RATE = 0.1f;
    public static final String WEIGHT_VARIABLE_NAME = "weight";
    public static final String BIAS_VARIABLE_NAME = "bias";

    public static ArrayList<Float> makeX() {
        ArrayList<Float> arrayList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            arrayList.add((float) ((i+1)*0.01));
        }
        return arrayList;
    }

    public static void main(String[] args) {

        // Prepare the data
        ArrayList<Float> xValues = makeX();
        ArrayList<Float> yValues = new ArrayList<>();

        Random rnd = new Random(42);

        for (int i = 0; i < xValues.size(); i++) {
//            yValues[i] = (float) (10 * xValues[i] + 2 + 0.1 * (rnd.nextDouble() - 0.5));
//            yValues.add((float)(xValues.get(i)*10 + 2 + 0.1 * (rnd.nextDouble() - 0.5)));
            yValues.add((float)(xValues.get(i)*2));
        }

//        SimpleModel.Action(xValues, yValues);
    }
}
