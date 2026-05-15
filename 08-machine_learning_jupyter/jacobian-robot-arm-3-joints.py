import numpy as np
import matplotlib.pyplot as plt


def plot_3link_arm():
    # Link lengths
    L = np.array([1.0, 0.8, 0.6])

    # Target position (x, y)
    target = np.array([1.2, 1.0])

    # Initial joint angles (radians)
    q = np.array([0.5, 0.5, 0.5])

    # Iterative solver (Inverse Kinematics)
    for _ in range(50):
        # 1. Forward Kinematics (calculate joint positions for plotting)
        x0, y0 = 0, 0
        x1, y1 = L[0] * np.cos(q[0]), L[0] * np.sin(q[0])
        x2, y2 = x1 + L[1] * np.cos(q[0] + q[1]), y1 + L[1] * np.sin(q[0] + q[1])
        x3, y3 = x2 + L[2] * np.cos(q[0] + q[1] + q[2]), y2 + L[2] * np.sin(q[0] + q[1] + q[2])

        current_pos = np.array([x3, y3])
        error = target - current_pos

        if np.linalg.norm(error) < 1e-3:
            break

        # 2. Build the 2x3 Jacobian
        s1, c1 = np.sin(q[0]), np.cos(q[0])
        s12, c12 = np.sin(q[0] + q[1]), np.cos(q[0] + q[1])
        s123, c123 = np.sin(q[0] + q[1] + q[2]), np.cos(q[0] + q[1] + q[2])

        J = np.array([
            [-L[0] * s1 - L[1] * s12 - L[2] * s123, -L[1] * s12 - L[2] * s123, -L[2] * s123],
            [L[0] * c1 + L[1] * c12 + L[2] * c123, L[1] * c12 + L[2] * c123, L[2] * c123]
        ])

        # 3. Use Pseudoinverse to update joints
        # This solves: J * dq = error
        dq = np.linalg.pinv(J) @ error
        q += dq

    # Final positions for plotting
    plt.figure(figsize=(8, 8))
    plt.plot([0, x1, x2, x3], [0, y1, y2, y3], 'o-', lw=4, color='royalblue', label='Robot Arm')
    plt.plot(target[0], target[1], 'rx', markersize=15, mew=3, label='Target')

    # Formatting
    plt.xlim(-0.5, 2.5)
    plt.ylim(-0.5, 2.5)
    plt.grid(True, linestyle=':')
    plt.title("3-Link Redundant Arm: Jacobian Inverse Kinematics")
    plt.legend()
    plt.gca().set_aspect('equal')
    plt.show()


plot_3link_arm()
