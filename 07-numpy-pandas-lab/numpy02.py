import numpy as np

if __name__ == '__main__':
    a = np.array([[2,3,5], [7,3,13], [17,19,23]])
    b = np.arange(1, 10).reshape((3,3))
    print(a)
    print(b)
    print(a+b)
    print(a-b)
    print(a*b)
    print(np.multiply(a, b))
    print(np.dot(a, b))
    print(a.dot(b))

    print(np.sqrt(a))

    # aggregations
    print(a)
    print(b)
    print(np.sum(a))
    print(np.average(a))
    print(np.sum(a, axis=0))
    print(np.sum(a, axis=1))
    print(np.max(a, axis=1))
    print(np.maximum(a, b))

    #transposing
    a = np.arange(0,10,0.5).reshape((4,5))
    print(a)
    print(a.T)

    #broadcasting
    a = np.array([[2, 3, 5], [7, 3, 13], [17, 19, 23], [27, 31, 37]])
    print('a=\n', a, a.shape)
    b= np.array([0,5,0])
    bb = np.tile(b, (4, 1))
    print(bb, bb.shape)
    print(a + np.array([0,5,0]))
    print(a + bb)

    print(a * 42)
    a = np.arange(1,4).reshape((3,1))
    print(a)
    b = np.array([4,5])
    print(b)
    print(a * b)

    a = np.array([[2, 3, 5], [7, 3, 13], [17, 19, 23], [27, 31, 37]])
    c= np.array([1,2,3])
    print((a + c))



