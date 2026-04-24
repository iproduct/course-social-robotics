import numpy as np
import open3d as o3d
from scipy.spatial import KDTree


def moving_least_squares_smoothing(pcd, radius=0.05):
    """
    Applies MLS smoothing by projecting each point onto a locally fitted plane.
    """
    points = np.asarray(pcd.points)
    tree = KDTree(points)
    smoothed_points = np.copy(points)

    for i, point in enumerate(points):
        # 1. Find local neighbors within radius
        indices = tree.query_ball_point(point, radius)
        if len(indices) < 3:
            continue

        neighbors = points[indices]

        # 2. Compute weights (Gaussian weight based on distance)
        distances = np.linalg.norm(neighbors - point, axis=1)
        weights = np.exp(-(distances ** 2) / (radius ** 2))

        # 3. Weighted Least Squares Plane Fitting
        # Shift neighbors to local origin
        centroid = np.average(neighbors, axis=0, weights=weights)
        shifted_neighbors = neighbors - centroid

        # Weighted Covariance Matrix
        W = np.diag(weights)
        cov = shifted_neighbors.T @ W @ shifted_neighbors

        # The normal is the eigenvector with the smallest eigenvalue
        eigenvalues, eigenvectors = np.linalg.eigh(cov)
        normal = eigenvectors[:, 0]

        # 4. Project the original point onto the local plane
        # Projection formula: p_proj = p - dot(p - centroid, normal) * normal
        dist_to_plane = np.dot(point - centroid, normal)
        smoothed_points[i] = point - dist_to_plane * normal

    # Create new Open3D point cloud
    smoothed_pcd = o3d.geometry.PointCloud()
    smoothed_pcd.points = o3d.utility.Vector3dVector(smoothed_points)
    return smoothed_pcd


# --- Usage Example ---
# 1. Create a noisy sphere
sphere = o3d.geometry.TriangleMesh.create_sphere(radius=1.0).sample_points_uniformly(2000)
noise = np.random.normal(0, 0.02, (2000, 3))
sphere.points = o3d.utility.Vector3dVector(np.asarray(sphere.points) + noise)

# 2. Apply MLS
smoothed_sphere = moving_least_squares_smoothing(sphere, radius=0.1)

# 3. Visualize
sphere.paint_uniform_color([1, 0.7, 0.7])  # Original (Red-ish)
smoothed_sphere.paint_uniform_color([0.7, 1, 0.7])  # Smoothed (Green-ish)
o3d.visualization.draw_geometries([sphere, smoothed_sphere])
