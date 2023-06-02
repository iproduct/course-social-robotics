import numpy as np

if __name__ == '__main__':
    arr1 = np.array([1, 2, 3, 4, 2 ])
    arr2 = np.array([3, 4, 5, 6])

    newarr = np.intersect1d(arr1, arr2, assume_unique=False)

    print(newarr)
    print(type(newarr))

    set1 = np.unique(arr1)
    print(set1)
    print(type(set1))
