import io

import matplotlib.pyplot as plt
import matplotlib
import numpy as np

if __name__ == "__main__":
    print(matplotlib.__version__)
    y = np.array([35, 35, 25, 5])
    plt.pie(y)
    plt.savefig("pie.png", format="png")
    plt.show()
