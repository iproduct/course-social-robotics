import numpy as np

if __name__ == '__main__':
    arr = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12], [13, 14, 15], [16, 17, 18]])
    print(arr)
    splitted = np.array_split(arr, 3, axis=1)
    print("\nAfter split:\n",*(i for i in splitted), sep="\n")

