import numpy as np

if __name__ == '__main__':
    arr = np.array([[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]])
    print(arr[:, 1:4])

    arr_ints = np.random.randint(1.0, 100.0, (5, 5), int)
    arr = np.random.uniform(1.0, 100.0, (5, 5))
    print("\n", arr)
    print("\n", arr[1:4, 1:4])

    arr = np.array([52, 53, 54, 55], dtype='S')
    print(arr)
    print(arr.dtype)

    arr_float = arr_ints.astype(str)
    print("\n", arr_float)

