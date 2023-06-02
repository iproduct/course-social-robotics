import numpy as np

if __name__ == '__main__':
    x = [1, 2, 3, 4]
    y = [5, 6, 7, 8]
    z = []

    for i, j in zip(x, y):
      z.append(str(i) + str(j))
    print(z)

    def myconcat(x, y):
        return int(str(x) + str(y))
    uconcat = np.frompyfunc(myconcat, 2, 1)
    arrx = np.array(x).reshape(2, 2)
    arry = np.array(y).reshape(2, 2)
    print(arrx)
    print(arry)
    print("\nDot:\n", arrx.dot(arry))
    print("\nConcat:\n",uconcat(arrx, arry))

    # with broadcasting
    x = [1, 2, 3, 4]
    y = [4, 5, 6]
    arrx = np.array(x)
    arry = np.array(y).reshape((3,1))
    print("\n", arrx)
    print(arry)
    print(uconcat(arrx, arry))

    z = np.array([[1, 2, 3], [4, 5, 6]])
    w = np.array([1, 2])
    print("\n", (z.T + w).T) # T means transposed