import numpy as np
nums = np.array([0, 1, 3, 5, 7, 9, 11, 13, 15])
print("Original vector:")
print(nums)
bin_nums = ((nums.reshape(-1,1) & (2**np.arange(8))) != 0).astype(int)
print("\nBinary representation of the said vector:")
print(bin_nums[:,::-1])