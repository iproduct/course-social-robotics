from __future__ import print_function
import cv2 as cv
import numpy as np

image_file="resources/harris_result.jpg"
source_window = 'Source image'
corners_window = 'Corners detected'
max_thresh = 255

def cornerHarris_demo(val):
    thresh = val
    # Detector parameters
    blockSize = 2
    apertureSize = 3
    k = 0.04
    # Detecting corners
    dst = cv.cornerHarris(src_gray, blockSize, apertureSize, k)
    # Normalizing
    dst_norm = np.empty(dst.shape, dtype=np.float32)
    cv.normalize(dst, dst_norm, alpha=0, beta=255, norm_type=cv.NORM_MINMAX)
    dst_norm_scaled = cv.convertScaleAbs(dst_norm)

    dst_norm_scaled_bgr = cv.cvtColor(dst_norm_scaled, cv.COLOR_GRAY2BGR)

    # Drawing a circle around corners
    for i in range(dst_norm.shape[0]):
        for j in range(dst_norm.shape[1]):
            if int(dst_norm[i,j]) > thresh:
                cv.circle(dst_norm_scaled_bgr, (j,i), 5, (0,0,255), 2)
    # Showing the result
    cv.namedWindow(corners_window)
    cv.imshow(corners_window, dst_norm_scaled_bgr)

if __name__ == '__main__':
    src = cv.imread(cv.samples.findFile(image_file))
    src_gray = cv.cvtColor(src, cv.COLOR_BGR2GRAY)

    # Create a window and a trackbar
    cv.namedWindow(source_window)
    thresh = 200 # initial threshold
    cv.createTrackbar('Threshold: ', source_window, thresh, max_thresh, cornerHarris_demo)
    cv.imshow(source_window, src)
    cornerHarris_demo(thresh)
    cv.waitKey(60000)
    cv.destroyAllWindows()