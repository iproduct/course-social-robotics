from typing import Sequence
from math import dist

Point = tuple[int, int];

def is_trinagle(points: Sequence[Point]) -> bool:
    for point in points:
        others = set(points)
        others.remove(point);
        sum = 0
        for other in others:
            sum += dist(point, other)
        others_list = list(others)
        if sum <= dist(others_list[0], others_list[1]):
            return False
    return True

if __name__ == '__main__':
    triangle = []
    for i in range(3):
        x = float(input(f'X[{i}]='))
        y = float(input(f'Y[{i}]='))
        triangle.append((x, y))
    print(triangle, "is triangle:", is_trinagle(triangle))