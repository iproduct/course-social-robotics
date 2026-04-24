import cv2

if __name__ == "__main__":
    # load the image, display it to our screen, and construct a list of
    # bilateral filtering parameters that we are going to explore
    image = cv2.imread('lena.bmp')
    cv2.imshow("Original", image)
    params = [(11, 21, 7), (11, 41, 21), (11, 61, 39)]
    # loop over the diameter, sigma color, and sigma space
    for (diameter, sigmaColor, sigmaSpace) in params:
        # apply bilateral filtering to the image using the current set of
        # parameters
        blurred = cv2.bilateralFilter(image, diameter, sigmaColor, sigmaSpace)
        # show the output image and associated parameters
        title = "Blurred d={}, sc={}, ss={}".format(
            diameter, sigmaColor, sigmaSpace)
        cv2.imshow(title, blurred)
        cv2.waitKey(0)

    cv2.waitKey(0)
    cv2.destroyAllWindows()