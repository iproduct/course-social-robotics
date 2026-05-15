# Authors: The scikit-learn developers
# SPDX-License-Identifier: BSD-3-Clause

import matplotlib.pyplot as plt
import numpy as np

from sklearn import linear_model
if __name__=='__main__':
    # X is the 10x10 Hilbert matrix
    X = 1.0 / (np.arange(1, 11) + np.arange(0, 10)[:, np.newaxis])
    y = np.ones(10)
    np.set_printoptions(linewidth=np.inf)
    print(X)
    print('\n', y)

    n_alphas = 200
    alphas = np.logspace(-10, -2, n_alphas)

    coefs = []
    for a in alphas:
        ridge = linear_model.Ridge(alpha=a, fit_intercept=False)
        ridge.fit(X, y)
        coefs.append(ridge.coef_)

    ax = plt.gca()

    ax.plot(alphas, coefs)
    ax.set_xscale("log")
    ax.set_xlim(ax.get_xlim()[::-1])  # reverse axis
    plt.xlabel("alpha")
    plt.ylabel("weights")
    plt.title("Ridge Coefficients vs Regularization Strength (alpha)")
    plt.axis("tight")
    plt.legend(
        [f"Feature {i + 1}" for i in range(X.shape[1])], loc="best", fontsize="small"
    )
    plt.show()