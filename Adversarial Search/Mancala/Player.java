import java.util.ArrayList;
import java.util.Scanner;

/*
    PLAYER TYPES
    -------------------
    HUMAN = 0
    COMPUTER = 1
*/

public class Player {

    int player_num;
    int opponent_num;
    int player_type;
    int depth_limit;
    Player opponent;
    int heuristic_choice;

    Player(int num, int type, int depth_limit, int heuristic_choice)
    {
        this.player_num = num;
        this.opponent_num = 2 - num + 1;
        this.player_type = type;
        this.depth_limit = depth_limit;
        this.heuristic_choice = heuristic_choice;
    }

    public int get_player_num()
    {
        return player_num;
    }

    // -- chooses a move from player -- //
    public int choose_move(Board board)
    {
        if(player_type == 0)
        {
            Scanner sc = new Scanner(System.in);
            return sc.nextInt();
        }
        else if(player_type == 1)
        {
//            int optimal_move = minimax_move(board, depth_limit);                        // simulates minimax without pruning
            int optimal_move = alpha_beta_move(board, depth_limit);                     // simulate minimax with alpha beta pruning

            System.out.println("Player Move: " + optimal_move);
            return optimal_move;
        }
        return 0;
    }

    //// --------------------------- MINIMAX ALGORITHM ------------------------- ////

    // -- simulates a minimax move -- //
    public int minimax_move(Board board, int depth)
    {
        int move = -1;
        int score = Integer.MIN_VALUE;
        Player turn = this;

        ArrayList<Integer>possible_moves = board.possible_moves(this);                  // getting all the possible moves of the board

        // for each move
        for (Integer possible_move : possible_moves) {
            Board new_board = new Board(board);                                                 // making a new board for running predictions

            new_board.single_move(this, possible_move);                                     // making the move on the board

            opponent = new Player(opponent_num, player_type, depth_limit, heuristic_choice);

            int temp_score = opponent.min_value(new_board, depth - 1, turn);            // opponent runs min_value
            if (temp_score > score)                                                          // player1 takes the max of the result
            {
                score = temp_score;
                move = possible_move;
            }
        }
        System.out.println("Score for Player " + player_num + ": " + score);
        return move;
    }

    public int max_value(Board board, int depth, Player turn)
    {
        if(board.game_over() || depth == 0)
        {
            if(heuristic_choice == 1)
            {
                return turn.h_probability(board);
            }
            else if(heuristic_choice == 2)
            {
                return turn.score_stones_diff(board);
            }
            else if(heuristic_choice == 3)
            {
                return turn.stones_diff(board);
            }
            else if(heuristic_choice == 4)
            {
                return turn.close_to_victory(board);
            }
            else if(heuristic_choice == 5)
            {
                return turn.close_to_storage(board, player_num);
            }
            else if(heuristic_choice == 6)
            {
                return turn.captured_stones(board);
            }
            else if(heuristic_choice == 7)
            {
                return turn.additional_move_earned(board);
            }
            else
                return -1;
        }
        else
        {
            int score = Integer.MIN_VALUE;                                                                  // score set to -INFINITY

            ArrayList<Integer>possible_moves = board.possible_moves(this);                              // taking all the possible moves

            // for each move in possible moves
            for (Integer possible_move : possible_moves) {
                Board new_board = new Board(board);                                                         // generating a copy of the board for prediction
                new_board.single_move(this, possible_move);                                             // giving the move on the copied board

                opponent = new Player(opponent_num, player_type, depth_limit, heuristic_choice);            // taking the opponent player object
                int temp_score = opponent.min_value(new_board, depth - 1, turn);                        // opponent gives the min move
                score = Math.max(score, temp_score);                                                        // player is selecting the max score
            }
            return score;
        }
    }

