import numpy as np
import matplotlib.pyplot as plt
from keras.datasets import mnist
from keras.datasets import mnist

if __name__ == '__main__':
    t = np.array(12)
    print(f'Tensor: {type(t)}: {t} of rank: {t.ndim}')
    print(f'Shape: {t.shape}\n')
    t = np.array([15,98,12.7, 7.5, 3])
    print(f'Tensor: {type(t)}:{t} of rank: {t.ndim}')
    print(f'Shape: {t.shape}\n')
    t = np.array([
        [15, 98, 23.4, 4.5, 1],
        [43, 98, 12.7, 72, 3],
        [19, 98, 3, 7.5, 7],
        [7, 98, 145, 15, 2],
    ])
    print(f'Tensor: {type(t)}:\n{t} of rank: {t.ndim}')
    print(f'Shape: {t.shape}\n')
    t = np.array([
        [
            [15, 98, 23.4, 4.5, 1],
            [43, 98, 12.7, 72, 3],
            [19, 98, 3, 7.5, 7],
            [7, 98, 145, 15, 2],
        ],
        [
            [15, 98, 23.4, 4.5, 1],
            [43, 98, 12.7, 72, 3],
            [19, 98, 3, 7.5, 7],
            [7, 98, 145, 15, 2],
        ],
        [
            [15, 98, 23.4, 4.5, 1],
            [43, 98, 12.7, 72, 3],
            [19, 98, 3, 7.5, 7],
            [7, 98, 145, 15, 2],
        ]
    ])
    print(f'Tensor: {type(t)}:\n{t} of rank: {t.ndim}')
    print(f'Shape: {t.shape}\n')

    # MNIST datasets - handwritten numbers
    (train_images, train_labels), (test_images, test_labels) = mnist.load_data()
    print(train_images.ndim)
    print(train_images.shape)
    print(train_images.dtype)

    im_slice = train_images[:20, :, :]
    lab_slice = train_labels[:20]
    examples = zip(im_slice, lab_slice)
    for digit, label in examples:
        img = plt.imshow(digit, cmap=plt.cm.binary)
        plt.title(label)
        plt.show()