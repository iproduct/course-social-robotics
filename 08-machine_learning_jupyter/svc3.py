from sklearn import datasets
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# Load example data
X, y = datasets.make_moons(noise=0.2, random_state=0)

# Split into train/test
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.3, random_state=42
)

# Create SVC model with RBF kernel
model = SVC(kernel='rbf', C=1.0, gamma='scale')
# SVC(kernel='linear')
# SVC(kernel='poly', degree=3)
# SVC(kernel='sigmoid')


# Train
model.fit(X_train, y_train)

# Predict
y_pred = model.predict(X_test)

# Accuracy
print("Accuracy:", accuracy_score(y_test, y_pred))
