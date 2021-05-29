import cv2
import numpy as np
import example06_img_stacking as stacking

def empty(arg):
    pass

if __name__ == '__main__':
    img = cv2.imread("resources/vintage-car.jpg")
    img_hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

    cv2.namedWindow("Trackbars")
    cv2.resizeWindow("Trackbars", 640, 320)
    cv2.createTrackbar("Hue Min", "Trackbars", 93, 179, empty)
    cv2.createTrackbar("Hue Max", "Trackbars", 179, 179, empty)
    cv2.createTrackbar("Sat Min", "Trackbars", 111, 255, empty)
    cv2.createTrackbar("Sat Max", "Trackbars", 255, 255, empty)
    cv2.createTrackbar("Val Min", "Trackbars", 92, 255, empty)
    cv2.createTrackbar("Val Max", "Trackbars", 255, 255, empty)

    while True:
        hmin = cv2.getTrackbarPos("Hue Min", "Trackbars")
        hmax = cv2.getTrackbarPos("Hue Max", "Trackbars")
        smin = cv2.getTrackbarPos("Sat Min", "Trackbars")
        smax = cv2.getTrackbarPos("Sat Max", "Trackbars")
        vmin = cv2.getTrackbarPos("Val Min", "Trackbars")
        vmax = cv2.getTrackbarPos("Val Max", "Trackbars")
        lower = np.array([hmin, smin, vmin])
        upper = np.array([hmax, smax, vmax])
        print(lower, upper)
        mask = cv2.inRange(img_hsv, lower, upper)
        img_mask = cv2.cvtColor(mask, cv2.COLOR_GRAY2BGR)
        img_result = cv2.bitwise_and(img, img, mask = mask)

        img_stack = stacking.stack_images_line((img, img_hsv, img_mask, img_result), 0.2)
        cv2.imshow("Red Car Images", img_stack)
        if cv2.waitKey(10) & 0xFF == 27:
            break

    cv2.destroyAllWindows()