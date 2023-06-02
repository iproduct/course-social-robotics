import numpy as np

if __name__ == '__main__':
    arr = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18])
    newarr = arr.reshape(2, -1, 3)
    newarr[1,1,2] = 42
    print("Reahaped:\n", newarr)
    print("\nOriginal: ", arr)
    print("\nReshaped base: ", newarr.base)

    linear = newarr.reshape(18)
    print("\n",linear)

    arr = np.arange(8).reshape((2, 2, 2))
    print("\n",arr)
    flipped = np.flip(arr, 1)
    print("\n", flipped)

    for x in np.nditer(arr):
        print(x)

