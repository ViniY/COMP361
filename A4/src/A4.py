import re
import matplotlib.pyplot as plt
import numpy as np
import random as rd
import math
cities_set = []
cities_tups = []
cities_dict = {}
nodes = []
initState = []
"""
The following variables are used in simulated annealing 
"""
bestSolution =[]
bestFitness=0
currentSolution=[]
currentFitness=0
tempreture=0


#  we open the TSP file and put each line cleaned of spaces
#  and newline characters in a list
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


def selectStart(city_distances):
    average_distance = []
    size = len(city_distances)
    for i in range(size):
        if size > 1 :
            dis = np.sum(city_distances[i]) / (size-1)
            average_distance.append(dis)
        else:
            average_distance.append(dis)

    index = average_distance.index(min(average_distance))

    return index


# simple heuristic to compute the result

def simpleHeuristic(city_data, start_node):
    solution = []
    size = len(city_data)
    solution.append(start_node)
    currentNode = start_node
    visitedNum = 0
    disNow = 0
    distances= []
    distances.append(0)
    while visitedNum < size:
        distancesNow = city_data[currentNode]
        distancesNow = list(enumerate(distancesNow))

        distancesNow = sorted(distancesNow, key = lambda x:x[1])
        for x in distancesNow:
            if x[0] not in solution and x[0]!=currentNode:
                disNow += city_data[currentNode][x[0]]
                distances.append(disNow)
                solution.append(x[0])
                currentNode = x[0]

        visitedNum += 1
    return solution, distances


def computeOptimalDistance(fn,map):
    with open(fn) as f:
        lines = f.readlines()
    optiamlCitys = []
    for line in lines:
        optiamlCitys.append(int(line)-1)
    optimalDistance = 0
    for i in range(len(optiamlCitys)-1):
        optimalDistance += map[optiamlCitys[i]][optiamlCitys[i+1]]
    print('\n')
    optimalDistance+= map[optiamlCitys[-1]][optiamlCitys[0]]
    print("The optimal distance should be ",optimalDistance)


def computeRandonDistance(sol,map):
    randomDis = 0
    for i in range(len(sol)-1):
        randomDis += map[sol[i]][sol[i+1]]
    print('\n')
    print("The random solution distance should be ",randomDis)
    return randomDis
"""
Simulated Annealing Start
"""


class SimAnnealing(object):
    def __init__(self, alpha = -1, T=-1, stopping_T = -1, stopping_iter=-1, maps=[], init_solution=None, coords=None, init_fitness=None):
        self.coords = coords
        self.alpha =0.995 if alpha==-1 else alpha
        self.stopping_temperature = 1e-8 if stopping_T == -1 else stopping_T
        self.stopping_iter = 100000 if stopping_iter == -1 else stopping_iter
        self.iteration = 1
        self.N = len(coords)
        self.T = math.sqrt(self.N) if T==-1 else T

        self.best_solution = None
        self.best_fitness = float("Inf")
        self.fitness_list = []

        self.distances = maps
        self.initialState = init_solution
        self.initFitness = init_fitness



    def evaluation(self, solution, maps):
        distance = 0
        for i in range(len(solution)-1):
            fromNode = solution[i]
            toNode = solution[i+1]
            distance += maps[fromNode][toNode]

        return distance

    """
    Initialisation of the simulated annealing approach
    """
    def accept(self, candidate, maps):
        """
        Accept with probability 1 if candidate is better than current.
        Accept with probabilty p_accept(..) if candidate is worse.
        """
        candidate_fitness = self.evaluation(candidate,maps)
        if(self.iteration%10 ==0):
            print("The candidate Fitness now is : ", candidate_fitness)
        if candidate_fitness < self.cur_fitness:
            self.cur_fitness, self.cur_solution = candidate_fitness, candidate
            if candidate_fitness < self.best_fitness:
                self.best_fitness, self.best_solution = candidate_fitness, candidate
        else:
            if rd.random() < self.p_accept(candidate_fitness):
                self.cur_fitness, self.cur_solution = candidate_fitness, candidate

    """
    The whole procedure of Simulated Annealing
    """

    def p_accept(self, candidate_fitness):
        """
        Probability of accepting if the candidate is worse than current.
        Depends on the current temperature and difference between candidate and current.
        """
        return math.exp(-abs(candidate_fitness - self.cur_fitness) / self.T)

    def anneal(self):
        """
        Execute simulated annealing algorithm.
        """
        self.cur_solution =initState
        self.initialState = initState
        self.cur_fitness = self.initFitness
        self.best_fitness = self.initFitness
        print("The initial Fitness is : ", self.cur_fitness)

        print("Starting annealing.")
        while self.T >= self.stopping_temperature and self.iteration < self.stopping_iter:
            candidate = self.cur_solution
            bestNeighbour = []
            bestFitness_neigh=10000000000000
            for i in range(10):
                swapIndex = rd.randint(0,self.N)
                swapIndex2 = rd.randint(0,self.N)
                if swapIndex!=0 and swapIndex2!=0:
                    temp = candidate[swapIndex]
                    temp2 = candidate[swapIndex2]
                    candidate[swapIndex] = temp2
                    candidate[swapIndex2] = temp
                elif swapIndex==0 and swapIndex2!=0:
                    temp = candidate[swapIndex]
                    temp2 = candidate[swapIndex2]
                    candidate[swapIndex] = temp2
                    candidate[-1] = temp2
                    candidate[swapIndex2] = temp
                elif swapIndex2==0 and swapIndex!=0:
                    temp = candidate[swapIndex]
                    temp2 = candidate[swapIndex2]
                    candidate[swapIndex2] = temp
                    candidate[-1] = temp
                    candidate[swapIndex] = temp2
                neighFit = self.evaluation(candidate, self.distances)
                if  neighFit < bestFitness_neigh:
                    bestNeighbour = candidate
                    bestFitness_neigh = neighFit
            candidate = bestNeighbour


            # l = rd.randint(2, self.N - 1)
            # i = rd.randint(0, self.N - l)
            # candidate[i : (i + l)] = reversed(candidate[i : (i + l)])
            self.accept(candidate, self.distances)
            self.T *= self.alpha
            self.iteration += 1

            self.fitness_list.append(self.cur_fitness)

        print("Best fitness obtained: ", self.best_fitness)
        improvement = 100 * (self.fitness_list[0] - self.best_fitness) / (self.fitness_list[0])
        print(f"Improvement over greedy heuristic: {improvement : .2f}%")
        return self.best_solution

    def plot_learning(self):
        """
        Plot the fitness through iterations.
        """
        plt.plot([i for i in range(len(self.fitness_list))], self.fitness_list)
        plt.ylabel("Fitness")
        plt.xlabel("Iteration")
        plt.show()


