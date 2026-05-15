import numpy as np
from matplotlib import pyplot as plt

if __name__ == '__main__':
    data = np.genfromtxt('data/PewDiePie.csv', delimiter=',', dtype=np.int32)
    print(np.shape(data))
    print(data.dtype)
    y = [data[i, 1] for i in range(data.shape[0])][1:]
    print(y)

    plt.plot(range(20), y)
    plt.show()
    # plt.hist(y, bins=10, range=(0, 80000))
    # plt.title("Histogram")

    plt.show()