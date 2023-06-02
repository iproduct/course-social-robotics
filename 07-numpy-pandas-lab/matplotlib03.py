import io

import matplotlib.pyplot as plt
import matplotlib
import numpy as np

if __name__ == "__main__":
    print(matplotlib.__version__)
    xpoints = np.array([5, 7, 8, 7, 2, 17, 2, 9, 4, 11, 12, 9, 6])
    ypoints1 = np.array([99, 86, 87, 88, 111, 86, 103, 87, 94, 78, 77, 85, 86])
    ypoints2 = np.random.randint(70, 120, len(xpoints))
    colors = np.array(np.arange(50, 150, 100 // len(xpoints)))[:len(xpoints)]
    plt.subplots_adjust(hspace=0.4)
    # ax1 = plt.subplot(2, 1, 1)
    plt.scatter(xpoints, ypoints1)
    plt.title("Sports Watch Data")
    plt.xlabel("Average Pulse")
    plt.ylabel("Calorie Burnage")
    plt.grid(color='green', linestyle='--', linewidth=0.5)

    # plt.subplot(2, 1, 2, sharex=ax1)
    plt.scatter(xpoints, ypoints2, c=colors, cmap='viridis')
    plt.colorbar()
    # plt.xlabel("Average Pulse")
    # plt.ylabel("Calorie Burnage")
    # plt.grid(color='green', linestyle='--', linewidth=0.5)

    plt.savefig("pie.png", format="png")
    plt.show()