    public int min_value(Board board, int depth, Player turn)
    {
        if(board.game_over() || depth == 0)
        {
            if(heuristic_choice == 1)
            {
                return turn.h_probability(board);
            }
            else if(heuristic_choice == 2)
            {
                return turn.score_stones_diff(board);
            }
            else if(heuristic_choice == 3)
            {
                return turn.stones_diff(board);
            }
            else if(heuristic_choice == 4)
            {
                return turn.close_to_victory(board);
            }
            else if(heuristic_choice == 5)
            {
                return turn.close_to_storage(board, player_num);
            }
            else if(heuristic_choice == 6)
            {
                return turn.captured_stones(board);
            }
            else if(heuristic_choice == 7)
            {
                return turn.additional_move_earned(board);
            }
            else
                return -1;
        }
        else
        {
            int score = Integer.MAX_VALUE;                                                                  // score set to +INFINITY

            ArrayList<Integer>possible_moves = board.possible_moves(this);                              // taking all the possible moves

            // for each move in possible moves
            for (Integer possible_move : possible_moves) {
                Board new_board = new Board(board);                                                         // copying the current board to a new board
                new_board.single_move(this, possible_move);                                     // giving the move on the new board

                opponent = new Player(opponent_num, player_type, depth_limit, heuristic_choice);            // taking the opponent player object
                int temp_score = opponent.max_value(new_board, depth - 1, turn);                          // opponent gives max move
                score = Math.min(score, temp_score);                                                            // player selects the minimum of the scores
            }
            return score;
        }
    }



    //// ------------------------ ALPHA BETA PRUNING ------------------------- ////

    // -- simulates minimax algorithm with alpha beta pruning -- //
    public int alpha_beta_move(Board board, int depth)
    {
        int move = -1;
        Player turn = this;
        int score = Integer.MIN_VALUE;                                                      // score set to -INFINITY

        int alpha = Integer.MIN_VALUE;                                                      // alpha set to -INFINITY
        int beta = Integer.MAX_VALUE;                                                       // beta set to +INFINITY

        ArrayList<Integer>possible_moves = board.possible_moves(this);                                  // getting all the possible moves of the board

        // for each move
        for (Integer possible_move : possible_moves) {
            Board new_board = new Board(board);                                                             // making a new board for running predictions

            new_board.single_move(this, possible_move);                                                 // making the move on the board

            opponent = new Player(opponent_num, player_type, depth_limit, heuristic_choice);

            int temp_score = opponent.ab_min_value(new_board, depth - 1, turn, alpha, beta);          // opponent runs min_value

            if (temp_score > score)                                                                         // player1 takes the max of the result
            {
                score = temp_score;
                move = possible_move;
            }
            alpha = Math.max(score, alpha);
        }
        return move;
    }

    public int ab_max_value(Board board, int depth, Player turn, int alpha, int beta)
    {
        if(board.game_over() || depth == 0)
        {
            if(heuristic_choice == 1)
            {
                return turn.h_probability(board);
            }
            else if(heuristic_choice == 2)
            {
                return turn.score_stones_diff(board);
            }
            else if(heuristic_choice == 3)
            {
                return turn.stones_diff(board);
            }
            else if(heuristic_choice == 4)
            {
                return turn.close_to_victory(board);
            }
            else if(heuristic_choice == 5)
            {
                return turn.close_to_storage(board, player_num);
            }
            else if(heuristic_choice == 6)
            {
                return turn.captured_stones(board);
            }
            else if(heuristic_choice == 7)
            {
                return turn.additional_move_earned(board);
            }
            else
                return -1;
        }
        else
        {
            int score = Integer.MIN_VALUE;

            ArrayList<Integer>possible_moves = board.possible_moves(this);

            for (Integer possible_move : possible_moves) {
                Board new_board = new Board(board);
                new_board.single_move(this, possible_move);

                opponent = new Player(opponent_num, player_type, depth_limit, heuristic_choice);

                int temp_score = opponent.ab_min_value(new_board, depth - 1, turn, alpha, beta);
                score = Math.max(score, temp_score);

                // alpha beta pruning -- //
                if (score >= beta)                                           // if we get a value bigger than beta, max will choose it, and min will not take it from this tree
                    return score;                                           // hence prune the rest of the tree

                alpha = Math.max(alpha, score);
            }
            return score;
        }
    }

