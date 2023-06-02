import numpy as np

if __name__ == '__main__':
    x1 = np.arange(9.0).reshape((3, 3))
    print(x1)
    x2 = np.arange(3.0)
    print(x2)
    x3 = np.add(x1, x2)
    print(x3)