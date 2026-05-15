import numpy as np
import random
import matplotlib.pyplot as plt

# Example: 5 cities with (x, y) coordinates
cities = np.array([
    [0, 0],
    [1, 5],
    [5, 2],
    [6, 6],
    [8, 3]
])
num_cities = len(cities)

# GA parameters
population_size = 10
generations = 50
mutation_rate = 0.2


# Calculate total distance of a tour
def tour_distance(tour):
    distance = 0
    for i in range(num_cities):
        city_a = cities[tour[i % num_cities]]
        city_b = cities[tour[(i + 1) % num_cities]]
        distance += np.linalg.norm(city_a - city_b)
    return distance


# Create initial population (random permutations)
def create_population():
    population = []
    for _ in range(population_size):
        tour = np.arange(num_cities)
        np.random.shuffle(tour)
        population.append(tour)
    return population


# Selection (tournament selection)
def select(population):
    tournament_size = 3
    selected = []
    for _ in range(population_size):
        participants = random.sample(population, tournament_size)
        best = min(participants, key=tour_distance)
        selected.append(best)
    return selected


# Ordered Crossover (OX)
def crossover(parent1, parent2):
    start, end = sorted(random.sample(range(num_cities), 2))
    child = [-1] * num_cities
    child[start:end + 1] = parent1[start:end + 1]

    ptr = 0
    for city in parent2:
        if city not in child:
            while child[ptr] != -1:
                ptr += 1
            child[ptr] = city
    return np.array(child)


# Swap mutation
def mutate(tour):
    if random.random() < mutation_rate:
        i, j = random.sample(range(num_cities), 2)
        tour[i], tour[j] = tour[j], tour[i]
    return tour


# GA main loop
population = create_population()

for gen in range(generations):
    population = select(population)

    # Crossover
    next_generation = []
    for i in range(0, population_size, 2):
        parent1, parent2 = population[i], population[i + 1]
        child1 = crossover(parent1, parent2)
        child2 = crossover(parent2, parent1)
        next_generation.extend([child1, child2])

    # Mutation
    population = [mutate(tour) for tour in next_generation]

# Best solution
best_tour = min(population, key=tour_distance)
best_distance = tour_distance(best_tour)
print("Best tour:", best_tour)
print("Best distance:", best_distance)

# --- Plot cities and best tour ---
plt.figure(figsize=(8, 6))
plt.scatter(cities[:, 0], cities[:, 1], color='red', s=100, label='Cities')

# Draw the tour
tour_cities = cities[np.append(best_tour, best_tour[0])]  # include return to start
plt.plot(tour_cities[:, 0], tour_cities[:, 1], '-o', color='blue', label='Best tour')

for i, (x, y) in enumerate(cities):
    plt.text(x + 0.1, y + 0.1, f'{i}', fontsize=12)

plt.title(f'TSP GA Best Tour - Distance: {best_distance:.2f}')
plt.xlabel('X')
plt.ylabel('Y')
plt.legend()
plt.grid(True)
plt.show()
