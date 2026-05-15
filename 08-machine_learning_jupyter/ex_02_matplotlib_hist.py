import numpy as np
import seaborn as sns
from matplotlib import pyplot as plt

if __name__ == '__main__':
    data = np.genfromtxt('data/PewDiePie.csv', delimiter=',')
    print(np.shape(data))
    print(data.dtype)
    y = [data[i][1] for i in range(len(data))][1:]
    print(y)

    plt.plot(range(20), y)
    plt.show()
    plt.hist(y, bins=10, range=(0, 80000))
    plt.title('Histogram')
    plt.show()



