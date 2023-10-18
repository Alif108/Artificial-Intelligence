import math

# Constants for player symbols
EMPTY = '.'
PLAYER_X = 'X'
PLAYER_O = 'O'

# Define the Gomoku board size (e.g., 15x15)
BOARD_SIZE = 5

# Win Condition
WIN_LENGTH = 4


# ------------------------------ Heuristics ----------------------------- #
# Heuristic weights
HEURISTIC_WEIGHTS = {
    "num_pieces_in_row": 100,
    "center_control": 10,
    "mobility": 10,
    "piece_count": 10,
    "defense": 1000,
    "corner_control": 10,
    "edge_control": 10,
    "threat_detection": 10,
    "open_row": 10,
}

# Heuristic 1: Number of Pieces in a Row (Horizontally, Vertically, and Diagonally)
def num_pieces_in_row(board, player):
    piece_count = 0
    
    # Check horizontally
    for row in board:
        for i in range(len(row)):
            if row[i] == player:
                piece_count += 1
                if piece_count == WIN_LENGTH:
                    return 1000  # Winning condition
            else:
                piece_count = 0

    # Check vertically
    for j in range(len(board[0])):
        for i in range(len(board)):
            if board[i][j] == player:
                piece_count += 1
                if piece_count == WIN_LENGTH:
                    return 1000  # Winning condition
            else:
                piece_count = 0

    # Check diagonally (from top-left to bottom-right)
    for i in range(len(board)):
        for j in range(len(board[0])):
            for k in range(WIN_LENGTH):
                if (
                    i + k < len(board) and
                    j + k < len(board[0]) and
                    board[i + k][j + k] == player
                ):
                    piece_count += 1
                    if piece_count == WIN_LENGTH:
                        return 1000  # Winning condition
                else:
                    piece_count = 0

    # Check diagonally (from top-right to bottom-left)
    for i in range(len(board)):
        for j in range(len(board[0])):
            for k in range(WIN_LENGTH):
                if (
                    i + k < len(board) and
                    j - k >= 0 and
                    board[i + k][j - k] == player
                ):
                    piece_count += 1
                    if piece_count == WIN_LENGTH:
                        return 1000  # Winning condition
                else:
                    piece_count = 0

    return piece_count


# Heuristic 2: Center Control
def center_control(board, player):
    center_x, center_y = len(board) // 2, len(board[0]) // 2
    return 1 if board[center_x][center_y] == player else 0

# Heuristic 3: Mobility
def mobility(board, player):
    empty_cells = 0
    for row in board:
        empty_cells += row.count(EMPTY)
    return empty_cells

# Heuristic 4: Piece Count
def piece_count(board, player):
    player_pieces = sum(row.count(player) for row in board)
    opponent_pieces = sum(row.count(get_opponent(player)) for row in board)
    return player_pieces - opponent_pieces

# Heuristic 5: Defense
def defense(board, player):
    opponent = get_opponent(player)
    threat_count = 0

    # Check rows for opponent's threats
    for row in board:
        for i in range(len(row) - (WIN_LENGTH-1)):
            window = row[i:i + WIN_LENGTH]
            # if window.count(opponent) == (WIN_LENGTH-1) and window.count(EMPTY) == 1:
            if window.count(opponent) >= WIN_LENGTH/2 :
                threat_count += 1

    # Check columns for opponent's threats
    for j in range(len(board[0])):
        column = [board[i][j] for i in range(len(board))]
        for i in range(len(column) - (WIN_LENGTH-1)):
            window = column[i:i + WIN_LENGTH]
            # if window.count(opponent) == (WIN_LENGTH-1) and window.count(EMPTY) == 1:
            if window.count(opponent) >= WIN_LENGTH/2 :
                threat_count += 1

    # Check diagonals (both directions) for opponent's threats
    for i in range(len(board)):
        for j in range(len(board[0])):
            if i + (WIN_LENGTH-1) < len(board) and j + (WIN_LENGTH-1) < len(board[0]):
                diagonal = [board[i + k][j + k] for k in range(WIN_LENGTH)]
                # if diagonal.count(opponent) == (WIN_LENGTH-1) and diagonal.count(EMPTY) == 1:
                if diagonal.count(opponent) >= WIN_LENGTH/2 :
                    threat_count += 1

            if i - (WIN_LENGTH-1) >= 0 and j + (WIN_LENGTH-1) < len(board[0]):
                diagonal = [board[i - k][j + k] for k in range(WIN_LENGTH)]
                # if diagonal.count(opponent) == (WIN_LENGTH-1) and diagonal.count(EMPTY) == 1:
                if diagonal.count(opponent) >= WIN_LENGTH/2 :
                    threat_count += 1

    return threat_count

