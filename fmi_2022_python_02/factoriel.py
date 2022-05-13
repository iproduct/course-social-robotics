"""factorial done recursively and iteratively"""

def fact_iter(n):
    ans = 1
    for i in range(2, n + 1):
        ans *= i
    return ans

def fact_rec(n):
    if n < 1:
        return 1
    else:
        return n * fact_rec(n - 1)

if __name__ == '__main__':
    print(fact_iter(900))
    print(fact_rec(900))

