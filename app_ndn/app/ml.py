import os
import tensorflow as tf
from tensorflow.keras.models import Sequential, load_model
from tensorflow.keras.layers import Conv2D, MaxPooling2D
from tensorflow.keras.layers import Flatten, Dense, Dropout
from tensorflow.keras.callbacks import TensorBoard, EarlyStopping
from tensorflow.python.ops.gen_experimental_dataset_ops import load_dataset

class ML:
    def init(self, model_name):
        self.log_dir = os.path.join(os.getcwd(), 'logdir')
        self.log_init()

        self.image_shape = (28, 28, 1) # MNIST画像フォーマット. 28x28ピクセルのグレースケール画像
        self.num_classes = 10 # 出力は0~9の10クラス
        self.model = self.load_model(model_name)
        (self.x_train, self.y_train), (self.x_test, self.y_test) = load_dataset()

        self.learn()
        self.test()

    def log_init(self):
        if os.path.exists(self.log_dir):
            import shutil
        shutil.rmtree(self.log_dir)
        os.mkdir(self.log_dir)

    def load_model(self, model_name):
        return load_model(model_name) if load_model(model_name) else self.cnn(self.image_shape, self.num_classes)

    def save_model(self, model_name):
        self.model.save(model_name)

    def load_dataset(self):
        # MNISTデータセットのロードと前処理
        mnist = tf.keras.datasets.mnist
        (x_train, y_train), (x_test, y_test) = mnist.load_data()
        x_train, x_test = x_train.reshape((60000, 28, 28, 1)), x_test.reshape((10000, 28, 28, 1))
        x_train, x_test = [self.preprocess(d) for d in [x_train, x_test]]
        y_train, y_test = [self.preprocess(d, label_data=True) for d in [y_train, y_test]]
        return (x_train, y_train), (x_test, y_test)

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
        model.add(Conv2D(16, kernel_size=(3, 3), activation='relu', input_shape=input_shape))
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
    def preprocess(data, label_data= False, num_classes=10):
        if label_data:
            # 教師ラベルをone-hotベクトルに変換する
            data = tf.keras.utils.to_categorical(data, num_classes)
        else:
            data = data.astype('float32')
            data /= 255
        return data
