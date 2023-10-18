import numpy as np

# delcare all the probabilities
PROB_ADJ_CELL = 0.9
PROB_CORNER_CELL = 1 - PROB_ADJ_CELL
SENSOR_PROB = 0.85


# initialize the room with equal probablity
def initialize_probability(arr, n_obstacles):
	total_cell = len(arr) * len(arr[0])
	total_free_cell = total_cell - n_obstacles

	for i in range(len(arr)):
		for j in range(len(arr[i])):
			if arr[i][j] != '#':
				arr[i][j] = 1/total_free_cell



# return the adjacent indices of cell (u, v)
def get_adjacent_indices(arr, u ,v):
	adjacent_indices = []

	if (u-1 >= 0) and (arr[u-1][v] != '#'):
		adjacent_indices.append((u-1, v))

	if (u+1 < len(arr)) and (arr[u+1][v] != '#'):
		adjacent_indices.append((u+1, v))
	
	if (v-1 >= 0) and (arr[u][v-1] != '#'):
		adjacent_indices.append((u, v-1))
	
	if (v+1 < len(arr[0])) and (arr[u][v+1] != '#'):
		adjacent_indices.append((u, v+1))

	return adjacent_indices



# return the corner indices of cell (u, v)
def get_corner_indices(arr, u ,v):
	corner_indices = []

	if (u-1 >= 0) and (v-1 >= 0) and (arr[u-1][v-1] != '#'):
		corner_indices.append((u-1, v-1))

	if (u+1 < len(arr)) and (v-1 >= 0) and (arr[u+1][v-1] != '#'):
		corner_indices.append((u+1, v-1))

	if (u-1 >= 0) and (v+1 < len(arr[0])) and (arr[u-1][v+1] != '#'):
		corner_indices.append((u-1, v+1)) 

	if (u+1 < len(arr)) and (v+1 < len(arr[0])) and (arr[u+1][v+1] != '#'):
		corner_indices.append((u+1, v+1))

	return corner_indices



# update the room with HMM probabilities (without evidence)
def time_step(arr):
	temp_arr = [row[:] for row in arr]

	for i in range(len(arr)):
		for j in range(len(arr[0])):

			if arr[i][j] == '#':								## nothing happens to obstacles
				continue

			arr[i][j] = 0

			adj_idxs = get_adjacent_indices(arr, i, j)			## e.g. adj_idxs = [(0, 0), (1, 2), ...]
			corner_idxs = get_corner_indices(arr, i, j)			## e.g. corner_idxs = [(0, 0), (1, 2), ...]

			for idx in adj_idxs:								# e.g. idx = (0 , 1)
				val = temp_arr[idx[0]][idx[1]]					# e.g. val = temp_arr[0][1]
				prob = PROB_ADJ_CELL/len(get_adjacent_indices(temp_arr, idx[0], idx[1]))
				arr[i][j] += val * prob							## e.g. arr[i][j] = 1/9 * 0.9/4

			for idx in corner_idxs:								# e.g. idx = (0 , 1)
				val = temp_arr[idx[0]][idx[1]]					# e.g. val = temp_arr[0][1]
				prob = PROB_CORNER_CELL/(len(get_corner_indices(temp_arr, idx[0], idx[1])) + 1)			## +1 for statying in same place
				arr[i][j] += val * prob							## e.g. arr[i][j] = 1/9 * 0.1/4

			arr[i][j] += temp_arr[i][j] * PROB_CORNER_CELL/(len(corner_idxs) + 1)		## staying in same place

	print("Partial Belief (Without Evidence): ")
	print_room(arr)



# print casper's probable location 
# i.e. return cell with max probability
def show_casper(arr):
	pos = np.unravel_index(np.array(arr).argmax(), np.array(arr).shape)
	print("Casper is probably at ", end = "")
	print(pos)



## calculate probabilty given evidence
## increase neighbor cells prob while decreasing the others 
def read_sensor(arr, u, v, b):

	time_step(arr)
	print()

	temp_arr = [row[:] for row in arr]

	adj_idxs = get_adjacent_indices(arr, u, v)
	corner_idxs = get_corner_indices(arr, u, v)

	neighbors = []
	not_neighbors = []

	if arr[u][v] != '#':
		self_cell = (u ,v)
		neighbors.append(self_cell)

	for idx in adj_idxs:
		neighbors.append(idx)

	for idx in corner_idxs:
		neighbors.append(idx)

	for i in range(len(arr)):
		for j in range(len(arr[0])):
			if arr[i][j] == '#':
				continue
			
			idx = (i, j)
			if idx not in neighbors:
				not_neighbors.append(idx)

	if b == 1:
		denominator = 0
		for idx in neighbors:
			denominator += temp_arr[idx[0]][idx[1]] * SENSOR_PROB
		for idx in not_neighbors:
			denominator += temp_arr[idx[0]][idx[1]] * (1 - SENSOR_PROB)

		for idx in neighbors:
			arr[idx[0]][idx[1]] = temp_arr[idx[0]][idx[1]] * SENSOR_PROB/denominator
		for idx in not_neighbors:
			arr[idx[0]][idx[1]] = temp_arr[idx[0]][idx[1]] * (1 - SENSOR_PROB)/denominator
	elif b == 0:
		denominator = 0
		for idx in neighbors:
			denominator += temp_arr[idx[0]][idx[1]] * (1 - SENSOR_PROB)
		for idx in not_neighbors:
			denominator += temp_arr[idx[0]][idx[1]] * SENSOR_PROB

		for idx in neighbors:
			arr[idx[0]][idx[1]] = temp_arr[idx[0]][idx[1]] * (1 - SENSOR_PROB)/denominator
		for idx in not_neighbors:
			arr[idx[0]][idx[1]] = temp_arr[idx[0]][idx[1]] * SENSOR_PROB/denominator

	print("Belief After Evidence: ")
	print_room(arr)





# printing the probability of each cell
def print_room(arr):
	sum = 0
	for i in range(len(arr)):
		for j in range(len(arr[i])):
			if arr[i][j] == '#':
				print(arr[i][j], end ="\t\t")
			else:
				sum += arr[i][j]
				print("%.4f" % round(arr[i][j] * 100, 4), end ="\t\t")

		print()
	print("Probablity Sum: %.2f" % round(sum * 100, 4))
	print()




## ------------------ driver code ---------------- ##

row, col, n_obstacles = input("Enter Row, Column and Number of Obstacles: ").split()

row = int(row)
col = int(col)
n_obstacles = int(n_obstacles)

arr = [[0.0 for i in range(col)] for j in range(row)]

for i in range(n_obstacles):
	u,v = input(str(i+1) + " no. obstacle coordinates: ").split()
	u = int(u)
	v = int(v)
	arr[u][v] = "#"

initialize_probability(arr, n_obstacles)
print("Initial Probablity: ")
print_room(arr)

while True:
	choice = input(">> ")

	if choice == 'q' or choice == 'quit':
		break

	elif choice == 'T':
		time_step(arr)

	elif choice == 'C':
		show_casper(arr)

	elif choice[0] == 'R':
		u = int(choice.split()[1])
		v = int(choice.split()[2])
		b = int(choice.split()[3])

		read_sensor(arr, u, v, b)
	else:
		print("Choice not valid")