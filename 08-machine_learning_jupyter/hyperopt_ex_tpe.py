from hyperopt import fmin, tpe, hp, Trials, STATUS_OK
from sklearn.datasets import load_iris
from sklearn.model_selection import cross_val_score
from sklearn.ensemble import RandomForestClassifier
import numpy as np

# Load data
X, y = load_iris(return_X_y=True)

# Define objective function
def objective(params):
    clf = RandomForestClassifier(
        n_estimators=int(params['n_estimators']),
        max_depth=int(params['max_depth']),
        min_samples_split=int(params['min_samples_split']),
        random_state=42
    )
    score = cross_val_score(clf, X, y, cv=3).mean()
    return {'loss': -score, 'status': STATUS_OK}  # minimize negative accuracy

# Define search space
space = {
    'n_estimators': hp.quniform('n_estimators', 50, 300, 1),
    'max_depth': hp.quniform('max_depth', 3, 15, 1),
    'min_samples_split': hp.quniform('min_samples_split', 2, 10, 1)
}

# Run TPE optimization
trials = Trials()
best = fmin(
    fn=objective,
    space=space,
    algo=tpe.suggest,  # TPE sampler
    max_evals=50,
    trials=trials,
    rstate=np.random.default_rng(42)
)

print("Best hyperparameters:", best)
