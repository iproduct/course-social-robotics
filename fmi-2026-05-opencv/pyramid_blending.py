import cv2
import matplotlib.pyplot as plt
import numpy as np

def pyramid_blend(img1, img2, levels=6):
    # 1. Generate Gaussian Pyramids
    gp1 = [img1.astype(np.float32)]
    gp2 = [img2.astype(np.float32)]
    for i in range(levels):
        gp1.append(cv2.pyrDown(gp1[-1]))
        gp2.append(cv2.pyrDown(gp2[-1]))
    # temp = gp1[5].copy()
    # temp.resize(256, 256)
    # cv2.imshow('gp5 ', temp)


    # 2. Generate Laplacian Pyramids
    lp1 = [gp1[levels]]
    lp2 = [gp2[levels]]
    for i in range(levels, 0, -1):
        # Expand lower level and subtract from higher level
        size = (gp1[i-1].shape[1], gp1[i-1].shape[0])
        # print(size)
        ge1 = cv2.pyrUp(gp1[i], dstsize=size)
        ge2 = cv2.pyrUp(gp2[i], dstsize=size)
        lp1.append(cv2.subtract(gp1[i-1], ge1))
        lp2.append(cv2.subtract(gp2[i-1], ge2))

    # 3. Blend the Laplacian Pyramids (Left half of img1, Right half of img2)
    blended_pyr = []
    for l1, l2 in zip(lp1, lp2):
        cols = l1.shape[1]
        # Horizontal split at the middle
        merged = np.hstack((l1[:, 0:cols//2], l2[:, cols//2:]))
        blended_pyr.append(merged)

    # 4. Reconstruct the final image
    reconstructed = blended_pyr[0]
    for i in range(1, levels + 1):
        size = (blended_pyr[i].shape[1], blended_pyr[i].shape[0])
        print(size,  blended_pyr[i].shape)
        # cv2.imshow('level '+ str(i), reconstructed)
        reconstructed = cv2.pyrUp(reconstructed, dstsize=size)
        reconstructed = cv2.add(reconstructed, blended_pyr[i])

    return np.clip(reconstructed, 0, 255).astype(np.uint8)

# Usage
A = cv2.imread('data/image_left.jpg')
B = cv2.imread('data/image_right.jpg')

# Ensure both images are the same size
A = cv2.resize(A, (512, 512))
B = cv2.resize(B, (512, 512))

result = pyramid_blend(A, B)
cv2.imshow('Pyramid Blended Result', result)
cv2.waitKey(0)
