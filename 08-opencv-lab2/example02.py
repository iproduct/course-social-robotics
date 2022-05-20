import sys

import cv2 as cv

WIDTH = 640
HEIGHT = 480

if __name__ == '__main__':
    # open a video file
    video_path = "resources/mov_bbb.mp4"
    cap = cv.VideoCapture(video_path)
    if cap.isOpened():
        print(f"Video '{video_path}' is opened.")
        while (cv.waitKey(20) & 0xFF != ord('q') and cv.waitKey(30) & 0xFF != 27):
            success, img = cap.read()
            if not success:
                print("Video ended:", video_path)
                break
            img = cv.resize(img, (WIDTH, HEIGHT))
            cv.imshow("Big Bugs Bunny Video", img)
    else:
        print("Could not open video:", video_path)

    cv.destroyAllWindows()
