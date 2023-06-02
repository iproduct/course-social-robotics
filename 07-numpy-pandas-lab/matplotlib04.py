import io

import matplotlib.pyplot as plt
import matplotlib
import numpy as np

if __name__ == "__main__":
    print(matplotlib.__version__)
    x = np.random.normal(110, 20, 250)
    print(x)

    # plot:
    fig, ax = plt.subplots()
    ax.hist(x, bins=8, linewidth=0.5, edgecolor="white")

    ax.set(ylim=(0, 100), yticks=np.arange(0,100, 10),
           xlim=(50, 150), xticks=np.arange(50, 150, 10))
    # plt.hist(x)

    plt.savefig("pie.png", format="png")
    plt.show()
