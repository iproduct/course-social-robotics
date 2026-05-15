import numpy as np
import matplotlib.pyplot as plt


def draw_vanderpol():
    mu = 0.5  # Nonlinear damping coefficient

    # 1. Define the system of ODEs
    def vdp_deriv(X):
        x, y = X
        dxdt = y
        dydt = mu * (1 - x ** 2) * y - x
        return np.array([dxdt, dydt])

    # 2. Create the vector field (the background arrows)
    x_range = np.linspace(-3, 3, 20)
    y_range = np.linspace(-4, 4, 20)
    X_grid, Y_grid = np.meshgrid(x_range, y_range)
    U = Y_grid
    V = mu * (1 - X_grid ** 2) * Y_grid - X_grid

    plt.figure(figsize=(10, 7))
    plt.streamplot(X_grid, Y_grid, U, V, color='gray')

    # 3. Simple Euler method to calculate trajectories
    def get_trajectory(x0, y0, dt=0.01, steps=3000):
        xs, ys = [x0], [y0]
        for _ in range(steps):
            deriv = vdp_deriv([xs[-1], ys[-1]])
            xs.append(xs[-1] + deriv[0] * dt)
            ys.append(ys[-1] + deriv[1] * dt)
        return xs, ys

    # Blue trajectory: Starting inside (unstable)
    ix, iy = get_trajectory(0.1, 0.1)
    plt.plot(ix, iy, 'b', label='Starting Inside (Spirals Out)')

    # Red trajectory: Starting outside (damped)
    ox, oy = get_trajectory(2.5, 3.5)
    plt.plot(ox, oy, 'r', label='Starting Outside (Spirals In)')

    # Black line: The stable Limit Cycle (the final path)
    plt.plot(ix[1500:], iy[1500:], 'black', linewidth=3, label='Stable Limit Cycle')

    plt.title(f'Van der Pol Oscillator Phase Portrait (mu = {mu})')
    plt.xlabel('Position (x)')
    plt.ylabel('Velocity (y)')
    plt.axhline(0, color='black', lw=1)
    plt.axvline(0, color='black', lw=1)
    plt.legend()
    plt.grid(True, linestyle=':', alpha=0.6)
    plt.show()


if __name__ == "__main__":
    draw_vanderpol()