    public int ab_min_value(Board board, int depth, Player turn, int alpha, int beta)
    {
        if(board.game_over() || depth == 0)
        {
            if(heuristic_choice == 1)
            {
                return turn.h_probability(board);
            }
            else if(heuristic_choice == 2)
            {
                return turn.score_stones_diff(board);
            }
            else if(heuristic_choice == 3)
            {
                return turn.stones_diff(board);
            }
            else if(heuristic_choice == 4)
            {
                return turn.close_to_victory(board);
            }
            else if(heuristic_choice == 5)
            {
                return turn.close_to_storage(board, player_num);
            }
            else if(heuristic_choice == 6)
            {
                return turn.captured_stones(board);
            }
            else if(heuristic_choice == 7)
            {
                return turn.additional_move_earned(board);
            }
            else
                return -1;

        }
        else
        {
            int score = Integer.MAX_VALUE;

            ArrayList<Integer>possible_moves = board.possible_moves(this);

            for (Integer possible_move : possible_moves) {
                Board new_board = new Board(board);
                new_board.single_move(this, possible_move);

                opponent = new Player(opponent_num, player_type, depth_limit, heuristic_choice);

                int temp_score = opponent.ab_max_value(new_board, depth - 1, turn, alpha, beta);
                score = Math.min(score, temp_score);

                // -- alpha beta pruning -- //
                if (score <= alpha)                              // if we get a value smaller than alpha, min will choose it, and max will not take it from this tree
                    return score;                               // hence prune the rest of the tree

                beta = Math.min(score, beta);
            }
            return score;
        }
    }



    //// -------------------------------- HEURISTICS ------------------------- ////

    // estimates using simple probability -- //
    public int h_probability(Board board)
    {
        if(board.has_won(player_num))
            return 100;
        else if(board.has_won(opponent_num))
            return 0;
        else
            return 50;
    }

    // -- estimates using score stones difference -- //
    public int score_stones_diff(Board board)
    {
        return (board.score_bins[player_num-1] - board.score_bins[opponent_num-1]);
    }

    // -- estimates using stones on side + stones in storage -- //
    public int stones_diff(Board board)
    {
        int score_diff = (board.score_bins[player_num-1] - board.score_bins[opponent_num-1]);

        int self_side_stones = 0;
        int opponent_side_stones = 0;

        if(player_num == 1)
        {
            for(int i=0; i<board.no_of_bins; i++)
            {
                self_side_stones += board.p1_bins[i];
                opponent_side_stones += board.p2_bins[i];
            }
        }
        else
        {
            for(int i=0; i<board.no_of_bins; i++)
            {
                self_side_stones += board.p2_bins[i];
                opponent_side_stones += board.p1_bins[i];
            }
        }

        int side_bins_diff = self_side_stones - opponent_side_stones;

        return 100 * score_diff + 50 * side_bins_diff;                    // score stones are given a higher weight since they are guaranteed
    }

    // -- measures how close a player is to victory depending on
    // if his storage is already close to containing half of total number of stones -- //
    public int close_to_victory(Board board)
    {
        int no_of_bins = board.no_of_bins;                                      // e.g. 6
        int stones_each_bin = board.stones_each_bin;                            // e.g. 4

        int total_stones = no_of_bins * stones_each_bin * 2;                    // e.g. 6 * 4 * 2 = 48
        int self_stones;
        int opponent_stones;

        if(player_num == 1)
        {
            self_stones = board.score_bins[0];
            opponent_stones = board.score_bins[1];
        }
        else
        {
            self_stones = board.score_bins[1];
            opponent_stones = board.score_bins[0];
        }

        int player_close_to_half = total_stones/2 - self_stones;
        int opponent_close_to_half = total_stones/2 - opponent_stones;

        // 1000 * 1.1^(-how close to half)  // if close -> returns a higher value
        int close_diff = ((int) Math.floor(1000 * Math.pow(1.1, -player_close_to_half))) - ((int) Math.floor(1000 * Math.pow(1.1, -opponent_close_to_half)));

//        return (int) Math.floor(1000 * Math.pow(1.1, -player_close_to_half));
        return close_diff;
    }

