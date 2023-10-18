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
        for i in range(len(row) - 4):
            window = row[i:i + 5]
            if window.count(opponent) == 4 and window.count(EMPTY) == 1:
                threat_count += 1

    # Check columns for opponent's threats
    for j in range(len(board[0])):
        column = [board[i][j] for i in range(len(board))]
        for i in range(len(column) - 4):
            window = column[i:i + 5]
            if window.count(opponent) == 4 and window.count(EMPTY) == 1:
                threat_count += 1

    # Check diagonals (both directions) for opponent's threats
    for i in range(len(board)):
        for j in range(len(board[0])):
            if i + 4 < len(board) and j + 4 < len(board[0]):
                diagonal = [board[i + k][j + k] for k in range(5)]
                if diagonal.count(opponent) == 4 and diagonal.count(EMPTY) == 1:
                    threat_count += 1

            if i - 4 >= 0 and j + 4 < len(board[0]):
                diagonal = [board[i - k][j + k] for k in range(5)]
                if diagonal.count(opponent) == 4 and diagonal.count(EMPTY) == 1:
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
        for i in range(len(row) - 4):
            window = row[i:i + 5]
            if window.count(player) == 4 and window.count(EMPTY) == 1:
                open_row_count += 1
    return open_row_count
