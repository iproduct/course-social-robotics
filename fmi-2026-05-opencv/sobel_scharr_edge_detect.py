import cv2

if __name__ == "__main__":
    # read a color image
    img1 = cv2.imread('lena.bmp')
    print(img1.shape)
    print(type(img1))
    print(img1.dtype)
    gray = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    # img2 = cv2.imread('lena.bmp', cv2.IMREAD_GRAYSCALE)
    cv2.imshow('grayscale', gray)
    # set the kernel size, depending on whether we are using the Sobel
    # filter or the Scharr operator, then compute the gradients along
    # the x and y axis, respectively
    for ksize in [-1, 3]:
        gX = cv2.Sobel(gray, ddepth=cv2.CV_32F, dx=1, dy=0, ksize=ksize)
        gY = cv2.Sobel(gray, ddepth=cv2.CV_32F, dx=0, dy=1, ksize=ksize)
        # the gradient magnitude images are now of the floating point data
        # type, so we need to take care to convert them back a to unsigned
        # 8-bit integer representation so other OpenCV functions can operate
        # on them and visualize them
        gX = cv2.convertScaleAbs(gX)
        gY = cv2.convertScaleAbs(gY)
        # combine the gradient representations into a single image
        combined = cv2.addWeighted(gX, 0.5, gY, 0.5, 0)
        # show our output images
        cv2.imshow("Sobel/Scharr X", gX)
        cv2.imshow("Sobel/Scharr Y", gY)
        cv2.imshow("Sobel/Scharr Combined", combined)
        cv2.waitKey(0)
    cv2.destroyAllWindows()