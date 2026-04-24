import pyrtools as pt
import matplotlib.pyplot as plt
import numpy as np
from PIL import Image

# 1. Setup and Decomposition
# Generate/Load a test image (e.g., a disk or chirp to see orientation clearly)
img = pt.synthetic_images.disk(size=256)
print(img.shape)
plt.imshow(img)
plt.show()
pyr = pt.pyramids.SteerablePyramidSpace(img, height=3, order=3)
# pyr = pt.pyramids.SteerablePyramidFreq(img, height=3, order=3)

# 2. Overview Visualization
# 'pyrshow' displays all subbands at their relative sizes in a single figure
print("Displaying full pyramid overview...")
pt.pyrshow(pyr.pyr_coeffs)
plt.show()


# 3. Detailed Level/Orientation Visualization using Matplotlib
def plot_pyramid_grid(pyr):
    # Determine grid size (Height x Number of Orientations)
    n_scales = pyr.height
    n_oris = pyr.num_orientations

    fig, axes = plt.subplots(n_scales, n_oris, figsize=(12, 8))
    fig.suptitle('Steerable Pyramid Subbands (Rows=Scale, Cols=Orientation)', fontsize=16)

    for s in range(n_scales):
        for o in range(n_oris):
            # Access coefficients: (scale, orientation)
            band = pyr.pyr_coeffs[(s, o)]
            ax = axes[s, o]
            ax.imshow(band, cmap='gray')
            ax.set_title(f"S:{s} O:{o}")
            ax.axis('off')

    plt.tight_layout()
    plt.show()


plot_pyramid_grid(pyr)

# 4. Filter Visualization (Frequency Domain)
# To see what the filters "look like", we use the Frequency implementation
pyr_freq = pt.pyramids.SteerablePyramidFreq(img, height=3, order=3)

plt.figure(figsize=(6, 6))
# Visualizing the high-pass residual filter response as an example
plt.imshow(np.abs(pyr_freq.pyr_coeffs['residual_highpass']), cmap='magma')
plt.title("High-Pass Residual Filter (Frequency Domain)")
plt.colorbar()
plt.show()
