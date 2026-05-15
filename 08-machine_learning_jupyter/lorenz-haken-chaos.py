import numpy as np
import matplotlib.pyplot as plt
from scipy.integrate import solve_ivp

# 1. Define the Lorenz System (Laser Analogy)
def laser_chaos(t, state, sigma, rho, beta):
    # x: Electric field amplitude
    # y: Atomic polarization
    # z: Population inversion
    x, y, z = state
    dxdt = sigma * (y - x)
    dydt = x * (rho - z) - y
    dzdt = x * y - beta * z
    return [dxdt, dydt, dzdt]

# 2. Parameters that trigger chaos
# sigma: cavity loss, rho: pump strength, beta: atomic decay rate
params = (10.0, 28.0, 8/3)
initial_state = [1.0, 1.0, 1.0001]
t_span = (0, 50)
t_eval = np.linspace(t_span[0], t_span[1], 10000)

# 3. Solve the differential equations
sol = solve_ivp(laser_chaos, t_span, initial_state, args=params, t_eval=t_eval)

# 4. Plot the "Strange Attractor"
fig = plt.figure(figsize=(12, 8))
ax = fig.add_subplot(111, projection='3d')
ax.plot(sol.y[0], sol.y[1], sol.y[2], lw=0.7, color='crimson')

ax.set_title("Laser Chaos: The Lorenz Strange Attractor")
ax.set_xlabel("Electric Field (x)")
ax.set_ylabel("Polarization (y)")
ax.set_zlabel("Population Inversion (z)")
plt.show()
