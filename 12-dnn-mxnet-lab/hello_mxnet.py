
from ctypes import *

mydll = cdll.LoadLibrary("D:/CourseDML/cuda/bin/cudnn64_8.dll")
mydll = cdll.LoadLibrary("D:/Program Files/CUDA/v11.2/development/bin/cublas64_11.dll")
mydll = cdll.LoadLibrary("D:/Program Files/CUDA/v11.2/development/bin/cufft64_10.dll")
mydll = cdll.LoadLibrary("D:/Program Files/CUDA/v11.2/development/bin/cusolver64_11.dll")
mydll = cdll.LoadLibrary("D:/Program Files/CUDA/v11.2/development/bin/curand64_10.dll")
mydll = cdll.LoadLibrary("D:/Program Files/CUDA/v11.2/development/bin/nvrtc64_112_0.dll")
#
# mydll = cdll.LoadLibrary("D:\Programs\Python37\Lib\site-packages\mxnet\libmxnet.dll")
import mxnet as mx

if __name__ == '__main__':
    a = mx.nd.ones((2, 3))
    b = a * 2 + 1
    print(b.asnumpy())
    pass
