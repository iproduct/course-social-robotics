
if __name__ == '__main__':
    d = {}
    d.setdefault('a', 1)
    print(d)
    d.setdefault('a', 2)
    print(d)
    d['a'] = 2
    print(d)
    del(d['a'])
    print(d)

    # dict comprehension
    squares = {n : n * n for n in range(1,51)}
    print(squares)
