import cv2 as cv
import sys

if __name__ == '__main__':
    print(f'OpenCV imported: {cv.version.opencv_version}')
    img = cv.imread("resources/lena.png")
    if img is None:
        sys.exit("Could not read the image.")

    cv.imshow("Lena", img)
    print(img.shape)

    cv.waitKey(30000)
