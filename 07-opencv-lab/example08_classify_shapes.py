import cv2
import numpy as np
import example06_img_stacking as stacking

def empty(arg):
    pass

def classify_figure(contour, w, h):
    num_vertices = len(contour)
    ratio = w / float(h)
    if num_vertices < 3:
        return "unrecognized"
    elif num_vertices == 3:
        return "Triangle"
    elif num_vertices == 4:
        if ratio > 0.95 and ratio < 1.05:
            return "Square"
        else:
            return "Rectangle"
    elif num_vertices > 4:
        if ratio > 0.95 and ratio < 1.05:
            return "Circle"
        else:
            return "Ellipse"

def find_contours(img):
    contours, hierachy = cv2.findContours(img, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
    for contour in contours:
        area = cv2.contourArea(contour)
        if area > 50:
            print("area = ", area)
            cv2.drawContours(img_copy, contour, -1, (0,0,255), 2)
            perimeter = cv2.arcLength(contour, True)
            print("perimeter = ", perimeter)
            approx = cv2.approxPolyDP(contour, 0.02*perimeter, True)
            print("Vertices:", len(approx))
            x, y, w, h = cv2.boundingRect(approx)
            cv2.rectangle(img_copy, (x, y), (x + w, y + h), (0,255,0), 2)
            figure = classify_figure(approx, w, h)
            cv2.putText(img_copy, f'{figure}', (x, y + h//2), cv2.QT_FONT_NORMAL, 1, (0, 0, 255), 2, cv2.LINE_AA )

if __name__ == '__main__':
    cv2.namedWindow("Trackbars")
    cv2.resizeWindow("Trackbars", 480, 100)
    cv2.createTrackbar("Treshold 1", "Trackbars", 50, 255, empty)
    cv2.createTrackbar("Treshold 2", "Trackbars", 50, 255, empty)
    img = cv2.imread("resources/geometric-shapes.png")
    blurred = cv2.GaussianBlur(img,(7,7), 1)
    blank = np.zeros_like(img)
    while True:
        t1 = cv2.getTrackbarPos("Treshold 1", "Trackbars")
        t2 = cv2.getTrackbarPos("Treshold 2", "Trackbars")

        canny = cv2.Canny(blurred, t1, t2)
        img_copy = img.copy();
        find_contours(canny);

        img_stack = stacking.stack_images(((img, blurred, canny),
                                           (img_copy, blank, blank)), 0.7)
        cv2.imshow("Shapes",  img_stack)

        if cv2.waitKey(10) & 0xFF == 27:
            break

    cv2.destroyAllWindows()