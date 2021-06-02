from keras import layers
from keras import models
from keras.datasets import mnist
from keras.utils import to_categorical
import tensorflow as tf
import os
import contextlib
import datetime

# @contextlib.contextmanager
# def options(options):
#   old_opts = tf.config.optimizer.get_experimental_options()
#   tf.config.optimizer.set_experimental_options(options)
#   try:
#     yield
#   finally:
#     tf.config.optimizer.set_experimental_options(old_opts)


if __name__ == '__main__':
    # os.environ['CUDA_VISIBLE_DEVICES'] = '-1'
    os.environ["XLA_FLAGS"] = '--xla_gpu_cuda_data_dir="D:/Program Files/CUDA/v11.2/development"'
    os.environ["TF_GPU_THREAD_MODE"] = 'gpu_private'

    physical_devices = tf.config.list_physical_devices('GPU')
    tf.config.experimental.set_memory_growth(physical_devices[0], True) # important!
    # tf.config.experimental.enable_mlir_bridge()
    # tf.config.experimental.enable_mlir_graph_optimization()
    tf.config.optimizer.set_jit(True)

    model = models.Sequential()
    model.add(layers.Conv2D(32, (3, 3), activation='relu', input_shape=(28, 28, 1)))
    model.add(layers.MaxPooling2D((2, 2)))
    model.add(layers.Conv2D(64, (3, 3), activation='relu'))
    model.add(layers.MaxPooling2D((2, 2)))
    model.add(layers.Conv2D(64, (3, 3), activation='relu'))
    model.add(layers.Flatten())
    model.add(layers.Dense(64, activation='relu'))
    model.add(layers.Dense(10, activation='softmax'))
    model.summary()

    (train_images, train_labels), (test_images, test_labels) = mnist.load_data()
    train_images = train_images.reshape((60000, 28, 28, 1))
    train_images = train_images.astype('float32') / 255
    test_images = test_images.reshape((10000, 28, 28, 1))
    test_images = test_images.astype('float32') / 255
    train_labels = to_categorical(train_labels)
    test_labels = to_categorical(test_labels)
    model.compile(optimizer='rmsprop',
                  loss='categorical_crossentropy',
                  metrics=['accuracy'])
    # with options({'constant_folding': True, 'debug_stripper': True}):
    # print(tf.config.optimizer.get_experimental_options())

    logdir = os.path.join("logs", datetime.datetime.now().strftime("%Y%m%d-%H%M%S"))
    tensorboard_callback = tf.keras.callbacks.TensorBoard(logdir, histogram_freq=1, profile_batch = '500,520')

    model.fit(train_images, train_labels, epochs=5, batch_size=64, callbacks=[tensorboard_callback])
    test_loss, test_acc = model.evaluate(test_images, test_labels)
    print(f'Test Accuracy: {test_acc}')
    print(f'Test Loss: {test_loss}')
    print('Demo finished')

