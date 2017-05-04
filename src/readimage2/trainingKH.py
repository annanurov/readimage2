
from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import os
import urllib

import numpy as np
import tensorflow as tf

#import os
os.environ['TF_CPP_MIN_LOG_LEVEL']='2'

homepage = "https://github.com/annanurov/readimage2/raw/master/src/readimage2/"
# Data sets
LETTERS_TRAINING = "resized_training.csv" #"resized.csv"
LETTERS_TRAINING_URL = homepage + "resized_training.csv"
#LETTERS_TRAINING_URL = homepage + "resized.csv"
##the file is 5MB

LETTERS_TEST = "resized_test.csv"#"resized.csv"
LETTERS_TEST_URL = homepage + "resized_test.csv"
#LETTERS_TEST_URL = homepage + "resized.csv"
##the file is 5MB

#omit warnings:
tf.logging.set_verbosity(tf.logging.ERROR)

def main():
  # If the training and test sets aren't stored locally, download them.
  if not os.path.exists(LETTERS_TRAINING):
    raw = urllib.urlopen(LETTERS_TRAINING_URL).read()
    with open(LETTERS_TRAINING, "w") as f:
      f.write(raw)

  if not os.path.exists(LETTERS_TEST):
    raw = urllib.urlopen(LETTERS_TEST_URL).read()
    with open(LETTERS_TEST, "w") as f:
      f.write(raw)

  # Load datasets.
  training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
      filename=LETTERS_TRAINING,
      target_dtype=np.int,
      features_dtype=np.float32)

  test_set = tf.contrib.learn.datasets.base.load_csv_with_header(
      filename=LETTERS_TEST,
      target_dtype=np.int,
      features_dtype=np.float32)

  # Specify that all features have real-value data
  feature_columns = [tf.contrib.layers.real_valued_column("", dimension=4)]

  # Build 3 layer DNN with 10, 20, 10 units respectively.
  classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns,
                                              hidden_units=[10, 20, 10],
                                              n_classes=17,#3,
                                              model_dir="/tmp/letters_model")
                                              #model_dir="/tmp/iris_model")

  # Define the training inputs
  def get_train_inputs():
    x = tf.constant(training_set.data)
    y = tf.constant(training_set.target)

    return x, y

  # Fit model.
  classifier.fit(input_fn=get_train_inputs, steps=2000)

  # Define the test inputs
  def get_test_inputs():
    x = tf.constant(test_set.data)
    y = tf.constant(test_set.target)

    return x, y

  # Evaluate accuracy.
  accuracy_score = classifier.evaluate(input_fn=get_test_inputs,
                                       steps=1)["accuracy"]
'''
#make a two new samples...
  print("\nTest Accuracy: {0:f}\n".format(accuracy_score))

  # Classify two new flower samples.
  def new_samples():
    return np.array(
      [[6.4, 3.2, 4.5, 1.5],
       [5.8, 3.1, 5.0, 1.7]], dtype=np.float32)

  predictions = list(classifier.predict(input_fn=new_samples))

  print(
      "New Samples, Class Predictions:    {}\n"
      .format(predictions))
'''
if __name__ == "__main__":
    main()
