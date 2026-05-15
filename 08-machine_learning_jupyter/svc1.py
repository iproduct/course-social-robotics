import numpy as np
import matplotlib.pyplot as plt
from sklearn.datasets import make_circles
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 1. Create a nonlinear synthetic dataset
X, y = make_circles(n_samples=600, noise=0.1, factor=0.3, random_state=0)

# Split into train/test
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.25, random_state=0
)

# 2. Train an SVC with an RBF kernel
clf = SVC(kernel='poly', degree=2, C=1.0, gamma="scale")
clf.fit(X_train, y_train)

# 3. Predict and evaluate
y_pred = clf.predict(X_test)
acc = accuracy_score(y_test, y_pred)
print("Test accuracy:", acc)

# 4. Plot decision boundary
def plot_decision_boundary(model, X, y):
    x_min, x_max = X[:, 0].min() - 0.5, X[:, 0].max() + 0.5
    y_min, y_max = X[:, 1].min() - 0.5, X[:, 1].max() + 0.5
    xx, yy = np.meshgrid(
        np.linspace(x_min, x_max, 500),
        np.linspace(y_min, y_max, 500)
    )
    print(xx.shape, yy.shape, np.c_[xx.ravel(), yy.ravel()].shape)
    Z = model.predict(np.c_[xx.ravel(), yy.ravel()])
    Z = Z.reshape(xx.shape)
    print(Z.shape)

    plt.contourf(xx, yy, Z, alpha=0.2)
    plt.scatter(X[:, 0], X[:, 1], c=y, s=20, edgecolor="k")
    plt.title("SVC Decision Boundary (Polynomial deg=2 Kernel)")
    plt.show()

plot_decision_boundary(clf, X, y)
