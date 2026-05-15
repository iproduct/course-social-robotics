import numpy as np
import matplotlib.pyplot as plt

if __name__ == "__main__":
    ys = 200 + np.random.randn(100)
    x = [x for x in range(len(ys))]

    plt.plot(x, ys, '-')
    plt.fill_between(x, ys, 195, where=(ys > 195), facecolor='g', alpha=0.6)
    plt.title('Sample visualization')
    plt.show()