import cv2

if __name__ == "__main__":
    # read a color image
    # cap = cv2.VideoCapture('test.mp4')
    cap = cv2.VideoCapture(0)
    if cap.isOpened():
        while(cv2.waitKey(30) != ord('q')):
            ret, frame = cap.read()
            if ret == False:
                print('Video ends')
                break
            cv2.imshow('video', frame)
    else:
        print('Video opening failed')

    cap.release()
    cv2.destroyAllWindows()