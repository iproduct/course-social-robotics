import cv2 as cv
import sys
import numpy as np

WIDTH = 640
HEIGHT = 480

if __name__ == '__main__':
    print(f'OpenCV imported: {cv.version.opencv_version}')
    # img = cv.imread("resources/lena.png")
    # if img is None:
    #     sys.exit("Could not read the image.")
    # video = cv.VideoCapture("resources/mov_bbb.mp4")
    video = cv.VideoCapture(0)
    video.set(cv.CAP_PROP_FRAME_WIDTH, WIDTH)
    video.set(cv.CAP_PROP_FRAME_HEIGHT, HEIGHT)
    video.set(cv.CAP_PROP_BRIGHTNESS, 125)

    cv.namedWindow("Video")
    kernel = np.ones((3, 3), np.uint8)

    while True:
        success, img = video.read()
        if not success:
            sys.exit("Could not find video.")
        # img = cv.resize(img, (WIDTH, HEIGHT))
        # cv.resizeWindow("Video", WIDTH, HEIGHT)
        gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
        edges = cv.Canny(gray, 80, 200)
        dilated = cv.dilate(edges, kernel, iterations=1)
        eroded = cv.erode(dilated, kernel, iterations=1)
        cv.imshow("Video", img)
        cv.imshow("Video [grayscale]", gray)
        cv.imshow("Video [edges]", edges)
        cv.imshow("Video [dilated]", dilated)
        cv.imshow("Video [eroded]", eroded)
        if cv.waitKey(30) & 0xFF == ord('q'):
            break;

    video.release()
    cv.destroyAllWindows()