import matplotlib.pyplot as plt
import tensorflow as tf
from keras.datasets import fashion_mnist
import os
import datetime

if __name__ == '__main__':
    os.environ["XLA_FLAGS"] = '--xla_gpu_cuda_data_dir="D:/Program Files/CUDA/v11.2/development"'
    physical_devices = tf.config.list_physical_devices('GPU')
    tf.config.experimental.set_memory_growth(physical_devices[0], True)  # important!
    tf.config.optimizer.set_jit(True)

    (x_train, y_train),(x_test, y_test) = fashion_mnist.load_data()
    x_train, x_test = x_train / 255.0, x_test / 255.0

    print(x_train.ndim)
    print(x_train.shape)
    print(x_train.dtype)

    x_slice = x_train[:20, :, :]
    y_slice = y_train[:20]
    for i in range(len(x_slice)):
        digit = x_train[i]
        img = plt.imshow(digit, cmap=plt.cm.binary)
        plt.title(y_train[i])
        plt.show()

    def create_model():
      return tf.keras.models.Sequential([
        tf.keras.layers.Flatten(input_shape=(28, 28)),
        tf.keras.layers.Dense(512, activation='relu'),
        tf.keras.layers.Dropout(0.2),
        tf.keras.layers.Dense(10, activation='softmax')
      ])

    model = create_model()
    model.summary()

    logdir = os.path.join("logs", datetime.datetime.now().strftime("%Y%m%d-%H%M%S"))
    tensorboard_callback = tf.keras.callbacks.TensorBoard(logdir, histogram_freq=1)

    def train_model(model):
      model.compile(optimizer='adam',
                    loss='sparse_categorical_crossentropy',
                    metrics=['accuracy'])

      model.fit(x=x_train,
                y=y_train,
                epochs=5,
                validation_data=(x_test, y_test),
                callbacks=[tensorboard_callback])

    train_model(model)

    def test_model(model):
        test_loss, test_acc = model.evaluate(x_test, y_test)
        print(f'Test Accuracy: {test_acc}')
        print(f'Test Loss: {test_loss}')
        print('Demo finished')

    test_model(model)