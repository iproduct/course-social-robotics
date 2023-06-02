import numpy as np

if __name__ == '__main__':
    a = np.arange(1,6)
    b = np.arange(0,2, 0.4)
    print(a,b)
    print(np.add(a,b))

    def exp(a, b):
        return a ** b

    myexp = np.frompyfunc(exp, 2, 1)
    print(type(myexp))
    print(myexp(a,b))

    newarr = np.sum([a, b], axis=0)

    print(newarr)
    c = np.array([0.5,0.8])
    print(np.in1d(c, b, assume_unique =True))