def SimulatedAnnealing(solutionFromHeuristic,initFitness ,maps,citydata):
    solutionFromHeuristic = solutionFromHeuristic.append(solutionFromHeuristic[0])

    smA = SimAnnealing(maps= maps, init_solution= solutionFromHeuristic,coords=citydata,init_fitness=initFitness)

    res = smA.anneal()
    smA.plot_learning()
    # initState = initialisation(solutionFromHeuristic)
    #     # currentFitness = evaluation(initState,maps)
    #     # bestSolution = initState
    return res
"""
Initialisation with a purely random solution
"""
def randomInitialisation(cityPos):
    l = len(citiesPos)
    sol =[]
    while len(sol) < l:
        r = rd.randint(0,l-1)
        if r not in sol:
            sol.append(r)
    return sol


if __name__ == '__main__':
    # fPath = "eil51.tsp"
    # fPath = "a280.tsp"
    # optimalSolution ='51optimal'
    # optimalSolution='a280Optimal'
    # fPath = "berlin52.tsp"
    # optimalSolution = 'berlin52OPtimal'
    # fPath = "ch130.tsp"
    # optimalSolution = 'ch130Optimal'
    # fPath = "ch150.tsp"
    # optimalSolution = 'ch150'
    # fPath = "eil101.tsp"
    # optimalSolution = 'eil101Optimal'
    # fPath = "eil76.tsp"
    # optimalSolution = 'eil76Optimal'
    # fPath = "tsp225.tsp"
    fPath = "vm1084.tsp"

    optimalSolution ='eil76Optimal'

    fPath = "lin105.tsp"
    optimalSolution ='lin105'


    citiesPos,city_distances= produce_final(fPath)

    distances_map= compute_distance(citiesPos)
    start_node = selectStart(distances_map)
    solution,distances= simpleHeuristic(distances_map,start_node)
    for i in range(len(solution)):
        print("This is the ", i , " th Node and is city ", solution[i])
        print("And the distance now is ", distances[i])

    for i in range(len(solution)):
        if i % 20 == 0:
            print('\n')

        print(solution[i], end='->')
    # print(solution)
    rdInitialize = randomInitialisation(citiesPos)
    """here we compute the optimal solution from website"""
    computeOptimalDistance(optimalSolution,distances_map)

    """here we compute the random solution from website"""
    rdDistance = computeRandonDistance(rdInitialize,distances_map)
    """Simulated Annealing"""
    initState = solution

    initState = rdInitialize

    print(solution)
    """Initialise with the heuristic"""
    # simAnnealingSolution = SimulatedAnnealing(solutionFromHeuristic= solution, initFitness= distances[-1], maps = distances_map, citydata= citiesPos)
    """randomly initialise """
    # simAnnealingSolution = SimulatedAnnealing(solutionFromHeuristic= rdInitialize, initFitness=rdDistance , maps = distances_map, citydata= citiesPos)