import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

# 1. Configuration
L = np.array([1.0, 0.8, 0.6])
target = np.array([1.2, 1.0])
q = np.array([0.1, 0.1, 0.1])
history_q = []

# 2. Pre-calculate path using the Jacobian
for _ in range(200):
    s1, c1 = np.sin(q[0]), np.cos(q[0])
    s12, c12 = np.sin(q[0] + q[1]), np.cos(q[0] + q[1])
    s123, c123 = np.sin(q[0] + q[1] + q[2]), np.cos(q[0] + q[1] + q[2])

    curr_pos = np.array([
        L[0] * c1 + L[1] * c12 + L[2] * c123,
        L[0] * s1 + L[1] * s12 + L[2] * s123
    ])

    error = target - curr_pos
    if np.linalg.norm(error) < 0.005: break

    J = np.array([
        [-L[0] * s1 - L[1] * s12 - L[2] * s123, -L[1] * s12 - L[2] * s123, -L[2] * s123],
        [L[0] * c1 + L[1] * c12 + L[2] * c123, L[1] * c12 + L[2] * c123, L[2] * c123]
    ])

    q += np.linalg.pinv(J) @ (error * 0.05)
    history_q.append(q.copy())

# 3. Setup Figure
fig, ax = plt.subplots(figsize=(6, 6))
line, = ax.plot([], [], 'o-', lw=5, color='royalblue', markersize=8)
ax.plot(target[0], target[1], 'rx', markersize=12, label='Target')
ax.set_xlim(-0.5, 2.5);
ax.set_ylim(-0.5, 2.5)
ax.set_aspect('equal');
ax.grid(True, alpha=0.3)


def update(frame):
    angles = history_q[frame]
    # Forward Kinematics for drawing
    x = [0,
         L[0] * np.cos(angles[0]),
         L[0] * np.cos(angles[0]) + L[1] * np.cos(angles[0] + angles[1]),
         L[0] * np.cos(angles[0]) + L[1] * np.cos(angles[0] + angles[1]) + L[2] * np.cos(
             angles[0] + angles[1] + angles[2])]
    y = [0,
         L[0] * np.sin(angles[0]),
         L[0] * np.sin(angles[0]) + L[1] * np.sin(angles[0] + angles[1]),
         L[0] * np.sin(angles[0]) + L[1] * np.sin(angles[0] + angles[1]) + L[2] * np.sin(
             angles[0] + angles[1] + angles[2])]
    line.set_data(x, y)
    return line,


# 4. Create and run animation
# We keep 'ani' as a global variable to prevent garbage collection
ani = FuncAnimation(fig, update, frames=len(history_q), interval=30, blit=True, repeat=True)

plt.legend()
plt.show()
