import cv2
import numpy as np

def nothing(x):
    pass

if __name__ == "__main__":
    # read a color image
    # cap = cv2.VideoCapture('test.mp4')

    cv2.namedWindow("video")
    cv2.createTrackbar("Min", "video", 40, 255, nothing)
    cv2.createTrackbar("Max", "video", 255, 255, nothing)

    cap = cv2.VideoCapture(0)

    if cap.isOpened():
        while cv2.waitKey(30) != ord('q'):
            ret, frame = cap.read()
            if not ret:
                print('Video ends')
                break
            frame_gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            # get min and max thresholds from trackbars
            minThr = cv2.getTrackbarPos("Min", "video")
            maxThr = cv2.getTrackbarPos("Max", "video")

            # Canny method for edges detection
            # argument: image_array, minVal, maxVal
            # above maxVal: sure-edges, below minVal : non-edges
            # between maxVal and minVal : depend on the connectivity to sure-edges
            edges = cv2.Canny(frame, minThr, maxThr)
            # print(edges.shape)

            # dilation with 7x7 kernel - makes contours wider
            edges = cv2.dilate(edges, (7, 7) )

            ## show frame and edges side-by-side
            # vis = np.concatenate((frame_gray, edges), axis=1)
            # vis = np.hstack((frame_gray, edges))

            ## blend frame and edges instead
            # alpha = 0.5
            # beta = (1.0 - alpha);
            # edges_bgr = cv2.cvtColor(edges, cv2.COLOR_GRAY2BGR)
            # vis = cv2.addWeighted(frame, alpha, edges_bgr, beta, 0.0);

            # draw dilated edges over the frame in green (0, 255, 0)
            frame[edges==255] = (0, 255, 0)
            # print(frame.shape)

            cv2.imshow('video', frame)
    else:
        print('Video opening failed')

    cap.release()
    cv2.destroyAllWindows()