import heapq
import random

random.seed(20)

class PuzzleNode:
    def __init__(self, board, parent=None, move=None):
        self.board = board
        self.parent = parent
        self.g = 0  # cost from start to current node
        self.h = 0  # heuristic value
        self.f = 0  # total cost

    def __lt__(self, other):
        return self.f < other.f

def hamming_distance(board, goal):
    distance = 0
    for i in range(len(board)):
        for j in range(len(board[i])):
            if board[i][j] != goal[i][j]:
                distance += 1
    return distance

def manhattan_distance(board, goal):
    distance = 0
    for i in range(len(board)):
        for j in range(len(board[i])):
            if board[i][j] != goal[i][j] and board[i][j] != 0:
                x, y = divmod(board[i][j] - 1, len(board))
                distance += abs(x - i) + abs(y - j)
    return distance

def linear_conflict(board, goal):
    distance = 0                                                                # initialize distance
    for i in range(len(board)):                                        # iterate through rows                       
        for j in range(len(board[i])):                   # iterate through columns      
            if board[i][j] != goal[i][j] and board[i][j] != 0:      # if the current tile is not in the correct position and is not the blank tile
                x, y = divmod(board[i][j] - 1, len(board))          # get the correct position of the tile
                distance += abs(x - i) + abs(y - j)                 
                if x == i:                                          # if the tile is in the same row as its correct position
                    for k in range(j + 1, len(board[i])):           # iterate through the rest of the row
                        if board[i][k] != goal[i][k] and board[i][k] != 0:  # if the tile is not in the correct position and is not the blank tile
                            x2, y2 = divmod(board[i][k] - 1, len(board))        # get the correct position of the tile
                            if x2 == i and y2 < y:                              # if the tile is in the same row as its correct position and is to the left of the current tile
                                distance += 2                                   # add 2 to the distance
                if y == j:                                          # if the tile is in the same column as its correct position
                    for k in range(i + 1, len(board)):                  # iterate through the rest of the column
                        if board[k][j] != goal[k][j] and board[k][j] != 0:    # if the tile is not in the correct position and is not the blank tile
                            x2, y2 = divmod(board[k][j] - 1, len(board))        # get the correct position of the tile
                            if y2 == j and x2 < x:                    # if the tile is in the same column as its correct position and is above the current tile
                                distance += 2                   # add 2 to the distance
    
    return manhattan_distance(board, goal) + 2*distance

def find_blank(board):
    for i in range(len(board)):
        for j in range(len(board[i])):
            if board[i][j] == 0:
                return i, j

def is_valid_move(i, j, n):
    return 0 <= i < n and 0 <= j < n

def get_valid_moves(i, j, n):
    moves = []
    for dx, dy in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
        ni, nj = i + dx, j + dy
        if is_valid_move(ni, nj, n):
            moves.append((ni, nj))
    return moves

def generate_neighbors(node):
    neighbors = []
    blank_i, blank_j = find_blank(node.board)
    valid_moves = get_valid_moves(blank_i, blank_j, len(node.board))

    for i, j in valid_moves:
        new_board = [row[:] for row in node.board]
        new_board[blank_i][blank_j], new_board[i][j] = new_board[i][j], new_board[blank_i][blank_j]
        neighbors.append(PuzzleNode(new_board, node, (i, j)))
    
    return neighbors

def reconstruct_path(node):
    path = []
    current = node
    while current is not None:
        path.append(current.board)
        current = current.parent
    path.reverse()
    return path

def count_inversion(init_matrix):
    # Inversion count is the occurrence of smaller numbers after a certain number
    inversion_count = 0
    array_of_num = []

    # Converting to a 1D array
    for i in range(len(init_matrix)):
        for j in range(len(init_matrix[i])):
            array_of_num.append(init_matrix[i][j])

    # Iterating over the array
    for i in range(len(array_of_num)):
        x = array_of_num[i]
        for j in range(i + 1, len(array_of_num)):
            if array_of_num[j] < x and array_of_num[j] != 0:  # If there is any smaller number after index of x
                inversion_count += 1

    return inversion_count

def is_solvable(size, init_matrix):
    if size % 2 != 0:  # If grid size is odd
        if count_inversion(init_matrix) % 2 == 0:  # If inversion is even -> solvable
            return True
        else:  # If inversion is odd -> not solvable
            return False
    else:  # If grid size is even
        blank_pos = [(i, j) for i, row in enumerate(init_matrix) for j, num in enumerate(row) if num == 0][0]

        if (blank_pos[0] % 2 == 0) and (count_inversion(init_matrix) % 2 != 0):  # If blank is in even row and inversion count is odd -> solvable
            return True
        elif (blank_pos[0] % 2 != 0) and (count_inversion(init_matrix) % 2 == 0):  # If blank is in odd row and inversion count is even -> solvable
            return True
        else:
            return False

