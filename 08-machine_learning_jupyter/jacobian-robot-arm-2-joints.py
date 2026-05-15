import numpy as np
import matplotlib.pyplot as plt


def robot_arm_jacobian_sim():
    L1, L2 = 1.0, 1.0  # Link lengths

    # Target path: a straight vertical line
    target_y = np.linspace(1.0, 1.5, 20)
    target_x = np.ones_like(target_y) * 0.5

    # Initial joint angles
    q = np.array([np.pi / 4, np.pi / 4])
    history_pos = []

    for i in range(len(target_x)):
        # 1. Forward Kinematics: Find current hand position (x, y)
        x = L1 * np.cos(q[0]) + L2 * np.cos(q[0] + q[1])
        y = L1 * np.sin(q[0]) + L2 * np.sin(q[0] + q[1])
        history_pos.append([x, y])

        # 2. Build the Jacobian Matrix
        J = np.array([
            [-L1 * np.sin(q[0]) - L2 * np.sin(q[0] + q[1]), -L2 * np.sin(q[0] + q[1])],
            [L1 * np.cos(q[0]) + L2 * np.cos(q[0] + q[1]), L2 * np.cos(q[0] + q[1])]
        ])

        # 3. Calculate movement needed to hit the target
        error = np.array([target_x[i] - x, target_y[i] - y])

        # 4. Use Inverse Jacobian to update motor angles
        try:
            dq = np.linalg.solve(J, error)
            q = q + dq
        except np.linalg.LinAlgError:
            print("Singularity hit!")
            break

    # Visualization
    h_pos = np.array(history_pos)
    plt.plot(target_x, target_y, 'k--', label='Target Path')
    plt.scatter(h_pos[:, 0], h_pos[:, 1], c='green', label='Actual Hand Path')
    plt.title('Jacobian-Based Path Following')
    plt.legend()
    plt.show()


robot_arm_jacobian_sim()
