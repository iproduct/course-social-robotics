import cv2
import numpy as np
from numpy.random import laplace


def nothing(x):
    pass

def add_gaussian_noise(image, mean=0, std=5):
        noise = np.random.normal(mean, std, image.shape).astype(np.uint8)
        noisy_image = cv2.add(image, noise)
        return noisy_image


def add_salt_and_pepper_noise(image, noise_ratio=0.02):
    noisy_image = image.copy()
    h, w, c = noisy_image.shape
    noisy_pixels = int(h * w * noise_ratio)
    for _ in range(noisy_pixels):
        row, col = np.random.randint(0, h), np.random.randint(0, w)
        if np.random.rand() < 0.5:
            noisy_image[row, col] = [0, 0, 0]
        else:
            noisy_image[row, col] = [255, 255, 255]
    return noisy_image

if __name__ == "__main__":
    # read a color image
    # cap = cv2.VideoCapture('test.mp4')

    cv2.namedWindow("video")
    # cv2.createTrackbar("Min", "video", 40, 255, nothing)
    # cv2.createTrackbar("Max", "video", 255, 255, nothing)

    cap = cv2.VideoCapture(0)

    if cap.isOpened():
        while cv2.waitKey(30) != ord('q'):
            ret, frame = cap.read()
            if not ret:
                print('Video ends')
                break
            frame_gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            # identity_kernel = np.array([[0,0,0], [0,1,0], [0,0,0]])
            # identity_img = cv2.filter2D(src = frame_gray, ddepth=-1, kernel=identity_kernel)
            # gausian_blur = cv2.GaussianBlur(frame_gray, (11, 11), sigmaX = 16, sigmaY = 16)
            # noisy_frame = add_salt_and_pepper_noise(frame)
            # median_blur = cv2.medianBlur(noisy_frame, ksize=3)
            sharpen_kernel = np.array([
                [-1, -1, -1],
                [-1,  9, -1],
                [-1, -1, -1]
            ])
            emboss_kernel = np.array([
                [-2, -1, 0],
                [-1,  1, 1],
                [ 0,  1, 2]
            ])
            sobelx_kernel = np.array([
                [-1, -2, -1],
                [0,   0,  0],
                [1,   2,  1]
            ])
            sobely_kernel = np.array([
                [-1, 0, 1],
                [2,  0, 2],
                [-1,  0, 1]
            ])
            scharry_kernel = np.array([
                [-3, 0, 3],
                [-10,  0, 10],
                [-3,  0, 3]
            ])
            laplace_kernel = np.array([
                [0, 1, 0],
                [1, -4, 1],
                [0,  1, 0]
            ])
            sarpened_img = cv2.filter2D(src=frame_gray, ddepth=-1, kernel= sobely_kernel)
            vis = np.hstack((frame_gray, sarpened_img))
            cv2.imshow('video', vis)
    else:
        print('Video opening failed')

    cap.release()
    cv2.destroyAllWindows()