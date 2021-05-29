import cv2 as cv
import sys
import numpy as np
from scipy.spatial import distance

corners = []
CARD_WIDTH = 250
CARD_HEIGHT = 350

def is_closing(point):
    for corner in corners:
        if distance.euclidean(point, corner) < 2:
            return True
    return False

def drawPoint(point):
    cv.circle(img, point, 3, (0, 0, 255), -1)
    cv.putText(img, f'({point[0]}, {point[1]})', (point[0] - 100, point[1] + 30), cv.FONT_HERSHEY_SCRIPT_COMPLEX, 0.7, (0, 0, 255), 2, cv.LINE_AA )


def drawBoundary():
    startPoint = corners[-1]
    for endPoint in corners:
        cv.line(img, startPoint, endPoint, (0, 255, 0), 2, cv.LINE_AA)
        startPoint = endPoint

def showUnwrapped():
    points1 = np.float32(corners)
    points2 = np.float32([[0,0], [CARD_WIDTH, 0], [CARD_WIDTH, CARD_HEIGHT], [0, CARD_HEIGHT]])
    matrix = cv.getPerspectiveTransform(points1, points2)
    imgResult = cv.warpPerspective( original, matrix, (CARD_WIDTH, CARD_HEIGHT))
    cv.imshow('Unwarped Image', imgResult)

# mouse callback function
def draw_circle(event,x,y,flags,param):
    if event == cv.EVENT_LBUTTONDOWN:
        point = (x,y)
        if(is_closing(point)):
            drawBoundary()
            showUnwrapped()
        else:
            corners.append(point);
            drawPoint(point)


# img = np.zeros((512,512,3), np.uint8)

if __name__ == '__main__':
    print(f'OpenCV imported: {cv.version.opencv_version}')
    original = cv.imread("resources/cards.jpg")
    if original is None:
        sys.exit("Could not read the image.")
    print(original.shape)
    img = original.copy()
    cv.namedWindow('Image')
    cv.setMouseCallback('Image', draw_circle)
    # cropped = img[200:400, 100:250]

    events = [i for i in dir(cv) if 'EVENT' in i]
    print(events)
    while True:
        cv.imshow('Image', img)
        if cv.waitKey(20) & 0xFF == 27:
            break

    cv.destroyAllWindows()
