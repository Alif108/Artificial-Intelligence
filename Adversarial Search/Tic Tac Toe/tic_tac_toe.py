import math

# Constants for player symbols
EMPTY = ' '
PLAYER_X = 'X'
PLAYER_O = 'O'

# Define the Tic Tac Toe board size
BOARD_SIZE = 3

# Define the depth of the minimax algorithm
DEPTH = 5

# Function to print the game board
def print_board(board):
    for row in board:
        print(' | '.join(row))
        print('-' * (5 * BOARD_SIZE - 1))
    print()

# Function to check if the game is over
def is_game_over(board):
    # Check rows, columns, and diagonals for a win
    for i in range(BOARD_SIZE):
        if board[i][0] == board[i][1] == board[i][2] != EMPTY:
            return True
        if board[0][i] == board[1][i] == board[2][i] != EMPTY:
            return True
    if board[0][0] == board[1][1] == board[2][2] != EMPTY:
        return True
    if board[0][2] == board[1][1] == board[2][0] != EMPTY:
        return True

    # Check for a draw
    for row in board:
        if EMPTY in row:
            return False
    return True

# Function to evaluate the board for the AI player
def evaluate_board(board, player):
    # Check rows, columns, and diagonals for wins
    for i in range(BOARD_SIZE):
        if board[i][0] == board[i][1] == board[i][2] == player:
            return 10
        if board[0][i] == board[1][i] == board[2][i] == player:
            return 10
    if board[0][0] == board[1][1] == board[2][2] == player:
        return 10
    if board[0][2] == board[1][1] == board[2][0] == player:
        return 10

    # Check rows, columns, and diagonals for opponent wins
    opponent = PLAYER_X if player == PLAYER_O else PLAYER_O
    for i in range(BOARD_SIZE):
        if board[i][0] == board[i][1] == board[i][2] == opponent:
            return -10
        if board[0][i] == board[1][i] == board[2][i] == opponent:
            return -10
    if board[0][0] == board[1][1] == board[2][2] == opponent:
        return -10
    if board[0][2] == board[1][1] == board[2][0] == opponent:
        return -10

    # No win or loss yet
    return 0

# Minimax algorithm with Alpha-Beta Pruning
def minimax(board, depth, alpha, beta, maximizing_player, player):
    if is_game_over(board) or depth == 0:
        return evaluate_board(board, player)

    if maximizing_player:
        max_eval = -math.inf
        for i in range(BOARD_SIZE):
            for j in range(BOARD_SIZE):
                if board[i][j] == EMPTY:
                    board[i][j] = player
                    eval = minimax(board, depth - 1, alpha, beta, False, player)
                    board[i][j] = EMPTY
                    max_eval = max(max_eval, eval)
                    alpha = max(alpha, eval)
                    if beta <= alpha:
                        break
        return max_eval
    else:
        min_eval = math.inf
        for i in range(BOARD_SIZE):
            for j in range(BOARD_SIZE):
                if board[i][j] == EMPTY:
                    board[i][j] = get_opponent(player)
                    eval = minimax(board, depth - 1, alpha, beta, True, player)
                    board[i][j] = EMPTY
                    min_eval = min(min_eval, eval)
                    beta = min(beta, eval)
                    if beta <= alpha:
                        break
        return min_eval

# Function to get the opponent's symbol
def get_opponent(player):
    return PLAYER_X if player == PLAYER_O else PLAYER_O

# Function to make the AI's move
def make_ai_move(board, player, depth=3):
    best_eval = -math.inf
    best_move = None
    for i in range(BOARD_SIZE):
        for j in range(BOARD_SIZE):
            if board[i][j] == EMPTY:
                board[i][j] = player
                eval = minimax(board, depth, -math.inf, math.inf, False, player)
                board[i][j] = EMPTY
                if eval > best_eval:
                    best_eval = eval
                    best_move = (i, j)
    return best_move

# Main game loop
def play_game():

    ai_vs_ai = False
    print("Welcome to Tic Tac Toe!")
    print("1. Human vs. AI")
    print("2. AI vs. AI")
    choice = int(input("Choose game mode: "))
    if choice == 2:
        ai_vs_ai = True
        print("Choose AI depth for Player X:")
        depth_x = int(input())
        print("Choose AI depth for Player O:")
        depth = int(input())
    else:
        print("Choose AI depth:")
        depth = int(input())

    board = [[EMPTY] * BOARD_SIZE for _ in range(BOARD_SIZE)]
    current_player = PLAYER_X

    while not is_game_over(board):
        print_board(board)

        if current_player == PLAYER_X:
            if ai_vs_ai:
                row, col = make_ai_move(board, current_player, depth_x)
            else:
                row, col = map(int, input(f"Player {current_player}'s turn (row and column, e.g., '1 2'): ").split())
        else:
            row, col = make_ai_move(board, current_player, depth)

        if board[row][col] == EMPTY:
            board[row][col] = current_player
            current_player = get_opponent(current_player)
        else:
            print("Invalid move. Try again.")

    print_board(board)
    winner = PLAYER_X if current_player == PLAYER_O else PLAYER_O
    if winner:
        print(f"Player {winner} wins!")
    else:
        print("It's a draw!")

# Run the game
if __name__ == "__main__":
    play_game()