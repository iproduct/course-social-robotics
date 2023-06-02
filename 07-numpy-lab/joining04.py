import numpy as np

if __name__ == '__main__':
    arr1 = np.array([1, 2, 3])
    arr2 = np.array([4, 5, 6])
    arr = np.concatenate((arr1, arr2))
    print(arr)

    stacked = np.stack((arr1, arr2), axis=0)
    print(stacked)
    stacked = np.stack((arr1, arr2), axis=1)
    print("\n", stacked)

    arr2d1 = np.arange(1,5).reshape(2, -1)
    arr2d2 = np.arange(5,9).reshape(2, -1)
    concat2 = np.concatenate((arr2d1, arr2d2), 0)
    stack2 = np.stack((arr2d1, arr2d2), 0)
    print("\nConcatenate:\n", concat2)
    print("\nStack:\n", stack2)

