import datetime

from keras import layers
from keras import models
from keras.datasets import mnist
from keras.utils import to_categorical
import tensorflow as tf
import os

if __name__ == '__main__':
    os.environ["XLA_FLAGS"] = '--xla_gpu_cuda_data_dir="D:/Program Files/CUDA/v11.2/development"'
    physical_devices = tf.config.list_physical_devices('GPU')
    tf.config.experimental.set_memory_growth(physical_devices[0], True) # important!
    tf.config.optimizer.set_jit(True)

    (train_images, train_labels), (test_images, test_labels) = mnist.load_data()
    train_images = train_images.reshape((60000, 28 * 28))
    train_images = train_images.astype('float32') / 255
    test_images = test_images.reshape((10000, 28 * 28))
    test_images = test_images.astype('float32') / 255
    train_labels = to_categorical(train_labels)
    test_labels = to_categorical(test_labels)

    network = models.Sequential()
    network.add(layers.Dense(512, activation='relu', input_shape=(28 * 28,)))
    network.add(layers.Dense(10, activation='softmax'))
    network.summary()

    network.compile(optimizer='rmsprop',
                    loss='categorical_crossentropy',
                    metrics=['accuracy'])

    logdir = os.path.join("logs", datetime.datetime.now().strftime("!%Y%m%d-%H%M%S"))
    tensorboard_callback = tf.keras.callbacks.TensorBoard(logdir, histogram_freq=1)

    network.fit(train_images, train_labels, epochs=5, batch_size=128, callbacks=[tensorboard_callback])


    test_loss, test_acc = network.evaluate(test_images, test_labels)
    print(f'Test Accuracy: {test_acc}')
    print(f'Test Loss: {test_loss}')
    print('Demo finished')

