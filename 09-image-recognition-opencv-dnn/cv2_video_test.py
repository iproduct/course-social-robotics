import cv2


if __name__ == '__main__':
    cap = cv2.VideoCapture(0)

    n = 0
    text = ''

    while True:
        # Capture frame-by-framehttps://github.com/iproduct/course-angular/wiki/Important-Dates
        ret, frame = cap.read()

        # Our operations on the frame come here
        # gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # Display the resulting frame
        cv2.putText(frame, text, (50, 50),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.45, (0, 0, 255), 2)
        cv2.imshow('frame', frame)
        n = n + 1
        if(n %100 == 0):
            text = f"dataset/trayan/trayan{n // 100}.jpg"
            cv2.imwrite(text, frame)

        if cv2.waitKey(10) & 0xFF == 27:
            break

    # When everything done, release the capture
    cap.release()
    cv2.destroyAllWindows()