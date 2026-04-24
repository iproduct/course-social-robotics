import cv2

if __name__ == "__main__":
    # read a color image
    img1 = cv2.imread('lena.bmp')
    print(img1.shape)
    print(type(img1))
    print(img1.dtype)
    img2 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    # img2 = cv2.imread('lena.bmp', cv2.IMREAD_GRAYSCALE)

    cv2.imshow('img1', img1)
    cv2.imshow('img2', img2)
    cv2.waitKey(0)
    cv2.destroyAllWindows()