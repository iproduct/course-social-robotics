import time
from random import Random
import numpy as np


if __name__ == '__main__':
    arr = np.array([1, 2, 3, 4, 5])
    print(arr)
    print(np.__version__)

    arr = np.fromiter({1, 2, 3, 4, 5, 6}, dtype=np.uint64)
    arr = np.fromiter({1, 2, 3, 4, 5, 6}, dtype=np.float64)
    arr = np.fromiter({1, 2, 3, 4, 5, 6}, dtype='U1')
    arr = np.fromiter(range(1, 7), dtype=(str, 1))
    print(arr)
    print(type(arr))
    print(arr.dtype)

    arr = np.array([[1, 2, 3], [4, 5, 6]])
    print(arr)

    arr = np.array([[[1.5, 2, 3], [4, 5, 6]], [[1, 2, 3], [4, 5, 6]]])
    print(arr)
    print('number of dimensions :', arr.ndim)
    print('strides of dimensions :', arr.strides)
    print('shape :',  arr.shape)
    print('element type :',  arr.dtype)
    print('itemsize :',  arr.itemsize)

    for v in arr.flat:
        print(v, end=" | ", sep=", ")

    print()
    arr = np.array([1, 2, 3, 4], ndmin=5)
    print(arr)
    print('number of dimensions :', arr.ndim)

    # from function
    arr = np.fromfunction(lambda i, j: i * j, (5, 5), dtype=int)
    print(arr)
    rand = Random()
    arr = np.fromfunction(lambda i,j: i * j, (5, 5), dtype=int)
    print("\n", arr)

    arr = np.random.random((5,5))
    print("\n", arr)
    print("\n", arr.dtype)

    arr = np.random.randint(1, 100, (5,5), int)
    print("\n", arr)
    print("\n", arr.dtype)

    x = np.random.choice([3, 5, 7, 9], p=[0.1, 0.3, 0.6, 0.0], size=(100))
    print("\n", x)

