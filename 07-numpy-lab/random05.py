import numpy as np
from numpy import random
import numpy as np

if __name__ == '__main__':
    rand1 = random.choice([3, 5, 7, 11, 13], p=[0.1, 0.2, 0.2, 0.45, 0.05], size=(5, 5))
    print("\nRandom primes:\n", rand1)

    arr = np.arange(1,10)
    random.shuffle(arr)
    print("\nRandom shuffle:\n", arr)
    print("\nRandom permutation:\n", random.permutation(np.arange(1,10)))