# Heuristic 6: Corner Control
def corner_control(board, player):
    corners = [(0, 0), (0, len(board[0]) - 1), (len(board) - 1, 0), (len(board) - 1, len(board[0]) - 1)]
    player_corners = sum(1 for corner in corners if board[corner[0]][corner[1]] == player)
    return player_corners

# Heuristic 7: Edge Control
def edge_control(board, player):
    edge_cells = [(0, i) for i in range(len(board[0]))] + [(i, 0) for i in range(len(board))] + [(len(board) - 1, i) for i in range(len(board[0]))] + [(i, len(board[0]) - 1) for i in range(len(board))]
    player_edge = sum(1 for cell in edge_cells if board[cell[0]][cell[1]] == player)
    return player_edge

# Heuristic 8: Threat Detection
def threat_detection(board, player):
    player_threats = 0
    for move in generate_possible_moves(board):
        board[move[0]][move[1]] = player
        if defense(board, player) > 0:
            player_threats += 1
        board[move[0]][move[1]] = EMPTY
    return player_threats

# Heuristic 9: Open Rows
def open_rows(board, player):
    open_row_count = 0
    for row in board:
        for i in range(len(row) - (WIN_LENGTH-1)):
            window = row[i:i + WIN_LENGTH]
            if window.count(player) == (WIN_LENGTH-1) and window.count(EMPTY) == 1:
                open_row_count += 1
    return open_row_count
    
#####################################################################################################

# Calculate the heuristic value of the board for the given player
def evaluate_board(board, player):
    heuristic_value = (
        HEURISTIC_WEIGHTS["num_pieces_in_row"] * num_pieces_in_row(board, player) +
        # HEURISTIC_WEIGHTS["center_control"] * center_control(board, player) +
        # HEURISTIC_WEIGHTS["mobility"] * mobility(board, player) +
        # HEURISTIC_WEIGHTS["piece_count"] * piece_count(board, player) +
        HEURISTIC_WEIGHTS["defense"] * defense(board, player) 
        # HEURISTIC_WEIGHTS["corner_control"] * corner_control(board, player) +
        # HEURISTIC_WEIGHTS["edge_control"] * edge_control(board, player) +
        # HEURISTIC_WEIGHTS["threat_detection"] * threat_detection(board, player) +
        # HEURISTIC_WEIGHTS["open_row"] * open_rows(board, player)
    )
    return heuristic_value

# Implement the minimax algorithm with alpha-beta pruning
def minimax_alpha_beta(board, depth, alpha, beta, maximizing_player, player):
    if depth == 0 or is_game_over(board):
        return evaluate_board(board, player)

    if maximizing_player:
        max_eval = -math.inf
        for move in generate_possible_moves(board):
            board[move[0]][move[1]] = player
            eval = minimax_alpha_beta(board, depth - 1, alpha, beta, False, player)
            board[move[0]][move[1]] = EMPTY
            max_eval = max(max_eval, eval)
            alpha = max(alpha, eval)
            if beta <= alpha:
                break
        return max_eval
    else:
        min_eval = math.inf
        for move in generate_possible_moves(board):
            board[move[0]][move[1]] = get_opponent(player)
            eval = minimax_alpha_beta(board, depth - 1, alpha, beta, True, player)
            board[move[0]][move[1]] = EMPTY
            min_eval = min(min_eval, eval)
            beta = min(beta, eval)
            if beta <= alpha:
                break
        return min_eval

