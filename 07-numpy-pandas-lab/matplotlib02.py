import io

import matplotlib.pyplot as plt
import matplotlib
import numpy as np

if __name__ == "__main__":
    print(matplotlib.__version__)
    xpoints = np.array([3, 4, 6, 11, 15, 20])
    ypoints = np.array([1, 12, 7, 23, 13, 8])
    ypoints2 = np.random.randint(0, 25, 6)

    # plt.plot(xpoints, ypoints,  '^--g',  mec = 'r', ms = 20)
    plt.subplots_adjust(hspace=0.4)
    ax1 = plt.subplot(2, 1, 1)
    plt.plot(xpoints, ypoints,  marker = 'o', ms = 20, mec = '#4CAF50', linewidth =4, mfc = '#4CAF50')
    plt.title("Sports Watch Data")
    plt.xlabel("Average Pulse")
    plt.ylabel("Calorie Burnage")
    plt.grid(color='green', linestyle='--', linewidth=0.5)

    plt.subplot(2, 1, 2, sharex=ax1)
    plt.plot(xpoints, ypoints2,  color='r', marker = '^', ms = 20, mec = 'r', linewidth =4, mfc = 'b')
    plt.xlabel("Average Pulse")
    plt.ylabel("Calorie Burnage")
    plt.grid(color='green', linestyle='--', linewidth=0.5)

    plt.savefig("pie.png", format="png")
    plt.show()