    // -- estimates using score stones and captured stones -- //
    public int captured_stones(Board board)
    {
        int self_captured_stones = board.get_captured_stones(player_num);
        int opponent_captured_stones = board.get_captured_stones(opponent_num);
        int captured_stones_diff = self_captured_stones - opponent_captured_stones;

        return 100 * stones_diff(board) + 200 * captured_stones_diff;
    }

    // -- estimates using side and score stones, and bonus move earned -- //
    // -- bonus move is given the highest priority -- //
    public int additional_move_earned(Board board)
    {
        int score_diff = (board.score_bins[player_num-1] - board.score_bins[opponent_num-1]);

        int self_side_stones = 0;
        int opponent_side_stones = 0;

        if(player_num == 1)
        {
            for(int i=0; i<board.no_of_bins; i++)
            {
                self_side_stones += board.p1_bins[i];
                opponent_side_stones += board.p2_bins[i];
            }
        }
        else
        {
            for(int i=0; i<board.no_of_bins; i++)
            {
                self_side_stones += board.p2_bins[i];
                opponent_side_stones += board.p1_bins[i];
            }
        }

        int side_bins_diff = self_side_stones - opponent_side_stones;

        int additional_move = 0;
        if(board.get_bonus_move(player_num))
            additional_move = 1;

        return 100 * score_diff + 50 * side_bins_diff + 500 * additional_move;    // score stones are given a higher weight, bonus move given the highest
    }

    // -- stones that are not going to be overflowed to opponent's side -- //
    public int close_to_storage_helper(Board board, int p_num)
    {
        int stones;
        int stones_close_to_storage = 0;
        int[] self_bins;

        if(p_num == 1) {
            self_bins = board.p1_bins;
        }
        else {
            self_bins = board.p2_bins;
        }

        for(int i=0; i<self_bins.length; i++)                       // for each bins
        {
            stones = self_bins[i];                                  // take the stones
            int self_bins_limit = (board.no_of_bins)-i;             // limit for a bin to overflow      // i.e. bin 2 can hold 5 bins that won't overflow  // 6-(2-1) = 5

            if(stones < self_bins_limit)
            {
                stones_close_to_storage += stones;                  // if stones wil not overflow to opponent's side, count them as own stones
            }
            else
            {
                int stones_left = stones;                           // all the stones left

                stones_left -= self_bins_limit;                     // take the limit no. of stones from all the stones left
                stones_close_to_storage += self_bins_limit;         // put it in storage

                int iteration = 0;

                while (stones_left > 0)
                {
                    if(iteration%2==0)                              // put the stones on the opponent's side on first iteration
                    {
                        stones_left -= board.no_of_bins;
                    }
                    else                                            // put the stones on self side on next iteration, and keep doing
                    {
                        if(stones_left > board.no_of_bins+1)                            // if overflows
                        {
                            stones_left -= (board.no_of_bins + 1);                      // take the limit no. stones
                            stones_close_to_storage += (board.no_of_bins+1);            // put them in self side
                        }
                        else                                                            // if no overflow
                        {
                            stones_close_to_storage += stones_left;                     // put them all in self side
                            stones_left = 0;
                        }
                    }
                    iteration++;
                }
            }
        }
        return stones_close_to_storage;
    }

    // -- takes the difference of close_to_storage stones of two players -- //
    public int close_to_storage(Board board, int p_num)
    {
        int opponent_num = 2-p_num+1;

        return (100 * score_stones_diff(board) + 50 * (close_to_storage_helper(board, p_num) - close_to_storage_helper(board, opponent_num)));
    }
}
