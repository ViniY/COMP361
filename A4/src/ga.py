import numpy as np
from deap import base, creator, tools
import re
from deap import algorithms
import matplotlib.pyplot as plt
import random as rd

cities_set = []
cities_tups = []
cities_dict = {}
nodes = []
initState = []
distances = None
def read_tsp_data(tsp_name):
    tsp_name = tsp_name
    with open(tsp_name) as f:
        content = f.read().splitlines()
        cleaned = [x.lstrip() for x in content if x != ""]
    # for i in cleaned:
    #     print(i)

    return cleaned


def detect_dimension(in_list):
    non_numeric = re.compile(r'[^\d]+')
    for element in in_list:
        if element.startswith("DIMENSION"):
            return non_numeric.sub("", element)


def get_cities(list, dimension):
    dimension = int(dimension)
    list = list[6:]
    del list[-1]

    for item in list:
        # print(item)
        index, space, rest = item.partition(' ')
        # if rest not in cities_set:
        # print(rest)
        cities_set.append(rest)
    # print("The length is : ",len(cities_set))
    return cities_set


"""
Brake each coordinate 33.00 44.00 to a tuple ('33.00','44.00')
"""


def city_tup(list):
    for item in list:
        item = re.sub(' +', ' ', item)
        first_coord, space, second_coord = item.partition(' ')
        cities_tups.append((first_coord.strip(), second_coord.strip()))
    return cities_tups


"""
We zip for reference each city to a number
in order to work and solve the TSP we need a list 
of cities like 
[1,2,3,4,5,...........]
with the dictionary we will have a reference of the coordinates of each city 
to calculate the distance within (i + 1, i) or (2 - 1) were 2 and 1 represents each city
"""


def create_cities_dict(cities_tups):
    return zip((range(1, len(cities_tups) + 1)), cities_tups)


"""
Just to plot the results
"""
def plot_cities(cities_tups):
    plt.clf()
    plt.scatter(*zip(*cities_tups))
    # plt.(*zip(*cities_tups))
    plt.show()


def produce_final(file):
    data = read_tsp_data(file)
    dimension = detect_dimension(data)

    cities_set = get_cities(data, dimension)

    cities_tups = city_tup(cities_set)
    cities_dict = create_cities_dict(cities_tups)
    plot_cities(cities_tups)
    # print (str(cities_dict))

    return (cities_tups,cities_dict);



# start for the simple heuristic algorithm
# First compute the average distance for each city across all the cities


def compute_distance(city_tuples):
    colCounter = 0
    size = len(city_tuples)
    maps = np.zeros(shape=(size,size))
    rowCounter= -1
    for p1 in cities_tups:
        rowCounter += 1
        colCounter = 0
        for p2 in cities_tups:
            dist = np.sqrt(
                np.power((float(p1[0])-float(p2[0])),2) +  np.power((float(p1[1])
                                                                     -float(p2[1])),2)
            )
            maps[rowCounter][colCounter] = dist
            colCounter += 1
    return maps


def EVALUATE(individual):
    summation = 0
    start = individual[0]
    for i in range(1,len(individual)):
        end = individual[i]
        summation += distances[start][end]
        start = end
    return (summation,)


def main():
    rd.seed(361)

    INDIVIDUAL_SIZE = len(distances[0])
    creator.create("FitnessMin", base.Fitness, weights=(-1.0,))

    creator.create("Individual", list, fitness=creator.FitnessMin)

    toolbox = base.Toolbox()
    toolbox.register("indices",
                     rd.sample,
                     range(INDIVIDUAL_SIZE),
                     INDIVIDUAL_SIZE
                     )
    toolbox.register("individual",
                     tools.initIterate,
                     creator.Individual, toolbox.indices
                     )

    toolbox.register("population", tools.initRepeat, list, toolbox.individual)
    toolbox.register("evaluate", EVALUATE)
    toolbox.register("mate", tools.cxOrdered)
    toolbox.register("mutate", tools.mutShuffleIndexes, indpb=0.05)
    toolbox.register("select", tools.selTournament, tournsize=10)


    pop = toolbox.population(n=3000)
    hof = tools.HallOfFame(1)

    stats = tools.Statistics(lambda ind : ind.fitness.values)
    stats.register("avg", np.mean)
    stats.register("std", np.std)
    stats.register("min", np.min)
    stats.register("max", np.max)


    pop,logbook = algorithms.eaSimple(pop, toolbox, 0.7, 0.2, ngen=200 , stats=stats, halloffame=hof)

    return pop, stats, hof,logbook


if __name__ == '__main__':
    fPath = "/Users/vini/Desktop/2021T1/COMP361-main/A4/src/a280.tsp"
    optimalSolution='/Users/vini/Desktop/2021T1/COMP361-main/A4/src/a280Optimal'
    citiesPos, city_distances = produce_final(fPath)
    distance = compute_distance(citiesPos)
    distances = distance
    pop, stats, hof, logbook = main()
    print("---"*40)
    gen = logbook.select("gen")
    fitmins = logbook.select("min")
    fitmins
    plt.plot(fitmins)
    plt.show()