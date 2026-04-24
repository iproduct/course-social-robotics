import cv2

if __name__ == "__main__":
    # load the image, display it to our screen, and initialize a list of
    # kernel sizes (so we can evaluate the relationship between kernel
    # size and amount of blurring)
    image = cv2.imread('lena.bmp')
    cv2.imshow("Original", image)
    kernelSizes = [(3, 3), (9, 9), (15, 15)]
    # loop over the kernel sizes
    for (kX, kY) in kernelSizes:
        # apply an "average" blur to the image using the current kernel
        # size
        blurred = cv2.GaussianBlur(image, (kX, kY), 0)
        cv2.imshow("Gaussian Blur ({}, {})".format(kX, kY), blurred)
        cv2.waitKey(0)

    cv2.waitKey(0)
    cv2.destroyAllWindows()