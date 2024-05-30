import datetime

import tensorflow as tf
from keras import layers
from keras import models
from keras.datasets import mnist
from keras.utils import to_categorical
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

    model = models.Sequential()
    model.add(layers.Dense(512, activation='relu', input_shape=(28 * 28,)))
    model.add(layers.Dense(10, activation='softmax'))
    model.summary()

    model.compile(optimizer='adam',
                    loss='categorical_crossentropy',
                    metrics=['accuracy'])

    logdir = os.path.join("logs", datetime.datetime.now().strftime("%Y%m%d-%H%M%S"))
    tboard_callback = tf.keras.callbacks.TensorBoard(log_dir=logdir,
                                                     histogram_freq=1,
                                                     profile_batch='500,520')

    # Prepare an optimizer.
    optimizer = tf.keras.optimizers.Adam()

    # Prepare a loss function.
    loss_fn = tf.keras.losses.kl_divergence
    # loss_object = tf.keras.losses.MeanSquaredError()


    # Iterate over the batches of a dataset.
    for i in range(100):

        # Open a GradientTape.
        with tf.GradientTape() as tape:
            # Forward pass.
            predictions = model(train_images)
            # Compute the loss value for this batch.
            loss_value = loss_fn(train_labels, predictions)

        # Get gradients of loss wrt the weights.
        gradients = tape.gradient(loss_value, model.trainable_weights)
        # Update the weights of the model.
        optimizer.apply_gradients(zip(gradients, model.trainable_weights))
        print(f'Iteration: {i} - loss: {loss_value[:20]}')


    test_loss, test_acc = model.evaluate(test_images, test_labels)
    print(f'Test Accuracy: {test_acc}')
    print(f'Test Loss: {test_loss}')
    print('Demo finished')