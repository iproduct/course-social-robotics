import sys

import cv2 as cv

if __name__ == '__main__':
    print(cv.__version__)
    image_path = "resources/lena.png"
    img = cv.imread(image_path)
    if img is None:
        sys.exit("Could not read the image:", image_path)
    cv.imshow("Lena", img)
    cv.waitKey(15000)
    cv.destroyAllWindows()
