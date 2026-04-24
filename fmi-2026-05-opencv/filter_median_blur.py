import cv2

if __name__ == "__main__":
    # load the image, display it to our screen, and initialize a list of
    # kernel sizes (so we can evaluate the relationship between kernel
    # size and amount of blurring)
    image = cv2.imread('lena.bmp')
    cv2.imshow("Original", image)
    kernelSizes = [3,9,15]
    # loop over the kernel sizes
    for k in kernelSizes:
        # apply an "average" blur to the image using the current kernel
        # size
        blurred = blurred = cv2.medianBlur(image, k)
        cv2.imshow("Median Blur ({})".format(k), blurred)
        cv2.waitKey(0)

    cv2.waitKey(0)
    cv2.destroyAllWindows()