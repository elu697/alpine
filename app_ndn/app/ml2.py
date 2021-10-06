import os
import tensorflow as tf
from tensorflow.keras.models import Sequential, load_model
from tensorflow.keras.layers import Conv2D, MaxPooling2D
from tensorflow.keras.layers import Flatten, Dense, Dropout
from tensorflow.keras.callbacks import TensorBoard, EarlyStopping
from tensorflow.keras.datasets import mnist
import random
import numpy


class ML2:
    def __init__(self, model_name, fold=1, data=0):
        self.log_dir = os.path.join(os.getcwd(), "logdir")
        self.log_init()
        print(tf.test.is_gpu_available())

        self.load_model(model_name)
        self.load_dataset(fold, data)
        self.learn()
        self.test()
        self.save_model(model_name)

    def log_init(self):
        if os.path.exists(self.log_dir):
            import shutil
            shutil.rmtree(self.log_dir)
            os.mkdir(self.log_dir)

    def load_model(self, model_name):
        # MNIST画像フォーマット. 28x28ピクセルのグレースケール画像
        # 出力は0~9の10クラス
        try:
            self.model = load_model(model_name)
        except:
            self.model = self.cnn((28, 28, 1), 10)

    def save_model(self, model_name):
        self.model.save(model_name)

    def load_dataset(self, fold=1, data=0):
        # MNISTデータセットのロードと前処理
        mnist = tf.keras.datasets.mnist
        (x_train, y_train), (x_test, y_test) = mnist.load_data()
        x_train, x_test = x_train.reshape(
            (60000, 28, 28, 1)), x_test.reshape((10000, 28, 28, 1))

        fold_x_train = numpy.array_split(x_train, fold)[data]
        fold_y_train = numpy.array_split(y_train, fold)[data]

        self.x_train, self.x_test = [
            self.preprocess(d) for d in [fold_x_train, x_test]]
        self.y_train, self.y_test = [self.preprocess(d, label_data=True) for d in [
            fold_y_train, y_test]]

    def learn(self):
        # モデルのコンパイルと学習
        self.model.compile(optimizer='adam',
                           loss='categorical_crossentropy',
                           metrics=['accuracy'])
        self.model.fit(self.x_train, self.y_train, batch_size=128, epochs=20, validation_split=0.2,
                       callbacks=[TensorBoard(log_dir=self.log_dir),
                                  EarlyStopping(monitor='val_loss', patience=2, verbose=0, mode='auto')],
                       verbose=1)

    def test(self):
        # テストデータを使って精度を検証
        score = self.model.evaluate(self.x_test, self.y_test, verbose=0)
        print("Test score: ", score[0])
        print("Test accuracy: ", score[1])

    @staticmethod
    def cnn(input_shape, num_classes):
        model = Sequential()
        model.add(Conv2D(16, kernel_size=(3, 3),
                  activation='relu', input_shape=input_shape))
        model.add(Conv2D(32, (3, 3), activation='relu'))
        model.add(MaxPooling2D(pool_size=(2, 2)))
        model.add(Conv2D(64, (3, 3), activation='relu'))
        model.add(MaxPooling2D(pool_size=(2, 2)))
        model.add(Dropout(0.3))
        model.add(Flatten())
        model.add(Dense(128, activation='relu'))
        model.add(Dropout(0.3))
        model.add(Dense(num_classes, activation='softmax'))
        model.summary()
        return model

    @staticmethod
    def preprocess(data, label_data=False, num_classes=10):
        if label_data:
            # 教師ラベルをone-hotベクトルに変換する
            data = tf.keras.utils.to_categorical(data, num_classes)
        else:
            data = data.astype('float32')
            data /= 255
        return data


if __name__ == '__main__':
    model = "test_model.h5"
    m1 = ML2(model_name=model, fold=2, data=0)
    m2 = ML2(model_name=model, fold=2, data=1)
    pass
