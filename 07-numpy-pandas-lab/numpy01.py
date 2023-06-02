import numpy as np

if __name__ == '__main__':
    a = np.array([1,2,3,4,5])
    print(a)
    print(a.shape)
    print(a[0], a[1], a[2], a[3], a[4])
    a[0] = 42
    print(a)

    b = np.array([[1,2,3], [4,5,6], [7,8,9]])
    print(b)
    print(b.shape)
    print(b[0,0], b[1,0], b[2,0])

    a = np.zeros((3, 4), dtype=np.int8)
    print(a, a.shape)

    a = np.ones((3, 4))
    print(a, a.shape)

    a = np.full((3, 4), 42.)
    print(a, a.shape, a.dtype)


    a = np.eye(4)
    print(a, a.shape, a.dtype)

    a = np.random.random((3, 4))
    print(a, a.shape, a.dtype)

    a = np.random.randint(10, 100, (2,3,4))
    print(a, a.shape, a.dtype)

    b = a[0, :, 1:3].copy()
    b[0, 0] = 42
    print(b, b.shape, b.dtype)

    print('Base:\n', b.base)
    print(a)

    b = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
    print(b)
    print(b[[0, 1, 2], [0, 1, 0]])
    print(b[[0, 1, 2], [0, 1, 2]])

    a = np.random.random((3, 4))
    print(a, a.shape, a.dtype)
    c= a[:, [0,3]]
    print(c, c.shape)

    r = np.arange(0, 10, 0.5)
    print(r)
    print(r[np.arange(0, 20, 3)] + 10)

    c = np.random.random((3, 4))
    print(c)
    bool_idx = (c > 0.5)
    print(bool_idx)
    print(c[bool_idx])
    print(c[c > 0.5])

    a = np.arange(10).reshape(2, 5)
    print(a)
    ixgrid = np.ix_([0, 1], [2, 4])
    print(ixgrid)
    print(ixgrid[0].shape, ixgrid[1].shape)
    print(a[ixgrid])

    arr = np.array([1.1, 2.1, 3.1])
    newarr = arr.astype('i')
    print(arr, arr.dtype)
    print(newarr, newarr.dtype)

    #reshape
    arr = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])
    newarr = arr.reshape(-1, 2, 2)
    newarr[1,0,1] = 42
    print(newarr)
    print(arr)
    print(newarr.reshape(-1))
    print(newarr.flatten('F'))





