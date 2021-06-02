# USAGE
# python recognize.py --detector face_detection_model \
#	--embedding-model openface_nn4.small2.v1.t7 \
#	--recognizer output/recognizer.pickle \
#	--le output/le.pickle

# import the necessary packages
from picamera import PiCamera
from PIL import Image
import numpy as np
import argparse
import imutils
import pickle
import cv2
import os
import io
import time

# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-d", "--detector", required=True,
	help="path to OpenCV's deep learning face detector")
ap.add_argument("-m", "--embedding-model", required=True,
	help="path to OpenCV's deep learning face embedding model")
ap.add_argument("-r", "--recognizer", required=True,
	help="path to model trained to recognize faces")
ap.add_argument("-l", "--le", required=True,
	help="path to label encoder")
ap.add_argument("-c", "--confidence", type=float, default=0.5,
	help="minimum probability to filter weak detections")
args = vars(ap.parse_args())

camera = PiCamera()
#MAX still pic resolution 2592x1944
# 1280, 720
camera.resolution = (640, 360)
#camera.framerate = 30
# Wait for the automatic gain control to settle
#time.sleep(2)
# Now fix the values
#camera.shutter_speed = camera.exposure_speed
#camera.exposure_mode = 'off'
#g = camera.awb_gains
#camera.awb_mode = 'on'
#camera.awb_gains = g
#Finally, take several photos with the fixed settings
#camera.capture_sequence(['image%02d.jpg' % i for i in range(10)])

# load our serialized face detector from disk
print("[INFO] loading face detector...")
protoPath = os.path.sep.join([args["detector"], "deploy.prototxt"])
modelPath = os.path.sep.join([args["detector"],
	"res10_300x300_ssd_iter_140000.caffemodel"])
detector = cv2.dnn.readNetFromCaffe(protoPath, modelPath)

# load our serialized face embedding model from disk
print("[INFO] loading face recognizer...")
embedder = cv2.dnn.readNetFromTorch(args["embedding_model"])

# load the actual face recognition model along with the label encoder
recognizer = pickle.loads(open(args["recognizer"], "rb").read())
le = pickle.loads(open(args["le"], "rb").read())

# load the image, resize it to have a width of 600 pixels (while
# maintaining the aspect ratio), and then grab the image dimensions

#image = cv2.imread(args["image"])

while True:
        # Create an in-memory stream
        stream = io.BytesIO()
        camera.capture(stream, format='jpeg')
        #stream.seek(0)
        #image = Image.open(stream)

        # Construct a numpy array from the stream
        data = np.fromstring(stream.getvalue(), dtype=np.uint8)
        # "Decode" the image from the array, preserving colour
        image = cv2.imdecode(data, 1)
        # OpenCV returns an array with data in BGR order. If you want RGB instead
        # use the following...
        #image = image[:, :, ::-1]

        image = imutils.resize(image, width=600)
        (h, w) = image.shape[:2]

        # construct a blob from the image
        imageBlob = cv2.dnn.blobFromImage(
                cv2.resize(image, (300, 300)), 1.0, (300, 300),
                (104.0, 177.0, 123.0), swapRB=False, crop=False)

        # apply OpenCV's deep learning-based face detector to localize
        # faces in the input image
        detector.setInput(imageBlob)
        detections = detector.forward()
        

        # loop over the detections
        for i in range(0, detections.shape[2]):
                # extract the confidence (i.e., probability) associated with the
                # prediction
                confidence = detections[0, 0, i, 2]

                # filter out weak detections
                if confidence > args["confidence"]:
                        # compute the (x, y)-coordinates of the bounding box for the
                        # face
                        box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
                        (startX, startY, endX, endY) = box.astype("int")

                        # extract the face ROI
                        face = image[startY:endY, startX:endX]
                        (fH, fW) = face.shape[:2]

                        # ensure the face width and height are sufficiently large
                        if fW < 20 or fH < 20:
                                continue

                        # construct a blob for the face ROI, then pass the blob
                        # through our face embedding model to obtain the 128-d
                        # quantification of the face
                        faceBlob = cv2.dnn.blobFromImage(face, 1.0 / 255, (96, 96),
                                (0, 0, 0), swapRB=True, crop=False)
                        embedder.setInput(faceBlob)
                        vec = embedder.forward()

                        # perform classification to recognize the face
                        preds = recognizer.predict_proba(vec)[0]
                        j = np.argmax(preds)
                        proba = preds[j]
                        name = le.classes_[j]

                        # draw the bounding box of the face along with the associated
                        # probability
                        text = "{}: {:.2f}%".format(name, proba * 100)
                        y = startY - 10 if startY - 10 > 10 else startY + 10
                        cv2.rectangle(image, (startX, startY), (endX, endY),
                                (0, 0, 255), 2)
                        cv2.putText(image, text, (startX, y),
                                cv2.FONT_HERSHEY_SIMPLEX, 0.45, (0, 0, 255), 2)


        cv2.imshow("Image", image)
        key = cv2.waitKey(250) & 0xFF
        # if the `q` key was pressed, break from the loop
        if key == ord("q"):
                break

# show the output image
cv2.imshow("Image", image)
cv2.waitKey(1000)
cv2.destroyAllWindows()
cv2.waitKey(1)
cv2.destroyAllWindows()
