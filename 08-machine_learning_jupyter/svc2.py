from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score

# Load a simple dataset (e.g., iris)
X, y = datasets.load_iris(return_X_y=True)

# Split into train and test sets
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.3, random_state=42
)

# Linear-kernel SVC
svc_linear = SVC(kernel="linear", C=1.0)
svc_linear.fit(X_train, y_train)
y_pred_linear = svc_linear.predict(X_test)
print("Linear kernel accuracy:", accuracy_score(y_test, y_pred_linear))

# RBF-kernel SVC
svc_rbf = SVC(kernel="rbf", C=1.0, gamma="scale")
svc_rbf.fit(X_train, y_train)
y_pred_rbf = svc_rbf.predict(X_test)
print("RBF kernel accuracy:", accuracy_score(y_test, y_pred_rbf))
