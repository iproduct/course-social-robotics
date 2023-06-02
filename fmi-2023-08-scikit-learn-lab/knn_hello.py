from sklearn.neighbors import KNeighborsClassifier
import numpy as np
if __name__ == '__main__':
       X = np.array([[-1, -1], [-2, -1], [-3, -2], [1, 1], [2, 1], [3, 2]])
       Y = np.array([-1, -1, -2, 1, 2, 2])
       nbrs = KNeighborsClassifier(n_neighbors=3).fit(X, Y)
       distances, indices = nbrs.kneighbors(X)
       test = [[0, 1],
              [1, 0],
              [2, 1],
              [3, 4],
              [4, 3],
              [5, 4]]
       for example in test:
              prediction = nbrs.predict([example])
              print(f'{example} -> {prediction}')