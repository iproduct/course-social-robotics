import pyrtools as pt
import numpy as np
from PIL import Image
import matplotlib.pyplot as plt

# 1. Load a grayscale image
# Replace 'your_image.jpg' with a path to a real image
img = np.array(Image.open(r'data\astronaut_s.jpg').convert('L'))

# 2. Decompose the image using a Steerable Pyramid
# height='auto' automatically determines max depth
# order=3 creates 4 orientation bands (order+1)
pyr = pt.pyramids.SteerablePyramidSpace(img, height='auto', order=3)

# 3. Access the decomposed subbands
# The 'pyr.pyr_coeffs' dictionary contains the subbands
print(f"Number of levels: {pyr.num_scales}")
for key in pyr.pyr_coeffs.keys():
    print(f"Subband {key} shape: {pyr.pyr_coeffs[key].shape}")

# 4. Recover (Reconstruct) the image from the pyramid
recon_img = pyr.recon_pyr()

# 5. Verify the recovery accuracy
# A perfect reconstruction should have a very low mean squared error
error = np.mean((img - recon_img)**2)
print(f"Reconstruction Mean Squared Error: {error:.2e}")

# 6. Optional: Visualize a specific oriented subband
# Visualizes the first scale, second orientation (0-indexed)
plt.title("Scale 0, Orientation 1")
plt.imshow(pyr.pyr_coeffs[(4, 2)])
plt.show()
