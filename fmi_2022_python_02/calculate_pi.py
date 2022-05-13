import random

from mpmath import mp

mp.dps = 1000

def calculate_pi(n):
    percent = n / 100
    acc = mp.mpf(0)
    for i in range(n):
        if i % percent == 0:
            print(chr(0x25A9), sep="", end="")
        acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    return acc

if __name__ == '__main__':
    print('\n', calculate_pi(100000))
    d = {'a':1, 'b':2}
    for k,v in d.items():
        print(k, '->', v)
    n = random.randint(1000, 10000)
    print(n)