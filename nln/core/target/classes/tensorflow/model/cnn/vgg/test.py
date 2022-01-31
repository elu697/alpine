import os

import tensorflow as tf
from tensorflow import keras

tmpdir = "./ModelStore"
module_no_signatures_path = os.path.join(tmpdir, 'modelA')

# saver = tf.train.Saver()

# loaded = tf.saved_model.load(module_no_signatures_path)
# loaded.summary()

# new_model = tf.keras.models.load_model(module_no_signatures_path)


# print("MobileNet has {} trainable variables: {}, ...".format(
#     len(loaded.trainable_variables),
#     ", ".join([v.name for v in loaded.trainable_variables])))
