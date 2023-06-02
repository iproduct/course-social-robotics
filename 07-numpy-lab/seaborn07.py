import matplotlib.pyplot as plt
import numpy as np
import seaborn as sb
from numpy import random

if __name__ == '__main__':
    # arr = np.arange(1, 101)
    # sb.distplot(arr)
    # print(arr)

    arr = random.normal(loc=50, scale=20, size=100)
    print(arr)
    # sb.histplot(arr, color="lightblue")
    sb.distplot(arr, kde=True, color="lightblue")

    # x = random.binomial(n=100, p=0.8, size=100)
    # print(x)
    # sb.distplot(x, hist=True, kde=False)

    # x = random.poisson(lam=5, size=100)
    # print(x)
    # sb.distplot(x, hist=True, kde=False)

    x = random.logistic(loc=50, scale=10, size=100)
    print(x)
    # sb.histplot(x, color="darkorange")
    sb.distplot(x, kde=True ,color="darkorange")

    plt.show()
