import os

import tensorflow as tf
from tensorflow import keras

tmpdir = "./"
module_no_signatures_path = os.path.join(tmpdir, 'modelsaves')
loaded = tf.saved_model.load(module_no_signatures_path)
print("MobileNet has {} trainable variables: {}, ...".format(
    len(loaded.trainable_variables),
    ", ".join([v.name for v in loaded.trainable_variables[:5]])))