def a_star_search(initial_board, goal_board, heuristic):

    expanded_nodes = 0
    explored_nodes = 0
    
    open_list = []
    closed_list = set()

    start_node = PuzzleNode(initial_board)
    start_node.h = heuristic(initial_board, goal_board)
    start_node.f = start_node.g + start_node.h
    heapq.heappush(open_list, start_node)

    while open_list:
        current_node = heapq.heappop(open_list)

        if current_node.board == goal_board:
            return (reconstruct_path(current_node), expanded_nodes, explored_nodes)

        closed_list.add(tuple(map(tuple, current_node.board)))

        neighbors = generate_neighbors(current_node)
        explored_nodes += len(neighbors)

        for neighbor in neighbors:
            if tuple(map(tuple, neighbor.board)) not in closed_list:
                neighbor.g = current_node.g + 1
                neighbor.h = heuristic(neighbor.board, goal_board)
                neighbor.f = neighbor.g + neighbor.h
                heapq.heappush(open_list, neighbor)

        expanded_nodes += 1
    
    return None

def create_puzzle_board(n):
    numbers = list(range(n * n))
    puzzle_board = [numbers[i:i + n] for i in range(0, len(numbers), n)]
    return puzzle_board

def get_user_input():
    while True:
        try:
            n = int(input("Enter the value of 'n' for N-Puzzle (e.g., 3 for 8-Puzzle): "))
            if n < 2:
                print("Please enter a valid value greater than 1.")
            else:
                initial_board = []
                print("Enter the values for the initial board (0 for blank space): ")
                for i in range(n):
                    row = []
                    for j in range(n):
                        row.append(int(input("Enter the value for row {} and column {}: ".format(i + 1, j + 1))))
                    initial_board.append(row)

                # goal_board = []
                # print("Enter the values for the goal board (0 for blank space): ")
                # for i in range(n):
                #     row = []
                #     for j in range(n):
                #         row.append(int(input("Enter the value for row {} and column {}: ".format(i + 1, j + 1))))
                #     goal_board.append(row)

                goal_board = [[1, 2, 3], [4, 5, 6], [7, 8, 0]]

                return n, initial_board, goal_board
        except ValueError:
            print("Invalid input. Please enter a valid integer value.")

def print_puzzle_board(board):
    for row in board:
        for num in row:
            if num == 0:
                print("*", end=" ")
            else:
                print(num, end=" ")
        print()

def main():

    n, initial_board, goal_board = get_user_input()

    # Scramble the initial board to create a solvable puzzle
    # initial_board = create_puzzle_board(3)
    # goal_board = create_puzzle_board(3)
    # random.shuffle(initial_board)
    # while initial_board == goal_board:
    #     random.shuffle(initial_board)

    print("Initial Puzzle:")
    print_puzzle_board(initial_board)

    print("Goal Puzzle:")
    print_puzzle_board(goal_board)

    if not is_solvable(n, initial_board):
        print("The puzzle is not solvable.")
        return

    # # Choose heuristic: 1 for Hamming distance, 2 for Manhattan distance
    # heuristic_choice = None
    # while heuristic_choice not in [1, 2]:
    #     heuristic_choice = int(input("Choose a heuristic (1 for Hamming, 2 for Manhattan): "))

    # if heuristic_choice == 1:
    #     heuristic = hamming_distance
    # else:
    #     heuristic = manhattan_distance

    # hamming distance
    path, expanded_nodes, explored_nodes = a_star_search(initial_board, goal_board, heuristic=hamming_distance)
    if path:
        print("Solution for Hamming Distance:")
        for board in path:
            print_puzzle_board(board)
            print()
        print("Number of expanded nodes: {}".format(expanded_nodes))
        print("Number of explored nodes: {}".format(explored_nodes))

    # manhattan distance
    path, expanded_nodes, explored_nodes = a_star_search(initial_board, goal_board, heuristic=manhattan_distance)
    if path:
        print("Solution for Manhattan Distance:")
        for board in path:
            print_puzzle_board(board)
            print()
        print("Number of expanded nodes: {}".format(expanded_nodes))
        print("Number of explored nodes: {}".format(explored_nodes))

    # row linear conflict
    path, expanded_nodes, explored_nodes = a_star_search(initial_board, goal_board, heuristic=linear_conflict)
    if path:
        print("Solution for Linear Conflict:")
        for board in path:
            print_puzzle_board(board)
            print()
        print("Number of expanded nodes: {}".format(expanded_nodes))
        print("Number of explored nodes: {}".format(explored_nodes))

    else:
        return

if __name__ == "__main__":
    main()
