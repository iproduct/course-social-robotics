import numpy as np

# Function to maximize
def f(x):
    return x * np.sin(10 * np.pi * x) + 1

# GA parameters
population_size = 20
generations = 50
mutation_rate = 0.1
crossover_rate = 0.8

# Initialize population (real numbers in [0,1])
population = np.random.rand(population_size)

for gen in range(generations):
    # Evaluate fitness
    fitness = f(population)

    # Selection (roulette wheel)
    probs = fitness / fitness.sum()
    indices = np.random.choice(population_size, size=population_size, p=probs)
    population = population[indices]

    # Crossover
    for i in range(0, population_size, 2):
        if np.random.rand() < crossover_rate and i+1 < population_size:
            alpha = np.random.rand()
            parent1, parent2 = population[i], population[i+1]
            population[i] = alpha * parent1 + (1-alpha) * parent2
            population[i+1] = alpha * parent2 + (1-alpha) * parent1

    # Mutation
    for i in range(population_size):
        if np.random.rand() < mutation_rate:
            population[i] += np.random.normal(0, 0.05)  # small Gaussian mutation
            population[i] = np.clip(population[i], 0, 1)  # keep in [0,1]

# Best solution
best_idx = np.argmax(f(population))
best_x = population[best_idx]
best_f = f(best_x)
print(f"Best x: {best_x:.4f}, Best f(x): {best_f:.4f}")