# Get the opponent's symbol
def get_opponent(player):
    return PLAYER_X if player == PLAYER_O else PLAYER_O

# Check if the game is over (a player has won or it's a draw)
def is_game_over(board):
    # Check for a winning condition
    for i in range(len(board)):
        for j in range(len(board[i])):
            if board[i][j] != EMPTY:
                if check_win(board, i, j):
                    return True

    # Check for a draw condition
    return all(cell != EMPTY for row in board for cell in row)

# Function to check if there's a win condition starting from a position (i, j)
def check_win(board, i, j):
    directions = [(1, 0), (0, 1), (1, 1), (1, -1)]
    for dx, dy in directions:
        count = 1
        for k in range(1, 5):
            x, y = i + k * dx, j + k * dy
            if not (0 <= x < len(board) and 0 <= y < len(board[x])):
                break
            if board[x][y] == board[i][j]:
                count += 1
            else:
                break
        if count == WIN_LENGTH:
            return True
    return False

# Generate possible moves on the board
def generate_possible_moves(board):
    moves = []
    for i in range(len(board)):
        for j in range(len(board[i])):
            if board[i][j] == EMPTY:
                moves.append((i, j))
    return moves


# Find the best move using minimax with alpha-beta pruning
def find_best_move(board, depth, player):
    best_val = -math.inf
    best_move = None

    for move in generate_possible_moves(board):
        board[move[0]][move[1]] = player
        eval = minimax_alpha_beta(board, depth, -math.inf, math.inf, False, player)
        board[move[0]][move[1]] = EMPTY

        if eval > best_val:
            best_val = eval
            best_move = move

    return best_move

# Initialize the Gomoku board
def initialize_board():
    board = [[EMPTY] * BOARD_SIZE for _ in range(BOARD_SIZE)]
    return board

# Print the Gomoku board
def print_board(board):
    for row in board:
        print(' '.join(row))
    print()

# Main game loop
def play_game():

    print("Welcome to Gomoku!")
    print("1. Player vs. Player")
    print("2. Player vs. AI")
    print("3. AI vs. AI")
    choice = int(input("Enter your choice: "))

    board = initialize_board()
    if choice == 2:
        depth = int(input("Enter search depth for AI: "))
    elif choice == 3:
        depth = int(input("Enter search depth for AI PLAYER_O: "))
        depth2 = int(input("Enter search depth for AI PLAYER_X: "))
    
    # starting player
    player = PLAYER_O

    while not is_game_over(board):
        print_board(board)
        print(f"Player {player}'s turn:")

        if choice == 1:
            move = input("Enter your move (row and column, e.g., '2 3'): ").split()
            best_move = (int(move[0]), int(move[1]))
            if is_valid_move(board, best_move):
                board[best_move[0]][best_move[1]] = player
            else:
                print("Invalid move. Try again.")
                continue
       
        elif choice == 2:
            if player == PLAYER_X:
                best_move = find_best_move(board, depth, player)
            else:
                move = input("Enter your move (row and column, e.g., '2 3'): ").split()
                best_move = (int(move[0]), int(move[1]))

            if is_valid_move(board, best_move):
                board[best_move[0]][best_move[1]] = player
            else:
                print("Invalid move. Try again.")
                continue

        elif choice == 3:
            if player == PLAYER_X:
                best_move = find_best_move(board, depth, player)
            else:
                best_move = find_best_move(board, depth2, player)
            if is_valid_move(board, best_move):
                board[best_move[0]][best_move[1]] = player
            else:
                print("Invalid move. Try again.")
                continue

        player = get_opponent(player)

    print_board(board)
    # check draw condition
    if all(cell != EMPTY for row in board for cell in row):
        print("It's a draw!")
    else:
        print(f"Player {get_opponent(player)} won!")

# Check if a move is valid (within the bounds and on an empty cell)
def is_valid_move(board, move):
    row, col = move
    return 0 <= row < BOARD_SIZE and 0 <= col < BOARD_SIZE and board[row][col] == EMPTY

# Run the Gomoku game
if __name__ == "__main__":
    play_game()
