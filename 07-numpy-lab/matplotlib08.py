import matplotlib.pyplot as plt
import numpy as np
import seaborn as sb
from numpy import random

if __name__ == '__main__':
    # Compute the x and y coordinates for points on sine and cosine curves
    x = np.arange(0, 3 * np.pi, 0.1)
    y_sin = np.sin(x)
    y_cos = np.cos(x)

    # Plot the points using matplotlib
    plt.plot(x, y_sin)
    plt.plot(x, y_cos)
    plt.xlabel('x axis label')
    plt.ylabel('y axis label')
    plt.title('Sine and Cosine')
    plt.legend(['Sine', 'Cosine'])
    plt.show()

    fig, ax = plt.subplots(5, 1, sharex="all")
    fig.set_figheight(10)
    arr = random.normal(loc=50, scale=20, size=1000)
    print(arr)
    # sb.histplot(arr, color="lightblue")
    sb.distplot(arr, kde=True, ax=ax[0], label="Normal Distribution")
    ax[0].set_title("Normal Distribution")

    x = random.binomial(n=100, p=0.8, size=100)
    print(x)
    sb.distplot(arr, kde=True, ax=ax[1])
    ax[1].set_title("Binomial Distribution")

    x = random.poisson(lam=5, size=100)
    print(x)
    sb.distplot(arr, kde=True, ax=ax[2])
    ax[2].set_title("Poison Distribution")

    x = random.logistic(loc=50, scale=2, size=1000)
    print(x)
    sb.distplot(x, ax=ax[3])
    ax[3].set_title("Logistic Distribution")
    ax[3].legend(["KDE", "Histogram"])

    sb.ecdfplot(x, ax=ax[4])
    ax[4].set_title("Logistic Distribution (Cumulative)")
    ax[4].legend(["KDE", "Histogram"])

    plt.subplots_adjust(hspace=0.3)
    plt.show()
