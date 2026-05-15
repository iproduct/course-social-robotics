facescrub
=========

Download dataset from http://vintage.winklerbros.net/facescrub.html

simply rum `python download.py`, all images are downloaded under `download`.

### Notice

Since I want to vertify the image and extract face region from the images, opencv-python package needed. If you are on Windows, this [url](http://www.lfd.uci.edu/~gohlke/pythonlibs/) can be very helpful to install packages.

# Running Face Recognition Demo - Main Steps 

1. Step 1:
```shell
extract_embeddings --dataset dataset --embeddings output/embeddings.pickle --detector face_detection_model --embedding-model openface_nn4.small2.v1.t7

```
2. Step 2:
```shell
train_model --embeddings output/embeddings.pickle --recognizer output/recognizer.pickle --le output/le.pickle

```
3. Step 3:
```shell
recognize_video --detector face_detection_model --embedding-model openface_nn4.small2.v1.t7 --recognizer output/recognizer.pickle --le output/le.pickle

```
