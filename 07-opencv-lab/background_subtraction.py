from __future__ import print_function
import cv2 as cv
import argparse

if __name__ == '__main__':
    # parser = argparse.ArgumentParser(description='This program shows how to use background subtraction methods provided by \
    #                                               OpenCV. You can process both videos and images.')
    # parser.add_argument('--input', type=str, help='Path to a video or a sequence of image.', default='vtest.avi')
    # parser.add_argument('--algo', type=str, help='Background subtraction method (KNN, MOG2).', default='MOG2')
    # args = parser.parse_args()
    # if args.algo == 'MOG2':
    #     backSub = cv.createBackgroundSubtractorMOG2()
    # else:
    #     backSub = cv.createBackgroundSubtractorKNN()
    backSub = cv.createBackgroundSubtractorMOG2()
    # backSub = cv.createBackgroundSubtractorKNN()
    capture = cv.VideoCapture(0)
    if not capture.isOpened:
        print('Unable to open: ' + 'cam_0')
        exit(0)
    while True:
        ret, frame = capture.read()
        if frame is None:
            break

        fgMask = backSub.apply(frame)

        cv.rectangle(frame, (10, 2), (100, 20), (255, 255, 255), -1)
        cv.putText(frame, str(capture.get(cv.CAP_PROP_POS_FRAMES)), (15, 15),
                   cv.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0))

        cv.imshow('Frame', frame)
        cv.imshow('FG Mask', fgMask)

        keyboard = cv.waitKey(30)
        if keyboard == 'q' or keyboard == 27:
            break
    capture.release()
    cv.destroyAllWindows()