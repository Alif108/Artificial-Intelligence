import numpy as np

# Define the Connect Four game class
class ConnectFour:
    def __init__(self):
        self.board = np.zeros((6, 7), dtype=int)
        self.player = 1

    def print_board(self):
        print(self.board)

    def make_move(self, column):
        for row in range(5, -1, -1):
            if self.board[row][column] == 0:
                self.board[row][column] = self.player
                break

    def is_valid_move(self, column):
        return self.board[0][column] == 0

    def is_game_over(self):
        return self.check_winner() or self.is_board_full()

    def check_winner(self):
        for row in range(6):
            for col in range(4):
                if (
                    self.board[row][col]
                    and self.board[row][col] == self.board[row][col + 1] == self.board[row][col + 2] == self.board[row][col + 3]
                ):
                    return True

        for row in range(3):
            for col in range(7):
                if (
                    self.board[row][col]
                    and self.board[row][col] == self.board[row + 1][col] == self.board[row + 2][col] == self.board[row + 3][col]
                ):
                    return True

        for row in range(3):
            for col in range(4):
                if (
                    self.board[row][col]
                    and self.board[row][col] == self.board[row + 1][col + 1] == self.board[row + 2][col + 2] == self.board[row + 3][col + 3]
                ):
                    return True

        for row in range(3, 6):
            for col in range(4):
                if (
                    self.board[row][col]
                    and self.board[row][col] == self.board[row - 1][col + 1] == self.board[row - 2][col + 2] == self.board[row - 3][col + 3]
                ):
                    return True

        return False

    def is_board_full(self):
        return all(self.board[0] != 0)

    def get_possible_moves(self):
        return [col for col in range(7) if self.is_valid_move(col)]

    def switch_player(self):
        self.player = 3 - self.player

    def evaluate_heuristic(self):
        # Heuristic 1: Piece Count Heuristic
        score = np.sum(self.board == self.player) - np.sum(self.board == 3 - self.player)

        # Heuristic 2: Winning Move Heuristic
        if self.check_winner():
            return float("inf") if self.player == 1 else float("-inf")

        # Heuristic 3: Blocking Opponent's Winning Move
        opponent = 3 - self.player
        if ConnectFour.check_winner_for_player(self.board, opponent):
            return float("-inf") if self.player == 1 else float("inf")

        # Heuristic 4: Center Control Heuristic
        center_count = np.sum(self.board[:, 2:5] == self.player)
        score += center_count * 3

        # Heuristic 5: Threat Assessment Heuristic
        score += self.evaluate_threats(self.player) * 2
        score -= self.evaluate_threats(opponent) * 2

        # Heuristic 6: Mobility Heuristic
        score += len(self.get_possible_moves())

        return score

    def evaluate_threats(self, player):
        threats = 0

        for row in range(6):
            for col in range(7):
                if self.board[row][col] == 0:
                    self.board[row][col] = player
                    if ConnectFour.check_winner_for_player(self.board, player):
                        threats += 1
                    self.board[row][col] = 0

        return threats

    @staticmethod
    def check_winner_for_player(board, player):
        for row in range(6):
            for col in range(4):
                if (
                    board[row][col]
                    and board[row][col] == board[row][col + 1] == board[row][col + 2] == board[row][col + 3] == player
                ):
                    return True

        for row in range(3):
            for col in range(7):
                if (
                    board[row][col]
                    and board[row][col] == board[row + 1][col] == board[row + 2][col] == board[row + 3][col] == player
                ):
                    return True

        for row in range(3):
            for col in range(4):
                if (
                    board[row][col]
                    and board[row][col] == board[row + 1][col + 1] == board[row + 2][col + 2] == board[row + 3][col + 3] == player
                ):
                    return True

        for row in range(3, 6):
            for col in range(4):
                if (
                    board[row][col]
                    and board[row][col] == board[row - 1][col + 1] == board[row - 2][col + 2] == board[row - 3][col + 3] == player
                ):
                    return True

        return False

    def minimax_alpha_beta(self, depth, alpha, beta, maximizing_player):
        if depth == 0 or self.is_game_over():
            return self.evaluate_heuristic()

        if maximizing_player:
            max_eval = float("-inf")
            for move in self.get_possible_moves():
                self.make_move(move)
                eval = self.minimax_alpha_beta(depth - 1, alpha, beta, False)
                self.board[self.board == 0] = 0
                max_eval = max(max_eval, eval)
                alpha = max(alpha, eval)
                if beta <= alpha:
                    break
            return max_eval
        else:
            min_eval = float("inf")
            for move in self.get_possible_moves():
                self.make_move(move)
                eval = self.minimax_alpha_beta(depth - 1, alpha, beta, True)
                self.board[self.board == 0] = 0
                min_eval = min(min_eval, eval)
                beta = min(beta, eval)
                if beta <= alpha:
                    break
            return min_eval

    def find_best_move(self, depth):
        best_move = None
        max_eval = float("-inf")

        for move in self.get_possible_moves():
            self.make_move(move)
            eval = self.minimax_alpha_beta(depth, float("-inf"), float("inf"), False)
            self.board[self.board == 0] = 0

            if eval > max_eval:
                max_eval = eval
                best_move = move

        return best_move
    

def print_instructions():
    print("Welcome to Connect Four!")
    print("You are playing as 'X', and the AI is 'O'.")
    print("To make a move, enter a column number from 0 to 6.")
    print("Here's the board with column numbers for reference:")
    print(" 0 1 2 3 4 5 6")
    print("|-+-+-+-+-+-+-|")
    print("| | | | | | | |")
    print("|-+-+-+-+-+-+-|")
    print("| | | | | | | |")
    print("|-+-+-+-+-+-+-|")
    print("| | | | | | | |")
    print("|-+-+-+-+-+-+-|")
    print("| | | | | | | |")
    print("|-+-+-+-+-+-+-|")

def player_move(game):
    while True:
        try:
            column = int(input("Enter your move (column number): "))
            if game.is_valid_move(column):
                return column
            else:
                print("Invalid move. Please try again.")
        except ValueError:
            print("Invalid input. Please enter a valid column number.")

def main():
    print_instructions()
    game = ConnectFour()

    while not game.is_game_over():
        game.print_board()

        # Human player's move
        human_move = player_move(game)
        game.make_move(human_move)

        if game.is_game_over():
            break

        # AI's move
        print("AI's turn...")
        best_move = game.find_best_move(depth=5)  # Adjust depth for AI's strength
        game.make_move(best_move)

    game.print_board()

    if game.check_winner():
        winner = "AI" if game.player == 2 else "Human"
        print(f"{winner} wins!")
    else:
        print("It's a draw!")

if __name__ == "__main__":
    main()


